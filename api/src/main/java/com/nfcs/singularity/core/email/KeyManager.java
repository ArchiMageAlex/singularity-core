package com.nfcs.singularity.core.email;

import javax.net.ssl.X509ExtendedKeyManager;
import java.net.Socket;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class KeyManager extends X509ExtendedKeyManager {

    KeyStore keystore = null;
    String password = null;

    public KeyManager(KeyStore keystore, String password) {
        this.keystore = keystore;
        this.password = password;
    }

    @Override
    public String chooseClientAlias(String[] arg0, Principal[] arg1, Socket arg2) {
        return ""; // can't be null
    }

    @Override
    public String chooseServerAlias(String arg0, Principal[] arg1, Socket arg2) {
        return null;
    }

    @Override
    public X509Certificate[] getCertificateChain(String arg0) {
        try {
            X509Certificate[] result = new X509Certificate[keystore.getCertificateChain(keystore.aliases().nextElement()).length];
            for (int i = 0; i < result.length; i++) {
                result[i] = (X509Certificate) keystore.getCertificateChain(keystore.aliases().nextElement())[i];
            }
            return result;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public String[] getClientAliases(String arg0, Principal[] arg1) {
        try {
            return new String[]{keystore.aliases().nextElement()};
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public PrivateKey getPrivateKey(String arg0) {
        try {
            return ((KeyStore.PrivateKeyEntry) keystore.getEntry(keystore.aliases().nextElement(),
                    new KeyStore.PasswordProtection(password.toCharArray()))).getPrivateKey();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public String[] getServerAliases(String arg0, Principal[] arg1) {
        return null;
    }
}
