
import API.Trade.Authentication;
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
        Ticker t = new Ticker("btc");
        Ticker t2 = new Ticker("ltc");
        Orderbook ob = new Orderbook();
        Trades tr = new Trades();
        Authentication auth = new Authentication();        
        String resultado = auth.Balance("557bf95117be498c915c0b6816f858f1", "YpsLRaDKBj2DSYmvp3TsXFEAqZPLmyL0/fMK2VrBFdA=", "user/balance", "");
        System.out.println("Resultado: " + resultado);
        // Balance b = new Balance();

        //serializa para uma string e imprime
        //System.out.println("Ticker BTC-> " + t.getTicker());
        //System.out.println("Ticker LTC-> " + t2.getTicker());
        //System.out.println("Order Book -> " + ob.getOrderBook());


        //System.out.println("======//======");
        //System.out.println("Balance -> " + b.getBalance());
    }

}
