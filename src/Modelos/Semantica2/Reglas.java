/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos.Semantica2;

/**
 *
 * @author marco
 */
public class Reglas {
    int regla;
    int linea;
    int ambito;
    boolean aceptada;

    public Reglas(int regla, int linea, int ambito, boolean aceptada) {
        this.regla = regla;
        this.linea = linea;
        this.ambito = ambito;
        this.aceptada = aceptada;
    }

    public int getRegla() {
        return regla;
    }

    public void setRegla(int regla) {
        this.regla = regla;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public int getAmbito() {
        return ambito;
    }

    public void setAmbito(int ambito) {
        this.ambito = ambito;
    }

    public boolean isAceptada() {
        return aceptada;
    }

    public void setAceptada(boolean aceptada) {
        this.aceptada = aceptada;
    }
}
