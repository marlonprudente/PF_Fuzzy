
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;

public class HttpNegocieCoins {

    public String privateUrl = "https://broker.negociecoins.com.br/tradeapi/v1/";
    public String appID = "557bf95117be498c915c0b6816f858f1";
    public String appkey = "YpsLRaDKBj2DSYmvp3TsXFEAqZPLmyL0/fMK2VrBFdA=";
    BufferedReader reader = null;

    public HttpNegocieCoins(String function, String method, String jSonPostParam) throws Exception {
        String urlMethod = privateUrl + function;
        String urlEncoded = URLEncoder.encode(urlMethod, "UTF-8").toLowerCase();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        System.out.println("timeStamp: " + timeStamp);
        /**
         * java.util.Date currentTime = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd:hh:mm:ss");    
           // Give it to me in GMT time.
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timeStamp = sdf.format(currentTime);
         * 
         */
        
        
        String nonce = String.valueOf(System.currentTimeMillis() + 100);
        String requestContent = "";
        String signatureRawData = appID + method + urlEncoded + timeStamp + nonce + requestContent;
        System.out.println("signatureRawData : " + signatureRawData);
        String signature = getMD5_B64(signatureRawData);
        String HMACSHA256 = this.sha256_B64(signature);

        String AUTH = "amx " + appID + ":" + HMACSHA256 + ":" + nonce + ":" + timeStamp;
        System.out.println("AUTH: " + AUTH);
        String response = this.continueForHttp(urlMethod, method, jSonPostParam, AUTH);
        System.out.println("API RESPONSE : " + response);

    }

    private String continueForHttp(String urlMethod, String method, String postParam, String AUTH) throws Exception {
        URL con = new URL(urlMethod); // CREATE POST CONNECTION
        HttpsURLConnection httpsConn = (HttpsURLConnection) con.openConnection();
        httpsConn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        httpsConn.setRequestProperty("Authorization", AUTH);
        httpsConn.setRequestProperty("Content-Type", "application/json");
        //System.out.println("Request== " + httpsConn.getRequestProperty("Authorization"));
        httpsConn.setDoOutput(true);

        httpsConn.setRequestMethod(method);
        System.out.println("Metodo de httpsConn: " + httpsConn.getRequestMethod());
        httpsConn.setInstanceFollowRedirects(true);
        httpsConn.connect();
        System.out.println("Connection: " + httpsConn.getResponseMessage());

        // WRITE POST PARAMS
        StringBuilder response = new StringBuilder();
        try {
            //DataOutputStream wr = new DataOutputStream(httpsConn.getOutputStream());                
            //wr.writeBytes(postParam);
            //wr.flush();
            //wr.close();

            // READ THE RESPONSE
            reader = new BufferedReader(new InputStreamReader(httpsConn.getInputStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }
            return buffer.toString();
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            if (reader != null) {
                reader.close();
            }
            System.out.println("::::::::::::");

        }
        return response.toString();

    }

    private String getMD5_B64(String postParameter) throws Exception {
        byte[] message = postParameter.getBytes("UTF-8");
        return DatatypeConverter.printBase64Binary(MessageDigest.getInstance("MD5").digest(message));
    }

    private String sha256_B64(String msg) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        byte[] decoded = DatatypeConverter.parseBase64Binary(appkey);
        SecretKeySpec secret_key = new SecretKeySpec(decoded, "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return DatatypeConverter.printBase64Binary(sha256_HMAC.doFinal(msg.getBytes("UTF-8")));
    }

    public static void main(String[] args) {
        try {
            new HttpNegocieCoins("user/balance", "GET", "");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
