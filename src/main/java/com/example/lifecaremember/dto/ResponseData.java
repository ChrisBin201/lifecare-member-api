package com.example.lifecaremember.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseData<T> {
    public boolean success;

    public String message;
    public String status;

    public Integer code;
    public T data;


    public void success() {
        this.message = "Successful";
        this.status = HttpStatus.OK.name();
        this.code = HttpStatus.OK.value();
        this.success = true;
//        error = false;
    }

    public void initData(T data) {
        this.data = data;
        this.success();
    }
    public void error(String message){
//        status = HttpStatus.INTERNAL_SERVER_ERROR.value();
//        error = true;
        this.message = message;
    }
}

