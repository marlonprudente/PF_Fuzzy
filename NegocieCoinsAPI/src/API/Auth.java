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
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import static org.apache.commons.codec.binary.Base64.*;


//import org.apache.commons.codec.binary.Base64;
/**
 *
 * @author Usuário
 */
public class Auth {

    public static int unsignedToBytes(byte b) {
        return b & 0xFF;
    }

    void printUnsignedByte(byte b) {
        int unsignedByte = b & 0xFF;
        System.out.println(unsignedByte); // "200"
    }

    public String amx_authorization_header(String funcao) throws Exception{
        //Definições de funcao, ID e password para acessar a API
        String endereco = "https://broker.negociecoins.com.br/tradeapi/v1/" + funcao;
        String urlString = URLEncoder.encode(endereco, "UTF-8").toLowerCase();
        String ID = "557bf95117be498c915c0b6816f858f1";
        String pass = "YpsLRaDKBj2DSYmvp3TsXFEAqZPLmyL0/fMK2VrBFdA=";
        String result = "";
        URL url;
        //BufferedReader reader = null;
        //String requestContentBase64String = "";
        HttpsURLConnection urlConnection = null;

        //Configurações para acesso a URL
        System.out.println("urlString: " + urlString);
        System.out.println("endereco: " + endereco);
        url = new URL(endereco);
        urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("User-Agent", "Mozilla/4.76");
        //urlConnection.setConnectTimeout(5000);

        
        //Calculando UNIX Time: Gerando um requestTimeStamp
        Date epochStart = new Date(1970 - 1900, 01, 01, 01, 01, 01);
        System.out.println("epochStart: " + epochStart.toString());
        String requestTimeStamp = "" + ((new Date().getTime() - epochStart.getTime()) / 1000);
        System.out.println("TimeStamp: " + requestTimeStamp);

        //Gerando um Nonce
        String nonce = java.util.UUID.randomUUID().toString().replace("-", "");
        System.out.println("Nonce: " + nonce + " Request Method: " + urlConnection.getRequestMethod());
        String signatureRawData = String.format("%s%s%s%s%s%s", ID, urlConnection.getRequestMethod(), urlString, requestTimeStamp, nonce, "");
        System.out.println("SignatureRawData: " + signatureRawData);
        
        //Tratamento do password para SHA256
        byte[] secretKeyByteArray = decodeBase64(pass.getBytes(StandardCharsets.UTF_8));
        int semsinal[] = new int[secretKeyByteArray.length];
        for (int i = 0; i < secretKeyByteArray.length; i++) {
            //System.out.println("Unsigned byte: " + (secretKeyByteArray[i] & 0xFF));
            semsinal[i] = secretKeyByteArray[i] & 0xFF;
        }
        byte[] signature = signatureRawData.getBytes("UTF-8");
        
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secretKeyByteArray, "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] signatureBytes = sha256_HMAC.doFinal(signature);
        String requestSignatureBase64String = encodeBase64String(signatureBytes);
        System.out.println("requestSignatureBase64String: " + requestSignatureBase64String);
        
        //Preparando HEADER para ser enviado na autenticação
        String header = String.format("amx %s:%s:%s:%s", ID, requestSignatureBase64String, nonce, requestTimeStamp); //Formato Header
        System.out.println("HEADER: " + header);
        
        //Envio da autenticação para a Conexão
        urlConnection.setRequestProperty("Authorization", header); 
        System.out.println("request == " + urlConnection.getRequestProperty("Authorization"));
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
