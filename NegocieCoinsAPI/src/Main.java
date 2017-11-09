
import org.json.JSONException;
import org.json.JSONObject;
import API.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Marlon Prudente
 */
public class Main {

    public static void main(String[] args) throws JSONException, Exception {
        //Criação de objetos das APIs
        Ticker t = new Ticker();
        Orderbook ob = new Orderbook();
        Trades tr = new Trades();
        Auth auth = new Auth();        
        auth.amx_authorization_header("user/balance");
       // Balance b = new Balance();

        //serializa para uma string e imprime
        //System.out.println("Ticker -> " + t.getTicker());
        //System.out.println("Order Book -> " + ob.getOrderBook());
        //System.out.println("Trades -> " + tr.getTrades());
        //System.out.println("======//======");
        //System.out.println("Balance -> " + b.getBalance());

    }

}
