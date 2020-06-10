package beans;

public class MethodBean {
    private String beanClassName;
    private String methodName;
    private String methodBody;
    
    public MethodBean(String methodName,String methodBody){
        this.methodBody=methodBody;
        this.methodName=methodName;
    }
    
    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }
    
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodBody() {
        return methodBody;
    }

    public void setMethodBody(String methodBody) {
        this.methodBody = methodBody;
    }
    
}
