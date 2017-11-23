
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;

public class HttpNegocieCoinsBKP {

	public String privateUrl = "https://broker.negociecoins.com.br/tradeapi/v1/";
	public String appID = "";
	public String appkey = "";

	public HttpNegocieCoinsBKP(String function, String method, String jSonPostParam) throws Exception {
		String urlMethod = privateUrl + function;
		String timeStamp = String.valueOf(System.currentTimeMillis());
		String nonce = String.valueOf(System.currentTimeMillis() + 100);
		String requestContent = "";
		String signatureRawData = appID + method + urlMethod + timeStamp + nonce + requestContent;
		System.out.println("signatureRawData : " + signatureRawData);
		String signature = getMD5_B64(appkey);
		String HMACSHA256 = this.sha256_B64(signature);

		String AUTH = "amx " + appID + ":" + HMACSHA256 + ":" + nonce + ":" + timeStamp;
		System.out.println("AUTH: " + AUTH);
		String response = this.continueForHttp(urlMethod, method, jSonPostParam, AUTH);
		System.out.println("API RESPONSE : " + response);

	}

	private String continueForHttp(String urlMethod, String method, String postParam, String AUTH) throws Exception {
		URLConnection con = new URL(urlMethod).openConnection(); // CREATE POST CONNECTION
		con.setDoOutput(true);
		HttpsURLConnection httpsConn = (HttpsURLConnection) con;
		httpsConn.setRequestMethod(method);
		httpsConn.setInstanceFollowRedirects(true);
		con.setRequestProperty("Authorization", AUTH);
		con.setRequestProperty("Content-Type", "application/json");

		// WRITE POST PARAMS
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(postParam);
		wr.flush();
		wr.close();

		// READ THE RESPONSE
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

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
			new HttpNegocieCoinsBKP("user/balance", "GET", "");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}