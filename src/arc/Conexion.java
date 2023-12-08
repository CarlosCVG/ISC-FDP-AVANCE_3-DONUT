/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package arc;

//Librerias
import java.sql.Connection; 
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author feriv
 */
    public class Conexion {
        String bd="control_escolar";
        String url ="jdbc:mysql://localhost:3306/";
        String user="root";
        String password="";
        String driver="com.mysql.cj.jdbc.Driver";
        Connection cx;
        //Pasar dirección de la base de datos local
        
        //Intentamos conectar con la DB (DataBase)
    public Connection conectar() {
        try {
            Class.forName(driver);
            cx = (Connection) DriverManager.getConnection(url+bd, user, password);
            } catch (ClassNotFoundException |SQLException ex) {
                System.out.println("No se conecto a " + bd);
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            }
            return cx;
        }

    //Metodo: Cerrar la base de datos cuando no se utiliza
    public void desconectar(){
        try {
            cx.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}