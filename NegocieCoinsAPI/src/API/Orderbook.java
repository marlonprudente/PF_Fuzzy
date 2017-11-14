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
 * @author Marlon Prudente
 */
public class Orderbook {

    JSONObject orderbook;
    JSONArray ask;
    JSONArray bid;

    public Orderbook() throws Exception {
        this.orderbook = new JSONObject(readUrl("https://broker.negociecoins.com.br/api/v3/BTCBRL/orderbook"));
        this.ask = orderbook.getJSONArray("ask");
        this.bid = orderbook.getJSONArray("bid");
    }

    public Orderbook(String moeda) throws Exception {
        this.orderbook = new JSONObject(readUrl("https://broker.negociecoins.com.br/api/v3/" + moeda.toUpperCase() + "BRL/orderbook"));
        this.ask = orderbook.getJSONArray("ask");
        this.bid = orderbook.getJSONArray("bid");
    }

    public String getOrderBook() {
        return this.orderbook.toString();
    }

    public String getAsk() {
        return this.ask.toString();
    }

    public JSONArray getAskList() {
        return this.ask;
    }

    public String getBid() {
        return this.bid.toString();
    }

    public JSONArray getBidList() {
        return this.bid;
    }

}
