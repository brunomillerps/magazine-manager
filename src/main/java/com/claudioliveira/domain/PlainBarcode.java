package com.claudioliveira.domain;

public class PlainBarcode {

    private final String plainBarcode;

    public PlainBarcode(String plainBarcode) {
        this.plainBarcode = plainBarcode;
    }

    public String barcode() {
        return this.plainBarcode.substring(0, 13);
    }

    public String plainBarcode() {
        return this.plainBarcode;
    }

    public String edition() {
        return this.plainBarcode.substring(13, this.plainBarcode.length());
    }

}