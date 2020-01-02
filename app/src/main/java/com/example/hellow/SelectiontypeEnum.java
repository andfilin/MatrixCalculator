package com.example.hellow;

import java.util.HashMap;
import java.util.Map;

/*
* Types of selection for MatrixSelectionActivity.
* The user can select either one or two Matrices, or one Matrix and a scalar.
* */
public enum SelectiontypeEnum {

    NONE(0), SINGLE(1), TWO(2), SCALAR(3);
    private int value;
    private static Map map = new HashMap<>();

    SelectiontypeEnum(int value){
        this.value = value;
    }

    static{
        for(SelectiontypeEnum type : SelectiontypeEnum.values()){
            map.put(type.value, type);
        }
    }

    public static SelectiontypeEnum fromInt(int value){
        return (SelectiontypeEnum) map.get(value);
    }
    public int toInt(){
        return value;
    }


}
