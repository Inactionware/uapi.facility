package uapi.protocol;

public class OperationType {

    public static final int QUERY   = 0x1;
    public static final int CREATE  = 0x2;
    public static final int REMOVE  = 0x4;
    public static final int MODIFY  = 0x8;

    private OperationType() { }
}
