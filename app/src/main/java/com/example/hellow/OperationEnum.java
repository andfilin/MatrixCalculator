package com.example.hellow;

import java.util.HashMap;
import java.util.Map;

/*
* Enum of operations.
* Each operation knows its symbol, number of operands, id of corresponding button.
* */
public enum OperationEnum {
    // instances
    //NONE(0, ' ', SelectiontypeEnum.SINGLE, R.id.calculation_transpose),
    ADD(1, '+', SelectiontypeEnum.TWO, R.id.calculation_add),
    SUBTRACT(2, '-', SelectiontypeEnum.TWO, R.id.calculation_subtract),
    MULTIPLY(3, 'x', SelectiontypeEnum.TWO, R.id.calculation_multiply),


    SCALE(10, 'x', SelectiontypeEnum.SCALAR, R.id.calculation_scale),

    TRANSPOSE(100, 'T', SelectiontypeEnum.SINGLE, R.id.calculation_transpose),
    INVERT(101, 'I', SelectiontypeEnum.SINGLE, R.id.calculation_invert),
    DETERMINANT(102, 'D', SelectiontypeEnum.SINGLE, R.id.calculation_determinant),
    RANK(103, 'R', SelectiontypeEnum.SINGLE, R.id.calculation_rank),
    TO_POWER(104, '^', SelectiontypeEnum.SCALAR, R.id.calculation_topower),

    ;

    // private value
    private int value;
    // mathematical symbol, eg. '+'
    private char symbol;
    // number of operands
    private SelectiontypeEnum operands;
    // id of corresponding button
    private int button;

    // maps buttons to corresponding operation.
    // allows lookup of operation from buttonid.
    private static Map fromButton_Map = new HashMap<>();
    static{
        for(OperationEnum operation : OperationEnum.values()){
            fromButton_Map.put(operation.button, operation);
        }
    }
    public static OperationEnum fromButton(int buttonId){
        return (OperationEnum) fromButton_Map.get(buttonId);
    }

    // private constructor
    private OperationEnum(int value, char symbol, SelectiontypeEnum operands, int button){
        this.value = value;
        this.symbol = symbol;
        this.operands = operands;
        this.button = button;
    }

    // getters
    public char getSymbol(){
        return symbol;
    };
    public SelectiontypeEnum getOperands(){
        return operands;
    }


}
