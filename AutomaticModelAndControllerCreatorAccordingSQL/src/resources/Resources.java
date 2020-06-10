package resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import beans.MethodBean;
import beans.ColumnBean;
import beans.TableBean;
import beans.SQLBean;
import java.io.FileOutputStream;

public class Resources {
    public static List<SQLBean> getSQLs()throws IOException{
        ArrayList<SQLBean> list=new ArrayList<SQLBean>();
        File sqlData=new File("Data\\SQL-DATA.txt");
        FileInputStream file=new FileInputStream(sqlData.getAbsolutePath());
        DataInputStream in=new DataInputStream(file);

        String line=in.readLine();
        while(line!=null){
            if(!line.trim().equals("")){
                SQLBean bean=new SQLBean();
                bean.setSql(line.toUpperCase());
                bean.setDriverPath(in.readLine());
                bean.setDbURL(in.readLine());
                list.add(bean);
            }    
            line=in.readLine();
        }
        return list;
    }//End of getSQLs method
    
    public static SQLBean getSQL(String sqlType)throws IOException{
        Iterator<SQLBean> sqls=getSQLs().iterator();
        SQLBean bean=null;
        while(sqls.hasNext()){
            bean =sqls.next();
            String s=bean.getSql();
            if(s.equals(sqlType.toUpperCase()))
                break;
            
            bean=null;
        }
        return bean;
    }//End of getSQL method
    
    public static boolean checkInteger(String colType){
        boolean cond=colType.equals("int") || colType.equals("long") || colType.equals("float") 
                    && colType.equals("double")  || colType.equals("short") || colType.equals("byte"); 
        return cond;
    }//End of checkInteger method
    
    public static String getTypeString(String colType){
        switch(colType){
            case "int":
                    return "getInt";
            case "long":
                    return "getLong";
            case "double":
                    return "getDouble";
            case "float":
                    return "getFloat";
            case "short":
                    return "getShort";
            case "byte":
                    return "getByte";
            case "java.lang.Boolean":
                    return "getBoolean";
            case "java.sql.Date":
                    return "getDate";
            case "java.sql.Timestamp":
                    return "getDate";
            default :
                    return "getString";
        }
    }//End of checkInteger method
    
    public static boolean  searchVariable(String search,String arr[]){
        boolean searched=false;
        for(int i=0;i<arr.length;i++){
            if(search.trim().equals(arr[i].trim())){
                searched=true;
                break;
            }
        }
        
        return searched;
    }//End of searchVariable method
    
    public static MethodBean getGetterMethod(String colName,String variableClassName){
        String columnType=variableClassName;
        String methodName=columnType+" get"+Decoder.getCamelCase(colName)+"()";
        String varName=Decoder.getVariableCamelCase(colName);
        String methodBody="public "+methodName+"{\n"
                + "\t\treturn "+varName+";\n"
                + "\t}//End of get"+Decoder.getCamelCase(colName)+"() method";
        
        return new MethodBean(methodName,methodBody);
    }
    
    public static MethodBean getSetterMethod(String colName,String variableClassName){
        String columnType=variableClassName;
        String varName=Decoder.getVariableCamelCase(colName);
        String methodName="void set"+Decoder.getCamelCase(colName)+"("+columnType+" "+varName+")";
        
        String methodBody="public "+methodName+"{\n"
                + "\t\tthis."+varName+" = "+varName+";\n"
                + "\t}//End of set"+Decoder.getCamelCase(colName)+"("+columnType+") method"; 
        
        return new MethodBean(methodName,methodBody);
    }//End of getSetterMethod
    
    public static ArrayList<MethodBean> getSetters(ArrayList<ColumnBean> columns){
        Iterator<ColumnBean> i=columns.iterator();
        ArrayList<MethodBean> list=new ArrayList<MethodBean>();
        String sym="";
        while(i.hasNext()){
            ColumnBean bean=i.next();
            MethodBean method=getSetterMethod(bean.getColumnName(),bean.getColumnClassType());
            method.setMethodBody(sym+method.getMethodBody());
            list.add(method);
            sym="\n\n\t";
        }
        return list;
    }//End of getSetters method
    
    public static ArrayList<MethodBean> getGetters(ArrayList<ColumnBean> columns){
        Iterator<ColumnBean> i=columns.iterator();
        ArrayList<MethodBean> list=new ArrayList<MethodBean>();
        String sym="";
        while(i.hasNext()){
            ColumnBean bean=i.next();
            MethodBean method=getGetterMethod(bean.getColumnName(),bean.getColumnClassType());
            method.setMethodBody(sym+method.getMethodBody());
            list.add(method);
            sym="\n\n\t";
        }
        return list;
    }//End of getGetters methodd
    
    public static MethodBean toStringMethod(String variableName){
        String methodBody="public String toString(){\n"
                + "\t\treturn "+variableName+"+\"\";\n"
                + "\t}//End of toString method";
        return new MethodBean(variableName, methodBody);
    }//End ot toStringMethod
    
    public static MethodBean getConnectionBlock(SQLBean sqlBean){
        String methodName="Connection static block";
        String methodBody="static{\n"
                + "\t\ttry{\n"
                + "\t\t\tClass.forName(\""+sqlBean.getDriverPath()+"\");\n"
                + "\t\t\tcon = java.sql.DriverManager.getConnection(\""+sqlBean.getDbURL()+sqlBean.getDbName().replace("\\", "\\\\")+"\",\""+sqlBean.getUsername()+"\",\""+sqlBean.getPassword()+"\");\n"
                + "\t\t\tSystem.out.println(\"Connection Established!!!\");\n"
                + "\t\t}catch(java.sql.SQLException sqle){\n"
                + "\t\t\tsqle.printStackTrace();\n"
                + "\t\t\tjavax.swing.JOptionPane.showMessageDialog(null,\"Connection Not Established due to \"+sqle.getMessage()+\"\");\n"
                + "\t\t}catch(ClassNotFoundException cnfe){\n"
                + "\t\t\tcnfe.printStackTrace();\n"
                + "\t\t\tjavax.swing.JOptionPane.showMessageDialog(null,\"Connection Not Established due to \"+cnfe.getMessage()+\"\");\n"
                + "\t\t}"
                + "\t}//End of conneciton static block";
        
        return new MethodBean(methodName, methodBody);
    }//End of getConnectionBlock method

    public static MethodBean getAddMethod(TableBean tableBean){
        String mthd=tableBean.getClassName();
        String beanName=tableBean.getBeanName();
        String beanVariable=Decoder.makeFirstCharSmall(beanName);
        
        String methodName="int add"+mthd+"(beans."+beanName+" "+beanVariable+")";
        String colNames="",values="",sym="";
        
        Iterator<ColumnBean> columns=tableBean.getColumns().iterator();
        int countCol=0,count=0;
        
        while(columns.hasNext()){
            ColumnBean cBean=columns.next();
            String quote="'";
            String newLine="",colNewLine="";
            if(!cBean.isIsAutoIncrement()){
                
                if(count>2){
                    count=0;
                    newLine="\"\n\t\t\t\t+\"";
                }
                if(countCol>2){
                    countCol=0;
                    colNewLine="\"\n\t\t\t\t+\"";
                }
                
                if(checkInteger(cBean.getColumnClassType()))
                    quote="";
                
                colNames+=colNewLine+sym+cBean.getColumnName();
                
                
                String value=quote+"\"+"+beanVariable+"."+cBean.getGetMethod()+"()+\""+quote;
                values+=newLine+sym+value;
                sym=", ";
                count++;
                countCol++;
                
            }
        }//End of while loop
        
        String methodBody="public static "+methodName+"throws java.sql.SQLException{\n\n"
                + "\t\tjava.sql.Statement st=null;\n\n\t\ttry{\n"
                + "\t\t\tst = con.createStatement();\n"
                + "\t\t\tString query = \"INSERT INTO "+tableBean.getTableName()+"("+colNames+") VALUES("+values+")\";\n"
                + "\n\t\t\tint row=st.executeUpdate(query,java.sql.Statement.RETURN_GENERATED_KEYS);\n"
                + "\t\t\tif(row>0){\n"
                + "\t\t\t\tjava.sql.ResultSet genKey=st.getGeneratedKeys();\n"
                + "\t\t\t\tif(genKey.next())\n"
                + "\t\t\t\t\treturn genKey.getInt(1);\n"
                + "\t\t\t}\n"
                + "\t\t\treturn row;\n"
                + "\t\t}finally{\n"
                + "\t\t\tst.close();\n\t\t}\n\n"
                + "\t}//End of add"+mthd+"("+beanName+" "+beanVariable+") method";
        
        return new MethodBean(methodName, methodBody);
    }//End of getAddMethod method
    
    public static ArrayList<MethodBean> getGetMethods(TableBean tableBean){
        ArrayList<MethodBean> getMethods=new ArrayList<MethodBean>();
        
        MethodBean withoutArgGetMethod=getGetMethod(null, tableBean);
        Iterator<ColumnBean> col=tableBean.getColumns().iterator();
        while(col.hasNext()){
            ColumnBean c=col.next();
            if(c.isUseAsArguments()){
                MethodBean m=getGetMethod(c,tableBean);
                getMethods.add(m);
            }
        }
        getMethods.add(withoutArgGetMethod);
        
        
        Iterator<ColumnBean> c=tableBean.getColumns().iterator();
        
        while(c.hasNext()){
            ColumnBean b=c.next();
            
            if(b.isUseAsArguments()){
                if(tableBean.getDateColumns()!=null){
                    Iterator<ColumnBean> coll=tableBean.getDateColumns().iterator();
                    while(coll.hasNext())
                        getMethods.add(getDateMethod(b,coll.next(),tableBean));
                    
                }
            }
        }
        
        if(tableBean.getDateColumns()!=null){
            Iterator<ColumnBean> coll=tableBean.getDateColumns().iterator();
            while(coll.hasNext()){
                getMethods.add(getDateMethod(null,coll.next(),tableBean));
            }
        }
        
        if(tableBean.getCompositKeyColumns()!=null && tableBean.getCompositKeyColumns().size()>1)
            getMethods.add(getCompositeKeyGetMethod(tableBean));

        return getMethods;
    }//End of getAddMethods Method
    
    public static MethodBean getDateMethod(ColumnBean foreignColumn,ColumnBean col,TableBean tableBean){
        
        String beanName=tableBean.getBeanName();
        String className=Decoder.singulerToPlural(tableBean.getClassName());
        String s1="",s2="",s3="";
        if(foreignColumn!=null){
            if(checkInteger(foreignColumn.getColumnClassType()))
                s1=foreignColumn.getColumnName()+"=\"+"+foreignColumn.getVariableName()+"+\" AND ";
            else
                s1=foreignColumn.getColumnName()+"='\"+"+foreignColumn.getVariableName()+"+\"' AND ";
                
            s2=foreignColumn.getColumnClassType()+" "+foreignColumn.getVariableName()+", ";
            s3="And"+Decoder.getCamelCase(foreignColumn.getColumnName());
        }
        String var=Decoder.getCamelCase(col.getColumnName());
        String methodName="java.util.Vector<beans."+beanName+"> get"+className+"By"+var+s3+"("+s2+col.getColumnClassType()+" fromDate, "+col.getColumnClassType()+" toDate)";
        
        String methodItems="\n\t\tjava.util.Vector<beans."+beanName+"> v = null;:"
                + "\n\t\t\tv = new java.util.Vector<beans."+beanName+">();:"
                + "\n\n\t\t\twhile(rs.next()){:"
                + "\n\t\t\t\tbeans."+beanName+
                " "+Decoder.makeFirstCharSmall(beanName)+" = new beans."+beanName+"();\n:"
                + "\n\n\t\t\t\tv.addElement("+Decoder.makeFirstCharSmall(beanName)+");\n\t\t\t}:"
                + "\n\n\t\t\treturn v;\n";

        String methodBody="public static "+methodName+"throws java.sql.SQLException{\n"
                + "\t\tjava.sql.Statement st=null;\n"
                + "\t\tjava.sql.ResultSet rs=null;"
                + methodItems.split(":")[0]
                + "\n\n\t\ttry{"
                + "\n\t\t\tst=con.createStatement();"
                +"\n\t\t\tString query=\"SELECT * FROM "+tableBean.getTableName()+" WHERE status=1 AND "+s1+col.getColumnName()+">'\"+fromDate+\"' AND "+col.getColumnName()+"<='\"+toDate+\"';\";"
                +"\n\t\t\tst.execute(query);\n"
                + "\t\t\trs=st.getResultSet();\n"
                + methodItems.split(":")[1]
                + methodItems.split(":")[2]
                + methodItems.split(":")[3];
                  
                    
        Iterator<ColumnBean> columns=tableBean.getColumns().iterator();
        while(columns.hasNext()){
            ColumnBean colVar=columns.next();
            
            String methodType=getTypeString(colVar.getColumnClassType());
            
            methodBody+="\n\t\t\t\t"+Decoder.makeFirstCharSmall(beanName)+"."+colVar.getSetMethod()+"(rs."+methodType+"(\""+colVar.getColumnName()+"\"));";
        }
        
        methodBody+= methodItems.split(":")[4]
                  +  methodItems.split(":")[5]
                  + "\n\t\t}finally{\n\t\t\tst.close();\n\t\t\trs.close();"
                  + "\n\t\t}"
                  + "\n\t}//End of get"+className+"ByDate"+s3+"("+s2+col.getColumnClassType()+" fromDate, "+col.getColumnClassType()+" toDate) method";
        
        return new MethodBean(methodName, methodBody);
    }//End  of getDateMethod method
    
    public static MethodBean getCompositeKeyGetMethod(TableBean tableBean){
        String argNames="";
        
        String beanName=tableBean.getBeanName();
        String className=Decoder.singulerToPlural(tableBean.getClassName());
        String methName="get"+className+"By";
        String methodName="java.util.Vector<beans."+beanName+"> ";
        String whereClause="WHERE status=1 AND";
        
        String methodItems="\n\t\tjava.util.Vector<beans."+beanName+"> v = null;:"
                + "\n\t\t\tv = new java.util.Vector<beans."+beanName+">();:"
                + "\n\n\t\t\twhile(rs.next()){:"
                + "\n\t\t\t\tbeans."+beanName+
                " "+Decoder.makeFirstCharSmall(beanName)+" = new beans."+beanName+"();\n:"
                + "\n\n\t\t\t\tv.addElement("+Decoder.makeFirstCharSmall(beanName)+");\n\t\t\t}:"
                + "\n\n\t\t\treturn v;\n";
        
        Iterator<ColumnBean> iter=tableBean.getCompositKeyColumns().iterator();
        String s1="",s2="";
        while(iter.hasNext()){
            
            ColumnBean col=iter.next();
            methName+=s1+Decoder.getCamelCase(col.getColumnName());
            argNames+=s2+col.getColumnClassType()+" "+col.getVariableName();
            String quote="'";
            if(checkInteger(col.getColumnClassType()))
                quote="";
            whereClause+=s1.toUpperCase()+" "+col.getColumnName()+" = "+quote+"\"+"+col.getVariableName()+"+\""+quote+" ";
            s1="And";
            s2=",";
                            
        }
        methodName+=methName+"("+argNames+")";
        String methodBody="public static "+methodName+"throws java.sql.SQLException{\n"
                + "\t\tjava.sql.Statement st=null;\n"
                + "\t\tjava.sql.ResultSet rs=null;"
                + methodItems.split(":")[0]
                + "\n\n\t\ttry{"
                + "\n\t\t\tst=con.createStatement();"
                +"\n\t\t\tString query=\"SELECT * FROM "+tableBean.getTableName()+" "+whereClause+";\";"
                +"\n\t\t\tst.execute(query);\n"
                + "\t\t\trs=st.getResultSet();\n"
                + methodItems.split(":")[1]
                + methodItems.split(":")[2]
                + methodItems.split(":")[3];
                  
                    
        Iterator<ColumnBean> columns=tableBean.getColumns().iterator();
        while(columns.hasNext()){
            ColumnBean colVar=columns.next();
            
            String methodType=getTypeString(colVar.getColumnClassType());
            
            methodBody+="\n\t\t\t\t"+Decoder.makeFirstCharSmall(beanName)+"."+colVar.getSetMethod()+"(rs."+methodType+"(\""+colVar.getColumnName()+"\"));";
        }
        
        methodBody+= methodItems.split(":")[4]
                  +  methodItems.split(":")[5]
                  + "\n\t\t}finally{\n\t\t\tst.close();\n\t\t\trs.close();"
                  + "\n\t\t}"
                  + "\n\t}//End of "+methName+" method" ;
        
        return new MethodBean(methodName, methodBody);
    }//End of getCompositeGetMethod method
    
    public static MethodBean getGetMethod(ColumnBean col,TableBean tableBean){
        String argName="";
        String variableForArgName="";
        String quote="";
        String beanName=tableBean.getBeanName();
        String className=Decoder.singulerToPlural(tableBean.getClassName());
        String methName="get"+className+"()";
        String methodName="java.util.Vector<beans."+beanName+"> "+methName;
        
        String methodItems="\n\t\tjava.util.Vector<beans."+beanName+"> v = null;\n\t\tboolean cond=true;:"
                + "\n\n\t\t\twhile(rs.next()){\n\t\t\t\tif(cond){v=new java.util.Vector<beans."+beanName+">(); cond=false;}:"
                + "\n\t\t\t\tbeans."+beanName+" "+Decoder.makeFirstCharSmall(beanName)+" = new beans."+beanName+"();\n:"
                + "\n\n\t\t\t\tv.addElement("+Decoder.makeFirstCharSmall(beanName)+");\n\t\t\t}:"
                + "\n\n\t\t\treturn v;\n";
        
        boolean whereClause=false;
        if(col!=null){
            
            argName=col.getVariableName();
            variableForArgName=Decoder.makeFirstCharSmall(argName);
            
            if(col.isPrimaryKey() && !col.isForeignKey()){
                className=tableBean.getClassName();
                methName="get"+className+"By"+Decoder.getCamelCase(col.getColumnName())+"("+col.getColumnClassType()+" "+argName+")";
                methodName="beans."+tableBean.getBeanName()+" "+methName;
                
                methodItems="\n\t\tbeans."+beanName+" "+Decoder.makeFirstCharSmall(beanName)+" = null;:"
                        + "\n\n\t\t\tif(rs.next()){:"
                        + "\n\t\t\t\t"+Decoder.makeFirstCharSmall(beanName)+" = new beans."+beanName+"();:"
                        + "\n\t\t\t}:"
                        + "\n\n\t\t\treturn "+Decoder.makeFirstCharSmall(beanName)+";";
            }
            
            if(!checkInteger(col.getColumnClassType()))
                quote="'";
            String bySearch=Decoder.getCamelCase(col.getColumnName());
            if(!col.isPrimaryKey() || col.isPrimaryKey() && col.isForeignKey()){
                methodName="java.util.Vector<beans."+tableBean.getBeanName()+"> get"+className+"By"+bySearch+"("+col.getColumnClassType()+" "+argName+")";
            } 
                
            whereClause=true;
        }
        
        String methodBody="public static "+methodName+"throws java.sql.SQLException{\n"
                + "\t\tjava.sql.Statement st=null;\n"
                + "\t\tjava.sql.ResultSet rs=null;"
                + methodItems.split(":")[0]
                + "\n\n\t\ttry{"
                + "\n\t\t\tst=con.createStatement();";
        
        if(col!=null)    
            methodBody+= "\n\t\t\tString query=\"SELECT * FROM "+tableBean.getTableName()+" WHERE status=1 AND "+col.getColumnName()+"="+quote+"\"+"+argName+"+\""+quote+";\";";
        else
            methodBody+= "\n\t\t\tString query=\"SELECT * FROM "+tableBean.getTableName()+" WHERE status=1\";";
        
        methodBody+= "\n\t\t\tst.execute(query);\n"
                    + "\t\t\trs=st.getResultSet();\n"
                    + methodItems.split(":")[1]
                    + methodItems.split(":")[2];
                  
                    
        String beanVarName=Decoder.makeFirstCharSmall(beanName);
        Iterator<ColumnBean> columns=tableBean.getColumns().iterator();
        while(columns.hasNext()){
            ColumnBean colVar=columns.next();
            
            String methodType=getTypeString(colVar.getColumnClassType());
            
            methodBody+="\n\t\t\t\t"+Decoder.makeFirstCharSmall(beanName)+"."+colVar.getSetMethod()+"(rs."+methodType+"(\""+colVar.getColumnName()+"\"));";
        }
        
        methodBody+=methodItems.split(":")[3] 
                  + methodItems.split(":")[4]
                  + "\n\t\t}finally{\n\t\t\tst"
                + ".close();\n\t\t\trs.close();"
                  + "\n\t\t}"
                  + "\n\t}//End of "+methName+" method" ;
        
        System.out.println(methodBody);
        
        return new MethodBean(methodName, methodBody);
    }//End of getGetMethod method
    
    public static ArrayList<MethodBean> getUpdateMethods(TableBean tableBean){
        ArrayList<MethodBean> tables=new ArrayList<MethodBean>();
        
        Iterator<ColumnBean> iter = tableBean.getColumns().iterator();
        while(iter.hasNext()){
        	ColumnBean column=iter.next();
					
					if(column.isUseAsArguments()){
						MethodBean method=getUpdateMethod(column, tableBean);
						tables.add(method);
					}
        }//End of while loop
        
				if(tableBean.getCompositKeyColumns()!=null && tableBean.getCompositKeyColumns().size()>1)
            tables.add(getCompositeUpdateMethod(tableBean));
        
        return tables;
    }//End of getUpdateMethod method
    
    public static MethodBean getCompositeUpdateMethod(TableBean tableBean){
        String beanName=tableBean.getBeanName();
        String beanVariable=Decoder.makeFirstCharSmall(beanName);
        String argNames="beans."+beanName+" "+beanVariable;
        String methodName="int update"+tableBean.getClassName()+"By";
        String whereClause="WHERE status=1 AND";
        
        Iterator<ColumnBean> iter=tableBean.getCompositKeyColumns().iterator();
        String s1="",s2="";
        while(iter.hasNext()){
            
            ColumnBean col=iter.next();
            methodName+=s1+Decoder.getCamelCase(col.getColumnName());
            argNames+=", "+col.getColumnClassType()+" "+col.getVariableName();
            String quote="'";
            if(checkInteger(col.getColumnClassType()))
                quote="";
            
            whereClause+=s1.toUpperCase()+" "+col.getColumnName()+" = "+quote+"\"+"+col.getVariableName()+"+\""+quote+" ";
            s1="And";
						// System.out.println("Where Clause: "+whereClause)
        }
        
				methodName+="("+argNames+")";
        String methodBody="public static "+methodName+"throws java.sql.SQLException{\n"
                + "\t\tjava.sql.Statement st = null;\n\n"
                + "\t\ttry{\n"
                + "\t\t\tst=con.createStatement();\n";
        
        Iterator<ColumnBean> column=tableBean.getColumns().iterator();
        String values="";
        int count=0;
        String comma="";
        while(column.hasNext()){
					ColumnBean c=column.next();
					String q="",sym="";
					if(!checkInteger(c.getColumnClassType()))
							q="'";
					
					if(count>1){
							count=0;
							sym="\"+\n\t\t\t\t\"";
					}
					
					iter=tableBean.getCompositKeyColumns().iterator();
            
					boolean cond=false;
					while(iter.hasNext())
						if(iter.next().getVariableName().equals(c.getVariableName()))
							cond=true;
					
					if(!c.isIsAutoIncrement() && !cond){
						values+=sym+comma+c.getColumnName()+" = "+q+"\"+"+beanVariable+"."+c.getGetMethod()+"()"+"+\""+q;
						comma=", ";
					}
					count++;
        }//End while
        
				System.out.println(values);
				
        methodBody+= "\t\t\tString query=\"UPDATE "+tableBean.getTableName()+" SET "+values+" "+whereClause+";\";"
                + "\t\t\treturn st.executeUpdate(query);\n"
                + "\t\t}finally{\n"
                + "\t\t\tst.close();\n"
                + "\t\t}\n"
                + "\t}//End of ";
  
        return new MethodBean(methodName, methodBody);
    }//End of getCompositeUpdateMethod method
    
    public static MethodBean getUpdateMethod(ColumnBean columnBean,TableBean tableBean){
        String beanName=tableBean.getBeanName();
        String beanVariable=Decoder.makeFirstCharSmall(beanName);
        String columnClass=columnBean.getColumnClassType();
        String variableName=columnBean.getVariableName();
        String bySearch=Decoder.getCamelCase(columnBean.getColumnName());
           
        String methodName="int update"+tableBean.getClassName()+"By"+bySearch+"(beans."+beanName+" "+beanVariable+", "+columnClass+" "+variableName+")";
        String quote="";
        
        if(!checkInteger(columnBean.getColumnClassType()))
                quote="'";
        
        String methodBody="public static "+methodName+"throws java.sql.SQLException{\n"
                + "\t\tjava.sql.Statement st = null;\n\n"
                + "\t\ttry{\n"
                + "\t\t\tst=con.createStatement();\n";
        
        Iterator<ColumnBean> column=tableBean.getColumns().iterator();
        String values="";
        int count=0;
		String comma="";
        while(column.hasNext()){
            ColumnBean c=column.next();
            String q="",sym="";
            if(!checkInteger(c.getColumnClassType()))
                q="'";
            
            if(count>1){
                count=0;
                sym="\"+\n\t\t\t\t\"";
            }
            
            if(!c.isIsAutoIncrement() && !columnBean.getVariableName().equals(c.getVariableName())){
                values+=sym+comma+c.getColumnName()+" = "+q+"\"+"+beanVariable+"."+c.getGetMethod()+"()"+"+\""+q;
				comma=", ";
            }
            count++;
        }
        
        methodBody+= "\t\t\tString query=\"UPDATE "+tableBean.getTableName()+" SET "+values+" WHERE "+columnBean.getColumnName()+"="+quote+"\"+"+variableName+"+\""+quote+";\";\n"
                + "\t\t\treturn st.executeUpdate(query);\n"
                + "\t\t}finally{\n"
                + "\t\t\tst.close();\n"
                + "\t\t}\n"
                + "\t}//End of update"+tableBean.getClassName()+"By"+bySearch+"(beans."+beanName+" "+beanVariable+", "+columnClass+" "+variableName+") method";
        
        return new MethodBean(methodName, methodBody);
    }//End of getUpdateMethod method
    
    public static ArrayList<MethodBean> getDeleteStatusMethods(TableBean tableBean){
        ArrayList<MethodBean> tables=new ArrayList<MethodBean>();
        
        Iterator<ColumnBean> iter = tableBean.getColumns().iterator();
        while(iter.hasNext()){
            ColumnBean column=iter.next();
            
            if(column.isUseAsArguments()){
                MethodBean method=getDeleteStatusMethod(column, tableBean);
                tables.add(method);
            }
        }
        
        if(tableBean.getCompositKeyColumns()!=null && tableBean.getCompositKeyColumns().size()>1)
            tables.add(getCompositeDeleteStatusMethod(tableBean));
        
        return tables;
    }//End of getDeleteMethods methods

    public static MethodBean getCompositeDeleteStatusMethod(TableBean tableBean){
        
        String methodName="int deleteStatus"+Decoder.singulerToPlural(tableBean.getClassName())+"By";
        String argNames="",whereClause="WHERE status=1 AND";
        
        Iterator<ColumnBean> iter=tableBean.getCompositKeyColumns().iterator();
        String s1="",s2="";
        while(iter.hasNext()){
            
            ColumnBean col=iter.next();
            methodName+=s1+Decoder.getCamelCase(col.getColumnName());
            argNames+=s2+col.getColumnClassType()+" "+col.getVariableName();
            String quote="'";
            if(checkInteger(col.getColumnClassType()))
                quote="";
            
            whereClause+=s1.toUpperCase()+" "+col.getColumnName()+" = "+quote+"\"+"+col.getVariableName()+"+\""+quote+" ";
            s1="And";
            s2=", ";
                            
        }
        methodName+="("+argNames+")";
        String methodBody="public static "+methodName+"throws java.sql.SQLException{\n"
                + "\t\tjava.sql.Statement st = null;\n\n"
                + "\t\ttry{\n"
                + "\t\t\tst=con.createStatement();\n";
        
        methodBody+= "\t\t\tString query=\"UPDATE "+tableBean.getTableName()+" SET status=0 "+whereClause+";\";\n"
                + "\t\t\treturn st.executeUpdate(query);\n"
                + "\t\t}finally{\n"
                + "\t\t\tst.close();\n"
                + "\t\t}\n"
                + "\t}//End of "+methodName+" method";
        
        return new MethodBean(methodName, methodBody);
    }//End of getCompositeDeleteStatusMethod method
    
    public static MethodBean getDeleteStatusMethod(ColumnBean columnBean, TableBean tableBean){
        String beanName=tableBean.getBeanName();
        String beanVariable=Decoder.makeFirstCharSmall(beanName);
        String columnClass=columnBean.getColumnClassType();
        String variableName=columnBean.getVariableName();
         String bySearch=Decoder.getCamelCase(columnBean.getColumnName());
           
        String methodName="int deleteStatus"+tableBean.getClassName()+"By"+bySearch+"("+columnClass+" "+variableName+")";
        String quote="";
        
        if(!checkInteger(columnBean.getColumnClassType()))
                quote="'";
        
        String methodBody="public static "+methodName+"throws java.sql.SQLException{\n"
                + "\t\tjava.sql.Statement st = null;\n\n"
                + "\t\ttry{\n"
                + "\t\t\tst=con.createStatement();\n";
        
        methodBody+= "\t\t\tString query=\"UPDATE "+tableBean.getTableName()+" SET status=0 WHERE "+columnBean.getColumnName()+"="+quote+"\"+"+variableName+"+\""+quote+";\";\n"
                + "\t\t\treturn st.executeUpdate(query);\n"
                + "\t\t}finally{\n"
                + "\t\t\tst.close();\n"
                + "\t\t}\n"
                + "\t}//End of deleteStatus"+tableBean.getClassName()+"By"+bySearch+"(beans."+beanName+" "+beanVariable+", "+columnClass+" "+variableName+") method";
        
        return new MethodBean(methodName, methodBody);
    }//End of getDeleteStatusMethod method
    
    public static ArrayList<MethodBean> getDeleteMethods(TableBean tableBean){
        ArrayList<MethodBean> tables=new ArrayList<MethodBean>();
        
        Iterator<ColumnBean> iter = tableBean.getColumns().iterator();
        while(iter.hasNext()){
            ColumnBean column=iter.next();
            
            if(column.isUseAsArguments()){
                MethodBean method=getDeleteMethod(column,tableBean);
                tables.add(method);
            }
        }
        
        if(tableBean.getCompositKeyColumns()!=null && tableBean.getCompositKeyColumns().size()>1)
            tables.add(getCompositeDeleteMethod(tableBean));
        
        return tables;
    }//End of getDeleteMethods method
    
    public static MethodBean getCompositeDeleteMethod(TableBean tableBean){
        
        String methodName="int delete"+Decoder.singulerToPlural(tableBean.getClassName())+"By";
        String argNames="",whereClause="";
        
        Iterator<ColumnBean> iter=tableBean.getCompositKeyColumns().iterator();
        String s1="",s2="";
        while(iter.hasNext()){
            
            ColumnBean col=iter.next();
            methodName+=s1+Decoder.getCamelCase(col.getColumnName());
            argNames+=s2+col.getColumnClassType()+" "+col.getVariableName();
            String quote="'";
            if(checkInteger(col.getColumnClassType()))
                quote="";
            
            whereClause+=s1.toUpperCase()+" "+col.getColumnName()+" = "+quote+"\"+"+col.getVariableName()+"+\""+quote+" ";
            s1="And";
            s2=", ";
                            
        }//End while
        methodName+="("+argNames+")";
        String methodBody="public static "+methodName+"throws java.sql.SQLException{\n"
                + "\t\tjava.sql.Statement st = null;\n\n"
                + "\t\ttry{\n"
                + "\t\t\tst=con.createStatement();\n";
        
        methodBody+= "\t\t\tString query=\"DELETE FROM "+tableBean.getTableName()+" WHERE "+whereClause+";\";\n"
                + "\t\t\treturn st.executeUpdate(query);\n"
                + "\t\t}finally{\n"
                + "\t\t\tst.close();\n"
                + "\t\t}\n"
                + "\t}//End of "+methodName+" method";
        
        return new MethodBean(methodName, methodBody);
    }//End of getCompositeDeleteMethod method
    
    public static MethodBean getDeleteMethod(ColumnBean columnBean, TableBean tableBean){
        String beanName=tableBean.getBeanName();
        String columnClass=columnBean.getColumnClassType();
        String variableName=columnBean.getVariableName();
         String bySearch=Decoder.getCamelCase(columnBean.getColumnName());
           
        String methodName="int delete"+tableBean.getClassName()+"By"+bySearch+"("+columnClass+" "+variableName+")";
        String quote="";
        
        if(!checkInteger(columnBean.getColumnClassType()))
                quote="'";
        
        String methodBody="public static "+methodName+"throws java.sql.SQLException{\n"
                + "\t\tjava.sql.Statement st = null;\n\n"
                + "\t\ttry{\n"
                + "\t\t\tst=con.createStatement();\n";
        
        methodBody+= "\t\t\tString query=\"DELETE FROM "+tableBean.getTableName()+" WHERE "+columnBean.getColumnName()+"="+quote+"\"+"+variableName+"+\""+quote+";\";\n"
                + "\t\t\treturn st.executeUpdate(query);\n"
                + "\t\t}finally{\n"
                + "\t\t\tst.close();\n"
                + "\t\t}\n"
                + "\t}//End of delete"+tableBean.getClassName()+"By"+bySearch+"("+columnClass+" "+variableName+") method";
        
        return new MethodBean(methodName, methodBody);
    }//End of getDeleteMethod method

    public static void createBeans(String dir,ArrayList<TableBean> tables){
        try{
            Iterator<TableBean> iter=tables.iterator();
            File file=new File(dir+"\\beans");
            file.mkdirs();

            while(iter.hasNext()){
                TableBean tableBean=iter.next();
                FileOutputStream beanFile=new FileOutputStream(file.getAbsolutePath()+"\\"+tableBean.getSourceFileName());
                beanFile.write(createBean(tableBean));
                beanFile.close();
            }//End of while loop
			
        }catch(Exception e){
            e.printStackTrace();
        }
    }//End of createBeans method
    
    public static byte[] createBean(TableBean bean){
        String newFile="";
        try{
            FileInputStream file=new FileInputStream(new java.io.File("Data\\Simple-Get-Set-Format.txt").getAbsolutePath());
            DataInputStream in =new DataInputStream(file);
            
            String line=in.readLine();
            
            while(line!=null){
                String getValue="";
                boolean cond=false;
                for(int i=0;i<line.length();i++){
                    if(line.charAt(i)=='<')
                        cond=true;

                    if(cond){
                        if(line.charAt(i)=='>'){
                            cond=false;
                            newFile+=getValue(getValue,bean);
                            getValue="";
                        }
                        else
                            getValue+=line.charAt(i);
                    }else
                        newFile+=line.charAt(i);
                }
                line=in.readLine();
                newFile+="\n";
            }//End of while 
        
        }catch(Exception e){e.printStackTrace();}
        
        return newFile.getBytes();
    }//End of createBean method

    
    public static void createDatebaseManager(String dir,ArrayList<TableBean> tables){
        try{
            
            File file=new File(dir+"\\databaseManager");
            file.mkdirs();
            
            FileOutputStream databaseManagerFile=new FileOutputStream(file.getAbsolutePath()+"\\DatabaseManager.java");
                
            databaseManagerFile.write(createDBFile(tables));
            databaseManagerFile.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//End of createDatabaseManager method
    
    public static byte[] createDBFile(ArrayList<TableBean> beans){
        String newFile="";
        try{
            FileInputStream file=new FileInputStream(new java.io.File("Data\\Database-Manager-Format.txt").getAbsolutePath());
            DataInputStream in =new DataInputStream(file);
            
            String line=in.readLine();
            
            while(line!=null){
                String getValue="";
                boolean cond=false;
                for(int i=0;i<line.length();i++){
                    if(line.charAt(i)=='<')
                        cond=true;

                    if(cond){
                        if(line.charAt(i)=='>'){
                            cond=false;
                            newFile+=getValue(getValue,beans);
                            getValue="";
                        }
                        else
                            getValue+=line.charAt(i);
                    }else
                        newFile+=line.charAt(i);
                }
                line=in.readLine();
                newFile+="\n";
            }//End of while 
        
        }catch(Exception e){e.printStackTrace();}
        
        return newFile.getBytes(); 

    }//End of createDBFile method
    
    public static String getValue(String getValue, TableBean bean){
        return getValue(getValue,null,bean);
    }
    
    public static String getValue(String getValue, ArrayList<TableBean> beans){
        return getValue(getValue,beans,null);
    }
    
    public static String getValue(String getValue,ArrayList<TableBean> beans,TableBean bean){
        switch(getValue){
            case "<BeanTitle":
                return bean.getSourceFileName();
            case "<classNameWithImp":
                return bean.getBeanName();
            case "<variables":
                String vars="",sym="";
                Iterator<ColumnBean> columns=bean.getColumns().iterator();
                while(columns.hasNext()){
                    ColumnBean c=columns.next();
                    vars+=sym+"private "+c.getColumnClassType()+" "+c.getVariableName()+";";
                    sym="\n\t";
                }
                return vars;
            case "<setters":
                Iterator<MethodBean> setMethods=bean.getSetterMethods().iterator();
                String setMethod="";
                while(setMethods.hasNext())
                    setMethod+=setMethods.next().getMethodBody();
                
                return setMethod;
            case "<getters":
                Iterator<MethodBean> getMethods=bean.getGetterMethods().iterator();
                String getMethod="";
                while(getMethods.hasNext())
                    getMethod+=getMethods.next().getMethodBody();
                
                return getMethod;
            case "<className":
                return bean.getBeanName();
            case "<toString":
                return bean.getToStringVariable().getMethodBody();
            case "<connectionBlock":
                return getConnectionBlock(beans.get(0).getSqlBean()).getMethodBody();    
            case "<addMethods":
                String addMethods="";
                if(beans!=null){
                    Iterator<TableBean> classes=beans.iterator();
                    sym="";
                    while(classes.hasNext()){
                        MethodBean method=classes.next().getAddMethod();
                        addMethods+=sym+method.getMethodBody();
                        sym="\n\n\t";
                    }
                }
                return addMethods;
            
            case "<getMethods":
                getMethod="";sym="";
                if(beans!=null){
                    Iterator<TableBean> classes=beans.iterator();
                    while(classes.hasNext()){
                        Iterator<MethodBean> method=classes.next().getGetMethods().iterator();
                            while(method.hasNext()){
                                getMethod+=sym+method.next().getMethodBody();
                                sym="\n\n\t";
                            }
                    }
                }
                return getMethod;
            
            case "<updateMethods":
                String updateMethod="";sym="";
                if(beans!=null){
                    Iterator<TableBean> classes=beans.iterator();
                    while(classes.hasNext()){
                        Iterator<MethodBean> method=classes.next().getUpdateMethods().iterator();
                        while(method.hasNext()){
                            updateMethod+=sym+method.next().getMethodBody();
                            sym="\n\n\t";
                        }
                    }
                }
                return updateMethod;
                
            case "<statusDeleteMethods":
                String statusDeleteMethod="";sym="";
                if(beans!=null){
                    Iterator<TableBean> classes=beans.iterator();
                    while(classes.hasNext()){
                        Iterator<MethodBean> method=classes.next().getDeleteStatusMethods().iterator();
                        while(method.hasNext()){
                            statusDeleteMethod+=sym+method.next().getMethodBody();
                            sym="\n\n\t";
                        }
                    }
                }
                return statusDeleteMethod;    
            
            case "<deleteMethods":
                String deleteMethod="";sym="";
                if(beans!=null){
                    Iterator<TableBean> classes=beans.iterator();
                    while(classes.hasNext()){
                        Iterator<MethodBean> method=classes.next().getDeleteMethods().iterator();
                        while(method.hasNext()){
                            deleteMethod+=sym+method.next().getMethodBody();
                            sym="\n\n\t";
                        }
                    }
                }
                return deleteMethod;
        }
        
        return getValue;
    }//End of getValue method
    
}//End of class
