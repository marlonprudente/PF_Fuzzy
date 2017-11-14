import AuthTest;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
//import org.apache.commons.codec.binary.Base64;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Usuário
 */
public class Main {



  public static void main(String[] args) {
    try {
    HttpGet getRequest = new HttpGet(endpoint);
    getRequest.addHeader("Authorization", "Basic " + getBasicAuthenticationEncoding());
    
    }
    catch (Exception e){
     System.out.println("Error");
    }
   
}
    
}
