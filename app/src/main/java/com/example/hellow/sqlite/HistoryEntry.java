package com.example.hellow.sqlite;

import com.example.hellow.OperationEnum;

/*
* Represents row of table 'Chronik'
* */
public class HistoryEntry {
    private String leftName;
    private double[][] leftData;
    private String rightName;
    private double[][] rightData;
    private OperationEnum operation;
    private double[][] resultData;

    public HistoryEntry(String leftName, double[][] leftData, String rightName, double[][] rightData, OperationEnum operation, double[][] resultData) {
        this.leftName = leftName;
        this.leftData = leftData;
        this.rightName = rightName;
        this.rightData = rightData;
        this.operation = operation;
        this.resultData = resultData;
    }

    public String getLeftName() {
        return leftName;
    }

    public double[][] getLeftData() {
        return leftData;
    }

    public String getRightName() {
        return rightName;
    }

    public double[][] getRightData() {
        return rightData;
    }

    public OperationEnum getOperation() {
        return operation;
    }

    public double[][] getResultData() {
        return resultData;
    }

    private String getDims(double[][] mat){
        if(mat == null){
            return null;
        }

        int rows = mat.length;
        int cols = mat[0].length;
        if(rows == 1 && cols == 1){
            return "Skalar";
        } else {
            return rows + "x" + cols;
        }
    }

    public String getDimsLeft(){
        return getDims(this.leftData);
    }
    public String getDimsRight(){
        return getDims(this.rightData);
    }
    public String getDimsResult(){
        return getDims(this.resultData);
    }
}
