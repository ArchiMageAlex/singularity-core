package com.nfcs.singularity.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Slf4j
@Configuration
public class JKSConfig {
    private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

    @Value("${com.nfcs.jssecacerts}")
    String jssecacerts = "cacerts";

    @Value("${spring.mail.host}")
    String host;

    @Value("${spring.mail.port}")
    int port;

    @Value("${com.nfcs.jks.passphrase}")
    char[] passphrase;

    @Bean
    public void initJKSConfig() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, KeyManagementException {
        File file = new File(jssecacerts);

        if (!file.isFile()) {
            char SEP = File.separatorChar;
            File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");
            file = new File(dir, jssecacerts);
            log.debug("JKS cacerts path: {}", file.getPath().toString());

            if (!file.isFile()) {
                log.debug("Unexpectedly cacerts is not a file...");
                file = new File(dir, "cacerts");
            }
        }

        log.debug("Loading KeyStore " + file + "...");
        InputStream in = new FileInputStream(file);
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(in, passphrase);
        in.close();

        SSLContext context = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
        context.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory factory = context.getSocketFactory();

        log.debug("Opening connection to " + host + ":" + port + "...");
        SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
        socket.setSoTimeout(10000);

        try {
            log.debug("Starting SSL handshake...");
            socket.startHandshake();
            socket.close();
            log.debug("No errors, certificate is already trusted");
        } catch (SSLException e) {
            log.error("There are errors, so try to get certificates", e);
        }

        X509Certificate[] chain = tm.chain;
        if (chain == null) {
            log.debug("Could not obtain server certificate chain");
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        log.debug("Server sent " + chain.length + " certificate(s):");
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        int k = 0;

        for (int i = 0; i < chain.length; i++) {
            X509Certificate cert = chain[i];
            log.debug(" " + (i + 1) + " Subject " + cert.getSubjectDN());
            log.debug("   Issuer  " + cert.getIssuerDN());
            sha1.update(cert.getEncoded());
            log.debug("   sha1    " + toHexString(sha1.digest()));
            md5.update(cert.getEncoded());
            log.debug("   md5     " + toHexString(md5.digest()));

            if (cert.getSubjectDN().equals("CN=smtp.yandex.ru, O=Yandex LLC, OU=ITO, L=Moscow, C=RU")) {
                k = i;
                break;
            }
        }


        X509Certificate cert = chain[k];
        String alias = host + "-" + (k + 1);
        ks.setCertificateEntry(alias, cert);

        OutputStream out = new FileOutputStream(file);
        ks.store(out, passphrase);
        out.close();

        log.debug(cert.toString());
        log.debug("Added certificate to keystore {} using alias {}", jssecacerts, alias);
    }

    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 3);
        for (int b : bytes) {
            b &= 0xff;
            sb.append(HEXDIGITS[b >> 4]);
            sb.append(HEXDIGITS[b & 15]);
            sb.append(' ');
        }
        return sb.toString();
    }

    private static class SavingTrustManager implements X509TrustManager {

        private final X509TrustManager tm;
        private X509Certificate[] chain;

        SavingTrustManager(X509TrustManager tm) {
            this.tm = tm;
        }

        public X509Certificate[] getAcceptedIssuers() {
            throw new UnsupportedOperationException();
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            throw new UnsupportedOperationException();
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            this.chain = chain;
            tm.checkServerTrusted(chain, authType);
        }
    }


}
