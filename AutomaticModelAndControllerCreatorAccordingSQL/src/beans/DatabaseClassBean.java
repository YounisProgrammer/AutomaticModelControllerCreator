package beans;

import java.util.*;

public class DatabaseClassBean {
    
    private String connectionVariable;
    private MethodBean staticBlock; 
    private ArrayList<TableBean> tables;

    public String getConnectionVariable() {
        return connectionVariable;
    }

    public void setConnectionVariable(String connectionVariable) {
        this.connectionVariable = connectionVariable;
    }

    public MethodBean getStaticBlock() {
        return staticBlock;
    }

    public void setStaticBlock(MethodBean staticBlock) {
        this.staticBlock = staticBlock;
    }

    public ArrayList<TableBean> getTables() {
        return tables;
    }

    public void setTables(ArrayList<TableBean> tables) {
        this.tables = tables;
    }
    
}//End of class
