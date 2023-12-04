package arc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

    public class Logica {

    Conexion cx;

    public void comboMaterias(String semestre, JComboBox combo) {
        cx = new Conexion();
        cx.conectar();

        combo.removeAllItems();

        String query = "SELECT * FROM materias" + semestre;

        try {
            Statement st = cx.conectar().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                combo.addItem(rs.getString("materia"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            
           cx.desconectar();
        }

    }

    public void mostrarDatos(String id, JTable tablaAlumno, JTable tablaAlumno1) {

        cx = new Conexion();
        cx.conectar();

        String query = "SELECT a.matricula, a.nombre1, a.nombre2, a.apellido1, a.apellido2, a.telefono, a.correo, a.semestre, c.carrera FROM alumnos a JOIN carreras c ON a.id_carrera = c.id WHERE a.id = '" + id + "'";

        DefaultTableModel model = new DefaultTableModel();
         DefaultTableModel model1 = new DefaultTableModel();

        model.addColumn("Matricula");
        model.addColumn("Primer nombre");
        model.addColumn("Ssegundo nombre");
        model.addColumn("Apellido paterno");
        model.addColumn("Apellido materno");
        
        model1.addColumn("Telefono");
        model1.addColumn("Correo");
        model1.addColumn("Semestre");
        model1.addColumn("Carrera");
        tablaAlumno.setModel(model);
        tablaAlumno1.setModel(model1);
        
        String[] datos = new String[5];
        String[] datos1 = new String[10];
         
        try {
            Statement st = cx.conectar().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);
                datos1[0] = rs.getString(6);
                datos1[1] = rs.getString(7);
                datos1[2] = rs.getString(8);
                datos1[3] = rs.getString(9);
                
                model.addRow(datos);
                model1.addRow(datos1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            
           cx.desconectar();
        }
        
    }

    public void mostrarDatosMaterias(String id, String materia, JTable tablaMateria) {

        cx = new Conexion();
        cx.conectar();

        String query = "SELECT m.creditos, CASE WHEN am.califa IS NULL THEN 'Sin calificación' ELSE am.califa END AS calificacion FROM materias m LEFT JOIN alumnos_materias am ON m.id = am.id_materia AND am.id_alumno = '" + id + "' WHERE m.materia = '"+ materia +"';";

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Creditos");
        model.addColumn("Calificacion");

        tablaMateria.setModel(model);

        String[] datos = new String[2];

        try {
            Statement st = cx.conectar().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);

                model.addRow(datos);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            
           cx.desconectar();
        }
        
    }
   public void mostrarCursos(String id, String materia,JPanel cont, JLabel welcome,JLabel titulo, JLabel link, JLabel unidad){
        cx = new Conexion();
        cx.conectar();

        String query = "SELECT cu.titulo, cu.link, cu.unidad, m.materia AS materia_reprobada, am.califa AS calificacion FROM cursos cu INNER JOIN materias m ON cu.id_materia = m.id INNER JOIN alumnos_materias am ON m.id = am.id_materia WHERE am.califa < 70 AND am.id_alumno = '" + id + "' AND m.materia = '" + materia + "' ";

        try {

            Statement st = cx.conectar().createStatement();
            ResultSet rs = st.executeQuery(query);

            String[] curso = new String[3];

            while (rs.next()) {

                curso[0] = rs.getString("titulo");
                curso[1] = rs.getString("link");
                curso[2] = rs.getString("unidad");

            }

            if (curso.length != 0) {
                welcome.setText("Te recomendamos los siguientes cursos: ");
                titulo.setText(curso[0]);
                link.setText(curso[1]);
                unidad.setText("Unidad " + curso[2]);
                cont.setVisible(true);
            } else { 
                 cont.setVisible(false);
             
            }

        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            
           cx.desconectar();
        }
          
    } 
        
   public void avanceAcademico(String id, JLabel promedioActual, JLabel avanceCreditos){
   
        cx = new Conexion();
        cx.conectar();

        String query = " SELECT AVG(califa) AS promedio  FROM alumnos_materias WHERE id_alumno = '"+ id +"' AND califa IS NOT NULL;";
        String query2 ="SELECT SUM(m.creditos) AS suma_creditos FROM alumnos_materias am JOIN materias m ON am.id_materia = m.id WHERE am.id_alumno = '"+ id +"' AND am.califa >= 70 AND am.califa IS NOT NULL";
        try {

            Statement st = cx.conectar().createStatement();
            Statement st2 = cx.conectar().createStatement();
            ResultSet rs = st.executeQuery(query);          
            ResultSet rs2 = st2.executeQuery(query2);

            while (rs.next()) {
                promedioActual.setText(rs.getString(1)); 
          
            }

            while (rs2.next()) {
                avanceCreditos.setText(rs2.getString(1));
         
            }
                                                            
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            
           cx.desconectar();
        }
                
       }   
    
   
   public void horario(String id, JTable tablaRecomendacion){
        
       cx = new Conexion();
    

        cx.conectar();

        String query = "SELECT m.materia, CASE WHEN mp.id_profesor IS NOT NULL THEN CONCAT(p.nombre1, ' ', COALESCE(p.nombre2, ''), ' ', p.apellido1, ' ', COALESCE(p.apellido2, '')) ELSE 'Sin asignar' END AS nombre_maestro FROM materias m LEFT JOIN materias_profesores mp ON m.id = mp.id_materia LEFT JOIN profesores p ON mp.id_profesor = p.id JOIN materias_carreras mc ON m.id = mc.id_materia JOIN alumnos a ON a.id_carrera = mc.id_carrera WHERE a.id =  '"+id+"' AND m.semestre BETWEEN a.semestre + 1 AND a.semestre + 2 ORDER BY RAND() LIMIT 6; ";

       DefaultTableModel model = new DefaultTableModel();
      
        model.addColumn("Materias");
        model.addColumn("Profesores");

       tablaRecomendacion.setModel(model);

        String[] datos = new String[12];

        try {
            Statement st = cx.conectar().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                datos[0] = rs.getString("materia");
                datos[1] = rs.getString("nombre_maestro");
                datos[2] = rs.getString("materia");
                datos[3] = rs.getString("nombre_maestro");
                datos[4] = rs.getString("materia");
                datos[5] = rs.getString("nombre_maestro");
                datos[6] = rs.getString("materia");
                datos[7] = rs.getString("nombre_maestro");
                datos[8] = rs.getString("materia");
                datos[9] = rs.getString("nombre_maestro");
                datos[10] = rs.getString("materia");
                datos[11] = rs.getString("nombre_maestro");
           
                model.addRow(datos);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            
           cx.desconectar();
        }
   
    }
}