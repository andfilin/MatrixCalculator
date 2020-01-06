package com.example.hellow.sqlite;

import java.io.Serializable;

/*
* Java-Representation of databaserow.
* */
public class Matrix implements Serializable {
    private int id;
    private String name;
    private double[][] data;
    private boolean isScalar;
    private int rows;
    private int cols;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double[][] getData() {
        return data;
    }

    public boolean isScalar(){
        return isScalar;
    }

    public String dimensionsString(){
        if(isScalar){
            return "Skalar";
        } else {
            return rows + "x" + cols;
        }
    }

    public Matrix(int id, String name, double[][] data, boolean isScalar){
        this.id = id;
        this.name = name;
        this.data = data;
        this.isScalar = isScalar;
        this.rows = data.length;
        this.cols = data[0].length;
    }




}
