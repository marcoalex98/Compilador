/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Token;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author marco
 */
public class ControladorTokenError {
    JTable tablaTokens;
    JTable tablaErrores;
    Token tokens[];
    Modelos.Error errores[];

    public ControladorTokenError(JTable tablaTokens, JTable tablaErrores) {
        this.tablaTokens = tablaTokens;
        this.tablaErrores = tablaErrores;
        inicializarVariables();
    }
    
    public Token[] obtenerArregloTokens(){
        return this.tokens;
    }
    
    public Modelos.Error[] obtenerArregloErrores(){
        return this.errores;
    }
    
    private void inicializarVariables(){
        tokens = new Token[1];
        errores = new Modelos.Error[1];
    }
    
    public void agregarToken(int estado, String lexema, int linea){
        tokens[tokens.length - 1] = new Token(estado, lexema, linea);
        aumentarArregloToken();
    }
    
    public void agregarError(int estado, String descripcion, String lexema, 
            int linea, String etapa){
        errores[errores.length - 1] = new Modelos.Error(estado, descripcion, lexema, linea, etapa);
        aumentarArregloError();
        System.out.println("<ControladorError> " +descripcion+", con lexema: "+lexema+","
                + " en la linea "+linea+", de la etapa "+etapa);
    }
    
    public void actualizarTablas(){
        DefaultTableModel modeloTabla = new DefaultTableModel(0,0);
        tablaTokens.setModel(modeloTabla);
        tablaTokens.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        modeloTabla.addColumn("Estado");
        modeloTabla.addColumn("Lexema");
        modeloTabla.addColumn("Linea");
        for (int i = 0; i < tokens.length - 1; i++) {
            modeloTabla.addRow(new Object[]{
                tokens[i].getEstado(),
                tokens[i].getLexema(),
                tokens[i].getLinea()});
        }
        modeloTabla = new DefaultTableModel(0,0);
        tablaErrores.setModel(modeloTabla);
        tablaErrores.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        modeloTabla.addColumn("Estado");
        modeloTabla.addColumn("Descripcion");
        modeloTabla.addColumn("Lexema");
        modeloTabla.addColumn("Linea");
        modeloTabla.addColumn("Tipo");
        for (int i = 0; i < errores.length - 1; i++) {
            modeloTabla.addRow(new Object[]{
                errores[i].getEstado(),
                errores[i].getDescripcion(),
                errores[i].getLexema(),
                errores[i].getLinea(),
                errores[i].getTipo()});
        }
    }
    
    private void aumentarArregloToken() {
        Token aux[] = tokens;
        tokens = new Token[tokens.length + 1];
        for (int i = 0; i < aux.length; i++) {
            tokens[i] = aux[i];
        }
    }
    
    private void aumentarArregloError() {
        Modelos.Error aux[] = errores;
        errores = new Modelos.Error[errores.length + 1];
        for (int i = 0; i < aux.length; i++) {
            errores[i] = aux[i];
        }
    }
}