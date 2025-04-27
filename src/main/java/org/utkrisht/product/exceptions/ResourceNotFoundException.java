package org.utkrisht.product.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    private String name;
    private String message;
    private String fieldName;
    private Long fieldId;

    public ResourceNotFoundException(){}

    public ResourceNotFoundException(String name,String message,String fieldName){
        super(String.format("Exception - %s, occurred on %s %s",name,message,fieldName));
        this.name=name;
        this.message=message;
        this.fieldName = fieldName;
    }

    public ResourceNotFoundException(String name,String message, Long fieldId){
        super(String.format("Exception - %s, occurred on %d %s",name,fieldId,message));
        this.name = name;
        this.message = message;
        this.fieldId = fieldId;
    }
}
