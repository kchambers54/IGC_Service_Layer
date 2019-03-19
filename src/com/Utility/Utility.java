//Utility inspired by "https://www.baeldung.com/java-http-request"

package com.Utility;

import javax.net.ssl.*;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * A utility class containing various static methods.
 */
public class Utility {

//    /**
//     * Correctly formats parameters for an http request
//     *
//     * @param params A Hash Map of parameter (key, value) pairs.
//     * @return returns a correctly formatted String of parameters
//     * @throws UnsupportedEncodingException:
//     */
//    public static String getParamString(Map<String, String> params)
//            throws UnsupportedEncodingException {
//        StringBuilder result = new StringBuilder();
//
//        for (Map.Entry<String, String> entry : params.entrySet()) {
//            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
//            result.append("=");
//            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
//            result.append("&");
//        }
//
//        String resultString = result.toString();
//        return resultString.length() > 0
//                ? resultString.substring(0, resultString.length() - 1)
//                : resultString;
//    }

    /**
     * Disables SSL Verification for HttpsURLConnection.
     * @throws IllegalStateException:
     */
    public static void disableSslVerification() throws IllegalStateException {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            System.out.println("Disabling SSL Verification...");
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            // Shouldnt even happen, just being safe.
            e.printStackTrace();
            throw new IllegalStateException(); //Throw runtime exception
        }
    }

    /**
     * Set the default username and password for HTTP requests.
     * @param username username.
     * @param password password.
     */
    public static void authenticate(String username, String password) {
        Authenticator.setDefault (new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication (username, password.toCharArray());
            }
        });
    }
}
