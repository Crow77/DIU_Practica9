import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.logging.*;

public class Conexion{ 
    private Map<String, Set<String>> tablas;
    Connection miConexion = null;
    DatabaseMetaData md = null;
    ResultSet rs = null;
    private Set<String> columnas;
public  Conexion(){
    columnas = new LinkedHashSet<>();
    tablas = new HashMap<>();
}
   
    public boolean getConexion(String usuario, String password){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //1.- Crear connexion                                   ruta driver jdbc para mysql. es la misma para mysql
            miConexion =  DriverManager.getConnection("jdbc:mysql://mozart.dis.ulpgc.es/PracticaDIU?useSSL=true",usuario, password);     
            return true;
        }catch(Exception e){return false;}
    }  
    
    public Map<String, Set<String>> datosBBDD(){
       
        try {          
            String[] types = {"TABLE"};
            md = miConexion.getMetaData();
            rs = md.getTables(null, null, "%", types);
            while (rs.next()) {
                String nombreTabla = rs.getString("TABLE_NAME");
                System.out.println("Tabla: " + nombreTabla);
                
                ResultSet rs2 = md.getColumns(null, null, nombreTabla, null);
                
                while (rs2.next()) {
                    String nombreCampo = rs2.getString("COLUMN_NAME");
                    System.out.println(" Campo: " + nombreCampo);
                    columnas.add(nombreCampo);    
                }
                tablas.put(nombreTabla, columnas);    
            }    
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }    
        return tablas;     
    }
    
    public boolean logOut(){
        try {
            miConexion.close();             //cerramos la conexion
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
