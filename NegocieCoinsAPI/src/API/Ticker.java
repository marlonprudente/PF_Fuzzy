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

    public String getTicker() throws Exception {
        JSONObject ticker =  new JSONObject(readUrl("https://broker.negociecoins.com.br/api/v3/BTCBRL/ticker"));
        
        return ticker.toString();
    }
}
