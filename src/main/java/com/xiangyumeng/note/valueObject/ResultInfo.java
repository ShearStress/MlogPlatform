package com.xiangyumeng.note.valueObject;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ResultInfo<T> {
    /**
     * encapsulate information, data, state
     * state code:
     *           success  = 1, fail = 0
     *  notification information
     *  return object: String, set, Map..
     *  I used generic type here
     */
    //state code
    private Integer code;

    // notification
    private String msg;

    // result object, could be String, set, Map.. so use generic type here.
    private T result;
}
