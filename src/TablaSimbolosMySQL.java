
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class TablaSimbolosMySQL extends javax.swing.JFrame {

    Connection con;
    //ConexionMySQL con = new ConexionMySQL();

    /**
     * Creates new form TablaSimbolosMySQL
     */
    public TablaSimbolosMySQL() {
        initComponents();
        llenarTabla();
    }

    public ResultSet ejecutarQuery(String query) {
        ResultSet rs = null;
        try {
            System.out.println("<MySQL> Prueba de conexion a MySQL");
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/a16130329?verifyServerCertificate=false&useSSL=true", "root", "root");
            System.out.println("<TablaSimbolosMySQL> Conexion exitosa");

            Statement estado = con.createStatement();
            rs = estado.executeQuery(query);

        } catch (SQLException e) {
            System.err.println("<TablaSimbolosMySQL> Error de MySQL");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("<TablaSimbolosMySQL> Error detectado: " + e.getMessage());
        }
        return rs;
    }

    void llenarTabla() {
        DefaultTableModel modelo = (DefaultTableModel)jTable1.getModel();
        ResultSet rs = ejecutarQuery("SELECT * FROM tablasimbolos");
        try {
            while (rs.next()) {
                modelo.addRow(new Object[]{rs.getString("id"),rs.getString("tipo"),rs.getString("clase"),
                rs.getString("ambito"),rs.getString("tamanoArreglo"),rs.getString("ambitoCreado"),
                rs.getString("noPos"),rs.getString("listaPertenece"),rs.getString("rango"),
                rs.getString("avance"),rs.getString("llave"),rs.getString("valor")});
            }
            jTable1.setModel(modelo);
        } catch (SQLException ex) {
            Logger.getLogger(TablaSimbolosMySQL.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Tipo", "Clase", "Ambito", "Tamano Arreglo", "Ambito Creado", "NoPos", "Lista Pertenece", "Rango", "Avance", "Llave", "Valor"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Cerrar Ventana");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 926, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 11, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(TablaSimbolosMySQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TablaSimbolosMySQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TablaSimbolosMySQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TablaSimbolosMySQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TablaSimbolosMySQL().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
