package com.example.hellow;

import java.util.HashMap;
import java.util.Map;

public enum SelectionType {

    SINGLE(1), TWO(2), SCALAR(3);
    private int value;
    private static Map map = new HashMap<>();

    private SelectionType(int value){
        this.value = value;
    }

    static{
        for(SelectionType type : SelectionType.values()){
            map.put(type.value, type);
        }
    }

    public static SelectionType fromInt(int value){
        return (SelectionType) map.get(value);
    }
    public int toInt(){
        return value;
    }


}
