package databaseManager;

import java.sql.*;
import java.io.*;
import java.util.*;
import beans.ColumnBean;
import beans.TableBean;
import resources.Decoder;
import resources.Resources;

public class DatabaseManager {
    static private Connection con;
    static private beans.SQLBean sqlBean;
    public static void createConnection(String sqlType,String dbName,String username,String password){
        try{
            con=Connectivity.getConnection(sqlType,dbName,username,password);
            sqlBean=Resources.getSQL(sqlType);
            sqlBean.setDbName(dbName);
            sqlBean.setUsername(username);
            sqlBean.setPassword(password);
        }catch(SQLException sqle){
            try{con.close();}catch(Exception e){e.printStackTrace();}
            sqle.printStackTrace();
        }catch(IOException ioe){
            try{con.close();}catch(Exception e){e.printStackTrace();}
            ioe.printStackTrace();
        }catch(ClassNotFoundException cnfe){
            try{con.close();}catch(Exception e){e.printStackTrace();}
            cnfe.printStackTrace();
        }
    }//End of createConnection method
    
    public static void createConnection(String sqlType,String dbName){
        createConnection(sqlType,dbName,"","");
    }//End of overridden createConneciton method
    
    public static ArrayList<TableBean> getTables()throws SQLException{
        ResultSet tables=null;
        ArrayList<TableBean> tableBeans=new ArrayList<TableBean>();
        try{
            DatabaseMetaData dbmd=con.getMetaData();
            tables=dbmd.getTables(null,null, null,new String[]{"TABLE"});

            while(tables.next()){
                TableBean bean = getTable(tables.getString("TABLE_NAME"));
                tableBeans.add(bean);
            }
        }finally{
            tables.close();
        }
        return tableBeans;
    }//End of getTables method
    
    public static TableBean getTable(String tableName)throws SQLException{
        Statement st=null;
        ResultSet pk=null;
        ResultSet fk=null;
        TableBean tableBean=null;
        try{
            String sym="";
            String fks="";
            String pks="";
            
            //Getting foreign keys
            DatabaseMetaData dbmd=con.getMetaData();
            fk=dbmd.getImportedKeys(null, null, tableName); //    getString("FKCOLUMN_NAME");
            while(fk.next()){
                fks+=sym+fk.getString("FKCOLUMN_NAME");
                sym=":";
            }
            
            //Getting primary keys
            sym="";
            pk=dbmd.getPrimaryKeys(null, null, tableName);  //    getString("COLUMN_NAME");
            while(pk.next()){
                pks+=sym+pk.getString("COLUMN_NAME");
                sym=":";
            }
            
            st=con.createStatement();
            st.execute("SELECT * FROM "+tableName);
            ResultSetMetaData rsmd=st.getResultSet().getMetaData();
            
            String colNames[]=new String[rsmd.getColumnCount()];
            ArrayList<ColumnBean> columns=new ArrayList(colNames.length);
            boolean toStringCheck=true;
            String toStringValue="";
            ArrayList<ColumnBean> compositeKeyColumns=new ArrayList<ColumnBean>();
            
            ArrayList<ColumnBean> dateColumns=new ArrayList<ColumnBean>();
            
            for(int i=0;i<colNames.length;i++){
                ColumnBean columnBean=new ColumnBean();
                String colName=rsmd.getColumnName(i+1);
                
                columnBean.setTableName(tableName);
                columnBean.setBeanName(Decoder.getClassCamelCase(tableName));
                columnBean.setColumnName(colName);
                columnBean.setVariableName(Decoder.getVariableCamelCase(colName));
                columnBean.setColumnType(rsmd.getColumnTypeName(i+1));
                columnBean.setColumnClassType(Decoder.getSuitableDataType(rsmd.getColumnClassName(i+1)));
                columnBean.setIsPrimaryKey(Resources.searchVariable(colName,pks.split(":")));
                columnBean.setIsForeignKey(Resources.searchVariable(colName,fks.split(":")));
                columnBean.setIsAutoIncrement(rsmd.isAutoIncrement(i+1));
                if(columnBean.isPrimaryKey())
                    columnBean.setUseAsArguments(columnBean.isPrimaryKey());
                else
                    columnBean.setUseAsArguments(columnBean.isForeignKey());
                
                if(columnBean.isPrimaryKey()==false && columnBean.isForeignKey()==false && columnBean.isIsAutoIncrement()==false && toStringCheck){
                    if(columnBean.getColumnClassType().endsWith("String")){
                        toStringValue=columnBean.getVariableName();
                        toStringCheck=false;
                    }else{
                        toStringValue=columnBean.getVariableName();
                        toStringCheck=false;
                    }
                }
					
                if(columnBean.isPrimaryKey() && columnBean.isForeignKey())
                    compositeKeyColumns.add(columnBean);
                
                if(columnBean.getColumnType().toLowerCase().equals("date"))
                    dateColumns.add(columnBean);
                
                columnBean.setGetMethod("get"+Decoder.getCamelCase(colName));
                columnBean.setSetMethod("set"+Decoder.getCamelCase(colName));
                columns.add(columnBean);
                
            }
            
            tableBean=new TableBean();
            
            if(compositeKeyColumns.size()>0){
                tableBean.setCompositeKeyColumns(compositeKeyColumns);
            }else
                tableBean.setCompositeKeyColumns(null);
            
            if(dateColumns.size()>0)
                tableBean.setDateColumns(dateColumns);
            
            tableBean.setColumns(columns);
            tableBean.setToStringVariable(Resources.toStringMethod(toStringValue));
            tableBean.setBeanName(Decoder.getClassCamelCase(tableName));
            tableBean.setClassName(Decoder.getCamelCase(tableName));
            tableBean.setTableName(tableName);
            tableBean.setSourceFileName(Decoder.getClassCamelCase(tableName)+".java");
            tableBean.setGetterMethods(Resources.getGetters(tableBean.getColumns()));
            tableBean.setSetterMethods(Resources.getSetters(tableBean.getColumns()));
            tableBean.setAddMethods(Resources.getAddMethod(tableBean));
            tableBean.setGetMethods(Resources.getGetMethods(tableBean));
            tableBean.setUpdateMethods(Resources.getUpdateMethods(tableBean));
            tableBean.setDeleteStatusMethods(Resources.getDeleteStatusMethods(tableBean));
            tableBean.setDeleteMethods(Resources.getDeleteMethods(tableBean));                
            tableBean.setSqlBean(sqlBean);
            
        }finally{
            st.close();
            pk.close();
            fk.close();
        }
        
        return tableBean;
    }//End of getTable method
}//End of class
