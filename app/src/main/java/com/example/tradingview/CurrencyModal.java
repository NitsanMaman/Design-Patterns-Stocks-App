package com.example.tradingview;

public class CurrencyModal {
    // variable for currency name,
    // currency symbol and price.
    private String name;
    private String symbol;
    private double price;


    private double percent_change_24h;

    public CurrencyModal(String name, String symbol, double price, double percent_change_24h) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.percent_change_24h = percent_change_24h;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getPercent_change_24h_str() {
        return String.format("%.2f", percent_change_24h);
    }
    public double getPercent_change_24h() {
        return percent_change_24h;
    }


    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
