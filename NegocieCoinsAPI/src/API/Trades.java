/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import static API.LerURL.readUrl;
import org.json.JSONArray;

/**
 *
 * @author Marlon Prudente
 */
public class Trades {

    JSONArray trades;

    public Trades() throws Exception {
        this.trades = new JSONArray(readUrl("https://broker.negociecoins.com.br/api/v3/BTCBRL/trades"));
    }
    public Trades(String moeda) throws Exception {
        this.trades = new JSONArray(readUrl("https://broker.negociecoins.com.br/api/v3/"+moeda.toUpperCase()+"BRL/trades"));
    }

    public JSONArray getTradesList() {
        return trades;
    }
}
