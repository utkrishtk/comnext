package org.utkrisht.ecom.exceptions;

public class APIException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public APIException(){}

    public APIException(String msg){
        super(msg);
    }
}
