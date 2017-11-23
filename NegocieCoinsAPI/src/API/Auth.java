/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;
import static org.apache.commons.codec.binary.Base64.*;

/**
 *
 * @author Marlon Prudente
 */
public class Auth {

    private String getMD5_B64(String postParameter) throws Exception {
        byte[] message = postParameter.getBytes("UTF-8");
        return DatatypeConverter.printBase64Binary(MessageDigest.getInstance("MD5").digest(message));
    }

    private String sha256_B64(String msg, String appkey) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        byte[] decoded = DatatypeConverter.parseBase64Binary(appkey);
        SecretKeySpec secret_key = new SecretKeySpec(decoded, "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return DatatypeConverter.printBase64Binary(sha256_HMAC.doFinal(msg.getBytes("UTF-8")));
    }

    public String amx_authorization_header(String funcao) throws Exception {
        //Definições de funcao, ID e password para acessar a API
        String endereco = "https://broker.negociecoins.com.br/tradeapi/v1/" + funcao;
        String urlString = URLEncoder.encode(endereco, "UTF-8").toLowerCase();
        String ID = "557bf95117be498c915c0b6816f858f1";
        String pass = "YpsLRaDKBj2DSYmvp3TsXFEAqZPLmyL0/fMK2VrBFdA=";
        String result = "";
        URL url;
        HttpsURLConnection urlConnection = null;

        //Calculando UNIX Time: Gerando um requestTimeStamp
        Date epochStart = new Date(1970 - 1900, 01, 01, 01, 01, 01);
        System.out.println("epochStart: " + epochStart.toString());
        String requestTimeStamp = "" + ((new Date().getTime() - epochStart.getTime()) / 1000);
        System.out.println("TimeStamp: " + requestTimeStamp);

        //Gerando um Nonce
        String nonce = java.util.UUID.randomUUID().toString().replace("-", "");
        String signatureRawData = String.format("%s%s%s%s%s%s", ID, "GET", urlString, requestTimeStamp, nonce, "");
        System.out.println("SignatureRawData: " + signatureRawData);

        //Tratamento do password para SHA256
        String signature = getMD5_B64(signatureRawData);
        String HMACSHA256 = sha256_B64(signature,pass);        
        String requestSignatureBase64String = HMACSHA256;
        System.out.println("requestSignatureBase64String: " + requestSignatureBase64String);

        //Preparando HEADER para ser enviado na autenticação
        String header = String.format("amx%s:%s:%s:%s", ID, requestSignatureBase64String, nonce, requestTimeStamp); //Formato Header
        System.out.println("HEADER: " + header);

        //Envio da autenticação para a Conexão
        //Configurações para acesso a URL
        System.out.println("urlString: " + urlString);
        System.out.println("endereco: " + endereco);
        url = new URL(endereco);
        urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");        
        urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        urlConnection.setRequestProperty("Authorization", header);
        //System.out.println("request == " + urlConnection.getRequestProperty("Authorization"));
        urlConnection.connect();
        System.out.println("Connection: " + urlConnection.getResponseMessage());

        //Leitura do retorno da API
        try {
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();
            while (data != -1) {
                char current = (char) data;
                result += current;
                data = reader.read();
            }
            System.out.println(result);

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("Finalizado");

        }
        return result;

    }
}
