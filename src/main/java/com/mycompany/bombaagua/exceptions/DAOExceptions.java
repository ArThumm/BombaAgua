package com.mycompany.bombaagua.exceptions;

//@author aoxle

public class DAOExceptions extends RuntimeException {
    
    public DAOExceptions(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    public DAOExceptions(String mensaje) {
        super(mensaje);
    }
    
    private int errorCodigo;
    
    public DAOExceptions(String mensaje, int errorCodigo) {
        super(mensaje);
        this.errorCodigo = errorCodigo;
    }
    
    public DAOExceptions(String mensaje, Throwable causa, int errorCode) {
        super(mensaje, causa);
        this.errorCodigo = errorCode;
    }
    
    public int getErrorCode() {
        return errorCodigo;
    }
}
