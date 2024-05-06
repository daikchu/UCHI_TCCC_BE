package com.vn.osp.notarialservices.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vn.osp.notarialservices.common.util.PagingResult;
import org.apache.log4j.Logger;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class APIUtil {
    private static final Logger LOGGER = Logger.getLogger(APIUtil.class);
    public static PagingResult getReturnObject(String actionURL) {
        HttpURLConnection conn =null;
        PagingResult result = new PagingResult();
        try {
            LOGGER.info("CALL API OSP: "+actionURL);
            URL url = new URL(actionURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream()), "UTF-8"));
            String output;

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            while ((output = br.readLine()) != null) {
                result = mapper.readValue(output, PagingResult.class);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }
        return result;
    }
    public static void callApiGet(String actionURL) {
        LOGGER.info("CALL API OSP: "+actionURL);
        HttpURLConnection conn =null;
        PagingResult result = new PagingResult();
        try {
            URL url = new URL(actionURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream()), "UTF-8"));


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }
    }

    public static Map getReturnMap(String actionURL) {
        LOGGER.info("CALL API OSP: "+actionURL);
        HttpURLConnection conn =null;
        Map result = new HashMap();
        try {
            URL url = new URL(actionURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream()), "UTF-8"));
            String output;

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            while ((output = br.readLine()) != null) {
                result = mapper.readValue(output, Map.class);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }
        return result;
    }

    public static HostnameVerifier getAllHostsValid(){
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
        try{
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        return allHostsValid;

    }
}
