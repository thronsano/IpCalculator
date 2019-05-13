package com.ipCalculator.entity.model;


import static com.ipCalculator.entity.model.HeadersPosition.HORIZONTAL_AND_VERTICAL;

public abstract class Table {
    String[][] table;
    int width;
    int height;
    HeadersPosition headersPosition = HORIZONTAL_AND_VERTICAL;

    public String[][] getTable() {
        return table;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public HeadersPosition getHeadersPosition() {
        return headersPosition;
    }
}