package com.yyy.exception;

import lombok.Data;

@Data
public class SystemException extends RuntimeException {
    private String errorMsg;
    public SystemException(String errorMsg){
        this.errorMsg=errorMsg;
    }
}
