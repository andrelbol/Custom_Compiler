package compilador;


public enum DataType{
    NA(-1),
    INT(0),
    FLOAT(1),
    CHAR(2);
    private int value;
    
    DataType(int value){
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }
}
