/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
//import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Usuário
 */
public class Auth {

    public String amx_authorization_header(String funcao) {
        //Definições de funcao, ID e password para acessar a API
        String urlString = "https://broker.negociecoins.com.br/tradeapi/v1/" + funcao;
        String ID = "557bf95117be498c915c0b6816f858f1";
        String pass = "YpsLRaDKBj2DSYmvp3TsXFEAqZPLmyL0/fMK2VrBFdA=";
        String result = "";
        URL url;
        String requestContentBase64String = "";
        HttpURLConnection urlConnection = null;
        try {
            //Configurações para acesso a URL
            url = new URL(urlString.toLowerCase());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            
            //Calculando UNIX Time: Gerando um requestTimeStamp
            Date epochStart = new Date(0);
            System.out.println("epochStart: " + epochStart);
            String requestTimeStamp = "" + ((new Date().getTime() - epochStart.getTime()) / 1000);
            System.out.println("TimeStamp: " + requestTimeStamp);
            
            //Gerando um Nonce
            String nonce = java.util.UUID.randomUUID().toString();
            System.out.println("Nonce: " + nonce);

            //Tratamento do password / Gerando SignatureData
            if (pass != null) {
                byte[] content = pass.getBytes();
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] requestContentHash = md.digest(content);
                requestContentBase64String = Base64.getUrlEncoder().encodeToString(requestContentHash);//Password criptografado
            }
            //Signaturedata contendo ID, METODO, URL, TIMESTAMP, NONCE, REQUEST64
            String signatureRawData = String.format("%s%s%s%s%s%s", ID, urlConnection.getRequestMethod(),
                    url.toString().toLowerCase(), requestTimeStamp, nonce, requestContentBase64String);
            
            System.out.println("RawData: " + signatureRawData);
            //Tratamento do password para SHA256
            byte[] secretKeyByteArray = Base64.getEncoder().encode(pass.getBytes());;
            byte[] signature = signatureRawData.getBytes("UTF-8");
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secretKeyByteArray, "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] signatureBytes = sha256_HMAC.doFinal(signature);
            String requestSignatureBase64String = Base64.getEncoder().encodeToString(signatureBytes);

            //Preparando HEADER para ser enviado na autenticação
            String header = String.format("amx %s:%s:%s:%s", ID, requestSignatureBase64String, nonce, requestTimeStamp); //Formato Header
            System.out.println("Formato HEADER: " + header);
            //Envio da autenticação para a Conexão
            urlConnection.setRequestProperty("Authorization", header);
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            //urlConnection.connect();

            //Leitura do retorno da API
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
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
