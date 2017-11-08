import org.json.JSONException;
import org.json.JSONObject;
import API.*;
/**
 * 
 * @author Marlon Prudente
 */
public class Main {
    public static void main(String[] args) throws JSONException,  Exception {
        //Criação de objetos das APIs
        Ticker t = new Ticker();
        Orderbook ob = new Orderbook();
        
        //serializa para uma string e imprime
        System.out.println("Ticker -> " + t.getTicker()); 
        System.out.println("Order Book -> " + ob.getOrderBook());

    }

}
