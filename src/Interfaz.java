
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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
    PrintStream out;
    boolean areaDeclaracion = true;
    boolean banderaAux = false;
    public static GenerarExcelLexico generarExcelLexico = new GenerarExcelLexico();
    public static GenerarExcelSintaxis generarExcelSintaxis = new GenerarExcelSintaxis();
    int[] contadoresLexico = new int[21];
    int contadoresSintaxis[] = new int[21];
    int[][] contadoresLinea;
    int[] conDia = new int[21];
    int ultimoAmbitoUtilizado = 0;
    Connection con;
    ResultSet rs;
    Statement st;
    int lineas = 0, lineaActual = 0, activador2 = 0;
    int par = 0, cor = 0, lla = 0;
    int nT = 0, nR = 0, multiLinea = 0, lineaMultiComentario;
    int ambito = 0;
    Token tokens[] = new Token[nT + 1];
    Error errores[] = new Error[nR + 1];
    NodoToken cola;
    OperToken oper = new OperToken();
    int numTokens = 0;
    boolean fila7 = false, fila8 = false, multi = false, token = false, error = false,
            banPar = false, banCor = false, banLlav = false;
    LeerExcelLexico xlsLexico;
    LeerExcelSintactico xlsSintactico;
    NumeroLinea numeroLinea;
    String[][] matrizExcelLexico;
    String[][] matrizExcelSintactico;
    String[] encabezadoLexico;
    String lexemaMultiple = "", lineaHoja = "";
    String lineaAuxiliar = "";
    int ambitoActualDisponible;
    Variable variableArreglo[] = new Variable[400];
    int contadorVariablesArreglo = 0;
    ConexionMySQL mysql = new ConexionMySQL();
    Conjunto conjunto[] = new Conjunto[400];
    Diccionario diccionario[];
    Lista[] listaArreglo = new Lista[3];
    Tupla[] tuplaArreglo = new Tupla[400];
    int contadorTupla = 0;
    int arreglodeprueba[] = new int[1];
    String[][] conAmbito;
    int contadorElementosLista = 0;
    TablaSimbolos tablaSimbolos[] = new TablaSimbolos[300];
    int contadorIdentificadores = 0;
    boolean estadoError = false, banderaParametro = false,
            banderaArreglo = false, banderaTupla = false, banderaListaMultiple = false,
            banderaRango = false, banderaListaNormal = false, agregarLista = false,
            agregarTupla = false, agregarConjunto, agregarDiccionario = false;
    String rango1 = "", rango2 = "", avance = "";
    int tamArr = 0, tamVariablesGuardadasArr = 0, contadorConjunto = 0, contadorDiccionario = 0;
    int[][] matrizLexico;
    int[][] matrizSintactico;
    Stack<Integer> pilaSintaxis = new Stack<Integer>();
    Stack<Integer> pilaAmbito = new Stack<Integer>();
    String claseVariable = "";
    String nombreVariable = "";
    String auxiliarNombreVariable = "";
    String tipoVariable = "";
    String valorVariable = "";
    String ambitoCreado = "";
    String ambitoVariable = "";
    String tarrVariable = "";
    String listaPerteneceVariable = "";
    String log = "";
    boolean banderaConstante = false;
    boolean banderaFuncion = false;
    boolean banderaConjunto = false;
    boolean banderaAgregandoConjuntos = false;
    boolean banderaDiccionario = false;
    boolean asignarValor = false;
    boolean bandera814 = false;
    int ambitoMayor = 0;
    int producciones[][] = {
        {},//0
        {-48, 802, 204, 250, 801, -51, 201}, //1
        {201, -45, 804, 200, 8032, -13, 203, 803, -53, -6, 901, -111},//2
        {201, -45, 211, -42, -6},//3
        {205, -6},//4 
        {204, 250, -45},
        {205, -6, -46},//5
        {207},//6
        {208, 209},//7
        {207, -20},//8
        {207, -24},//9
        {207, -26},//10
        {207, -28},//11
        {210},//12
        {370, 226},//13
        {370, 226, 210, -22},//14
        {212, 900},//15
        {-8, 809},//16 Flotante
        {-4, 810},//17 Cadena
        {-1, 811},//18 Caracter
        {213},//19
        {-9, 812},//20 Compleja
        {-81, 813},//21 Booleana
        {-82, 813},//22 Booleana
        {218},//23
        {-83, 814},//24 None
        {214},//25
        {-7, 805},//26 Decimal
        {-10, 806},//27 Binario
        {-12, 808},//28 Hexadecimal
        {-11, 807},//29 Octal
        {216},//30
        {217, 206},//31
        {216, -17},//32
        {216, -14},//33
        {219},//34
        {8162, -13, 220, 232, 816, -53},//35
        {365},//36
        {-13, 213, -46, 213, -46, 213, -53, -84, 819},//37
        {8202, -48, 902, 222, 221, 211, 820, -51},//38
        {220, 232, -46},//39
        {211, 821, -110},//40
        {222, 221, 211, 820, -46},//41
        {224},//42
        {225, 260},//43
        {224, -30},//44
        {227},//45
        {211},//46
        {228, -6},//47
        {228, -6, -15},//48
        {228, -6, -18},//49
        {263},//50
        {229, 365},//51
        {230, 256},//52
        {-15},//53
        {-18},//54
        {258, -112},//55
        {230, 256},//56
        {232},//57
        {-13, 231, -53, -85},//58
        {-4},//59
        {233},//60
        {234, 238},//61
        {233, -35},//62
        {236},//63
        {237, 215},//64
        {236, -37},//65
        {236, -40},//66
        {239},//67
        {240, 223},//68
        {239, -33},//69
        {239, -117},//70
        {242},//71
        {243, 235},//72
        {242, -32},//73
        {245},//74
        {246, 247},//75
        {245, -34},//76
        {248},//77
        {249, 241},//78
        {248, -44},//79
        {251},//80
        {-13, 252, 232, -53, -75},//81
        {253, -53, -86},//82
        {254, 255, 250, -110, 232, -68},//83
        {-87, 255, 250, -110, 232, -116, 232, -67},//84
        {-88, 255, 250, -110, 232, -79},//85
        {-59},//86
        {-61},//87
        {232, -77},//88
        {232},//89
        {252, 232, -46},//90
        {-13},//91
        {-13, 252, 232},//92
        {254, 250, -45},//93
        {254, 255, 250, -62},//94
        {-87, 255, 250, -63},//95
        {-87},//96
        {255, 250, -45},//97
        {257},//98
        {-42},//99
        {-16},//100
        {-27},//101
        {-23},//102
        {-19},//103
        {-25},//104
        {-21},//105
        {-29},//106
        {259},//107
        {-13, -53, -89},//108
        {-13, -53, -90},//109
        {-13, 232, -53, -91},//110
        {-13, 232, -53, -92},//111
        {-13, 232, -53, -93},//112
        {-13, 232, -53, -94},//113
        {-13, 232, -53, -95},//114
        {-13, 232, -53, -96},//115
        {-13, 232, -46, 232, -53, -97},//116
        {261},//117
        {262, 244},//118
        {261, -36},//119
        {261, -38},//120
        {261, -43},//121
        {261, -31},//122
        {261, -41},//123
        {261, -39},//124
        {261, -71},//125
        {261, -72},//126
        {261, -70},//127
        {261, -98},//128
        {264},//129
        {-13, 232, -46, 232, -53, -99},//130
        {-13, 232, -46, 232, -53, -100},//131
        {-13, 232, -53, -101},//132
        {-13, 232, -46, 232, -53, -102},//133
        {-13, 232, -53, -103},//134
        {-13, -53, -104},//135
        {-13, 232, -53, -105},//136
        {-13, 232, -53, -106},//137
        {-13, 232, -53, -107},//138
        {-13, 232, -53, -108},//139
        {-13, 232, -53, -109},//140
        {8152, -47, 367, 232, 366, -52, 815},//141
        {-17},//142
        {368, 232, 366, -110},//143
        {232, 366, -110}//144
    };

    public Interfaz() {
        initComponents();
        jTextArea1.requestFocus();
        numeroLinea = new NumeroLinea(jTextArea1);
        jScrollPane1.setRowHeaderView(numeroLinea);
        File f = new File("matriz-lexico.xlsx");
        if (f.exists()) {
            xlsLexico = new LeerExcelLexico(f);
            matrizExcelLexico = xlsLexico.matrizGeneral;
            encabezadoLexico();
        }
        f = new File("matriz-sintaxis.xlsx");
        if (f.exists()) {
            xlsSintactico = new LeerExcelSintactico(f);
            matrizExcelSintactico = xlsSintactico.matrizGeneral;
            encabezadoSintactico();
        }
    }

    void encabezadoLexico() {
        encabezadoLexico = new String[51];
        for (int i = 0; i < 51; i++) {
            switch (matrizExcelLexico[0][i]) {
                case "0.0":
                    encabezadoLexico[i] = 0 + "";
                    break;
                case "1.0":
                    encabezadoLexico[i] = 1 + "";
                    break;
                case "2.0":
                    encabezadoLexico[i] = 2 + "";
                    break;
                case "3.0":
                    encabezadoLexico[i] = 3 + "";
                    break;
                case "4.0":
                    encabezadoLexico[i] = 4 + "";
                    break;
                case "5.0":
                    encabezadoLexico[i] = 5 + "";
                    break;
                case "6.0":
                    encabezadoLexico[i] = 6 + "";
                    break;
                case "7.0":
                    encabezadoLexico[i] = 7 + "";
                    break;
                case "8.0":
                    encabezadoLexico[i] = 8 + "";
                    break;
                case "9.0":
                    encabezadoLexico[i] = 9 + "";
                    break;
                default:
                    encabezadoLexico[i] = matrizExcelLexico[0][i];
            }
        }
        matrizLexico = new int[88][51];
        for (int i = 0; i < 88; i++) {
            for (int j = 0; j < 51; j++) {
                matrizLexico[i][j] = (int) Math.round(Float.parseFloat(matrizExcelLexico[i + 1][j]));
//                System.out.print(matrizLexico[i][j] + " ");
            }
//            System.out.println("");
        }
    }

    void encabezadoSintactico() {
        matrizSintactico = new int[69][95];
        for (int i = 0; i < 69; i++) {
            for (int j = 0; j < 95; j++) {
                matrizSintactico[i][j] = (int) Math.round(Float.parseFloat(matrizExcelSintactico[i + 1][j + 1]));
//                System.out.print(matrizSintactico[i][j]+"  ");
            }
//            System.out.println("");
        }
//        for (int i = 0; i < 69; i++) {
//            for (int j = 0; j < 95; j++) {
//                System.out.print(matrizSintactico[i][j] + "  ");
//            }
//            System.out.println("");
//        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btnCargar = new javax.swing.JButton();
        btnCompilar = new javax.swing.JButton();
        btnXlsx = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaTokens = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaErrores = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));

        jPanel1.setBackground(new java.awt.Color(172, 0, 0));

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        btnCargar.setBackground(new java.awt.Color(51, 51, 51));
        btnCargar.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnCargar.setForeground(new java.awt.Color(255, 255, 255));
        btnCargar.setIcon(new javax.swing.ImageIcon("C:\\Users\\marco\\Documents\\NetBeansProjects\\Compilador\\img\\btn-cargar.png")); // NOI18N
        btnCargar.setText("Cargar");
        btnCargar.setBorderPainted(false);
        btnCargar.setContentAreaFilled(false);
        btnCargar.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        btnCargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarActionPerformed(evt);
            }
        });

        btnCompilar.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnCompilar.setForeground(new java.awt.Color(255, 255, 255));
        btnCompilar.setIcon(new javax.swing.ImageIcon("C:\\Users\\marco\\Documents\\NetBeansProjects\\Compilador\\img\\btn-compilar.png")); // NOI18N
        btnCompilar.setText("Compilar");
        btnCompilar.setBorderPainted(false);
        btnCompilar.setContentAreaFilled(false);
        btnCompilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompilarActionPerformed(evt);
            }
        });

        btnXlsx.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnXlsx.setForeground(new java.awt.Color(255, 255, 255));
        btnXlsx.setIcon(new javax.swing.ImageIcon("C:\\Users\\marco\\Documents\\NetBeansProjects\\Compilador\\img\\btn-xlsx.png")); // NOI18N
        btnXlsx.setText("Excel");
        btnXlsx.setBorderPainted(false);
        btnXlsx.setContentAreaFilled(false);
        btnXlsx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXlsxActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(51, 51, 51));
        jButton2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon("C:\\Users\\marco\\Documents\\NetBeansProjects\\Compilador\\img\\btn-cerrar.png")); // NOI18N
        jButton2.setText("Salir");
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setText("Tabla de Simbolos");
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
                .addGap(31, 31, 31)
                .addComponent(btnCargar, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(btnXlsx)
                .addGap(199, 199, 199)
                .addComponent(btnCompilar)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(220, 220, 220)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnCargar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCompilar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1))
                    .addComponent(btnXlsx, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
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
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        jScrollPane1.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane1.setForeground(new java.awt.Color(255, 255, 255));

        jTextArea1.setBackground(new java.awt.Color(25, 25, 25));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(255, 255, 255));
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        tablaTokens.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
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
        jLabel1.setForeground(new java.awt.Color(153, 0, 0));
        jLabel1.setText("Errores");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 153, 0));
        jLabel2.setText("Tokens");

        tablaErrores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Estado", "Descripcion", "Lexema", "Linea"
            }
        ));
        jScrollPane4.setViewportView(tablaErrores);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(29, 29, 29)
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
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(169, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        String aux = "";
        String aux2 = "";
        String texto = "";
        String resultado = "";
        File archivo;
        String letra = "";
        JFileChooser select = new JFileChooser();
        select.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos txt", "txt");
        select.setFileFilter(filtro);
        int opcion = select.showOpenDialog(select);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            jTextArea1.setText("");
            try {
                archivo = new File(select.getSelectedFile() + "");
                if (archivo != null) {
                    FileReader archivos = new FileReader(archivo);
                    BufferedReader leer = new BufferedReader(archivos);
                    aux = leer.readLine();
                    texto = aux;
                    jTextArea1.append(texto);
                    while ((aux = leer.readLine()) != null) {
                        aux2 = aux;
                        texto = aux;
                        jTextArea1.append("\n" + texto);
                        System.out.println("{" + texto + "},");
                    }
                    leer.close();
                }
            } catch (Exception ev) {
                JOptionPane.showMessageDialog(null, ev, "Error al abrir", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnCargarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        System.exit(0);  // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnCompilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompilarActionPerformed
       File f = createFileToLog();
        try{
            FileOutputStream fos = new FileOutputStream(f);
            out = new PrintStream(fos);
            System.setOut(out);
        }catch(Exception e){
            
        }
        
        contadorVariablesArreglo = 0;
        String query = "DELETE FROM tablasimbolos";
        try {
            System.out.println("<MySQL> Prueba de conexion a MySQL");
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/a16130329?verifyServerCertificate=false&useSSL=true", "root", "root");
            st = con.createStatement();
            st.executeUpdate(query);
             
        } catch (SQLException e) {
            System.err.println("<MySQL> Error de MySQL");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }
        nT = 0;
        nR = 0;
        tamArr = 0;
        lineaActual = 0;
        claseVariable = "";
        nombreVariable = "";
        ambito = 0;
        ambitoActualDisponible = 0;
        pilaAmbito.push(ambitoActualDisponible);
        ambitoActualDisponible++;
        tokens = new Token[nT + 1];
        errores = new Error[nR + 1];
        contadoresLexico = new int[21];
        tamVariablesGuardadasArr = 0;
        conDia = new int[21];
        oper = new OperToken();
        limpiarTablas();
        String texto = jTextArea1.getText();
        String[] lineasHoja = texto.split("\n");
        System.out.println("Texto original:\n" + texto);
        System.out.println("Texto modificado:\n");
        for (int i = 0; i < lineasHoja.length; i++) {
            System.out.println("Linea " + i + ": " + lineasHoja[i]);
            addToLog(lineasHoja[i]);
        }
        lineas = lineasHoja.length;
        System.out.println("Lineas del documento: " + lineas);
        contadoresLinea = new int[lineas][21];
        establecerConexion();
        for (int i = 0; i < lineasHoja.length; i++) {//Lineas del textarea
            lineaActual++;
            int pos = 0;
            char c[] = new char[lineasHoja[i].length()];//Arreglo de char para la linea i
            System.out.println("Largo de arreglo de la linea [" + i + "]: " + c.length);
            for (int j = 0; j < c.length; j++) {//Convertir la lina a un arreglo de caracter por caracter
                c[j] = lineasHoja[i].charAt(j);
                System.out.println("j: " + j + ", contenido: " + c[j]);
            }
            try {
                lineaAuxiliar = "";
                for (int j = 0; j < c.length; j++) {
                    lineaAuxiliar += c[j];
                }
                lineaAuxiliar = lineaAuxiliar.replaceAll("^\\s*", "");
                analizador(c);
//                imprimir();
//                insertarProgram();
            } catch (Exception e) {
                System.err.println("Excepcion: " + e.getMessage());
            }
        }
        System.out.println("Sintaxis");
        try{
            actualizarTablaToken(tokens);
            actualizarTablaError(errores);
        }catch(Exception e){
            
        }
        
        sintaxis();
        System.out.println("Fin sintaxis");
        
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        try (FileWriter fr = new FileWriter(f);
//                BufferedWriter bw = new BufferedWriter(fr)) {
//                bw.write(log);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        
    }//GEN-LAST:event_btnCompilarActionPerformed

    void contadoresSintaxis(int produccion) {
        System.out.println("---Contadores Sintaxis---");
        System.out.println("Produccion: " + produccion);

        switch (produccion) {
            case 200://Program
                contadoresSintaxis[0]++;
                break;
            case 205://TerminoPascal
                contadoresSintaxis[4]++;
                break;
            case 208://Constante
                contadoresSintaxis[1]++;
                break;
            case 210://Elevacion
                contadoresSintaxis[5]++;
                break;
            case 213://ConstEntero
                contadoresSintaxis[2]++;
                break;
            case 215://ListUpRangos
                contadoresSintaxis[3]++;
                break;
            case 220://SimpleExpPas
                contadoresSintaxis[6]++;
                break;
            case 223://Or
                contadoresSintaxis[9]++;
                break;
            case 226://OpBit
                contadoresSintaxis[10]++;
                break;
            case 229://And
                contadoresSintaxis[11]++;
                break;
            case 232://AndLog
                contadoresSintaxis[12]++;
                break;
            case 235://OrLog
                contadoresSintaxis[13]++;
                break;
            case 238://XOrLog
                contadoresSintaxis[14]++;
                break;
            case 241://Factor
                contadoresSintaxis[7]++;
                break;
            case 247://Not
                contadoresSintaxis[8]++;
                break;
            case 250://Est
                contadoresSintaxis[15]++;
                break;
            case 256://ExpPas
                contadoresSintaxis[20]++;
                break;
            case 259://Funciones
                contadoresSintaxis[19]++;
                break;
            case 261://Funlist
                contadoresSintaxis[17]++;
                break;
            case 263://Asign
                contadoresSintaxis[16]++;
                break;
            case 265://Arr
                contadoresSintaxis[18]++;
                break;
        }
    }

    void contadoresLinea() {
        contadoresLinea = new int[lineaActual][21];
        for (int i = 0; i < tokens.length - 1; i++) {
            switch (tokens[i].getEstado()) {
                case -6:
//                contadoresLexico[1]++;
                    contadoresLinea[tokens[i].getLinea() - 1][1]++;
                    break;
                case -3:
                case -5:
//                contadoresLexico[2]++;
                    contadoresLinea[tokens[i].getLinea() - 1][2]++;
                    break;
                case -56://Palabras reservadas
                case -57:
                case -58:
                case -59:
                case -60:
                case -61:
                case -62:
                case -63:
                case -64:
                case -65:
                case -66:
                case -67:
                case -68:
                case -69:
                case -73:
                case -74:
                case -75:
                case -76:
                case -77:
                case -78:
                case -79:
                case -80:
                case -81:
                case -82:
                case -83:
                case -84:
                case -85:
                case -86:
                case -87:
                case -88:
                case -89:
                case -90:
                case -91:
                case -92:
                case -93:
                case -94:
                case -95:
                case -96:
                case -97:
                case -99:
                case -100:
                case -101:
                case -102:
                case -103:
                case -104:
                case -105:
                case -106:
                case -107:
                case -108:
                case -109:
                case -111:
                case -116:
//                contadoresLexico[3]++;
                    contadoresLinea[tokens[i].getLinea() - 1][3]++;
                    break;
                case -7:
//                contadoresLexico[4]++;
                    contadoresLinea[tokens[i].getLinea() - 1][4]++;
                    break;
                case -10:
//                contadoresLexico[5]++;
                    contadoresLinea[tokens[i].getLinea() - 1][5]++;
                    break;
                case -12:
//                contadoresLexico[6]++;
                    contadoresLinea[tokens[i].getLinea() - 1][6]++;
                    break;
                case -11:
//                contadoresLexico[7]++;
                    contadoresLinea[tokens[i].getLinea() - 1][7]++;
                    break;
                case -2:
                case -4:
//                contadoresLexico[8]++;
                    contadoresLinea[tokens[i].getLinea() - 1][8]++;
                    break;
                case -8:
//                contadoresLexico[9]++;
                    contadoresLinea[tokens[i].getLinea() - 1][9]++;
                    break;
                case -9:
//                contadoresLexico[10]++;
                    contadoresLinea[tokens[i].getLinea() - 1][10]++;
                    break;
                case -1:
//                contadoresLexico[11]++;
                    contadoresLinea[tokens[i].getLinea() - 1][11]++;
                    break;
                case -14:
                case -17:
                case -20:
                case -22:
                case -24:
                case -26:
                case -28:
//                contadoresLexico[12]++;
                    contadoresLinea[tokens[i].getLinea() - 1][12]++;
                    break;
                case -15:
                case -18:
//                contadoresLexico[13]++;
                    contadoresLinea[lineaActual - 1][13]++;
                    break;
                case -30:
                case -33:
                case -35:
                case -117:
//                contadoresLexico[14]++;
                    contadoresLinea[tokens[i].getLinea() - 1][14]++;
                    break;
                case -32:
                case -34:
                case -44:
                case -37:
                case -40:
//                contadoresLexico[15]++;
                    contadoresLinea[tokens[i].getLinea() - 1][15]++;
                    break;
                case -71: //is    Identidad
                case -72: //is not
                case -98:
                case -70:
//                contadoresLexico[16]++;
                    contadoresLinea[tokens[i].getLinea() - 1][16]++;
                    break;
                case -36:
                case -38:
                case -39:
                case -41:
                case -43:
                case -31:
//                contadoresLexico[17]++;
                    contadoresLinea[tokens[i].getLinea() - 1][17]++;
                    break;
                case -45:
                case -46:
                case -112:
                case -110:
//                contadoresLexico[18]++;
                    contadoresLinea[tokens[i].getLinea() - 1][18]++;
                    break;
                case -13:
                case -47:
                case -48:
                case -53:
                case -51:
                case -52:
//                contadoresLexico[19]++;
                    contadoresLinea[tokens[i].getLinea() - 1][19]++;
                    break;
                case -42:
                case -16:
                case -19:
                case -23:
                case -21:
                case -27:
                case -25:
                case -29:
//                contadoresLexico[20]++;
                    contadoresLinea[tokens[i].getLinea() - 1][20]++;
                    break;
                default:
                    System.err.println("Ha llegado un contador no valido: " + tokens[i].getEstado());
            }
        }
        for (int i = 0; i < errores.length - 1; i++) {
            System.out.println("For: " + i);
            contadoresLinea[errores[i].getLinea()][0]++;
        }

    }

    public void contadorDiagrama(int i) {
        System.out.println("SE AUMENTO EL CONTADOR DE " + i);
        switch (i) {
            case 1://Program
                conDia[0]++;
                break;
            case 9://Constante
                conDia[1]++;
                break;
            case 14://ConstEntero
                conDia[2]++;
                break;
            case 16://ListUpRangos
                conDia[3]++;
                break;
            case 6://TerminoPascal
                conDia[4]++;
                break;
            case 31://Elevacion
                conDia[5]++;
                break;
            case 35://Simple ExpPas
                conDia[6]++;
                break;
            case 43://Factor
                conDia[7]++;
                break;
            case 46://Not
                conDia[8]++;
                break;
            case 61://Or
                conDia[9]++;
                break;
            case 64://Opbit
                conDia[10]++;
                break;
            case 68://And
                conDia[11]++;
                break;
            case 72://Andlog
                conDia[12]++;
                break;
            case 75://Orlog
                conDia[13]++;
                break;
            case 78://Xorlog
                conDia[14]++;
                break;
            case 81://Est
                conDia[15]++;
                break;
            case 99://Asign
                conDia[16]++;
                break;
            case 108://Funlist
                conDia[17]++;
                break;
            case 118://Arr
                conDia[18]++;
                break;
            case 130://Funciones
                conDia[19]++;
                break;
            case 142://ExpPas
                conDia[20]++;
                break;

        }
    }

    void contadoresLexico(int estado) {
        switch (estado) {
            case -6:
                contadoresLexico[1]++;
                contadoresLinea[lineaActual - 1][1]++;
                break;
            case -3:
            case -5:
                contadoresLexico[2]++;
                contadoresLinea[lineaActual - 1][2]++;
                break;
            case -56://Palabras reservadas
            case -57:
            case -58:
            case -59:
            case -60:
            case -61:
            case -62:
            case -63:
            case -64:
            case -65:
            case -66:
            case -67:
            case -68:
            case -69:
            case -73:
            case -74:
            case -75:
            case -76:
            case -77:
            case -78:
            case -79:
            case -80:
            case -81:
            case -82:
            case -83:
            case -84:
            case -85:
            case -86:
            case -87:
            case -88:
            case -89:
            case -90:
            case -91:
            case -92:
            case -93:
            case -94:
            case -95:
            case -96:
            case -97:
            case -99:
            case -100:
            case -101:
            case -102:
            case -103:
            case -104:
            case -105:
            case -106:
            case -107:
            case -108:
            case -109:
            case -111:
            case -116:
                contadoresLexico[3]++;
                contadoresLinea[lineaActual - 1][3]++;
                break;
            case -7:
                contadoresLexico[4]++;
                contadoresLinea[lineaActual - 1][4]++;
                break;
            case -10:
                contadoresLexico[5]++;
                contadoresLinea[lineaActual - 1][5]++;
                break;
            case -12:
                contadoresLexico[6]++;
                contadoresLinea[lineaActual - 1][6]++;
                break;
            case -11:
                contadoresLexico[7]++;
                contadoresLinea[lineaActual - 1][7]++;
                break;
            case -2:
            case -4:
                contadoresLexico[8]++;
                contadoresLinea[lineaActual - 1][8]++;
                break;
            case -8:
                contadoresLexico[9]++;
                contadoresLinea[lineaActual - 1][9]++;
                break;
            case -9:
                contadoresLexico[10]++;
                contadoresLinea[lineaActual - 1][10]++;
                break;
            case -1:
                contadoresLexico[11]++;
                contadoresLinea[lineaActual - 1][11]++;
                break;
            case -14:
            case -17:
            case -20:
            case -22:
            case -24:
            case -26:
            case -28:
                contadoresLexico[12]++;
                contadoresLinea[lineaActual - 1][12]++;
                break;
            case -15:
            case -18:
                contadoresLexico[13]++;
                contadoresLinea[lineaActual - 1][13]++;
                break;
            case -30:
            case -33:
            case -35:
            case -117:
                contadoresLexico[14]++;
                contadoresLinea[lineaActual - 1][14]++;
                break;
            case -32:
            case -34:
            case -44:
            case -37:
            case -40:
                contadoresLexico[15]++;
                contadoresLinea[lineaActual - 1][15]++;
                break;
            case -71: //is    Identidad
            case -72: //is not
            case -98:
            case -70:
                contadoresLexico[16]++;
                contadoresLinea[lineaActual - 1][16]++;
                break;
            case -36:
            case -38:
            case -39:
            case -41:
            case -43:
            case -31:
                contadoresLexico[17]++;
                contadoresLinea[lineaActual - 1][17]++;
                break;
            case -45:
            case -46:
            case -112:
            case -110:
                contadoresLexico[18]++;
                contadoresLinea[lineaActual - 1][18]++;
                break;
            case -13:
            case -47:
            case -48:
            case -53:
            case -51:
            case -52:
                contadoresLexico[19]++;
                contadoresLinea[lineaActual - 1][19]++;
                break;
            case -42:
            case -16:
            case -19:
            case -23:
            case -21:
            case -27:
            case -25:
            case -29:
                contadoresLexico[20]++;
                contadoresLinea[lineaActual - 1][20]++;
                break;
            default:
                System.err.println("Ha llegado un contador no valido");
        }
    }

    int filaSintactico(int produccion) {
        System.out.println("<FILA SINTACTICO> Valor de produccion: " + produccion);
        int fil;
        switch (produccion) {
            case 0:
                fil = 0;
                break;
            case 1:
                fil = 0;
                break;
            case 2:
                fil = 1;
                break;
            case 3:
                fil = 1;
                break;
            case 4:
                fil = 2;
                break;
            case 5:
                fil = 3;
                break;
            case 6:
                fil = 4;
                break;
            case 7:
                fil = 5;
                break;
            case 8:
                fil = 6;
                break;
            case 9:
                fil = 6;
                break;
            case 10:
                fil = 6;
                break;
            case 11:
                fil = 6;
                break;
            case 12:
                fil = 6;
                break;
            case 13:
                fil = 6;
                break;
            case 14:
                fil = 6;
                break;
            case 15:
                fil = 6;
                break;
            case 16:
                fil = 7;
                break;
            case 17:
                fil = 8;
                break;
            case 18:
                fil = 8;
                break;
            case 19:
                fil = 8;
                break;
            case 20:
                fil = 8;
                break;
            case 21:
                fil = 9;
                break;
            case 22:
                fil = 10;
                break;
            case 23:
                fil = 10;
                break;
            case 24:
                fil = 10;
                break;
            case 25:
                fil = 10;
                break;
            case 26:
                fil = 10;
                break;
            case 27:
                fil = 10;
                break;
            case 28:
                fil = 10;
                break;
            case 29:
                fil = 10;
                break;
            case 30:
                fil = 10;
                break;
            case 31:
                fil = 11;
                break;
            case 32:
                fil = 12;
                break;
            case 33:
                fil = 12;
                break;
            case 34:
                fil = 13;
                break;
            case 35:
                fil = 13;
                break;
            case 36:
                fil = 14;
                break;
            case 37:
                fil = 14;
                break;
            case 38:
                fil = 15;
                break;
            case 39:
                fil = 16;
                break;
            case 40:
                fil = 17;
                break;
            case 41:
                fil = 18;
                break;
            case 42:
                fil = 19;
                break;
            case 43:
                fil = 20;
                break;
            case 44:
                fil = 21;
                break;
            case 45:
                fil = 21;
                break;
            case 46:
                fil = 21;
                break;
            case 47:
                fil = 21;
                break;
            case 48:
                fil = 22;
                break;
            case 49:
                fil = 23;
                break;
            case 50:
                fil = 24;
                break;
            case 51:
                fil = 25;
                break;
            case 52:
                fil = 26;
                break;
            case 53:
                fil = 26;
                break;
            case 54:
                fil = 26;
                break;
            case 55:
                fil = 26;
                break;
            case 56:
                fil = 26;
                break;
            case 57:
                fil = 27;
                break;
            case 58:
                fil = 27;
                break;
            case 59:
                fil = 27;
                break;
            case 60:
                fil = 27;
                break;
            case 61:
                fil = 27;
                break;
            case 62:
                fil = 28;
                break;
            case 63:
                fil = 29;
                break;
            case 64:
                fil = 29;
                break;
            case 65:
                fil = 30;
                break;
            case 66:
                fil = 31;
                break;
            case 67:
                fil = 32;
                break;
            case 68:
                fil = 33;
                break;
            case 69:
                fil = 33;
                break;
            case 70:
                fil = 34;
                break;
            case 71:
                fil = 35;
                break;
            case 72:
                fil = 36;
                break;
            case 73:
                fil = 37;
                break;
            case 74:
                fil = 38;
                break;
            case 75:
                fil = 39;
                break;
            case 76:
                fil = 39;
                break;
            case 77:
                fil = 40;
                break;
            case 78:
                fil = 41;
                break;
            case 79:
                fil = 42;
                break;
            case 80:
                fil = 42;
                break;
            case 81:
                fil = 43;
                break;
            case 82:
                fil = 44;
                break;
            case 83:
                fil = 45;
                break;
            case 84:
                fil = 46;
                break;
            case 85:
                fil = 47;
                break;
            case 86:
                fil = 48;
                break;
            case 87:
                fil = 49;
                break;
            case 88:
                fil = 50;
                break;
            case 89:
                fil = 51;
                break;
            case 90:
                fil = 52;
                break;
            case 91:
                fil = 53;
                break;
            case 92:
                fil = 53;
                break;
            case 93:
                fil = 53;
                break;
            case 94:
                fil = 53;
                break;
            case 95:
                fil = 53;
                break;
            case 96:
                fil = 53;
                break;
            case 97:
                fil = 53;
                break;
            case 98:
                fil = 53;
                break;
            case 99:
                fil = 53;
                break;
            case 100:
                fil = 54;
                break;
            case 101:
                fil = 55;
                break;
            case 102:
                fil = 55;
                break;
            case 103:
                fil = 56;
                break;
            case 104:
                fil = 56;
                break;
            case 105:
                fil = 56;
                break;
            case 106:
                fil = 56;
                break;
            case 107:
                fil = 57;
                break;
            case 108:
                fil = 58;
                break;
            case 109:
                fil = 59;
                break;
            case 110:
                fil = 59;
                break;
            case 111:
                fil = 59;
                break;
            case 112:
                fil = 59;
                break;
            case 113:
                fil = 59;
                break;
            case 114:
                fil = 59;
                break;
            case 115:
                fil = 59;
                break;
            case 116:
                fil = 59;
                break;
            case 117:
                fil = 59;
                break;
            case 118:
                fil = 60;
                break;
            case 119:
                fil = 61;
                break;
            case 120:
                fil = 62;
                break;
            case 121:
                fil = 63;
                break;
            case 122:
                fil = 64;
                break;
            case 123:
                fil = 65;
                break;
            case 124:
                fil = 66;
                break;
            case 125:
                fil = 66;
                break;
            case 126:
                fil = 66;
                break;
            case 127:
                fil = 66;
                break;
            case 128:
                fil = 66;
                break;
            case 129:
                fil = 66;
                break;
            case 130:
                fil = 66;
                break;
            case 131:
                fil = 66;
                break;
            case 132:
                fil = 66;
                break;
            case 133:
                fil = 64;
                break;
            case 134:
                fil = 67;
                break;
            case 135:
                fil = 68;
                break;
            case 136:
                fil = 68;
                break;
            case 137:
                fil = 68;
                break;
            case 138:
                fil = 68;
                break;
            case 139:
                fil = 68;
                break;
            case 140:
                fil = 68;
                break;
            case 141:
                fil = 68;
                break;
            case 142:
                fil = 68;
                break;
            case 143:
                fil = 68;
                break;
            case 144:
                fil = 68;
                break;
            case 145:
                fil = 68;
                break;
            default:
                fil = produccion;
        }
        return fil;
    }

    int columnaSintactico(int token) {
        System.out.println("<COLUMNA SINTACTICO> Valor de token: " + token);
        int col;
        switch (token) {
            case -1:
                col = 30;
                break;
            case -2:
                col = 29;
                break;
            case -3:
                col = 29;
                break;
            case -4:
                col = 29;
                break;
            case -6:
                col = 27;
                break;
            case -7:
                col = 16;
                break;
            case -8:
                col = 28;
                break;
            case -9:
                col = 31;
                break;
            case -10:
                col = 17;
                break;
            case -11:
                col = 19;
                break;
            case -12:
                col = 18;
                break;
            case -13:
                col = 21;
                break;
            case -14:
                col = 4;
                break;
            case -15:
                col = 40;
                break;
            case -16:
                col = 9;
                break;
            case -17:
                col = 5;
                break;
            case -18:
                col = 41;
                break;
            case -19:
                col = 12;
                break;
            case -20:
                col = 6;
                break;
            case -21:
                col = 14;
                break;
            case -22:
                col = 38;
                break;
            case -23:
                col = 11;
                break;
            case -24:
                col = 7;
                break;
            case -25:
                col = 13;
                break;
            case -26:
                col = 37;
                break;
            case -27:
                col = 10;
                break;
            case -28:
                col = 39;
                break;
            case -29:
                col = 15;
                break;
            case -30:
                col = 36;
                break;
            case -31:
                col = 74;
                break;
            case -32:
                col = 47;
                break;
            case -33:
                col = 44;
                break;
            case -34:
                col = 46;
                break;
            case -35:
                col = 43;
                break;
            case -36:
                col = 71;
                break;
            case -37:
                col = 48;
                break;
            case -38:
                col = 72;
                break;
            case -39:
                col = 76;
                break;
            case -40:
                col = 49;
                break;
            case -41:
                col = 75;
                break;
            case -42:
                col = 8;
                break;
            case -43:
                col = 73;
                break;
            case -44:
                col = 50;
                break;
            case -45:
                col = 2;
                break;
            case -46:
                col = 1;
                break;
            case -47:
                col = 23;
                break;
            case -48:
                col = 25;
                break;
            case -51:
                col = 24;
                break;
            case -52:
                col = 22;
                break;
            case -53:
                col = 20;
                break;
            case -59:
                col = 58;
                break;
            case -61:
                col = 59;
                break;
            case -62:
                col = 61;
                break;
            case -63:
                col = 92;
                break;
            case -67:
                col = 55;
                break;
            case -68:
                col = 53;
                break;
            case -70:
                col = 79;
                break;
            case -71:
                col = 77;
                break;
            case -72:
                col = 78;
                break;
            case -75:
                col = 51;
                break;
            case -77:
                col = 60;
                break;
            case -79:
                col = 56;
                break;
            case -81:
                col = 32;
                break;
            case -82:
                col = 33;
                break;
            case -83:
                col = 34;
                break;
            case -84:
                col = 35;
                break;
            case -85:
                col = 42;
                break;
            case -86:
                col = 52;
                break;
            case -87:
                col = 54;
                break;
            case -88:
                col = 57;
                break;
            case -89:
                col = 62;
                break;
            case -90:
                col = 63;
                break;
            case -91:
                col = 64;
                break;
            case -92:
                col = 65;
                break;
            case -93:
                col = 66;
                break;
            case -94:
                col = 67;
                break;
            case -95:
                col = 68;
                break;
            case -96:
                col = 69;
                break;
            case -97:
                col = 70;
                break;
            case -98:
                col = 80;
                break;
            case -99:
                col = 81;
                break;
            case -100:
                col = 82;
                break;
            case -101:
                col = 83;
                break;
            case -102:
                col = 84;
                break;
            case -103:
                col = 85;
                break;
            case -104:
                col = 86;
                break;
            case -105:
                col = 87;
                break;
            case -106:
                col = 88;
                break;
            case -107:
                col = 89;
                break;
            case -108:
                col = 90;
                break;
            case -109:
                col = 91;
                break;
            case -110:
                col = 3;
                break;
            case -111:
                col = 26;
                break;
            case -112:
                col = 0;
                break;
            case -1000:
                col = 94;
                break;
            default:
                col = -1;
        }
        return col;
    }

    void insertarProgram() {
        for (int i = 0; i < producciones[0].length; i++) {
            System.out.println(producciones[0][i]);
            pilaSintaxis.push(producciones[0][i]);
        }

    }

    void imprimir() {
        oper.mostrarDatos();
    }

    private void btnXlsxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXlsxActionPerformed
        System.out.println("Tamaño de nT: " + nT);
        Token auxToken[] = new Token[nT];
        for (int i = 0; i < auxToken.length; i++) {
            auxToken[i] = tokens[i];
        }
        System.out.println("Tamaño de nR: " + nR);
        Error auxError[] = new Error[nR];
        for (int i = 0; i < auxError.length; i++) {
            auxError[i] = errores[i];
        }

        String ruta = "MarcoAlejandro-Marcial-Coronado-LexicoSintaxis.xls";

        for (int i = 0; i < auxToken.length; i++) {
            contadoresLexico(auxToken[i].getEstado());
        }
        System.out.println("Tamaño de nR");
//        System.out.println("Error meco: "+auxError[0].getEstado());
        contadoresLinea();
        System.out.println("GENERADOR DEL EXCEL------");
        generarExcelLexico.generarExcel(auxToken, auxError, ruta, contadoresLexico, contadoresLinea,
                conDia, conAmbito);

    }//GEN-LAST:event_btnXlsxActionPerformed
    void contadorAmbito() {
        /////////////////////CREACION DE ARREGLOS DE CONTEO DE VARIABLES Y AMBITOS
        conAmbito = new String[(ambitoMayor + 4)][19];
        for (int i = 0; i < conAmbito.length; i++) {
            for (int j = 0; j < conAmbito[i].length; j++) {
                conAmbito[i][j] = 0 + "";
            }
        }
        conAmbito[ambitoMayor + 2][0] = "-";
        conAmbito[ambitoMayor + 2][1] = "Tot_Decimal";
        conAmbito[ambitoMayor + 2][2] = "Tot_Binario";
        conAmbito[ambitoMayor + 2][3] = "Tot_Octal";
        conAmbito[ambitoMayor + 2][4] = "Tot_Hexadecimal";
        conAmbito[ambitoMayor + 2][5] = "Tot_Flotante";
        conAmbito[ambitoMayor + 2][6] = "Tot_Cadena";
        conAmbito[ambitoMayor + 2][7] = "Tot_Carácter";
        conAmbito[ambitoMayor + 2][8] = "Tot_Compleja";
        conAmbito[ambitoMayor + 2][9] = "Tot_Booleana";
        conAmbito[ambitoMayor + 2][10] = "Tot_None";
        conAmbito[ambitoMayor + 2][11] = "Tot_Arreglo";
        conAmbito[ambitoMayor + 2][12] = "Tot_Tuplas";
        conAmbito[ambitoMayor + 2][13] = "Tot_Lista";
        conAmbito[ambitoMayor + 2][14] = "Tot_Registro";
        conAmbito[ambitoMayor + 2][15] = "Tot_Rango";
        conAmbito[ambitoMayor + 2][16] = "Tot_Conjuntos";
        conAmbito[ambitoMayor + 2][17] = "Tot_Diccionarios";
        conAmbito[ambitoMayor + 2][18] = "TotalGeneral";

        try {
            conAmbito[0][0] = "Ambito";
            conAmbito[0][1] = "Decimal";
            conAmbito[0][2] = "Binario";
            conAmbito[0][3] = "Octal";
            conAmbito[0][4] = "Hexadecimal";
            conAmbito[0][5] = "Flotante";
            conAmbito[0][6] = "Cadena";
            conAmbito[0][7] = "Carácter";
            conAmbito[0][8] = "Compleja";
            conAmbito[0][9] = "Booleana";
            conAmbito[0][10] = "None";
            conAmbito[0][11] = "Arreglo";
            conAmbito[0][12] = "Tuplas";
            conAmbito[0][13] = "Lista";
            conAmbito[0][14] = "Registro";
            conAmbito[0][15] = "Rango";
            conAmbito[0][16] = "Conjuntos";
            conAmbito[0][17] = "DicsumatoriaTotalVariablesEnAmbitocionarios";
            conAmbito[0][18] = "TotalAmbitos";
            int j = 0, sumatoriaTotalVariablesEnAmbito = 0;
            for (int i = 1; i < ambitoMayor + 2; i++) {
                if (j <= ambito) {
                    conAmbito[i][0] = j + "";
                    conAmbito[i][1] = obtenerNumeroBD(j + "", "Decimal", "Tipo") + "";
                    conAmbito[i][2] = obtenerNumeroBD(j + "", "Binario", "Tipo") + "";
                    conAmbito[i][3] = obtenerNumeroBD(j + "", "Octal", "Tipo") + "";
                    conAmbito[i][4] = obtenerNumeroBD(j + "", "Hexadecimal", "Tipo") + "";
                    conAmbito[i][5] = obtenerNumeroBD(j + "", "FLotante", "Tipo") + "";
                    conAmbito[i][6] = obtenerNumeroBD(j + "", "Cadena", "Tipo") + "";
                    conAmbito[i][7] = obtenerNumeroBD(j + "", "Caracter", "Tipo") + "";
                    conAmbito[i][8] = obtenerNumeroBD(j + "", "Compleja", "Tipo") + "";
                    conAmbito[i][9] = obtenerNumeroBD(j + "", "Booleana", "Tipo") + "";
                    conAmbito[i][10] = obtenerNumeroBD(j + "", "None", "Tipo") + "";
                    conAmbito[i][11] = obtenerNumeroBD(j + "", "Arreglo", "Clase") + "";
                    conAmbito[i][12] = obtenerNumeroBD(j + "", "Tuplas", "Clase") + "";
                    conAmbito[i][13] = obtenerNumeroBD(j + "", "Lista", "Clase") + "";
                    conAmbito[i][14] = obtenerNumeroBD(j + "", "Registro", "Clase") + "";
                    conAmbito[i][15] = obtenerNumeroBD(j + "", "Rango", "Clase") + "";
                    conAmbito[i][16] = obtenerNumeroBD(j + "", "Conjunto", "Clase") + "";
                    conAmbito[i][17] = obtenerNumeroBD(j + "", "Diccionario", "Clase") + "";
                    for (int k = 1; k < conAmbito[i].length; k++) {
                        sumatoriaTotalVariablesEnAmbito += Integer.parseInt(conAmbito[i][k]);
                    }
                    conAmbito[i][18] = (sumatoriaTotalVariablesEnAmbito) + "";
                    sumatoriaTotalVariablesEnAmbito = 0;
                    j++;
                }
            }
            int sumatoriaDecimales = 0;
            for (int q = 1; q < 19; q++) {
                for (int i = 1; i < ambitoMayor + 2; i++) {
                    sumatoriaDecimales += Integer.parseInt(conAmbito[i][q]);
                    if (i == ambitoMayor + 1) {
                        conAmbito[ambitoMayor + 3][q] = sumatoriaDecimales + "";
                        sumatoriaDecimales = 0;
                    }
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int obtenerNumeroBD(String ambito, String campo, String tipoOClase) throws SQLException {
        int total = 0;
        Connection con = null;
        try {
            System.out.println("<MySQL:agregarVariable> Prueba de conexion a MySQL");
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/a16130329?verifyServerCertificate=false&useSSL=true", "root", "root");
            st = con.createStatement();
             
        } catch (SQLException e) {
            System.err.println("<MySQL:agregarVariable> Error de MySQL");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResultSet rs = traer("SELECT * FROM tablasimbolos WHERE Ambito=" + "'" + ambito + "'" + " AND " + tipoOClase + "= '" + campo + "'");
        while (rs.next()) {
            total++;
        }
        return total;

    }

    public ResultSet traer(String Consulta) {
        Statement st;
        ResultSet datos = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/a16130329?verifyServerCertificate=false&useSSL=true", "root", "root");
            st = con.createStatement();
            datos = st.executeQuery(Consulta);
             
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return datos;
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        TablaSimbolosMySQL ts = new TablaSimbolosMySQL();
        ts.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed
    int asignarColumna(char c) {
        int col = 0;
        boolean igual = false;
        if (c == 10) {
            col = 1;
            igual = true;
        } else if (c == 32) {
            col = 2;
            igual = true;
        } else if (c == 10 || c == '\t') {
            col = 3;
            igual = true;
        } else if (c == 'a' | c == 'A') {
            col = 14;
            igual = true;
        } else if (c == 'c' | c == 'C') {
            col = 16;
            igual = true;
        } else if (c == 'd' | c == 'D') {
            col = 17;
            igual = true;
        } else if (c == 'e' | c == 'E') {
            col = 18;
            igual = true;
        } else if (c == 'f' | c == 'F') {
            col = 19;
            igual = true;
        } else if (((c >= 103 && c <= 105) || (c >= 107 && c <= 119) || (c >= 121 && c <= 122)) || c == 'ñ') {
            col = 23;
            igual = true;
        } else if ((c >= 71 && c <= 90) || c == 'Ñ') {
            col = 24;
            igual = true;
        } else if (c == '.') {
            col = 48;
            igual = true;
        } else if (c == ':') {
            col = 46;
            igual = true;
        } else if (c == ',') {
            col = 47;
        } else {
            for (int j = 0; j < encabezadoLexico.length; j++) {//Comparar si existe el elemento char en los encabezados
                if (encabezadoLexico[j].equals(c + "")) {
                    igual = true;
                    break;
                }
                if (c == '(') {
                    par++;
                }
                if (c == '[') {
                    cor++;
                }
                if (c == '{') {
                    lla++;
                }
                if (igual == false) {
                    col++;
                }
            }
        }
        if (igual = false) {
            col = 50;
        }
        if (col == 51) {
            col = 50;
        }
        return col;
    }

    String deslexemizador(String lexemaTokenizado) {//Elimina de la linea actual el lexema tokenizado
        System.out.println("[DESLEXEMIZADOR] Lexema a eliminar: " + lexemaTokenizado);
        System.out.println("[DESLEXEMIZADOR] Linea auxiliar entrante: " + lineaAuxiliar);
        String lexemaAuxiliar = "";
        char[] aux = new char[lineaAuxiliar.length()];
        System.out.println("[DESLEXEMIZADOR] aux[].length: " + aux.length);
        for (int i = 0; i < aux.length; i++) {
            aux[i] = lineaAuxiliar.charAt(i);
            System.out.println("[DESLEXEMIZADOR] aux[" + i + "]: " + aux[i]);
        }
        System.out.println("---------------------------------------");

        char[] newAux = new char[lineaAuxiliar.length() - lexemaTokenizado.length()];
        System.out.println("[DESLEXEMIZADOR] newAux[].length: " + newAux.length);

        int banderaEspacio = 0;
        if (aux[0] == ' ') {
            banderaEspacio = 1;
        }
        for (int i = lexemaTokenizado.length() + banderaEspacio, j = 0; i < aux.length; i++, j++) {
            //System.out.println("[DESLEXEMIZADOR FOR] aux["+i+"]: "+aux[i]);
            newAux[j] = aux[i];
            System.out.println("[DESLEXEMIZADOR FOR] newAux[" + j + "]: " + newAux[j]);
        }
        for (int i = 0; i < newAux.length; i++) {
            lexemaAuxiliar += newAux[i] + "";
        }
        lineaAuxiliar = lexemaAuxiliar;
        System.out.println("[DESLEXEMIZADOR] Linea auxiliar saliente: |" + lineaAuxiliar + "|");
        System.out.println("[DESLEXEMIZADOR] Lexema resultante: |" + lexemaAuxiliar + "|");
        return lexemaAuxiliar;
    }

    void addToLog(String logText) {
        log += logText + "\n";
    }

    int palabraReservada(String lexema, int estado) {
        String lexema2 = "";
        lexema.toLowerCase();
        lexema = destabulador(lexema);
        char[] c = new char[lexema.length()];
        System.out.println("Lexema:" + lexema + "|");
        if (lexema.charAt(lexema.length() - 1) == ' ') {
            for (int i = 0; i < lexema.length() - 1; i++) {
                lexema2 += lexema.charAt(i);
            }
            lexema = lexema2;
        }
        lexema = lexema.toLowerCase();
        int igualdad = 0;
        switch (lexema) {
            case "break":
                estado = -59;
                break;
            case "continue":
                estado = -61;
                break;
            case "elif":
                estado = -62;
                break;
            case "else":
                estado = -63;
                break;
            case "for":
                estado = -67;
                break;
            case "if":
                estado = -68;
                break;
            case "in":
                estado = -70;
                break;
            case "is":
                estado = -71;
                break;
            case "isnot":
                estado = -72;
                break;
            case "print":
                estado = -75;
                break;
            case "return":
                estado = -77;
                break;
            case "while":
                estado = -79;
                break;
            case "true":
                estado = -81;
                break;
            case "false":
                estado = -82;
                break;
            case "none":
                estado = -83;
                break;
            case "range":
                estado = -84;
                break;
            case "input":
                estado = -85;
                break;
            case "println":
                estado = -86;
                break;
            case "end":
                estado = -87;
                break;
            case "wend":
                estado = -88;
                break;
            case "sort":
                estado = -89;
                break;
            case "reverse":
                estado = -90;
                break;
            case "count":
                estado = -91;
                break;
            case "index":
                estado = -92;
                break;
            case "append":
                estado = -93;
                break;
            case "extend":
                estado = -94;
                break;
            case "pop":
                estado = -95;
                break;
            case "remove":
                estado = -96;
                break;
            case "insert":
                estado = -97;
                break;
            case "innot":
                estado = -98;
                break;
            case "findall":
                estado = -99;
                break;
            case "replace":
                estado = -100;
                break;
            case "len":
                estado = -101;
                break;
            case "sample":
                estado = -102;
                break;
            case "choice":
                estado = -103;
                break;
            case "random":
                estado = -104;
                break;
            case "randrange":
                estado = -105;
                break;
            case "mean":
                estado = -106;
                break;
            case "median":
                estado = -107;
                break;
            case "variance":
                estado = -108;
                break;
            case "sum":
                estado = -109;
                break;
            case "def":
                estado = -111;
                break;
            case "to":
                estado = -116;
        }
        return estado;
    }

    void desmenuzadorCriminalMutilador(String lineaAux) {
        System.out.println("--------DESMENUZADOR CRIMINAL MUTILADOR--------");
        System.out.println("[DCM] lineaAux: " + lineaAux);
        lineaAux = lineaAuxiliar;
        boolean igual = false, banMas = false, banMenos = false, banderaBinHexOct = false,
                banderaBin = false, banderaHex = false, primBin = false, pimHex = false;
        int fil = 0, bin = 0, oct = 0, hex = 0;
        int largoExpresion = 0;
        String lexema = "";
        String lexemaBin = "";
        char linea[] = new char[lineaAux.length()];
        for (int i = 0; i < lineaAux.length(); i++) {
            linea[i] = lineaAux.charAt(i);
        }
//        int espacios=0;
//        for(int i=0;linea[i]==' ';i++){
//            System.out.println("Espacios juju");
//            espacios++;
//        }
//        char lineaAuxEsp[]=linea;
//        for(int i=0;i<(linea.length-espacios);i++){
//            linea[i]=lineaAuxEsp[i+espacios];
//        }
        for (int i = 0; i < linea.length; i++) {
            if (linea[i] != ' ') {
                //--------BINARIO----------
                if (i + 2 < linea.length) {
                    // System.out.println( +  + "I+2" + );

                    if (linea[i] == '0') {
                        //   System.out.println( +  + "I==0" + );

                        if (linea[i + 1] == 'b') {
                            //     System.out.println( +  + "I+1==b" + );
                            if (linea[i + 2] == '0' || linea[i + 2] == '1') {
                                //       System.out.println( +  + "BINARIO" + );
                                lexema += "0b";
                                bin = 2;
                                boolean binario = true;
                                int j = i + 2;
                                while (binario) {
                                    //         System.out.println( +  + "Vuelta binario ciclo" + );

                                    if (linea[j] == '0' || linea[j] == '1') {
                                        bin++;
                                        lexema += linea[j];
                                        j++;
                                    } else {
                                        //           System.out.println("else asqueroso");
                                        binario = false;
                                    }
                                }
                                // System.out.println("Salio de ciclo");

                                tokens[nT] = new Token(-10, lexema, lineaActual);
                                oper.insertarUltimo(-10, lexema, lineaActual);
                                lexema = deslexemizador(lexema);
                                actualizarTablaToken(tokens);
                                aumentarArregloToken();
                                if (fil == -6) {
                                    contadorIdentificadores++;
                                }
                                // System.out.println("[[[TOKENIZADO]]] en -10");
//                            contadoresLexico(-10);
                                i += bin;
                                bin = 0;
                                // System.out.println("Valor de i:" + i);
                                lexema = "";
                            }
                        }
                    }
                }
                //-----FIN DE BINARIO-----
                //--------OCTAL----------
                if (i + 2 < linea.length) {
                    if (linea[i] == '0') {
                        System.out.println("[OCTAL] Primer if");
                        System.out.println("[OCTAL] Segundo if");
                        if (linea[i + 1] >= '0' && linea[i + 1] <= '7') {
                            //  System.out.println("[OCTAL] Tercer if");
                            lexema += "0";
                            oct = 2;
                            boolean octal = true;
                            int k = i + 2;
                            while (octal) {
                                //    System.out.println("[OCTAL]: " + linea[k]);
                                if (linea[k] >= '0' && linea[k] <= '7') {
                                    //      System.out.println("[OCTAL] Ciclo if");
                                    oct++;
                                    lexema += linea[k];
                                    k++;
                                } else {
                                    octal = false;
                                }
                            }
                            if (linea[k] >= '7' && linea[k] <= '9') {
                                boolean dec = true;
                                while (dec) {
                                    if (linea[k] >= '0' && linea[k] <= '9') {
                                        oct++;
                                        lexema += linea[k];
                                        k++;
                                    } else {
                                        dec = false;
                                    }
                                    tokens[nT] = new Token(-7, lexema, lineaActual);
                                    oper.insertarUltimo(-7, lexema, lineaActual);
                                    lexema = deslexemizador(lexema);
                                    actualizarTablaToken(tokens);
                                    aumentarArregloToken();
                                    if (fil == -6) {
                                        contadorIdentificadores++;
                                    }
//                                    contadoresLexico(-7);
                                    i += oct;
                                    oct = 0;
                                    lexema = "";
                                }
                            } else {
                                System.out.println("[OCTAL] Fin de ciclo");
                                tokens[nT] = new Token(-11, lexema, lineaActual);
                                oper.insertarUltimo(-11, lexema, lineaActual);
                                lexema = deslexemizador(lexema);
                                actualizarTablaToken(tokens);
                                aumentarArregloToken();
                                if (fil == -6) {
                                    contadorIdentificadores++;
                                }
//                                contadoresLexico(-11);
                                System.out.println("[OCTAL] Tokenizado");
                                i += oct;
                                oct = 0;
                                System.out.println("Valor de i:" + i);
                                lexema = "";
                            }
                        }

                    }
                }
                //-----FIN DE OCTAL-----
                //--------HEX----------
                if (i + 2 < linea.length) {
                    if (linea[i] == '0') {
                        if (linea[i + 1] == 'x') {
                            if ((linea[i + 2] >= '0' && linea[i + 2] <= '9')
                                    || (linea[i + 2] >= 'A' && linea[i + 2] <= 'F')) {
                                lexema += "0x";
                                hex = 2;
                                boolean hexa = true;
                                int l = i + 2;
                                while (hexa) {
                                    if ((linea[l] >= '0' && linea[l] <= '9')
                                            || (linea[l] >= 'A' && linea[l] <= 'F')
                                            || (linea[l] >= 'a' && linea[l] <= 'f')) {
                                        hex++;
                                        lexema += linea[l];
                                        l++;
                                    } else {
                                        hexa = false;
                                    }
                                }
                                tokens[nT] = new Token(-12, lexema, lineaActual);
                                oper.insertarUltimo(-12, lexema, lineaActual);
                                lexema = deslexemizador(lexema);
                                actualizarTablaToken(tokens);
                                aumentarArregloToken();
                                if (fil == -6) {
                                    contadorIdentificadores++;
                                }
//                            contadoresLexico(-12);
                                i += hex;
                                hex = 0;
                                System.out.println("Valor de i:" + i);
                                lexema = "";
                            }
                        }
                    }
                }
                //-----FIN DE HEX-----
                lexema += linea[i];
                System.out.println("El pinche lexema es: " + lexema);
                if (igual = false) {
                    fil = 0;
                }
                char aux = 0;
                int colAux;

                if (i + 1 < linea.length) {
                    aux = linea[i + 1];
                    colAux = asignarColumna(aux);
                } else {
                    colAux = 50;
                }
                int columnaElemento = asignarColumna(linea[i]);
                System.out.println(">>>>dCM: Lexema: " + lexema);
                System.out.println(">>>>dCM: Columna caracter[" + linea[i] + "]: " + columnaElemento);
                if (i + 1 < linea.length && linea[i] == '0' && linea[i + 1] == 'b' && banderaBin == false) {
                    fil = 28;
                    colAux = 4;
                    banderaBin = true;
                } else {
                    fil = matrizLexico[fil][columnaElemento];
                }

                System.out.println(">>>>dCM: Fila: " + fil);
                if (fil == 31 || fil == 32 || fil == 78 || fil == 79 || fil == 80 || fil == 81) {
                    fil = matrizLexico[fil][50];
                    System.out.println(">>>>dCM: Fila actualizada caracter[" + linea[i] + "]: " + fil);
                    if (fil > 0) {
                        fil = matrizLexico[fil][50];
                    }
                    if (lexema.equals(":")) {
                        fil = -110;
                    }
                    if (lexema.equals(".")) {
                        fil = -112;
                    }
                    if (lexema.equals(",")) {
                        fil = -46;
                    }
                    fil = palabraReservada(lexema, fil);

                    tokens[nT] = new Token(fil, lexema, lineaActual);
                    if (fil != -5) {
                        oper.insertarUltimo(fil, lexema, lineaActual);
                    }
                    if (fil == -6) {
                        contadorIdentificadores++;
                    }
                    lexema = deslexemizador(lexema);
                    actualizarTablaToken(tokens);
                    aumentarArregloToken();
                    contadoresLexico(fil);
                    System.out.println("[[[TOKENIZADO]]] en " + fil);
                    fil = 0;
                    lexema = "";
                } else {
                    if (fil < 0 || fil >= 500) {
                        if (fil >= 500) {
                            contadoresLexico[0]++;
                            switch (fil) {
                                case 500:
                                    errores[nR] = new Error(fil, "Se esperaba un carácter o un '", lexema, lineaActual, "Lexico");
                                    break;
                                case 501:
                                    errores[nR] = new Error(fil, "Se esperaba un '", lexema, lineaActual, "Lexico");
                                    break;
                                case 502:
                                    errores[nR] = new Error(fil, "Se esperaba un carácter o una \"", lexema, lineaActual, "Lexico");
                                    break;
                                case 504:
                                    errores[nR] = new Error(fil, "Se esperaba un número del 0 al 9", lexema, lineaActual, "Lexico");
                                    break;
                                case 505:
                                    errores[nR] = new Error(fil, "Se esperaba un número del 0 al 9 o un - o un +", lexema, lineaActual, "Lexico");
                                    break;
                                case 506:
                                    errores[nR] = new Error(fil, "Se esperaba un número del 0 al 9 o una j", lexema, lineaActual, "Lexico");
                                    break;
                                case 507:
                                    errores[nR] = new Error(fil, "Se esperaba un número del 0 al 9, punto o una j", lexema, lineaActual, "Lexico");
                                    break;
                                case 508:
                                    errores[nR] = new Error(fil, "Se esperaba un )", lexema, lineaActual, "Lexico");
                                    break;
                                case 509:
                                    errores[nR] = new Error(fil, "Se esperaba un número del o al 9 o un +", lexema, lineaActual, "Lexico");
                                    break;
                                case 510:
                                    errores[nR] = new Error(fil, "Se esperaba un número o un )", lexema, lineaActual, "Lexico");
                                    break;
                                case 511:
                                    errores[nR] = new Error(fil, "Se esperaba un ]", lexema, lineaActual, "Lexico");
                                    break;
                                case 512:
                                    errores[nR] = new Error(fil, "Se esperaba un }", lexema, lineaActual, "Lexico");
                                    break;
                                case 513:
                                    errores[nR] = new Error(fil, "Se esperaba un ' , \" , # , _ , letra, número, ( , + , - , * , / , % , | , & , < , > , = , ; , . , [ , { , ^ o una ,", lexema, lineaActual, "Lexico");
                                    break;
                                case 514:
                                    errores[nR] = new Error(fil, "Se esperaba un número del 0 al 9, un +, una j o un punto", lexema, lineaActual, "Lexico");
                                    break;
                                case 515:
                                    errores[nR] = new Error(fil, "Se esperaba un punto, j, x, b, e, E o un número del 0 al 8", lexema, lineaActual, "Lexico");
                                    break;
                                case 516:
                                    errores[nR] = new Error(fil, "Se espera un 0 o 1", lexema, lineaActual, "Lexico");
                                    break;
                                case 517:
                                    errores[nR] = new Error(fil, "Se espera un numero del 0 al 7", lexema, lineaActual, "Lexico");
                                    break;
                                case 518:
                                    errores[nR] = new Error(fil, "Se espera un numero del 0 al 9 o una letra de la A a la F", lexema, lineaActual, "Lexico");
                                    break;
                                default:
                                    errores[nR] = new Error(fil, "Error desconocido", lexema, lineaActual, "Lexico");
                            }
                            actualizarTablaError(errores);
                            aumentarArregloError();
                            System.out.println("[[[ERRORIZADO]]] en " + fil);
                            fil = 0;
                            lexema = "";

                        } else {
                            if (fil > 0) {
                                fil = matrizLexico[fil][50];
                            }
                            if (lexema == ":") {
                                fil = -110;
                            }
                            if (lexema.equals(".")) {
                                fil = -112;
                            }
                            if (lexema.equals(",")) {
                                fil = -46;
                            }

                            fil = palabraReservada(lexema, fil);

                            tokens[nT] = new Token(fil, lexema, lineaActual);
                            contadoresLexico(fil);
                            if (fil != -5) {
                                oper.insertarUltimo(fil, lexema, lineaActual);
                            }
                            actualizarTablaToken(tokens);
                            if (fil == -6) {
                                contadorIdentificadores++;
                            }
                            aumentarArregloToken();
                            lexema = deslexemizador(lexema);
                            System.out.println("[[[TOKENIZADO]]] en " + fil);
                            fil = 0;
                            lexema = "";
                        }
                    } else {
                        int estadoAux = matrizLexico[fil][colAux];
                        if (estadoAux != fil) {
                            if (fil >= 500 || estadoAux >= 500) {
                                contadoresLexico[0]++;
                                switch (fil) {
                                    case 500:
                                        errores[nR] = new Error(fil, "Se esperaba un carácter o un '", lexema, lineaActual, "Lexico");
                                        break;
                                    case 501:
                                        errores[nR] = new Error(fil, "Se esperaba un '", lexema, lineaActual, "Lexico");
                                        break;
                                    case 502:
                                        errores[nR] = new Error(fil, "Se esperaba un carácter o una \"", lexema, lineaActual, "Lexico");
                                        break;
                                    case 504:
                                        errores[nR] = new Error(fil, "Se esperaba un número del 0 al 9", lexema, lineaActual, "Lexico");
                                        break;
                                    case 505:
                                        errores[nR] = new Error(fil, "Se esperaba un número del 0 al 9 o un - o un +", lexema, lineaActual, "Lexico");
                                        break;
                                    case 506:
                                        errores[nR] = new Error(fil, "Se esperaba un número del 0 al 9 o una j", lexema, lineaActual, "Lexico");
                                        break;
                                    case 507:
                                        errores[nR] = new Error(fil, "Se esperaba un número del 0 al 9, punto o una j", lexema, lineaActual, "Lexico");
                                        break;
                                    case 508:
                                        errores[nR] = new Error(fil, "Se esperaba un )", lexema, lineaActual, "Lexico");
                                        break;
                                    case 509:
                                        errores[nR] = new Error(fil, "Se esperaba un número del o al 9 o un +", lexema, lineaActual, "Lexico");
                                        break;
                                    case 510:
                                        errores[nR] = new Error(fil, "Se esperaba un número o un )", lexema, lineaActual, "Lexico");
                                        break;
                                    case 511:
                                        errores[nR] = new Error(fil, "Se esperaba un ]", lexema, lineaActual, "Lexico");
                                        break;
                                    case 512:
                                        errores[nR] = new Error(fil, "Se esperaba un }", lexema, lineaActual, "Lexico");
                                        break;
                                    case 513:
                                        errores[nR] = new Error(fil, "Se esperaba un ' , \" , # , _ , letra, número, ( , + , - , * , / , % , | , & , < , > , = , ; , . , [ , { , ^ o una ,", lexema, lineaActual, "Lexico");
                                        break;
                                    case 514:
                                        errores[nR] = new Error(fil, "Se esperaba un número del 0 al 9, un +, una j o un punto", lexema, lineaActual, "Lexico");
                                        break;
                                    case 515:
                                        errores[nR] = new Error(fil, "Se esperaba un punto, j, x, b, e, E o un número del 0 al 8", lexema, lineaActual, "Lexico");
                                        break;
                                    case 516:
                                        errores[nR] = new Error(fil, "Se espera un 0 o 1", lexema, lineaActual, "Lexico");
                                        break;
                                    case 517:
                                        errores[nR] = new Error(fil, "Se espera un numero del 0 al 7", lexema, lineaActual, "Lexico");
                                        break;
                                    case 518:
                                        errores[nR] = new Error(fil, "Se espera un numero del 0 al 9 o una letra de la A a la F", lexema, lineaActual, "Lexico");
                                        break;
                                    default:
                                        errores[nR] = new Error(fil, "Error desconocido", lexema, lineaActual, "Lexico");
                                }
                                actualizarTablaError(errores);
                                aumentarArregloError();
                                fil = 0;
                                lexema = "";
                                System.out.println("[[[ERRORIZADO]]]");
                            } else {
                                if (lexema == ":") {
                                    fil = -110;
                                }
                                if (lexema.equals(".")) {
                                    fil = -112;
                                }
                                if (fil > 0) {
                                    fil = matrizLexico[fil][50];
                                }
                                if (lexema.equals(",")) {
                                    fil = -46;
                                }
                                fil = palabraReservada(lexema, fil);
                                System.out.println("[[TOKENIZADO MAMALON]]Esto es un nge? " + lexema);
                                tokens[nT] = new Token(fil, lexema, lineaActual);
                                contadoresLexico(fil);
                                if (fil != -5) {
                                    oper.insertarUltimo(fil, lexema, lineaActual);
                                }
                                lexema = deslexemizador(lexema);
                                actualizarTablaToken(tokens);
                                if (fil == -6) {
                                    contadorIdentificadores++;
                                }
                                aumentarArregloToken();
                                System.out.println("[[[TOKENIZADO]]] en " + fil);
                                fil = 0;
                                lexema = "";
                                banderaBinHexOct = false;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("----- FIN DE DESMENUZADOR CRIMINAL MUTILADOR-----");
    }

    String destabulador(String lexema) {
        char[] aux = new char[lexema.length()];
        for (int i = 0; i < lexema.length(); i++) {
            aux[i] = lexema.charAt(i);
        }

        lexema = "";
        for (int i = 0; i < aux.length; i++) {
            if (aux[i] == '\t') {

            } else {
                lexema += aux[i];
            }
        }
        return lexema;
    }

    void analizador(char[] linea) {
        String lexema = "";
        lineaHoja = "";
        int fil = 0, largoExpresion = 0;
//        int espacios=0;
//        for(int i=0;linea[i]==' ';i++){
//            System.out.println("Espacios juju");
//            espacios++;
//        }
//        char lineaAuxEsp[]=linea;
//        linea=new char[lineaAuxEsp.length-espacios];
//        for(int i=0;i<(linea.length-espacios);i++){
//            linea[i]=lineaAuxEsp[i+espacios];
//        }

        for (int i = 0; i < linea.length; i++) {//Desmenuzador de lineas
            largoExpresion++;
            lineaHoja += linea[i] + "";
            boolean quebrador = false;
            System.out.println("--- Caracter: [" + linea[i] + "] ---");
            int col = asignarColumna(linea[i]);
            char aux = ' ';
            int colAux = 0;
            if (i + 1 < linea.length) {
                aux = linea[i + 1];
                colAux = asignarColumna(aux);
            }

            System.out.println("Fila: " + fil + "  /// Columna: " + col);
            int estado;
            if (fil < 500 && fil > 0) {
                estado = matrizLexico[fil][col];
            } else {
                estado = fil;
            }

            System.out.println("Estado: " + estado);
            if (estado < 500 && estado >= 0) {
                fil = matrizLexico[fil][col];
            } else if (estado >= 500) {
                estadoError = true;
            }
            int estadoAux;
            if (fil < 500 && fil > 0) {
                estadoAux = matrizLexico[fil][colAux];
            } else {
                estadoAux = fil;
            }
            System.out.println("Estado auxiliar´: " + estadoAux);
            if (estado == 7 && multi == false) {
                multi = true;
                lineaMultiComentario = lineaActual;
            }
            if (multi == true) {
                if ("".equals(lexemaMultiple)) {
                    lexemaMultiple = "''";
                }
                lexemaMultiple += linea[i];
                if (activador2 == 1) {
                    if (linea[i] == '\'') {
                        multiLinea++;
                    } else {
                        multiLinea = 0;
                    }
                }
                activador2 = 1;
                if (multiLinea == 3) {
                    System.out.println("-------Entro a MultiLinea=3");
                    estado = -3;
                    multi = false;
                    tokens[nT] = new Token(estado, lexemaMultiple, lineaMultiComentario);
                    if (estado == -6) {
                        contadorIdentificadores++;
                    }
//                    oper.insertarUltimo(estado, lexemaMultiple, lineaMultiComentario);
                    actualizarTablaToken(tokens);
//                    contadoresLexico(estado);
                    aumentarArregloToken();
                    lexema = deslexemizador(lexemaMultiple);
                    activador2 = 0;
                    multiLinea = 0;
                    lexemaMultiple = "";
                    lineaMultiComentario = 0;
                    estado = 0;
                    fil = 0;
                }
            } else {//-------------Resto de las expresiones------------
                System.out.println("Largo de expresion: " + largoExpresion + ", linea.lenght: " + linea.length);
                try {
                    if (largoExpresion == linea.length) {
                        col = 1;
                        System.out.println("Se agrego un salto de linea");
                    }
                } catch (Exception e) {

                }
                System.out.println("Estado: " + estado);
                if (linea[i] != ' ' || linea[i] != ' ') {
                    lexema += linea[i];
                }
                estado = estadoAux;
                System.out.println("--Estado: " + estado);
                if (i + 1 == linea.length) {
                    if (fil > 0 && fil < 500) {
                        estadoAux = matrizLexico[fil][50];
                    }
                    //  System.out.println("Largo de la linea, fil: " + fil + ", col: 50");
                    System.out.println("Contenido: " + estadoAux);
                    estado = estadoAux;
                    System.out.println("--Estado 2: " + estado);
                }
                if (estadoAux >= 500 || estadoError == true) {
                    estadoError = false;
                    contadoresLinea[lineaActual - 1][0]++;
                    //System.err.println("-------------Entro a error------------");
                    System.out.println("Fila: " + fil + " Columna: " + col + " nE: " + nR);
                    System.out.println("Contenido del error erroroso: " + estado);

                    switch (estado) {
                        case 500:
                            contadoresLexico[0]++;
                            errores[nR] = new Error(estado, "Se esperaba un carácter o un '", lexema, lineaActual, "Lexico");
                            break;
                        case 501:
                            contadoresLexico[0]++;
                            errores[nR] = new Error(estado, "Se esperaba un '", lexema, lineaActual, "Lexico");
                            break;
                        case 502:
                            contadoresLexico[0]++;
                            errores[nR] = new Error(estado, "Se esperaba un carácter o una \"", lexema, lineaActual, "Lexico");
                            desmenuzadorCriminalMutilador(lexema);
                            break;
                        case 504:
                            contadoresLexico[0]++;
                            desmenuzadorCriminalMutilador(lexema);
                            errores[nR] = new Error(estado, "Se esperaba un número del 0 al 9", lexema, lineaActual, "Lexico");
                            break;
                        case 505:
                            contadoresLexico[0]++;
                            errores[nR] = new Error(estado, "Se esperaba un número del 0 al 9 o un - o un +", lexema, lineaActual, "Lexico");

                            break;
                        case 506:
                            contadoresLexico[0]++;
                            desmenuzadorCriminalMutilador(lexema);
                            break;
                        case 507:
                            contadoresLexico[0]++;
                            desmenuzadorCriminalMutilador(lexema);
                            break;
                        case 508:
                            contadoresLexico[0]++;
                            desmenuzadorCriminalMutilador(lexema);
                            break;
                        case 509:
                            contadoresLexico[0]++;
                            desmenuzadorCriminalMutilador(lexema);
                            break;
                        case 510:
                            contadoresLexico[0]++;
                            errores[nR] = new Error(estado, "Se esperaba un número o un )", lexema, lineaActual, "Lexico");
                            break;
                        case 511:
                            contadoresLexico[0]++;
                            errores[nR] = new Error(estado, "Se esperaba un ]", lexema, lineaActual, "Lexico");
                            break;
                        case 512:
                            contadoresLexico[0]++;
                            errores[nR] = new Error(estado, "Se esperaba un }", lexema, lineaActual, "Lexico");
                            break;
                        case 513:
                            contadoresLexico[0]++;
                            errores[nR] = new Error(estado, "Se esperaba un ' , \" , # , _ , letra, número, ( , + , - , * , / , % , | , & , < , > , = , ; , . , [ , { , ^ o una ,", lexema, lineaActual, "Lexico");
//                            desmenuzadorCriminalMutilador(lineaAuxiliar);
                            break;
                        case 514:
                            desmenuzadorCriminalMutilador(lexema);
                            System.out.println("[ANALIZADOR ERROR 514] Lexema antes del error " + lexema);
                            desmenuzadorCriminalMutilador(lexema.charAt(0) + "");
                            //lexema=deslexemizador(lexema.charAt(0)+"");
                            System.out.println("[ANALIZADOR ERROR 514] Lexema despues del error " + lexema);
                            break;
                        case 515:
                            contadoresLexico[0]++;
                            errores[nR] = new Error(estado, "Se esperaba un punto, j, x, b, e, E o un número del 0 al 8", lexema, lineaActual, "Lexico");
                            break;
                        case 516:
                            contadoresLexico[0]++;
                            errores[nR] = new Error(estado, "Se esperaba un 0 o 1", lexema, lineaActual, "Lexico");
//                            desmenuzadorCriminalMutilador(lineaAuxiliar);
                            break;
                        case 517:
                            contadoresLexico[0]++;
                            desmenuzadorCriminalMutilador(lexema);
                            break;
                        case 518:
                            contadoresLexico[0]++;
                            desmenuzadorCriminalMutilador(lexema);
                            break;
                        default:
                            contadoresLexico[0]++;
                            errores[nR] = new Error(estado, "Error desconocido", lexema, lineaActual, "Lexico");
                    }
                    lineaHoja = "";
                    lexema = "";
                    actualizarTablaError(errores);
                    aumentarArregloError();
                    if (quebrador == true) {
                        desmenuzadorCriminalMutilador(lineaHoja);
                        quebrador = false;
                        lineaHoja = "";
                    }
                    fil = 0;
                    col = 0;
                } else if (estadoAux < 0) {
                    System.out.println("Nt antes de ingresar a arreglo: " + nT);
                    System.out.println("Exito");
                    System.out.println("Fila: " + fil + " Columna: " + col + " nT: " + nT);
                    estado = palabraReservada(lexema, estado);
                    System.out.println("[ANALIZADOR] Estado despues de palabra reservada: " + estado);
                    if (lexema.equals(":")) {
                        estado = -110;
                    }
                    if (lexema.equals(".")) {
                        fil = -112;
                    }
                    if (lexema.equals(",")) {
                        estado = -46;
                    }
                    tokens[nT] = new Token(estado, lexema, lineaActual);

                    System.out.println("[[ANALIZADOR - TOKENIZADO]]");
                    if (estado != -5) {
                        oper.insertarUltimo(estado, lexema, lineaActual);
                    }
                    //lexema = deslexemizador(lexema);
                    if (estado == -6) {
                        contadorIdentificadores++;
                    }
//                    contadoresLexico(estado);
//                    for (int w = 0; w < tokens.length; w++) {
//                        System.out.println("----------Tokens----------");
//                        System.out.println("Estado del token [" + w + "]: " + tokens[w].getEstado());
//                        System.out.println("Lexema del token [" + w + "]: " + tokens[w].getLexema());
//                        System.out.println("--------------------------");
//                    }
                    System.out.println("Lexema antes de deslexemizador: " + lexema);
                    lexema = deslexemizador(lexema);
                    System.out.println("Lexema despues de deslexemizador: " + lexema);
                    lexema = "";
                    actualizarTablaToken(tokens);
                    aumentarArregloToken();

                    fil = 0;
                    col = 0;
                }
            }
            col = 0;//Se necesita para comenzar el recorrido desde la primera columna
            System.out.println("");
        }
        System.out.println("---Termino el analizador de lexico---");
//        System.out.println(oper.mostrarPrimero());

    }

    public int valorFila(int i) {
        int filaMatrizProducciones = 0;
        switch (i) {
            case 200:
                filaMatrizProducciones = 1;
                break;
            case 201:
                filaMatrizProducciones = 2;
                break;
            case 203:
                filaMatrizProducciones = 3;
                break;
            case 204:
                filaMatrizProducciones = 4;
                break;
            case 205:
                filaMatrizProducciones = 5;
                break;
            case 206:
                filaMatrizProducciones = 6;
                break;
            case 207:
                filaMatrizProducciones = 7;
                break;
            case 208:
                filaMatrizProducciones = 8;
                break;
            case 211:
                filaMatrizProducciones = 9;
                break;
            case 212:
                filaMatrizProducciones = 10;
                break;
            case 209:
                filaMatrizProducciones = 11;
                break;
            case 210:
                filaMatrizProducciones = 12;
                break;
            case 370:
                filaMatrizProducciones = 13;
                break;
            case 213:
                filaMatrizProducciones = 14;
                break;
            case 214:
                filaMatrizProducciones = 15;
                break;
            case 218:
                filaMatrizProducciones = 16;
                break;
            case 219:
                filaMatrizProducciones = 17;
                break;
            case 220:
                filaMatrizProducciones = 18;
                break;
            case 221:
                filaMatrizProducciones = 19;
                break;
            case 222:
                filaMatrizProducciones = 20;
                break;
            case 215:
                filaMatrizProducciones = 21;
                break;
            case 216:
                filaMatrizProducciones = 22;
                break;
            case 217:
                filaMatrizProducciones = 23;
                break;
            case 232:
                filaMatrizProducciones = 24;
                break;
            case 233:
                filaMatrizProducciones = 25;
                break;
            case 234:
                filaMatrizProducciones = 26;
                break;
            case 235:
                filaMatrizProducciones = 27;
                break;
            case 236:
                filaMatrizProducciones = 28;
                break;
            case 237:
                filaMatrizProducciones = 29;
                break;
            case 238:
                filaMatrizProducciones = 30;
                break;
            case 239:
                filaMatrizProducciones = 31;
                break;
            case 240:
                filaMatrizProducciones = 32;
                break;
            case 241:
                filaMatrizProducciones = 33;
                break;
            case 242:
                filaMatrizProducciones = 34;
                break;
            case 243:
                filaMatrizProducciones = 35;
                break;
            case 244:
                filaMatrizProducciones = 36;
                break;
            case 245:
                filaMatrizProducciones = 37;
                break;
            case 246:
                filaMatrizProducciones = 38;
                break;
            case 247:
                filaMatrizProducciones = 39;
                break;
            case 248:
                filaMatrizProducciones = 40;
                break;
            case 249:
                filaMatrizProducciones = 41;
                break;
            case 226:
                filaMatrizProducciones = 42;
                break;
            case 227:
                filaMatrizProducciones = 43;
                break;
            case 228:
                filaMatrizProducciones = 44;
                break;
            case 229:
                filaMatrizProducciones = 45;
                break;
            case 230:
                filaMatrizProducciones = 46;
                break;
            case 231:
                filaMatrizProducciones = 47;
                break;
            case 223:
                filaMatrizProducciones = 48;
                break;
            case 224:
                filaMatrizProducciones = 49;
                break;
            case 225:
                filaMatrizProducciones = 50;
                break;
            case 250:
                filaMatrizProducciones = 51;
                break;
            case 251:
                filaMatrizProducciones = 52;
                break;
            case 252:
                filaMatrizProducciones = 53;
                break;
            case 253:
                filaMatrizProducciones = 54;
                break;
            case 254:
                filaMatrizProducciones = 55;
                break;
            case 255:
                filaMatrizProducciones = 56;
                break;
            case 260:
                filaMatrizProducciones = 57;
                break;
            case 261:
                filaMatrizProducciones = 58;
                break;
            case 262:
                filaMatrizProducciones = 59;
                break;
            case 263:
                filaMatrizProducciones = 60;
                break;
            case 264:
                filaMatrizProducciones = 61;
                break;
            case 258:
                filaMatrizProducciones = 62;
                break;
            case 259:
                filaMatrizProducciones = 63;
                break;
            case 256:
                filaMatrizProducciones = 64;
                break;
            case 257:
                filaMatrizProducciones = 65;
                break;
            case 365:
                filaMatrizProducciones = 66;
                break;
            case 366:
                filaMatrizProducciones = 67;
                break;
            case 367:
                filaMatrizProducciones = 68;
                break;
            case 368:
                filaMatrizProducciones = 69;
                break;

        }
        return filaMatrizProducciones - 1;
    }

    public int valorColumna(int i) {
        int columnaMatrizProducciones = 0;
        switch (i) {
            case -111:
                columnaMatrizProducciones = 1;
                break;
            case -6:
                columnaMatrizProducciones = 2;
                break;
            case -53:
                columnaMatrizProducciones = 3;
                break;
            case -13:
                columnaMatrizProducciones = 4;
                break;
            case -46:
                columnaMatrizProducciones = 5;
                break;
            case -42:
                columnaMatrizProducciones = 6;
                break;
            case -45:
                columnaMatrizProducciones = 7;
                break;
            case -51:
                columnaMatrizProducciones = 8;
                break;
            case -48:
                columnaMatrizProducciones = 9;
                break;
            case -20:
                columnaMatrizProducciones = 10;
                break;
            case -24:
                columnaMatrizProducciones = 11;
                break;
            case -26:
                columnaMatrizProducciones = 12;
                break;
            case -28:
                columnaMatrizProducciones = 13;
                break;
            case -8:
                columnaMatrizProducciones = 14;
                break;
            case -4:
                columnaMatrizProducciones = 15;
                break;
            case -1:
                columnaMatrizProducciones = 16;
                break;
            case -9:
                columnaMatrizProducciones = 17;
                break;
            case -81:
                columnaMatrizProducciones = 18;
                break;
            case -82:
                columnaMatrizProducciones = 19;
                break;
            case -83:
                columnaMatrizProducciones = 20;
                break;
            case -22:
                columnaMatrizProducciones = 21;
                break;
            case -7:
                columnaMatrizProducciones = 22;
                break;
            case -10:
                columnaMatrizProducciones = 23;
                break;
            case -12:
                columnaMatrizProducciones = 24;
                break;
            case -11:
                columnaMatrizProducciones = 25;
                break;
            case -14:
                columnaMatrizProducciones = 26;
                break;
            case -17:
                columnaMatrizProducciones = 27;
                break;
            case -35:
                columnaMatrizProducciones = 28;
                break;
            case -37:
                columnaMatrizProducciones = 29;
                break;
            case -40:
                columnaMatrizProducciones = 30;
                break;
            case -33:
                columnaMatrizProducciones = 31;
                break;
            case -117:
                columnaMatrizProducciones = 32;
                break;
            case -32:
                columnaMatrizProducciones = 33;
                break;
            case -34:
                columnaMatrizProducciones = 34;
                break;
            case -44:
                columnaMatrizProducciones = 35;
                break;
            case -15:
                columnaMatrizProducciones = 36;
                break;
            case -18:
                columnaMatrizProducciones = 37;
                break;
            case -112:
                columnaMatrizProducciones = 38;
                break;
            case -85:
                columnaMatrizProducciones = 39;
                break;
            case -30:
                columnaMatrizProducciones = 40;
                break;
            case -75:
                columnaMatrizProducciones = 41;
                break;
            case -86:
                columnaMatrizProducciones = 42;
                break;
            case -68:
                columnaMatrizProducciones = 43;
                break;
            case -67:
                columnaMatrizProducciones = 44;
                break;
            case -79:
                columnaMatrizProducciones = 45;
                break;
            case -59:
                columnaMatrizProducciones = 46;
                break;
            case -61:
                columnaMatrizProducciones = 47;
                break;
            case -77:
                columnaMatrizProducciones = 48;
                break;
            case -70:
                columnaMatrizProducciones = 49;
                break;
            case -88:
                columnaMatrizProducciones = 50;
                break;
            case -62:
                columnaMatrizProducciones = 51;
                break;
            case -63:
                columnaMatrizProducciones = 52;
                break;
            case -87:
                columnaMatrizProducciones = 53;
                break;
            case -36:
                columnaMatrizProducciones = 54;
                break;
            case -38:
                columnaMatrizProducciones = 55;
                break;
            case -43:
                columnaMatrizProducciones = 56;
                break;
            case -31:
                columnaMatrizProducciones = 57;
                break;
            case -41:
                columnaMatrizProducciones = 58;
                break;
            case -39:
                columnaMatrizProducciones = 59;
                break;
            case -71:
                columnaMatrizProducciones = 60;
                break;
            case -72:
                columnaMatrizProducciones = 61;
                break;
            case -98:
                columnaMatrizProducciones = 62;
                break;
            case -99:
                columnaMatrizProducciones = 63;
                break;
            case -100:
                columnaMatrizProducciones = 64;
                break;
            case -101:
                columnaMatrizProducciones = 65;
                break;
            case -102:
                columnaMatrizProducciones = 66;
                break;
            case -103:
                columnaMatrizProducciones = 67;
                break;
            case -104:
                columnaMatrizProducciones = 68;
                break;
            case -105:
                columnaMatrizProducciones = 69;
                break;
            case -106:
                columnaMatrizProducciones = 70;
                break;
            case -107:
                columnaMatrizProducciones = 71;
                break;
            case -108:
                columnaMatrizProducciones = 72;
                break;
            case -109:
                columnaMatrizProducciones = 73;
                break;
            case -89:
                columnaMatrizProducciones = 74;
                break;
            case -90:
                columnaMatrizProducciones = 75;
                break;
            case -91:
                columnaMatrizProducciones = 76;
                break;
            case -92:
                columnaMatrizProducciones = 77;
                break;
            case -93:
                columnaMatrizProducciones = 78;
                break;
            case -94:
                columnaMatrizProducciones = 79;
                break;
            case -95:
                columnaMatrizProducciones = 80;
                break;
            case -96:
                columnaMatrizProducciones = 81;
                break;
            case -97:
                columnaMatrizProducciones = 82;
                break;
            case -110:
                columnaMatrizProducciones = 83;
                break;
            case -16:
                columnaMatrizProducciones = 84;
                break;
            case -27:
                columnaMatrizProducciones = 85;
                break;
            case -23:
                columnaMatrizProducciones = 86;
                break;
            case -19:
                columnaMatrizProducciones = 87;
                break;
            case -25:
                columnaMatrizProducciones = 88;
                break;
            case -21:
                columnaMatrizProducciones = 89;
                break;
            case -29:
                columnaMatrizProducciones = 90;
                break;
            case -52:
                columnaMatrizProducciones = 91;
                break;
            case -47:
                columnaMatrizProducciones = 92;
                break;
            case -84:
                columnaMatrizProducciones = 93;
                break;
            case -116:
                columnaMatrizProducciones = 94;
                break;
            case -1000:
                columnaMatrizProducciones = 95;
                break;

        }
        if (columnaMatrizProducciones == 0) {
            return columnaMatrizProducciones;
        } else {
            return columnaMatrizProducciones - 1;
        }
    }

    void tipo800(int arroba) {
        String tipo = "";
        switch (arroba) {
            case 805:
                tipo = "Decimal";
                break;
            case 806:
                tipo = "Binario";
                break;
            case 807:
                tipo = "Octal";
                break;
            case 808:
                tipo = "Hexadecimal";
                break;
            case 809:
                tipo = "Flotante";
                break;
            case 810:
                tipo = "Cadena";
                break;
            case 811:
                tipo = "Caracter";
                break;
            case 812:
                tipo = "Compleja";
                break;
            case 813:
                tipo = "Booleana";
                break;
            case 814:
                tipo = "None";
                break;
            case 816:
                tipo = "Tupla";
                break;
            case 815:
            case 817:
                tipo = "Lista";
                break;
            case 818:
                tipo = "Registro";
                break;
            case 819:
                tipo = "Rango";
                break;
            case 820:
                tipo = "Conjunto";
                break;
            case 821:
                tipo = "Diccionario";
                break;
            default:
                tipo = "Desconocido";
        }
        if (banderaParametro) {
            tipo = "None";
            tipoVariable = "par";
        }
        claseVariable = tipo;
    }

    void aumentarAmbito() {
        pilaAmbito.push(ambitoActualDisponible);
        ambito = pilaAmbito.peek();
        if (ambito >= ambitoMayor) {
            ambitoMayor = ambito;
        }
        System.out.println("<AUMENTAR AMBITO> Se ha aumentado el ambito" );
        ambitoActualDisponible++;
    }

    void reducirAmbito() {
        pilaAmbito.pop();
        ambito = pilaAmbito.peek();
    }

    boolean variableDeclarada(String id) {
        boolean existencia = false;
        int estadoInt = 0;
        System.out.println("Valor ID: " + id);
        System.out.println("Valor ambito: " + ambitoVariable);
        try {
            System.out.println("<MySQL:agregarVariable> Prueba de conexion a MySQL");
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/a16130329?verifyServerCertificate=false&useSSL=true", "root", "root");
            st = con.createStatement();
             
        } catch (SQLException e) {
            System.err.println("<MySQL:agregarVariable> Error de MySQL");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }
        String query = "SELECT COUNT(id) FROM tablasimbolos WHERE (id="
                + "'" + id + "' AND ambito='" + ambitoVariable + "')";
        try {
            rs = st.executeQuery(query);
            while (rs.next()) {
                estadoInt = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("VALOR DE ESTADO INT: " + estadoInt);
        if (estadoInt == 1) {
            existencia = true;
            System.out.println("VARIABLE EXITENTE");
        } else {
            existencia = false;
            System.out.println("VARIABLE NO EXISTENTE");
        }
        return existencia;
    }

    boolean variableDuplicada(String id) {
        boolean existencia = false;
        int estadoInt = 0;
        try {
            System.out.println("<MySQL:agregarVariable> Prueba de conexion a MySQL");
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/a16130329?verifyServerCertificate=false&useSSL=true", "root", "root");
            st = con.createStatement();
             
        } catch (SQLException e) {
            System.err.println("<MySQL:agregarVariable> Error de MySQL");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }
        String query = "SELECT COUNT(id) FROM tablasimbolos WHERE id="
                + "'" + id + "' AND ambito='" + ambitoVariable + "';";
        try {
            rs = st.executeQuery(query);
            while (rs.next()) {
                estadoInt = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("VALOR DE ESTADO INT: " + estadoInt);
        if (estadoInt >= 1) {
            existencia = true;
            System.out.println("VARIABLE DUPLICADA");
        } else {
            existencia = false;
            System.out.println("VARIABLE NO DUPLICADA");
        }

        return existencia;
    }

    void agregarVariable() {
        valorVariable = valorVariable.replace("'", "\"");
        System.out.println("----Agregar Variable----");
        try {
            System.out.println("<MySQL:agregarVariable> Prueba de conexion a MySQL");
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/a16130329?verifyServerCertificate=false&useSSL=true", "root", "root");
            st = con.createStatement();
             
        } catch (SQLException e) {
            System.err.println("<MySQL:agregarVariable> Error de MySQL");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("<AMBITO:agregarVariable> " + "Variable: " + nombreVariable
                + ", Tipo: " + claseVariable
                + ", Ambito: " + ambito
                + ", Clase: " + tipoVariable);
        String query;

        boolean variableDuplicada = variableDuplicada(nombreVariable);
        boolean variableDeclarada = variableDeclarada(nombreVariable);
        switch (claseVariable) {
            case "Decimal":
            case "Binario":
            case "Octal":
            case "Hexadecimal":
            case "Flotante":
            case "Cadena":
            case "Caracter":
            case "Compleja":
            case "Booleana":
                if (areaDeclaracion && !variableDuplicada) {
                    tipoVariable = "var";
                    ambitoVariable = ambito + "";
                    query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,valor) VALUES("
                            + "'" + nombreVariable + "',"
                            + "'" + claseVariable + "',"
                            + "'" + tipoVariable + "',"
                            + "'" + ambitoVariable + "',"
                            + "'" + valorVariable + "');";
                    try {
                        st.executeUpdate(query);
                    } catch (SQLException ex) {
                        Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    variableArreglo[contadorVariablesArreglo] = new Variable(nombreVariable, ambitoVariable);
                } else {
                    if (!areaDeclaracion) {
                        if (variableDeclarada) {

                        } else {
                            errores[nR] = new Error(1001, "Variable " + nombreVariable + " no declarada",
                                    "Ambito: " + ambito + "", 0, "Ambito");
                            actualizarTablaError(errores);
                            aumentarArregloError();
                        }
                    } else if (variableDuplicada) {
                        errores[nR] = new Error(1002, "Variable " + nombreVariable + " duplicada",
                                "Ambito: " + ambito + ", Tipo: " + tipoVariable, 0, "Ambito");
                        actualizarTablaError(errores);
                        aumentarArregloError();
                    }
                }
                valorVariable = "";
                banderaConstante = false;
                break;
            case "None":
                if (areaDeclaracion && !variableDuplicada) {
                    if (banderaFuncion && claseVariable != "par") {
                        ambitoVariable = ambito + "";
                        aumentarAmbito();
                        ambitoCreado = ambito + "";
                    }
                    query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,ambitoCreado) VALUES("
                            + "'" + nombreVariable + "',"
                            + "'" + claseVariable + "',"
                            + "'" + tipoVariable + "',"
                            + "'" + ambitoVariable + "',"
                            + "'" + ambitoCreado + "');";

                    try {
                        st.executeUpdate(query);
                    } catch (SQLException ex) {
                        Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    if (!areaDeclaracion) {
                        if (variableDeclarada) {

                        } else {
                            errores[nR] = new Error(1001, "Variable " + nombreVariable + " no declarada",
                                    "Ambito: " + ambito + "", 0, "Ambito");
                            actualizarTablaError(errores);
                            aumentarArregloError();
                        }
                    } else if (variableDuplicada) {
                        errores[nR] = new Error(1002, "Variable " + nombreVariable + " duplicada",
                                "Ambito: " + ambito + ", Tipo: " + tipoVariable, 0, "Ambito");
                        actualizarTablaError(errores);
                        aumentarArregloError();
                    }
                }
                bandera814 = false;
                ambitoCreado = "";
                banderaFuncion = false;
//                if (banderaFuncion) {
//                    reducirAmbito();   
//                }
                break;
            case "Tupla":
                if (areaDeclaracion && !variableDuplicada) {
                    aumentarAmbito();
                    ambitoCreado = ambito + "";
                    query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,ambitoCreado) VALUES("
                            + "'" + nombreVariable + "',"
                            + "'" + claseVariable + "',"
                            + "'" + tipoVariable + "',"
                            + "'" + ambitoVariable + "',"
                            + "'" + ambitoCreado + "');";
                    auxiliarNombreVariable = nombreVariable;
                    try {
                        st.executeUpdate(query);
                    } catch (SQLException ex) {
                        Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    banderaTupla = false;
                    agregarTupla = false;
                    bandera814 = false;
                    System.out.println("contadorTupla: " + contadorTupla);
                    for (int i = 0; i < contadorTupla; i++) {
                        tuplaArreglo[i].setAmb(ambito + "");
                        String tipoE = tuplaArreglo[i].getTipo();
                        String claseE = tuplaArreglo[i].getClase();
                        String ambitoE = tuplaArreglo[i].getAmb();
                        String noPosE = tuplaArreglo[i].getNoPos();
                        String listaPerE = tuplaArreglo[i].getListaPertenece();
                        query = "INSERT INTO tablasimbolos (clase,tipo,ambito,noPos,listaPertenece) VALUES("
                                + "'" + claseE + "',"
                                + "'" + tipoE + "',"
                                + "'" + ambitoE + "',"
                                + "'" + noPosE + "',"
                                + "'" + listaPerE + "');";

                        try {
                            st.executeUpdate(query);
                        } catch (SQLException ex) {
                            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        query = "UPDATE tablasimbolos SET tamanoArreglo = '" + contadorTupla + "' where id='" + auxiliarNombreVariable + "';";
                        try {
                            st.executeUpdate(query);
                        } catch (SQLException ex) {
                            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                contadorElementosLista = 0;
                reducirAmbito();
                contadorTupla = 0;
                bandera814 = false;
                break;
            case "Lista":
                if (banderaListaNormal) {
                    query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,tamanoArreglo) VALUES("
                            + "'" + nombreVariable + "',"
                            + "'" + claseVariable + "',"
                            + "'" + tipoVariable + "',"
                            + "'" + ambitoVariable + "',"
                            + "'" + tarrVariable + "');";
                    try {
                        st.executeUpdate(query);
                    } catch (SQLException ex) {
                        Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    banderaListaMultiple = false;
                    banderaListaNormal = false;
                    agregarLista = false;
                    bandera814 = false;
                    contadorElementosLista = 0;
                } else if (banderaListaMultiple) {
                    aumentarAmbito();
                    ambitoCreado = ambito + "";
                    query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,ambitoCreado) VALUES("
                            + "'" + nombreVariable + "',"
                            + "'" + claseVariable + "',"
                            + "'" + tipoVariable + "',"
                            + "'" + ambitoVariable + "',"
                            + "'" + ambitoCreado + "');";
                    auxiliarNombreVariable = nombreVariable;
                    try {
                        st.executeUpdate(query);
                    } catch (SQLException ex) {
                        Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    banderaListaMultiple = false;
                    banderaListaNormal = false;
                    agregarLista = false;
                    bandera814 = false;
                    tamVariablesGuardadasArr++;
                    System.out.println("contadorElemenosLista: " + contadorElementosLista);
                    for (int i = 0; i < contadorElementosLista; i++) {
                        listaArreglo[i].setAmb(ambito + "");
                        String tipoE = listaArreglo[i].getTipo();
                        String claseE = listaArreglo[i].getClase();
                        String ambitoE = listaArreglo[i].getAmb();
                        String noPosE = listaArreglo[i].getNoPos();
                        String listaPerE = listaArreglo[i].getListaPertenece();
                        query = "INSERT INTO tablasimbolos (clase,tipo,ambito,noPos,listaPertenece) VALUES("
                                + "'" + claseE + "',"
                                + "'" + tipoE + "',"
                                + "'" + ambitoE + "',"
                                + "'" + noPosE + "',"
                                + "'" + listaPerE + "');";

                        try {
                            st.executeUpdate(query);
                        } catch (SQLException ex) {
                            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        query = "UPDATE tablasimbolos SET tamanoArreglo = '" + contadorElementosLista + "' where id='" + auxiliarNombreVariable + "';";
                        try {
                            st.executeUpdate(query);
                        } catch (SQLException ex) {
                            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    contadorElementosLista = 0;
                    reducirAmbito();
                }
                bandera814 = false;
                break;
            case "Rango":
                tipoVariable = "struct";
                ambitoVariable = ambito + "";
                String r1 = rango1 + ", " + rango2;
                query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,rango,avance) VALUES("
                        + "'" + nombreVariable + "',"
                        + "'" + claseVariable + "',"
                        + "'" + tipoVariable + "',"
                        + "'" + ambitoVariable + "',"
                        + "'" + r1 + "',"
                        + "'" + avance + "');";

                try {
                    st.executeUpdate(query);
                } catch (SQLException ex) {
                    Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                }

//                tablaSimbolos[tamVariablesGuardadasArr] = new TablaSimbolos(nombreVariable, claseVariable,
//                        tipoVariable, ambito + "", null, null, null, null, r1 + "", avance, null, null);
                banderaRango = false;
                rango1 = "";
                rango2 = "";
                avance = "";
                bandera814 = false;
                break;
            case "Conjunto":
                aumentarAmbito();
                ambitoCreado = ambito + "";
                query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,ambitoCreado) VALUES("
                        + "'" + nombreVariable + "',"
                        + "'" + claseVariable + "',"
                        + "'" + tipoVariable + "',"
                        + "'" + ambitoVariable + "',"
                        + "'" + ambitoCreado + "');";
                auxiliarNombreVariable = nombreVariable;
                try {
                    st.executeUpdate(query);
                } catch (SQLException ex) {
                    Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                }

                System.out.println("contadorConjunto: " + contadorConjunto);
                for (int i = 0; i < contadorConjunto; i++) {
                    conjunto[i].setAmb(ambito + "");
                    String tipoE = conjunto[i].getTipo();
                    String claseE = conjunto[i].getClase();
                    String ambitoE = conjunto[i].getAmb();
                    String noPosE = conjunto[i].getNoPosicion();
                    String listaPerE = conjunto[i].getListaPertenece();
                    query = "INSERT INTO tablasimbolos (clase,tipo,ambito,noPos,listaPertenece) VALUES("
                            + "'" + claseE + "',"
                            + "'" + tipoE + "',"
                            + "'" + ambitoE + "',"
                            + "'" + noPosE + "',"
                            + "'" + listaPerE + "');";

                    try {
                        st.executeUpdate(query);
                    } catch (SQLException ex) {
                        Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    query = "UPDATE tablasimbolos SET tamanoArreglo = '" + contadorConjunto + "' where id='" + auxiliarNombreVariable + "';";
                    try {
                        st.executeUpdate(query);
                    } catch (SQLException ex) {
                        Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                agregarConjunto = false;
                contadorConjunto = 0;
                reducirAmbito();
                banderaConjunto = false;
                agregarConjunto = false;
                bandera814 = false;
                break;
            case "Diccionario":
                System.out.println("InsertarDiccionario");
                aumentarAmbito();
                ambitoCreado = ambito + "";
                query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,ambitoCreado) VALUES("
                        + "'" + nombreVariable + "',"
                        + "'" + claseVariable + "',"
                        + "'" + tipoVariable + "',"
                        + "'" + ambitoVariable + "',"
                        + "'" + ambitoCreado + "');";
                auxiliarNombreVariable = nombreVariable;
                try {
                    st.executeUpdate(query);
                } catch (SQLException ex) {
                    Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                }

                System.out.println("contadorDiccionario: " + contadorDiccionario);
                for (int i = 0; i < contadorDiccionario; i++) {
                    diccionario[i].setAmb(ambito + "");
                    String tipoE = diccionario[i].getTipo();
                    String claseE = diccionario[i].getClase();
                    String ambitoE = diccionario[i].getAmb();
                    String valorE = diccionario[i].getValor();
                    String noPosE = diccionario[i].getNoPosicion();
                    String llaveE = diccionario[i].getLlave();
                    String listaPerE = diccionario[i].getListaPertenece();
                    query = "INSERT INTO tablasimbolos (clase,tipo,ambito,valor,noPos,llave,listaPertenece) VALUES("
                            + "'" + claseE + "',"
                            + "'" + tipoE + "',"
                            + "'" + ambitoE + "',"
                            + "'" + valorE + "',"
                            + "'" + noPosE + "',"
                            + "'" + llaveE + "',"
                            + "'" + listaPerE + "');";

                    try {
                        st.executeUpdate(query);
                    } catch (SQLException ex) {
                        Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    query = "UPDATE tablasimbolos SET tamanoArreglo = '" + contadorDiccionario + "' where id='" + auxiliarNombreVariable + "';";
                    try {
                        st.executeUpdate(query);
                    } catch (SQLException ex) {
                        Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                agregarDiccionario = false;
                contadorDiccionario = 0;
                reducirAmbito();
                banderaDiccionario = false;
                agregarDiccionario = false;
                bandera814 = false;
                break;
        }
        tamVariablesGuardadasArr = 0;
        claseVariable = "";
        nombreVariable = "";
        tipoVariable = "";
        valorVariable = "";
        ambitoCreado = "";
        tarrVariable = "";
        if (banderaParametro) {
            claseVariable = "None";
            tipoVariable = "par";
        }
        System.out.println( "---- Fin Agregar Variable----");
    }

    String tipoConstante(int token) {
        String tipo = "";
        switch (token) {
            case -7:
                tipo = "Decimal";
                break;
            case -9:
                tipo = "Complejo";
                break;
            case -10:
                tipo = "Binario";
                break;
            case -12:
                tipo = "Hexadecimal";
                break;
            case -8:
                tipo = "Complejo";
                break;
            case -4:
                tipo = "Cadena";
                break;
            case -1:
                tipo = "Caracter";
                break;
            case -81:
            case -82:
                tipo = "Boolean";
                break;
            default:
                return tipo;
        }
        return tipo;
    }

    boolean buscarVariable(String id) {
        String ambitoAuxiliar = "-1";
        boolean resultado = false;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM tablasimbolos WHERE id= '" + id + "'");
            while (rs.next()) {
                ambitoAuxiliar = rs.getString("ambito");
                if (!ambitoAuxiliar.equals(-1)) {
                    if (Integer.parseInt(ambitoVariable) >= Integer.parseInt(ambitoAuxiliar)) {
                        resultado = true;
                    } else {
                        resultado = false;
                    }
                    break;
                } else {
                    resultado = false;
                }
            }
        } catch (SQLException e) {
        }
        return resultado;
    }

    void sintaxis() {
        tablaSimbolos = new TablaSimbolos[contadorIdentificadores];
        System.out.println("<ARREGLO DE AMBITO> Largo de Arreglo: " + tablaSimbolos.length);
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("----------SINTAXIS DE LA MUERTE----------");
        boolean tamArrPrimeraVez = false;
        pilaSintaxis.push(-1000);
        pilaSintaxis.push(200);
        oper.insertarUltimo(-1000, "$", lineaActual);
        int fila = 0, columna = 0, cont = 1, auxiliarContador = 0;
        String auxiliarID = "", auxiliarConstante = "", auxiliarConstante2 = "", auxiliarTipo = "";
        boolean primeraVezConjuntoDiccionario = false;
        boolean primeraVezConjunto = true;
        boolean primeraVezTipoLista = true;
        boolean primeraVezDiccionario = true;
        boolean llaveDiccionario = false;
        int auxiliarTipoVariable = 0;
        String auxiliarDato = "";
        while (pilaSintaxis.empty() == false && oper.listaVacia() == false) {
            System.out.println("<AMBITO:sintaxis> Nombre variable: " + nombreVariable);
            System.out.println("<AMBITO:sintaxis> Tipo variable: " + claseVariable);
            System.out.println("<AMBITO:sintaxis> Clase variable: " + tipoVariable);
            System.out.println("<AMBITO:sintaxis> Rango 1: " + rango1);
            System.out.println("<AMBITO:sintaxis> Rango 2: " + rango2);
            System.out.println("<AMBITO:sintaxis> Avance: " + avance);
            System.out.println("<AMBITO:sintaxis> Ambito: " + ambito);
            System.out.println("<AMBITO:sintaxis> Ambito Creado: " + ambitoCreado);
            System.out.println("<AMBITO:sintaxis> Cima de pila: " + pilaSintaxis.peek());

            if (pilaSintaxis.peek() == 8152) {
                pilaSintaxis.pop();
                agregarLista = true;
            }
            if (pilaSintaxis.peek() == 8032) {
                pilaSintaxis.pop();
                banderaParametro = false;
//                reducirAmbito();
            }
            if (pilaSintaxis.peek() == 8162) {
                pilaSintaxis.pop();
                agregarTupla = true;
                //reducirAmbito();
            }
            if (pilaSintaxis.peek() == 8202) {
                pilaSintaxis.pop();
                if (banderaConjunto) {
                    System.out.println("<AMBITO:sintaxis> Bandera Agregar Conjunto");
                    agregarConjunto = true;
                } else if (banderaDiccionario) {
                    System.out.println("<AMBITO:sintaxis> Bandera Agregar Diccionario");
                    agregarDiccionario = true;
                }
            }

            if (nombreVariable != "" && claseVariable != "" && valorVariable != "" && banderaConstante) {
                System.out.println("<AMBITO:sintaxis> Agregar Variable Constante");
                agregarVariable();
            } else if (nombreVariable != "" && claseVariable != "" && tipoVariable != "" && banderaFuncion) {
                System.out.println("<AMBITO:sintaxis> Agregar Variable Funcion");
                agregarVariable();
            } else if (nombreVariable != "" && claseVariable != "" && tipoVariable != "" && banderaParametro) {
                System.out.println("<AMBITO:sintaxis> Agregar Variable Parametro");
                agregarVariable();
            } else if (nombreVariable != "" && claseVariable != "" && rango1 != "" && rango2 != ""
                    && avance != "" && banderaRango) {
                System.out.println("<AMBITO:sintaxis> Agregar Variable Rango");
                agregarVariable();
            } else if (nombreVariable != "" && claseVariable != "" && tipoVariable != "" && ambitoVariable != ""
                    && tarrVariable != "" && (banderaListaMultiple || banderaListaNormal) && agregarLista) {
                System.out.println("<AMBITO:sintaxis> Agregar Variable Lista");
                agregarVariable();
                primeraVezTipoLista = true;
                auxiliarTipoVariable = 0;
                contadorElementosLista = 0;
                banderaListaMultiple = false;
                banderaListaNormal = false;
            } else if (nombreVariable != "" && claseVariable != "" && tipoVariable != "" && ambitoVariable != ""
                    && tarrVariable != "" && banderaTupla && agregarTupla) {
                System.out.println("<AMBITO:sintaxis> Agregar Variable Tupla");
                agregarVariable();
            } else if (nombreVariable != "" && claseVariable != "" && tipoVariable != "" && ambitoVariable != ""
                    && tarrVariable != "" && banderaConjunto && agregarConjunto) {
                System.out.println("<AMBITO:sintaxis> Agregar Variable Conjunto");
                agregarVariable();
            } else if (nombreVariable != "" && claseVariable != "" && tipoVariable != "" && ambitoVariable != ""
                    && tarrVariable != "" && banderaDiccionario && agregarDiccionario) {
                System.out.println("<AMBITO:sintaxis> Agregar Variable Diccionario");
                agregarVariable();
            }

            if (pilaSintaxis.peek() == -6) {
                auxiliarID = oper.mostrarLexemaPrimero();
            }

            switch (pilaSintaxis.peek()) {
                case 801:
                    System.out.println("AREA DE DECLARACION HA SIDO DESACTIVADA");
                    areaDeclaracion = false;
                    pilaSintaxis.pop();
                    break;
                case 802:
                    System.out.println("AREA DE DECLARACION HA SIDO ACTIVADA");
                    areaDeclaracion = true;
                    pilaSintaxis.pop();
                    break;
                case 803:
                    if (banderaParametro) {
                        banderaParametro = false;
                        pilaSintaxis.pop();
                        claseVariable = "";
//                        aumentarAmbito();
                    } else {
                        banderaParametro = true;
                        claseVariable = "None";
                        tipoVariable = "par";
                        ambitoVariable = ambito + "";

                        pilaSintaxis.pop();
                    }
                    break;
                case 804:
                    reducirAmbito();
                    pilaSintaxis.pop();
                    break;
            }

            if (pilaSintaxis.peek() >= 805 && pilaSintaxis.peek() <= 814 && !bandera814) {
                banderaConstante = true;
                System.out.println("<AMBITO:sintaxis>  Entro a if de 805 y 814 | 800:" + pilaSintaxis.peek());
                tipo800(pilaSintaxis.peek());
                pilaSintaxis.pop();
            } else if (pilaSintaxis.peek() > 814 && pilaSintaxis.peek() <= 821) {
                bandera814 = true;
                System.out.println("<AMBITO:sintaxis>  Entro a if de 815 y 821 | 800:" + pilaSintaxis.peek());
                tipo800(pilaSintaxis.peek());
                if (claseVariable.equals("Lista")) {
                    banderaListaNormal = true;
                    tipoVariable = "struct";
                    ambitoVariable = ambito + "";
                } else if (claseVariable.equals("Rango")) {
                    banderaRango = true;
                    System.out.println("<AMBITO:sintaxis> Bandera Rango");
                } else if (claseVariable.equals("Tupla")) {
                    banderaTupla = true;
                    tipoVariable = "struct";
                    System.out.println("<AMBITO:sintaxis> Bandera Tupla");
                    ambitoVariable = ambito + "";
                } else if (claseVariable.equals("Conjunto") && !banderaDiccionario) {
                    banderaConjunto = true;
                    tipoVariable = "struct";
                    System.out.println("<AMBITO:sintaxis> Bandera Conjunto");
                    ambitoVariable = ambito + "";
                } else if (claseVariable.equals("Diccionario")) {
                    banderaDiccionario = true;
                    banderaConjunto = false;
                    tipoVariable = "struct";
                    System.out.println("<AMBITO:sintaxis> Bandera Diccionario");
                    ambitoVariable = ambito + "";
                }
                pilaSintaxis.pop();
            }
            /////////////////////////DICCIONARIOS///////////////////////////////
            if (banderaDiccionario) {
                banderaConjunto = false;
                if (nombreVariable == "") {
                    nombreVariable = auxiliarID;
                }
                if (ambitoVariable == "") {
                    ambitoVariable = ambito + "";
                }
                if (pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7
                        || pilaSintaxis.peek() == -8 || pilaSintaxis.peek() == -81
                        || pilaSintaxis.peek() == -82 && !llaveDiccionario) {
                    auxiliarDato = oper.mostrarLexemaPrimero();
                    llaveDiccionario = true;
                } else if (pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7
                        || pilaSintaxis.peek() == -8 || pilaSintaxis.peek() == -81
                        || pilaSintaxis.peek() == -82 && llaveDiccionario) {
                    System.out.println("AGREGAR DICCIONARIO");
                    diccionario[contadorDiccionario] = new Diccionario(tipoConstante(pilaSintaxis.peek()),
                            "datoDic", ambito + "", auxiliarDato, contadorDiccionario + "", oper.mostrarLexemaPrimero(), nombreVariable);
                    contadorDiccionario++;
                    llaveDiccionario = false;
                }
            }
            /////////////////////FIN DICCIONARIOS///////////////////////////////

            //////////////////////////CONJUNTOS/////////////////////////////////
            if (banderaConjunto && !banderaDiccionario) {
                if (nombreVariable == "") {
                    nombreVariable = auxiliarID;
                }
                if (ambitoVariable == "") {
                    ambitoVariable = ambito + "";
                }
                if (pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7
                        || pilaSintaxis.peek() == -8 || pilaSintaxis.peek() == -81
                        || pilaSintaxis.peek() == -82) {
                    conjunto[contadorConjunto] = new Conjunto(tipoConstante(pilaSintaxis.peek()), "datoConj",
                            ambito + "", contadorConjunto + "", nombreVariable);
                    contadorConjunto++;
                    tarrVariable = contadorConjunto + "";
                }
            }
            //////////////////////////FIN CONJUNTOS/////////////////////////////

            ////////////////////////////TUPLAS//////////////////////////////////
            if (banderaTupla) {
                if (nombreVariable == "") {
                    nombreVariable = auxiliarID;
                }
                if (ambitoVariable == "") {
                    ambitoVariable = ambito + "";
                }
                if (pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7
                        || pilaSintaxis.peek() == -8 || pilaSintaxis.peek() == -81 || pilaSintaxis.peek() == -82) {

                    tuplaArreglo[contadorTupla] = new Tupla(tipoConstante(pilaSintaxis.peek()), "datoTupla",
                            ambito + "", contadorTupla + "", nombreVariable);
                    contadorTupla++;
                    tarrVariable = contadorTupla + "";
                }
            }
            ////////////////////////////FIN TUPLAS//////////////////////////////

            ////////////////////////////LISTAS//////////////////////////////////
            if (banderaListaNormal && !banderaListaMultiple) {
                if (nombreVariable == "") {
                    nombreVariable = auxiliarID;
                }
                if (ambitoVariable == "") {
                    ambitoVariable = ambito + "";
                }
                if (tipoVariable == "") {
                    tipoVariable = "struct";
                }
                if (claseVariable == "") {
                    claseVariable = "Lista";
                }
                if ((pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7
                        || pilaSintaxis.peek() == -8 || (pilaSintaxis.peek() == -81 || pilaSintaxis.peek() == -82))
                        && primeraVezTipoLista) {
                    primeraVezTipoLista = false;
                    auxiliarTipoVariable = pilaSintaxis.peek();
                    listaArreglo[contadorElementosLista] = new Lista(tipoConstante(pilaSintaxis.peek()), "datoLista",
                            (ambito + 1) + "", contadorElementosLista + "", nombreVariable);
                    contadorElementosLista++;
                    tarrVariable = contadorElementosLista + "";
                } else if (pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7
                        || pilaSintaxis.peek() == -8 || pilaSintaxis.peek() == -81
                        || pilaSintaxis.peek() == -82) {
                    tarrVariable = contadorElementosLista + "";
                    if (pilaSintaxis.peek() == auxiliarTipoVariable
                            || ((pilaSintaxis.peek() == -81 || pilaSintaxis.peek() == -82)
                            && (auxiliarTipoVariable == -81 || auxiliarTipoVariable == -82))) {
                        listaArreglo[contadorElementosLista] = new Lista(tipoConstante(pilaSintaxis.peek()), "datoLista",
                                (ambito + 1) + "", contadorElementosLista + "", nombreVariable);
                        contadorElementosLista++;
                        tarrVariable = contadorElementosLista + "";
                    } else {
                        banderaListaMultiple = true;
                        banderaListaNormal = false;
                        listaArreglo[contadorElementosLista] = new Lista(tipoConstante(pilaSintaxis.peek()), "datoLista",
                                ambito + "", contadorElementosLista + "", nombreVariable);
                        contadorElementosLista++;
                        tarrVariable = contadorElementosLista + "";
                    }
                }
            } else if (banderaListaMultiple && !banderaListaNormal) {
                if (nombreVariable == "") {
                    nombreVariable = auxiliarID;
                }
                if (ambitoVariable == "") {
                    ambitoVariable = ambito + "";
                }

                if (pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7
                        || pilaSintaxis.peek() == -8 || pilaSintaxis.peek() == -81 || pilaSintaxis.peek() == -82) {
                    System.out.println("Tipo de constante: " + tipoConstante(pilaSintaxis.peek()));
                    System.out.println("Ambito: " + (ambito + 1));
                    System.out.println("NoPos | contadorElementosLista: " + contadorElementosLista);
                    System.out.println("ListaPertenece: " + nombreVariable);

                    System.out.println("AGREGAR ELEMENTO LISTA");
                    listaArreglo[contadorElementosLista] = new Lista(tipoConstante(pilaSintaxis.peek()), "datoLista",
                            (ambito + 1) + "", contadorElementosLista + "", nombreVariable);
                    contadorElementosLista++;
                    tarrVariable = contadorElementosLista + "";
                }

            }
            ////////////////////////////FIN LISTAS//////////////////////////////

            //////////////////////////////RANGO/////////////////////////////////
            if (banderaRango && (pilaSintaxis.peek() == -7 || pilaSintaxis.peek() == -10 || pilaSintaxis.peek() == -11
                    || pilaSintaxis.peek() == -12) && rango1 == "") {
                System.out.println("<AMBITO:sintaxis> Agregar Rango 1");
                rango1 = oper.mostrarLexemaPrimero();
            } else if (banderaRango && (pilaSintaxis.peek() == -7 || pilaSintaxis.peek() == -10 || pilaSintaxis.peek() == -11
                    || pilaSintaxis.peek() == -12) && !rango1.equals("") && rango2.equals("")) {
                System.out.println("<AMBITO:sintaxis> Agregar Rango 2");
                rango2 = oper.mostrarLexemaPrimero();
            } else if (banderaRango && (pilaSintaxis.peek() == -7 || pilaSintaxis.peek() == -10 || pilaSintaxis.peek() == -11
                    || pilaSintaxis.peek() == -12) && !rango1.equals("") && !rango2.equals("") && avance.equals("")) {
                System.out.println("<AMBITO:sintaxis> Agregar Avance");
                avance = oper.mostrarLexemaPrimero();
            }
            //////////////////////////////FIN DE RANGO//////////////////////////

            //Activadores auxiliares de ambito
            switch (pilaSintaxis.peek()) {
                case 900:
                    valorVariable = oper.mostrarLexemaPrimero();
                    pilaSintaxis.pop();
                    break;
                case 901:
                    tipoVariable = "fun";
                    claseVariable = "None";
                    pilaSintaxis.pop();
                    ambitoVariable = ambito + "";
//                    aumentarAmbito();
                    ambitoCreado = ambito + "";
                    banderaFuncion = true;
                    break;
            }
//            if(pilaSintaxis.peek()==805){
//                pilaSintaxis.pop();
//            }

            System.out.println("--------------<INICIO DE CICLO SINTAXIS VUELTA " + cont + ">--------------");
            System.out.println("<SINTAXIS> Cima de la pila: " + pilaSintaxis.peek());
            System.out.println("<SINTAXIS> Lexema de primero: " + oper.mostrarLexemaPrimero());

            int produccion, lugar;
            if (pilaSintaxis.peek() > 199 && pilaSintaxis.peek() < 800) {
                fila = valorFila(pilaSintaxis.peek());
                System.out.println("<SINTAXIS> Valor de la fila: " + fila);
                System.out.println("");
                System.out.println("<SINTAXIS> INICIA TRY COLUMNA");
                try { //TRY COLUMNA
                    columna = valorColumna(oper.mostrarPrimero());
//                    System.out.println("<SINTAXIS TRY COLUMNA>  Numero de columna: " + columna);
                } catch (Exception ex) {
                    System.err.println("<SINTAXIS CATCH COLUMNA EN VUELTA: " + cont + ">    Primero de cola: " + oper.mostrarPrimero());
                    System.err.println("<SINTAXIS CATCH COLUMNA EN VUELTA: " + cont + ">    Causa " + ex.getCause());
                    System.err.println("<SINTAXIS CATCH COLUMNA EN VUELTA: " + cont + ">    Mensaje " + ex.getMessage());
                    System.err.println("<SINTAXIS CATCH COLUMNA EN VUELTA: " + cont + ">    Mensaje " + ex.getLocalizedMessage());
                }
                System.out.println("<SINTAXIS> TERMINA TRY COLUMNA");
                System.out.println("");
                System.out.println("Valor de la columna: " + columna);
                System.out.println("Valor de la fila: " + fila);
                produccion = matrizSintactico[fila][columna];
                System.out.println("<SINTAXIS Fila: " + fila + ", Columna: " + columna + ">   Produccion: " + produccion + "\n");
                System.out.println("Contenido de columnas");
                for (int i = 0; i <= columna; i++) {
                    System.out.print(matrizSintactico[fila][i] + "    ");
                }

            } else {
                produccion = 0;
            }

            if ((pilaSintaxis.peek() >= 200 && produccion < 600 && produccion != 369) && !oper.listaVacia()) {
                contadoresSintaxis(pilaSintaxis.peek());
                contadorDiagrama(produccion);
                pilaSintaxis.pop();
                lugar = produccion;
                System.out.println("<SINTAXIS> Valor de lugar: " + lugar);
                for (int i = 0; i < producciones[lugar].length; i++) {
                    System.out.println("<SINTAXIS> Se agrego " + producciones[lugar][i] + " a la pila");
                    pilaSintaxis.push(producciones[lugar][i]);
                }
            } else if (produccion == 369) {
                System.out.println("<SINTAXIS> EPSILON DETECTADO");
                pilaSintaxis.pop();
            } else if (produccion > 599) {
                System.out.println("<SINTAXIS ERROR> " + produccion);
                errores[nR] = new Error(produccion, "Error de sintaxis", produccion + "", cont, "Sintaxis");
                actualizarTablaError(errores);
                aumentarArregloError();
                oper.eliminarInicio();
//                    pila.pop();
            } else {
                try {
                    if (pilaSintaxis.peek() == oper.mostrarPrimero()) {
                        if (banderaArreglo == true) {//Arreglonizador de Mundos
                            if (tamArrPrimeraVez == false && pilaSintaxis.peek() == -7) {
                                tamArr = Integer.parseInt(oper.mostrarLexemaPrimero());
                                tamArrPrimeraVez = true;
                            } else {
                                if (pilaSintaxis.peek() == -7) {
                                    tamArr *= Integer.parseInt(oper.mostrarLexemaPrimero());
                                }
                            }
                        }
                        if (pilaSintaxis.peek() == -6) {
                            nombreVariable = oper.mostrarLexemaPrimero();
//                            System.out.println( + "<AMBITO:sintaxis> ID Detectado" + );
//                            System.out.println( + "<AMBITO:sintaxis> Nombre variable: " + nombreVariable + );
//                            System.out.println( + "<AMBITO:sintaxis> Tipo variable: " + claseVariable + );
//                            System.out.println( + "<AMBITO:sintaxis> Clase variable: " + tipoVariable + );

                        }
//                        if (tipoVariable != "" && nombreVariable != "") {
//                            System.out.println( + "<AMBITO:sintaxis> IF agregar variable" + );
//                            agregarVariable();
//                        }
                        oper.eliminarInicio();
                        pilaSintaxis.pop();
                    } else if (pilaSintaxis.peek() != oper.mostrarPrimero()) {
                        errores[nR] = new Error(1000, "ERROR DE FUERZA BRUTA", "", -1, "Sintaxis");
                        actualizarTablaError(errores);
                        aumentarArregloError();
                        System.out.println("<SINTAXIS ERROR> FUERZA BRUTA");
                        System.out.println("<SINTAXIS ERROR> FUERZA BRUTA Pila.peek:" + pilaSintaxis.peek() + "");
                        System.out.println("<SINTAXIS ERROR> FUERZA BRUTA Oper.mostrarPrimero:" + oper.mostrarPrimero());
                        System.out.println("<SINTAXIS ERROR> FUERZA BRUTA Oper.mostrarLexemaPrimero:" + oper.mostrarLexemaPrimero());
                        reproducir("src/fuerzabruta.wav");
                        JOptionPane.showMessageDialog(null, "Es momento de dar de baja la matería",
                                "Valio barriga", JOptionPane.WARNING_MESSAGE);
                        break;
                    }
                } catch (Exception ex) {
                    Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                    System.err.println("Excepcion cachada: " + ex.getMessage());
                    try {
                        Thread.sleep(0);
                    } catch (InterruptedException eex) {
                        Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, eex);
                    }
                }

            }
            System.out.println("--------------<FIN DE CICLO SINTAXIS>--------------");
            System.out.println("");
            cont++;

        }
        System.out.println("---------- FIN SINTAXIS DE LA MUERTE----------");
        if (ambito == 0) {
            System.out.println("<AMBITO> Ambito 0");
        } else {
            errores[nR] = new Error(2000, "ERROR DE AMBITO", "Ambito Final: " + ambito + "", -1, "Ambito");
            actualizarTablaError(errores);
            aumentarArregloError();
        }
        imprimirArregloTablaSimbolos();
        contadorAmbito();
    }

    void imprimirArregloTablaSimbolos() {
        System.out.println("-----Tabla de Simbolos-----");
        try {
            for (int i = 0; i < tamVariablesGuardadasArr; i++) {
                System.out.println("<ArregloSimbolosAmbito> ID:" + tablaSimbolos[i].getId());
                System.out.println("<ArregloSimbolosAmbito> Tipo:" + tablaSimbolos[i].getTipo());
                System.out.println("<ArregloSimbolosAmbito> Clase:" + tablaSimbolos[i].getClase());
                System.out.println("<ArregloSimbolosAmbito> Ambito:" + tablaSimbolos[i].getAmb());
                System.out.println("<ArregloSimbolosAmbito> Tamano Arr:" + tablaSimbolos[i].getTarr());
                System.out.println("<ArregloSimbolosAmbito> Ambito Creado:" + tablaSimbolos[i].getAmbCreado());
                System.out.println("<ArregloSimbolosAmbito> Numero de Posicion:" + tablaSimbolos[i].getNoPos());
                System.out.println("<ArregloSimbolosAmbito> Valor:" + tablaSimbolos[i].getValor());
                System.out.println("<ArregloSimbolosAmbito> Rango:" + tablaSimbolos[i].getRango());
                System.out.println("<ArregloSimbolosAmbito> Avance:" + tablaSimbolos[i].getAvance());
                System.out.println("");
            }
        } catch (Exception e) {

        }
        System.out.println("-----Fin Tabla de Simbolos-----");
    }

    void establecerConexion() {
        mysql.establecerConexion();
    }

    void reproducir(String archivo
    ) {
        AudioInputStream audioIn;
        Clip clip;
        File f = new File(archivo);
        try {
            audioIn = AudioSystem.getAudioInputStream(f);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
//            InputStream in = new FileInputStream(archivo);
//            AudioStream audio = new AudioStream(in);
//            AudioPlayer.player.start(audio);
        } catch (Exception e) {

        }
    }

//    }
    void actualizarTablaToken(Token[] tok
    ) {
        for (int i = 0; i < tok.length-1; i++) {
            tablaTokens.setValueAt(tok[i].getEstado(), i, 0);
            tablaTokens.setValueAt(tok[i].getLexema(), i, 1);
            tablaTokens.setValueAt(tok[i].getLinea(), i, 2);
        }

    }

    void actualizarTablaError(Error[] tok) {
        for (int i = 0; i < tok.length; i++) {
            tablaErrores.setValueAt(tok[i].getEstado(), i, 0);
            tablaErrores.setValueAt(tok[i].getDescripcion(), i, 1);
            tablaErrores.setValueAt(tok[i].getLexema(), i, 2);
            tablaErrores.setValueAt(tok[i].getLinea(), i, 3);
            tablaErrores.setValueAt(tok[i].getTipo(), i, 4);
        }
    }

    void aumentarArregloToken() {
        Token aux[] = tokens;
        int cont = nT;
        nT++;
        tokens = new Token[nT + 1];
        for (int i = 0; i <= cont; i++) {
            tokens[i] = aux[i];
        }
    }

    void aumentarArregloError() {
        Error aux[] = errores;
        int cont = nR;
        nR++;
        errores = new Error[nR + 1];
        for (int i = 0; i <= cont; i++) {
            errores[i] = aux[i];
        }
    }

    void limpiarTablas() {
        tablaTokens = new javax.swing.JTable();
        tablaTokens.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null}
                },
                new String[]{
                    "Estado", "Lexema", "Linea"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane3.setViewportView(tablaTokens);

        tablaErrores = new javax.swing.JTable();
        tablaErrores.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null}
                },
                new String[]{
                    "Estado", "Descripcion", "Lexema", "Linea", "Tipo"
                }
        ));
        jScrollPane4.setViewportView(tablaErrores);
    }
    
    File createFileToLog(){
        LocalDateTime localDate = LocalDateTime.now();
        int segundo = localDate.getSecond();
        int minuto = localDate.getMinute();
        int hora = localDate.getHour();
        int dia = localDate.getDayOfMonth();
        String mes = localDate.getMonth() + "";
        int ano = localDate.getYear();
        return new File("test/Log [" + hora + "-" + minuto + "-" + segundo + "]@[" + dia + "-" + mes + "-" + ano + "].txt");
    }

    void logWriter() {
        
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
    private javax.swing.JButton btnCargar;
    private javax.swing.JButton btnCompilar;
    private javax.swing.JButton btnXlsx;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTable tablaErrores;
    private javax.swing.JTable tablaTokens;
    // End of variables declaration//GEN-END:variables
}
