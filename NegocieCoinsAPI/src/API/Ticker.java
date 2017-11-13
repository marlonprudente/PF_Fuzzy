/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import static API.LerURL.readUrl;
import org.json.JSONObject;

/**
 *
 * @author Usuário
 */
public class Ticker {

    String date = "";
    String high = "";
    String vol = "";
    String last = "";
    String low = "";
    String sell = "";
    String buy = "";

    JSONObject ticker = null;

    public Ticker() throws Exception {
        this.ticker = new JSONObject(readUrl("https://broker.negociecoins.com.br/api/v3/BTCBRL/ticker"));
        this.date = ticker.optString("date", "defaultValue");
        this.high = ticker.optString("high", "defaultValue");
        this.vol = ticker.optString("vol", "defaultValue");
        this.last = ticker.optString("last", "defaultValue");
        this.low = ticker.optString("low", "defaultValue");
        this.sell = ticker.optString("sell", "defaultValue");
        this.buy = ticker.optString("buy", "defaultValue");
    }

    public String getTicker() throws Exception {
        return ticker.toString();
    }

    public String getDate() {

        return this.date;
    }

    public String getHigh() {

        return this.high;
    }

    public String getVol() {

        return this.vol;
    }

    public String getLast() {

        return this.last;
    }

    public String getLow() {

        return this.low;
    }

    public String getSell() {
        return this.sell;
    }

    public String getBuy() {
        return this.buy;
    }
}
