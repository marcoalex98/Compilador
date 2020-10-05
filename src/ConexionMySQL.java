import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class ConexionMySQL {
    Connection con;
    public void establecerConexion(){
        try{
            System.out.println("<MySQL> Prueba de conexion a MySQL");
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost/a16130329?verifyServerCertificate=false&useSSL=true","root","root");
            System.out.println("<MySQL> Conexion exitosa");
            
            Statement estado=con.createStatement();
            ResultSet resultado = estado.executeQuery("select * from tablasimbolos");
            
            System.out.println("<MySQL> Resultado de la tabla simbolo");
            while(resultado.next()){
                System.out.println(resultado.getString("id"));
            }
            con.close();
        }catch(SQLException e){
            System.err.println("<MySQL> Error de MySQL");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(Exception e){
            System.err.println("<MySQL> Error detectado: "+e.getMessage());
        }
    }
    
    public ResultSet selectTodo(){
        ResultSet rs=null;
        establecerConexion();
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/a16130329?verifyServerCertificate=false&useSSL=true","root","root");
            Statement s = con.createStatement();
            rs = s.executeQuery("select * from tablasimbolos");
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rs;
    }
}
