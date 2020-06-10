package beans;

public class SQLBean {
    private String sql;
    private String driverPath;
    private String dbURL;
    private String dbName;
    private String username;
    private String password;
    
    public SQLBean(){}
    
    public SQLBean(String sql, String dbName, String username,String password){
        this.dbName=dbName;
        this.sql=sql;
        this.username=username;
        this.password=password;
    }
    
    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getDriverPath() {
        return driverPath;
    }

    public void setDriverPath(String driverPath) {
        this.driverPath = driverPath;
    }

    public String getDbURL() {
        return dbURL;
    }

    public void setDbURL(String dbURL) {
        this.dbURL = dbURL;
    }
    public String toString(){
        return sql;
    }
}
