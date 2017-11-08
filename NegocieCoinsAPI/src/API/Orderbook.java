/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import static API.LerURL.readUrl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONObject;

/**
 *
 * @author Usuário
 */
public class Orderbook {

    public String getOrderBook() throws Exception {
        JSONObject orderbook =  new JSONObject(readUrl("https://broker.negociecoins.com.br/api/v3/BTCBRL/ticker"));
        
        return orderbook.toString();
    }
}
