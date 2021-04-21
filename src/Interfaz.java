
import Analizador.Ambito;
import SQL.TablaSimbolosMySQL;
import SQL.ControladorSQL;
import Excel.GenerarExcel;
import Modelos.NumeroLinea;
import Analizador.Lexico;
import Analizador.Semantica1;
import Analizador.Sintaxis;
import Controladores.ControladorTokenError;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author marco
 */
public class Interfaz extends javax.swing.JFrame {

    Lexico analizadorLexico;
    Sintaxis analizadorSintaxis;
    Ambito analizadorAmbito;
    Semantica1 analizadorSemantica1;
    ControladorTokenError controladorTokenError;
    public static GenerarExcel generarExcel;
    ControladorSQL controladorSQL;
    String urlLog;

    public Interfaz() {
        initComponents();
        cargarTextArea();
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btnCargar = new javax.swing.JButton();
        btnCompilar = new javax.swing.JButton();
        btnXlsx = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaTokens = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaErrores = new javax.swing.JTable();
        btnAbrirLog = new javax.swing.JButton();
        btnEjecutarUltimaPrueba = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jRBLexico = new javax.swing.JRadioButton();
        jRBSintaxis = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));

        jPanel1.setBackground(new java.awt.Color(204, 0, 204));

        jPanel2.setBackground(new java.awt.Color(30, 30, 30));

        btnCargar.setBackground(new java.awt.Color(51, 51, 51));
        btnCargar.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnCargar.setForeground(new java.awt.Color(255, 255, 255));
        btnCargar.setText("Cargar");
        btnCargar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        btnCargar.setContentAreaFilled(false);
        btnCargar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCargar.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        btnCargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarActionPerformed(evt);
            }
        });

        btnCompilar.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnCompilar.setForeground(new java.awt.Color(255, 255, 255));
        btnCompilar.setText("Compilar");
        btnCompilar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));
        btnCompilar.setContentAreaFilled(false);
        btnCompilar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCompilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompilarActionPerformed(evt);
            }
        });

        btnXlsx.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnXlsx.setForeground(new java.awt.Color(255, 255, 255));
        btnXlsx.setText("Generar Excel");
        btnXlsx.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        btnXlsx.setContentAreaFilled(false);
        btnXlsx.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXlsx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXlsxActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Tabla de Simbolos");
        jButton1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(btnCargar, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnXlsx, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCompilar, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCompilar, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                        .addComponent(btnXlsx, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                        .addComponent(btnCargar, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)))
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        jScrollPane1.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jScrollPane1.setForeground(new java.awt.Color(255, 255, 255));

        jTextArea1.setBackground(new java.awt.Color(25, 25, 25));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(255, 255, 255));
        jTextArea1.setRows(5);
        jTextArea1.setBorder(null);
        jScrollPane1.setViewportView(jTextArea1);

        tablaTokens.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Estado", "Lexema", "Linea"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tablaTokens);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Errores");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Tokens");

        tablaErrores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Estado", "Descripcion", "Lexema", "Linea", "Tipo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tablaErrores);

        btnAbrirLog.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnAbrirLog.setForeground(new java.awt.Color(255, 255, 255));
        btnAbrirLog.setText("Abrir Log");
        btnAbrirLog.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));
        btnAbrirLog.setContentAreaFilled(false);
        btnAbrirLog.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAbrirLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirLogActionPerformed(evt);
            }
        });

        btnEjecutarUltimaPrueba.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnEjecutarUltimaPrueba.setForeground(new java.awt.Color(255, 255, 255));
        btnEjecutarUltimaPrueba.setText("Ejecutar Última Prueba");
        btnEjecutarUltimaPrueba.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));
        btnEjecutarUltimaPrueba.setContentAreaFilled(false);
        btnEjecutarUltimaPrueba.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEjecutarUltimaPrueba.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEjecutarUltimaPruebaActionPerformed(evt);
            }
        });

        btnLimpiar.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setText("Limpiar TextArea");
        btnLimpiar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        btnLimpiar.setContentAreaFilled(false);
        btnLimpiar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRBLexico);
        jRBLexico.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jRBLexico.setForeground(new java.awt.Color(255, 255, 255));
        jRBLexico.setText("Léxico");
        jRBLexico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        buttonGroup1.add(jRBSintaxis);
        jRBSintaxis.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jRBSintaxis.setForeground(new java.awt.Color(255, 255, 255));
        jRBSintaxis.setText("Sintaxis-Ambito-Semantica1");
        jRBSintaxis.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 787, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane4)
                                    .addComponent(jScrollPane3)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel1))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jRBLexico)
                                        .addGap(10, 10, 10)
                                        .addComponent(jRBSintaxis))
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnAbrirLog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnEjecutarUltimaPrueba, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE))))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEjecutarUltimaPrueba, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAbrirLog, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRBLexico)
                            .addComponent(jRBSintaxis))))
                .addGap(18, 18, 18)
                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarActionPerformed
        String aux;
        String texto;
        File archivo;
        JFileChooser select = new JFileChooser();
        select.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos txt", "txt");
        select.setFileFilter(filtro);
        int opcion = select.showOpenDialog(select);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            jTextArea1.setText("");
            try {
                archivo = new File(select.getSelectedFile() + "");
                FileReader archivos = new FileReader(archivo);
                BufferedReader leer = new BufferedReader(archivos);
                aux = leer.readLine();
                texto = aux;
                jTextArea1.append(texto);
                while ((aux = leer.readLine()) != null) {
                    texto = aux;
                    jTextArea1.append("\n" + texto);
                    System.out.println("{" + texto + "},");
                }
                leer.close();

            } catch (Exception ev) {
                JOptionPane.showMessageDialog(null, ev, "Error al abrir", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnCargarActionPerformed

    private String crearDirectorioLog() {
        LocalDateTime localDate = LocalDateTime.now();
        String url = "Log [" + localDate.getHour() + "-" + localDate.getMinute()
                + "-" + localDate.getSecond() + "]@[" + localDate.getDayOfMonth()
                + "-" + localDate.getMonth() + "" + "-" + localDate.getYear() + "]";
        File directorio = new File("logs/" + url);
        directorio.mkdir();
        return url;
    }

    private void btnCompilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompilarActionPerformed
        controladorSQL = new ControladorSQL();
        controladorSQL.limpiarTablaSimbolos();
        urlLog = crearDirectorioLog();
        establecerUltimaPrueba();
        controladorTokenError = new ControladorTokenError(tablaTokens, tablaErrores);
        analizadorLexico = new Lexico(urlLog, controladorTokenError);
        analizadorLexico.iniciarLexico(jTextArea1);
        analizadorSemantica1 = new Semantica1(controladorSQL, controladorTokenError);
        analizadorAmbito = new Ambito(controladorSQL, controladorTokenError, analizadorSemantica1);
        analizadorAmbito.iniciarAmbito();
        analizadorSintaxis = new Sintaxis(
                urlLog,
                analizadorLexico.obtenerOperToken(),
                analizadorLexico.obtenerContadorIdentificadores(),
                controladorTokenError,
                analizadorAmbito);
        analizadorSintaxis.iniciarSintaxis();
        //analizadorSemantica1.iniciarSemantica1();
        controladorTokenError.actualizarTablas();
        controladorSQL.cerrarConexion();
        reproducir();
    }//GEN-LAST:event_btnCompilarActionPerformed

    private void establecerUltimaPrueba() {
        if (jTextArea1.getText() != "") {
            String pruebaActual = jTextArea1.getText();
            File archivo = new File("UltimaPrueba.txt");
            if (archivo.exists()) {
                archivo.delete();
            }
            BufferedWriter bw;
            try {
                bw = new BufferedWriter(new FileWriter("UltimaPrueba.txt"));
                bw.write(pruebaActual);
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void btnXlsxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXlsxActionPerformed
        generarExcel = new GenerarExcel();
        generarExcel.generarExcel(
                controladorTokenError.obtenerArregloTokens(),
                controladorTokenError.obtenerArregloErrores(),
                analizadorLexico.obtenerContadoresLexico(),
                analizadorLexico.obtenerContadoresPorLineaLexico(),
                analizadorSintaxis.obtenerContadorDiagramasSintaxis(),
                analizadorAmbito.obtenerContadorEstructurasAmbito());
    }//GEN-LAST:event_btnXlsxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        TablaSimbolosMySQL ts = new TablaSimbolosMySQL();
        ts.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnAbrirLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirLogActionPerformed
        String ruta = "logs/" + urlLog;
        if (!new File(ruta).exists()) {

        } else {
            if(jRBLexico.isSelected()){
                ruta += "/"+urlLog+"-Lexico.txt";
            }else if(jRBSintaxis.isSelected()){
                ruta += "/"+urlLog+"-[Sintaxis-Ambito-Semantica1].txt";
            }
            try {
                Desktop.getDesktop().open(new File(ruta));
            } catch (IOException ex) {

            }
        }
    }//GEN-LAST:event_btnAbrirLogActionPerformed

    private void btnEjecutarUltimaPruebaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEjecutarUltimaPruebaActionPerformed
        jTextArea1.setText("");
        File ultimaPrueba = new File("UltimaPrueba.txt");
        if (ultimaPrueba.exists()) {
            FileReader archivos = null;
            try {
                archivos = new FileReader(ultimaPrueba);
                BufferedReader leer = new BufferedReader(archivos);
                try {
                    String aux = leer.readLine();
                    jTextArea1.append(aux);
                    while ((aux = leer.readLine()) != null) {
                        jTextArea1.append("\n" + aux);
                    }
                    btnCompilarActionPerformed(null);
                } catch (IOException ex) {
                    Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    archivos.close();
                } catch (IOException ex) {
                    Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_btnEjecutarUltimaPruebaActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        jTextArea1.setText("");
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void cargarTextArea() {
        jTextArea1.requestFocus();
        NumeroLinea numeroLinea = new NumeroLinea(jTextArea1);
        jScrollPane1.setRowHeaderView(numeroLinea);
    }

    private void reproducir() {
        try {
            File f = new File("src/fuerzabruta.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interfaz().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbrirLog;
    private javax.swing.JButton btnCargar;
    private javax.swing.JButton btnCompilar;
    private javax.swing.JButton btnEjecutarUltimaPrueba;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnXlsx;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JRadioButton jRBLexico;
    private javax.swing.JRadioButton jRBSintaxis;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTable tablaErrores;
    private javax.swing.JTable tablaTokens;
    // End of variables declaration//GEN-END:variables
}
