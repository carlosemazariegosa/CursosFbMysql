package com.mycompany.cursosfbmysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author 
 */

/*
 TO DO:
 - Timer https://poesiabinaria.net/2014/01/intro-timertask-java/
 - Log: 
 - - Crear el archivo tipo Logger: https://coderanch.com/t/410260/java/Writing-log-file
 - - Crear el formato de fecha: https://www.lawebdelprogramador.com/foros/Java/88884-Crear-archivo-llevando-x-titulo-la-fecha-del-sist.html
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
    private String user_my = "root";
    private String password_my = "";
    private Boolean reslog;
    
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
          connection_my = DriverManager.getConnection("jdbc:mysql://localhost/" + db_my, this.user_my, this.password_my);
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
   
  public boolean insert(String table, String fields, String values,Logger logger)
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
             //reclog("Insert Firebird -> OK");
          }catch(Exception e){
             System.out.println(e);
             System.out.println("Fallo el Insert ...");
             reslog = reclog("Error al insertar en Firebird -> OK", logger);             
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
  public String select(Logger logger)
  {
     String res=" ID | CodMov | Nombre  \n ";
     try {
       statement = connection.createStatement();
       resultSet = statement.executeQuery("SELECT first 3 * FROM acad_tipo_mov order by codigo_mov");
       
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
     reslog = reclog("Consulta FireBird -> OK",logger);
     return res;
     
  }
  
public String select_my(Logger logger)
  {
     String res=" Semestre | Facultad | Año | Carnet  \n ";
     try {
       statement_my = connection_my.createStatement();
       resultSet_my = statement_my.executeQuery("SELECT * FROM Inscripcion order by Referencia desc limit 3");
       
       while (resultSet_my.next())
       {
         res+=resultSet_my.getString("Semes_anio") + " | " +
             resultSet_my.getString("Facultad") + " | " + 
             resultSet_my.getString("Anio") + " | " +                  
             resultSet_my.getString("Carnet") + " \n ";
             
        }
       reslog = reclog("Consulta MySQL... -> OK",logger);
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

  public void desconectar(Logger logger)
     {
         try {
             resultSet.close();
             statement.close();
             connection.close();
             System.out.println("Desconectado de la base de datos [ " + this.db + "]");
             reslog = reclog("Desconecta Firebird -> OK",logger);
         }        
         catch (SQLException ex) {
             System.out.println(ex);
         }
     }
 
public void desconectar_my(Logger logger)
     {
         try {
             resultSet_my.close();
             statement_my.close();
             connection_my.close();
             System.out.println("Desconectado de la base de datos [ " + this.db_my + "]");
             reslog = reclog("Desconecta MySQL... -> OK",logger);
         }        
         catch (SQLException ex) {
             System.out.println(ex);
         }
     }  

     
public boolean reclog(String imsj, Logger logger) {
         
        // Logger logger = Logger.getLogger("MyLog");
        // FileHandler fh;
         
        
             
            // This block configure the logger with handler and formatter
            // El valor true indica que escriba en el mismo archivo
            // fh = new FileHandler("./MyLogFile.log", true);
            // logger.addHandler(fh);
            // logger.setLevel(Level.ALL);
            // SimpleFormatter formatter = new SimpleFormatter();
            // fh.setFormatter(formatter);
             
            // the following statement is used to log any messages
            logger.info(imsj);
             
        
         
        //logger.info("Hi How r u?");
        return true;
        
}
     
// -------------------------------
// -------------------------------   
// -------------------------------   


public static void main(String[] args) { 
    
        // Timer
        Timer timer;
        timer = new Timer();
        
        TimerTask task = new TimerTask() {
            Integer icontador = 1;  
                
            Logger logger = Logger.getLogger("MyLog");
            public FileHandler fh;    
            
            @Override
            public void run() {
                //
                // Se definen la configuración para la bitacora
                //

                try {  
                   
                   fh = new FileHandler("./MyLogFile.log", true);
                   logger.addHandler(fh);
                   logger.setLevel(Level.ALL);
                   SimpleFormatter formatter = new SimpleFormatter();
                   fh.setFormatter(formatter);
                } catch (SecurityException e) {
                    e.printStackTrace();
                  } catch (IOException e) {
                    e.printStackTrace();
                    }

                //Se crea el objeto y se CONECTA a firebird

                CursosFbMysql fbc = new CursosFbMysql();

                //Se insertan algunos datos
                fbc.insert("acad_tipo_mov", " id, codigo_mov , nombre ", "991, 'AAA' , 'A1' ", logger);
                fbc.insert("acad_tipo_mov", " id, codigo_mov , nombre ", "992, 'BBB' , 'B1' ", logger);
                fbc.insert("acad_tipo_mov", " id, codigo_mov , nombre ", "993, 'CCC' , 'C1' ", logger);

                //Se imprimen los datos de la tabla - SELECT

                System.out.println( fbc.select(logger) );
                fbc.desconectar(logger);  

                //Se crea el objeto y se CONECTA a MySQL

                CursosFbMysql mys = new CursosFbMysql();
                mys.CursosMysql();

                //Se imprimen los datos de la tabla - SELECT

                System.out.println( mys.select_my(logger) );
                mys.desconectar_my(logger);  
                System.out.println("Ejecuciones -------------> " + icontador);
                icontador++;
            }
        
        };
        timer.schedule(task, 10, 20000);
    }
     
}              