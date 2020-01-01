package com.example.hellow.sqlite;

public class Matrix {
    private int id;
    private String name;
    private int[][] data;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int[][] getData() {
        return data;
    }


    public Matrix(int id, String name, int[][] data){
        this.id = id;
        this.name = name;
        this.data = data;
    }


}
