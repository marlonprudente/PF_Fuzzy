/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Marlon Prudente <m.prudente at btc-banco.com>
 * <marlonoliveira at alunos.utfpr.edu.br>
 */
public class Spec extends SecretKeySpec{
    
    public Spec(byte[] bytes, int i, int i1, String string) {
        super(bytes, i, i1, string);
        
    }
    
}
