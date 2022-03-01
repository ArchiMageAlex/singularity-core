package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.email.KeyManager;
import com.nfcs.singularity.core.service.CustomExchangeService;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.SSLProtocolSocketFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller("/")
@Configuration
public class MainController {
/*    private static Logger log = Logger.getLogger(MainController.class.getName());
    private static TrustManagerFactory tmf;
    private static SSLContext ctx;
    private static TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }
            }};
*/
    @GetMapping({"/"})
    public String home(Map<String, Object> model) {
        return "main";
    }

    @GetMapping("/login")
    public String login() {
/*        log.info("### Start mail send ###");
        ExchangeService service = new CustomExchangeService(ExchangeVersion.Exchange2010_SP2);
        ExchangeCredentials credentials = new WebCredentials(username, password);
        service.setCredentials(credentials);

        try {
            // prepareExchangeConnection();
            service.setUrl(new URI(server));
            EmailMessage msg = new EmailMessage(service);
            msg.setSubject("Hello world!");
            msg.setBody(MessageBody.getMessageBodyFromText("Sent using the EWS Java API."));
            msg.getToRecipients().add("Mitenko.A.Ni@omega.ca.sbrf.ru");
            msg.send();
            log.info("### Mail was sent ###");
        } catch (Exception e) {
            log.log(Level.SEVERE, "Mail doesn't sent", e);
        }*/

        return "login";
    }

/*    private void prepareExchangeConnection() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, KeyManagementException {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(certificateFilePath), certificatePass.toCharArray());

        ctx = SSLContext.getInstance("TLS");
        ctx.init(new KeyManager[]{new KeyManager(ks, "pass")}, trustAllCerts, new SecureRandom());
        SSLContext.setDefault(ctx);

        ProtocolSocketFactory psf = new SSLProtocolSocketFactory();
        Protocol https = new Protocol("https", psf, 443);
        Protocol.registerProtocol("https", https);
    }*/
}
