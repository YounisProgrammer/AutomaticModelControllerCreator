package beans;
import java.util.ArrayList;

public class TableBean {
    private String tableName;
    private String beanName;
    private String className;
    private ArrayList<ColumnBean> columns;
    private String beanClassData;
    private String sourceFileName;
    private MethodBean toStringVariable;
    private MethodBean addMethods;
    private ArrayList<MethodBean> getterMethods;
    private ArrayList<MethodBean> setterMethods;
    private ArrayList<MethodBean> getMethods;
    private ArrayList<MethodBean> deleteStatusMethods;
    private ArrayList<MethodBean> updateMethods;
    private ArrayList<MethodBean> deleteMethods;
    private ArrayList<ColumnBean> compositeKeyColumns;
    private ArrayList<ColumnBean> dateColumns;
    private SQLBean sqlBean;

    public ArrayList<ColumnBean> getDateColumns() {
        return dateColumns;
    }

    public void setDateColumns(ArrayList<ColumnBean> dateColumns) {
        this.dateColumns = dateColumns;
    }
    

    public ArrayList<ColumnBean> getCompositKeyColumns(){
            return compositeKeyColumns;
    }

    public void setCompositeKeyColumns(ArrayList<ColumnBean> compositeKeyColumns){
            this.compositeKeyColumns=compositeKeyColumns;
    }

    public SQLBean getSqlBean() {
        return sqlBean;
    }

    public void setSqlBean(SQLBean sqlBean) {
        this.sqlBean = sqlBean;
    }

    public ArrayList<MethodBean> getDeleteStatusMethods() {
        return deleteStatusMethods;
    }

    public void setDeleteStatusMethods(ArrayList<MethodBean> deleteStatusMethods) {
        this.deleteStatusMethods = deleteStatusMethods;
    }    

    public MethodBean getAddMethods() {
        return addMethods;
    }

    public ArrayList<MethodBean> getUpdateMethods() {
        return updateMethods;
    }

    public ArrayList<MethodBean> getDeleteMethods() {
        return deleteMethods;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setAddMethods(MethodBean addMethods) {
        this.addMethods = addMethods;
    }
    
    public MethodBean getAddMethod(){
        return addMethods;
    }

    public void setGetMethods(ArrayList<MethodBean> getMethods) {
        this.getMethods = getMethods;
    }
    
    public ArrayList<MethodBean> getGetMethods(){
        return getMethods;
    }

    public void setUpdateMethods(ArrayList<MethodBean> updateMethods) {
        this.updateMethods = updateMethods;
    }

    public void setDeleteMethods(ArrayList<MethodBean> deleteMethods) {
        this.deleteMethods = deleteMethods;
    }

    public ArrayList<MethodBean> getGetterMethods() {
        return getterMethods;
    }

    public void setGetterMethods(ArrayList<MethodBean> getterMethods) {
        this.getterMethods = getterMethods;
    }

    public ArrayList<MethodBean> getSetterMethods() {
        return setterMethods;
    }

    public void setSetterMethods(ArrayList<MethodBean> setterMethods) {
        this.setterMethods = setterMethods;
    }

    public MethodBean getToStringVariable() {
        return toStringVariable;
    }

    public void setToStringVariable(MethodBean toStringVariable) {
        this.toStringVariable = toStringVariable;
    }

    public String getSourceFileName() {
        return sourceFileName;
    }

    public void setSourceFileName(String sourceFileName) {
        this.sourceFileName = sourceFileName;
    }
    
    public String getBeanClassData() {
        return beanClassData;
    }

    public void setBeanClassData(String beanClassData) {
        this.beanClassData = beanClassData;
    }
    
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public ArrayList<ColumnBean> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<ColumnBean> columns) {
        this.columns = columns;
    }
    
    public String toString(){
        return tableName;
    }
}

