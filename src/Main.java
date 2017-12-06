/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marlon Prudente
 */
import net.sourceforge.jFuzzyLogic.*;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.*;
//import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

/**
 * Test parsing an FCL file
 * @author pcingola@users.sourceforge.net
 */
public class Main {
    public static void main(String[] args) throws Exception {
        // Load from 'FCL' file
        String fileName = "src/tipper_arbitragem.fcl";
        FIS fis = FIS.load(fileName,true);

        // Error while loading?
        if( fis == null ) { 
            System.err.println("Can't load file: '" + fileName + "'");
            return;
        }

        // Show 
        JFuzzyChart.get().chart(fis);

        // Set inputs
        fis.setVariable("valor_mercado", -0.5);
        fis.setVariable("valor_exchange", -1);

        // Evaluate
        fis.evaluate();

        // Show output variable's chart
        //fis.getVariable("decisao").getLatestDefuzzifiedValue();
        Variable tip = fis.getVariable("decisao");        

        JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);

        // Print ruleSet
        System.out.println(fis);
    }
}