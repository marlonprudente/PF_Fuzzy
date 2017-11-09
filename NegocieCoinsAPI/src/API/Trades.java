/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import static API.LerURL.readUrl;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Usuário
 */
public class Trades {

    public String getTrades() throws Exception {
        JSONArray trades = new JSONArray(readUrl("https://broker.negociecoins.com.br/api/v3/BTCBRL/trades"));
        //System.out.println("Tamanho: " + trades.length());
        
        return trades.get(0).toString();
    }
}
