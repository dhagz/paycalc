package com.walng.dhagz.paypalcalc.models;

/**
 * @author Dhagz
 * @since 2016-03-25
 */
public class Currency {

    private String name;
    private String symbol;
    private float percentageCharge;
    private float amountCharge;

    public Currency(String name, String symbol, float percentageCharge, float amountCharge) {
        this.name = name;
        this.symbol = symbol;
        this.percentageCharge = percentageCharge;
        this.amountCharge = amountCharge;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public float getPercentageCharge() {
        return percentageCharge;
    }

    public float getAmountCharge() {
        return amountCharge;
    }

    @Override
    public String toString() {
        return name + " (" + symbol + ")";
    }
}
