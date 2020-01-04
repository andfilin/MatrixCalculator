package com.example.hellow.sqlite;
/*
* Java-Representation of databaserow.
* */
public class Matrix {
    private int id;
    private String name;
    private double[][] data;
    private boolean isScalar;

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


    public Matrix(int id, String name, double[][] data, boolean isScalar){
        this.id = id;
        this.name = name;
        this.data = data;
        this.isScalar = isScalar;
    }


}
