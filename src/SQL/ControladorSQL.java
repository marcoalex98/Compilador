package SQL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControladorSQL {
    Connection conexionSQL;
    Statement st;

    public ControladorSQL() {
        establecerConexion();
    }

    private void establecerConexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexionSQL = DriverManager.getConnection("jdbc:mysql://localhost/a16130329?verifyServerCertificate=false&useSSL=true", "root", "root");
            st = conexionSQL.createStatement();
        } catch (SQLException e) {
            System.err.println("<SQL> Error SQL al intentar establecer conexion con la base de datos");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("<SQL> Error SQL al intentar establecer conexion con la base de datos");
        } catch (Exception e) {
            System.err.println("<SQL> Error detectado: " + e.getMessage());
            System.err.println("<SQL> Excepcion simple al intentar establecer conexion con la base de datos");
        }
    }

    public void cerrarConexion() {
        try {
            conexionSQL.close();
        } catch (SQLException ex) {
            Logger.getLogger(ControladorSQL.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("<MySQL> Error al intentar cerrar la conexion a la base de datos");
        }
    }

    public void limpiarTablaSimbolos() {
        try {
            st.executeUpdate("DELETE FROM tablasimbolos");
        } catch (SQLException ex) {
            Logger.getLogger(ControladorSQL.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("<SQL> Error al vaciar tabla de simbolos");
        }
    }

    public void ejecutarQuery(String query) {
        try {
            st.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(ControladorSQL.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("<SQL> Error al ejecutar el query: "+query);
        }
    }
    
    public ResultSet obtenerResultSet(String query) throws SQLException{
        return st.executeQuery(query);
    }
}