package com.mycompany.cursosfbmysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * @author 
 */
public class CursosFbMysql {

     private Connection connection = null;
     private Connection connection_my = null;
     private ResultSet resultSet = null;
     private ResultSet resultSet_my = null;
     private Statement statement = null;
     private Statement statement_my = null;
     private String db= "db_jee2";
     private String user = "SYSDBA";
     private String password = "20591";
     private String db_my= "inscripciones_alumnos";
     private String user_my = "desadev";
     private String password_my = "25012K967";
 
 //Constructor de la clase que se conecta a la base de datos una vez que se crea la instancia
   public CursosFbMysql(){
       try{
          // Funciona de las 2 formas con o sin .newInstance() 
          // Class.forName("org.firebirdsql.jdbc.FBDriver").newInstance();
          
          Class.forName("org.firebirdsql.jdbc.FBDriver");
                                               
          connection = DriverManager.getConnection("jdbc:firebirdsql:10.1.0.15:" + db,this.user, this.password);
          System.out.println("Conectado a la base de datos [ " + this.db + "]");
       }catch(Exception e){
          System.out.println(e);
          System.out.println("Connect ...");
       }
     }

   
    public boolean CursosMysql(){
       try{
          
          Class.forName("com.mysql.jdbc.Driver");
          connection_my = DriverManager.getConnection("jdbc:mysql://192.168.10.10/" + db_my, this.user_my, this.password_my);
          System.out.println("Conectado a la base de datos [ " + this.db_my + "]");
       }catch(Exception e){
          System.out.println(e);
          System.out.println("Connect ...");
       }       
       return true;
     }   


 /* METODO PARA INSERTAR UN REGISTRO EN LA BASE DE DATOS
  * INPUT:
  table = Nombre de la tabla
  fields = String con los nombres de los campos donde insertar Ej.: campo1,campo2campo_n
  values = String con los datos de los campos a insertar Ej.: valor1, valor2, valor_n
  * OUTPUT:
  * Boolean
*/
   
  public boolean insert(String table, String fields, String values)
     {
         boolean res=false;
         //Se arma la consulta
         String q=" INSERT INTO " + table + " ( " + fields + " ) VALUES ( " + values + " ) ";
         //se ejecuta la consulta
         try {
             PreparedStatement pstm = connection.prepareStatement(q);
             pstm.execute();
             pstm.close();
             res=true;
          }catch(Exception e){
             System.out.println(e);
             System.out.println("Fallo el Insert ...");
         }
       return res;
     }

// -------------------------------
// -------------------------------   
// -------------------------------   

 /* METODO PARA REALIZAR UNA CONSULTA A LA BASE DE DATOS
  * INPUT:
 
  * OUTPUT:
  String con los datos concatenados
 */
  public String select()
  {
     String res=" ID | CodMov | Nombre  \n ";
     try {
       statement = connection.createStatement();
       resultSet = statement.executeQuery("SELECT * FROM acad_tipo_mov order by codigo_mov");
       
       while (resultSet.next())
       {
         res+=resultSet.getString("ID") + " | " +
             resultSet.getString("Codigo_mov") + " | " + 
             resultSet.getString("Nombre") + " \n ";
        }
      }
      catch (SQLException ex) {
         System.out.println(ex);
         System.out.println("Select ...");
         
      }
     
     return res;
     
  }
  
public String select_my()
  {
     String res=" ID | CodMov | Nombre  \n ";
     try {
       statement_my = connection_my.createStatement();
       resultSet_my = statement_my.executeQuery("SELECT * FROM Inscripcion order by Referencia desc limit 25");
       
       while (resultSet_my.next())
       {
         res+=resultSet_my.getString("Semes_anio") + " | " +
             resultSet_my.getString("Facultad") + " | " + 
             resultSet_my.getString("Anio") + " | " +                  
             resultSet_my.getString("Carnet") + " \n ";
             
        }
      }
      catch (SQLException ex) {
         System.out.println(ex);
         System.out.println("Error 0003: Select ...");
         
      }
     
     return res;
     
  }  
// -------------------------------
// -------------------------------   
// -------------------------------   

  public void desconectar()
     {
         try {
             resultSet.close();
             statement.close();
             connection.close();
             System.out.println("Desconectado de la base de datos [ " + this.db + "]");
         }        
         catch (SQLException ex) {
             System.out.println(ex);
         }
     }
 
public void desconectar_my()
     {
         try {
             resultSet_my.close();
             statement_my.close();
             connection_my.close();
             System.out.println("Desconectado de la base de datos [ " + this.db_my + "]");
         }        
         catch (SQLException ex) {
             System.out.println(ex);
         }
     }  
// -------------------------------
// -------------------------------   
// -------------------------------   


public static void main(String[] args) {        
         //Se crea el objeto y se conecta a firebird
         CursosFbMysql fbc = new CursosFbMysql();
         //Se insertan algunos datos
         fbc.insert("acad_tipo_mov", " id, codigo_mov , nombre ", "991, 'AAA' , 'A1' ");
         fbc.insert("acad_tipo_mov", " id, codigo_mov , nombre ", "992, 'BBB' , 'B1' ");
         fbc.insert("acad_tipo_mov", " id, codigo_mov , nombre ", "993, 'CCC' , 'C1' ");
         
         //Se imprimen los datos de la tabla
         System.out.println( fbc.select() );
         fbc.desconectar();  
         
         //
         //
         
         CursosFbMysql mys = new CursosFbMysql();
         mys.CursosMysql();
         
         System.out.println( mys.select_my() );
         mys.desconectar_my();  
    }
     
}
