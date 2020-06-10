package resources;

public class Decoder{
    public static String getSuitableDataType(String dataType){
        if(dataType.endsWith("Integer"))
           return "int";
        else if(dataType.endsWith("Long"))
            return "long";
        else if(dataType.endsWith("Float"))
            return "float";
        else if(dataType.endsWith("Double"))
            return "double";
        else if(dataType.endsWith("Short"))
            return "short";
        else if(dataType.endsWith("Byte"))
            return "byte";
        else if(dataType.endsWith("Boolean"))
            return "boolean";
        else if(dataType.endsWith("String"))
           return "String";
        else if(dataType.endsWith("Character"))
            return "char";
        else
            return dataType;
    }//End of setSuitableDataType
    
    public static String getVariableCamelCase(String variable){
        String variableCamelCase="";
        String temp="";
        variable+="_";
        variable=variable.toLowerCase();
        boolean cond=false;
        for(int i=0;i<variable.length();i++){
            char ch=variable.charAt(i);
            if(ch!='_'){
                if(cond){
                    temp+=new String(ch+"").toUpperCase();
                    cond=false;
                }
                else
                    temp+=ch;
            }else{
                variableCamelCase+=temp;
                temp="";
                cond=true;
            }
        }
        
        return variableCamelCase;
    }//End of constructor
    
    public static String makeFirstCharSmall(String str){
        return new String(new StringBuilder(str).replace(0,1,new String(str.charAt(0)+"").toLowerCase()));
    }
    
    public static void main(String[] args) {
        System.out.println(getCamelCase("abc_def_ghi_zyz_abbasi"));
    }
    
    public static String singulerToPlural(String str){
        if(!str.endsWith("s"))
            if(str.endsWith("y"))
                return new String(new StringBuilder(str).replace(str.length()-1, str.length(),"ies"));
            else
                str+="s";
        return str;
    }
    
    public static String getCamelCase(String name){
        String str=getVariableCamelCase(name);
         
        return new String(new StringBuilder(str).replace(0,1,new String(str.charAt(0)+"").toUpperCase()));
    }
    
    public static String getClassCamelCase(String className){
        String str=getVariableCamelCase(className)+"Bean";
         
        return new String(new StringBuilder(str).replace(0,1,new String(str.charAt(0)+"").toUpperCase()));
    }//End of getClassCamelCase method
    
}//End of class
