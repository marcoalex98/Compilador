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
    
    public String obtenerClaseVariable(String variable, int ambito) {
        String query = "SELECT clase FROM tablasimbolos WHERE (id = BINARY '" + variable + "' AND (ambito = '" + ambito + "' OR ambito = '0'))";
        String claseVariable = "";
        try {
            ResultSet rs = obtenerResultSet(query);
            while (rs.next()) {
                claseVariable = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControladorSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return claseVariable;
    }
    
    public void actualizarTipoArreglo(String id, String ambito, String clase){
        String idAuxiliar = id.replaceAll("\\s", "");
        String query = "UPDATE tablasimbolos "
                + "SET listaPertenece = '" + clase +"' "
                + "WHERE (id = BINARY '" + idAuxiliar + "' AND (ambito='" + ambito + "' OR ambito='0'))";
        ejecutarQuery(query);
    }
    
    public boolean isArregloVirgen(String id, String ambito){
        String idAuxiliar = id.replaceAll("\\s", "");
        String query = "SELECT listaPertenece FROM tablasimbolos WHERE (id = BINARY "
                + "'" + idAuxiliar + "' AND (ambito='" + ambito + "' OR ambito='0'))";
        try {
            ResultSet rs = obtenerResultSet(query);
            return rs.next();
            
        } catch (SQLException ex) {
            Logger.getLogger(ControladorSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public String obtenerTipoArreglo(String variable, int ambito) {
        String query = "SELECT listaPertenece FROM tablasimbolos WHERE (id = BINARY '" + variable + "' AND (ambito = '" + ambito + "' OR ambito = '0'))";
        String tipoVariable = "";
        try {
            ResultSet rs = obtenerResultSet(query);
            while (rs.next()) {
                tipoVariable = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControladorSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tipoVariable;
    }
    
    public boolean comprobarVariableDeclarada(String id, String ambito){
        String idAuxiliar = id.replaceAll("\\s", "");
        String query = "SELECT * FROM tablasimbolos WHERE (id = BINARY "
                + "'" + idAuxiliar + "' AND (ambito='" + ambito + "' OR ambito='0'))";
        try {
            ResultSet rs = obtenerResultSet(query);
            return rs.next();
            
        } catch (SQLException ex) {
            Logger.getLogger(ControladorSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean comprobarVariableDuplicada(String id, String ambito){
        String idAuxiliar = id.replaceAll("\\s", "");
        String query = "SELECT * FROM tablasimbolos WHERE (id = BINARY " + "'"
                + idAuxiliar + "' AND (ambito='" + ambito + "'))";
        try {
            ResultSet rs = obtenerResultSet(query);
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(ControladorSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    
}