package beans;

public class ColumnBean {
    
    private String tableName;
    private String beanName;
    private String columnName;
    private String variableName;
    private String columnType;
    private String columnClassType;
    private String getMethod;
    private String setMethod;
    private boolean isPKey;
    private boolean isFKey;
    private boolean useAsArguments;
    private boolean isAutoIncrement;

    public boolean isUseAsArguments() {
        return useAsArguments;
    }

    public void setUseAsArguments(boolean useAsArguments) {
        this.useAsArguments = useAsArguments;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }
    
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    
    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnClassType() {
        return columnClassType;
    }

    public void setColumnClassType(String columnClassType) {
        this.columnClassType = columnClassType;
    }

    public String getGetMethod() {
        return getMethod;
    }

    public void setGetMethod(String getMethod) {
        this.getMethod = getMethod;
    }

    public String getSetMethod() {
        return setMethod;
    }

    public void setSetMethod(String setMethod) {
        this.setMethod = setMethod;
    }
    
    public boolean isPrimaryKey() {
        return isPKey;
    }

    public void setIsPrimaryKey(boolean isPrimaryKey) {
        this.isPKey = isPrimaryKey;
    }

    public boolean isForeignKey() {
        return isFKey;
    }

    public void setIsForeignKey(boolean isForeignKey) {
        this.isFKey = isForeignKey;
    }

    public boolean isIsAutoIncrement() {
        return isAutoIncrement;
    }

    public void setIsAutoIncrement(boolean isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }

    public String toString(){
        return variableName;
    }
    
}//End of class
