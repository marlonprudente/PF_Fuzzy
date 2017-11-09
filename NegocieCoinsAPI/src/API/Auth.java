/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
//import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Usuário
 */
public class Auth {

    public String amx_authorization_header(String funcao){
        //Definições de funcao, ID e password para acessar a API
        String urlString = "https://broker.negociecoins.com.br/tradeapi/v1/" + funcao;
        String ID = "557bf95117be498c915c0b6816f858f1";
        String pass = "YpsLRaDKBj2DSYmvp3TsXFEAqZPLmyL0/fMK2VrBFdA=";
        //String json = "";//"{\"key\":1}";
       // json = json + "{Password :'" + pass + "'}";
        String result = "";
        URL url;
        String requestContentBase64String = "";
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(urlString);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            Date epochStart = new Date(0);
            String requestTimeStamp = "" + ((new Date().getTime() - epochStart.getTime()) / 1000);


            String nonce = java.util.UUID.randomUUID().toString().replace("-", "");
            
            //Tratamento do password
            if (pass != null) {
                byte[] content = pass.getBytes();
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] requestContentHash = md.digest(content);
                requestContentBase64String = Base64.getUrlEncoder().encodeToString(requestContentHash);//Password criptografado
            }
            
            String signatureRawData = String.format("%s%s%s%s%s%s", ID, urlConnection.getRequestMethod(),
                    url.toString().toLowerCase(), requestTimeStamp, nonce, requestContentBase64String);

            byte[] secretKeyByteArray = Base64.getEncoder().encode(pass.getBytes());;
            byte[] signature = signatureRawData.getBytes("UTF-8");
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secretKeyByteArray, "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] signatureBytes = sha256_HMAC.doFinal(signature);
            String requestSignatureBase64String = Base64.getEncoder().encodeToString(signatureBytes);

            String header = String.format("amx %s:%s:%s:%s", ID, requestSignatureBase64String, nonce, requestTimeStamp); //Formato Header
            System.out.println("Formato HEADER: " + header);
            urlConnection.setRequestProperty("Authorization", header);
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            //urlConnection.connect();

            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();
            while (data != -1) {
                char current = (char) data;
                result += current;
                data = reader.read();
            }
            System.out.println(result);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
