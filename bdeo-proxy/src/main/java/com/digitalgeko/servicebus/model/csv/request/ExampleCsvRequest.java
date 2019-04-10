package com.digitalgeko.servicebus.model.csv.request;

import com.digitalgeko.servicebus.util.CsvOrder;

public class ExampleCsvRequest {

    @CsvOrder(value = 1)
    private String value1;
    @CsvOrder(value = 2)
    private String value2;
    @CsvOrder(value = 3)
    private String value3;

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }
}
