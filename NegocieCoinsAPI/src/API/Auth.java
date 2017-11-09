/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;


/**
 *
 * @author Usuário
 */
public class Auth {

    public String amx_authorization_header(String funcao) throws MalformedURLException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        String urlString = "https://broker.negociecoins.com.br/tradeapi/v1/" + funcao;
        String ID = "557bf95117be498c915c0b6816f858f1";
        String pass = "YpsLRaDKBj2DSYmvp3TsXFEAqZPLmyL0/fMK2VrBFdA=";

        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            URLConnection urlcon = url.openConnection();

            String userCredentials = ID + ":" + pass;
            
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(pass.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            String basicAuth = Base64.encodeBase64String(userCredentials.getBytes(StandardCharsets.UTF_8));
            //userCredentials = ID + ":" + basicAuth;
            System.out.println("Auth = => " + basicAuth);
            
            urlcon.setRequestProperty("User-Agent", "Mozilla/5.0");
            urlcon.setRequestProperty("Content-Type", "text/plain");
            urlcon.setRequestProperty("charset", "UTF-8");
           // urlcon.setRequestProperty("Authorization","Basic " + basicAuth);
            
            HttpURLConnection httpcon = (HttpURLConnection) urlcon;
            httpcon.setRequestProperty("Authorization","Basic " + basicAuth);
            
            httpcon.setRequestMethod("GET");
            //System.out.println("=>" + httpcon.getResponseMessage());
            reader = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }
            System.out.println("Resposta: " + buffer.toString());
            return buffer.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

    }

}
