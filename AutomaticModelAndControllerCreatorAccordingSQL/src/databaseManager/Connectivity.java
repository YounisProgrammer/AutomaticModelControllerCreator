package databaseManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import beans.*;
import java.io.IOException;
import resources.Resources;

public class Connectivity {
    public static Connection getConnection(String sqlType,String dbName,String username,String password)throws IOException,SQLException,ClassNotFoundException{
        Connection con=null;
        
            SQLBean sql=Resources.getSQL(sqlType);
            
            Class.forName(sql.getDriverPath());
            con=DriverManager.getConnection(sql.getDbURL()+dbName,username,password);
            
            System.out.println("Connection Established!!!");
        
            
        return con;
    }//End of getConnection method
    
}//End of class
