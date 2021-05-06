/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author marco
 */
public class Utilities {
    private static HashSet<Integer> tokensVariables;
    private static HashMap<Integer, String> tiposVariables;

    public static boolean isConstante(int token) {
        tokensVariables = new HashSet<>();
        tokensVariables.add(-7);
        tokensVariables.add(-9);
        tokensVariables.add(-10);
        tokensVariables.add(-12);
        tokensVariables.add(-8);
        tokensVariables.add(-4);
        tokensVariables.add(-1);
        tokensVariables.add(-81);
        tokensVariables.add(-82);
        return tokensVariables.contains(token);
    }

    public static String getTipoVariable(int token) {
        tiposVariables = new HashMap<>();
        tiposVariables.put(-7, "Decimal");
        tiposVariables.put(-9, "Complejo");
        tiposVariables.put(-10, "Binario");
        tiposVariables.put(-12, "Hexadecimal");
        tiposVariables.put(-8, "Flotante");
        tiposVariables.put(-4, "Cadena");
        tiposVariables.put(-1, "Caracter");
        tiposVariables.put(-81, "Boolean");
        tiposVariables.put(-82, "Boolean");
        return tiposVariables.get(token);
    }
}
