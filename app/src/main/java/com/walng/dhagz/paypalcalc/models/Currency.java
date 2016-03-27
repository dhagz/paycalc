package com.walng.dhagz.paypalcalc.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Dhagz
 * @since 2016-03-25
 */
public class Currency implements Parcelable {

    private int id;
    private String name;
    private String symbol;
    private float percentageCharge;
    private float amountCharge;

    public Currency(int id, String name, String symbol, float percentageCharge, float amountCharge) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.percentageCharge = percentageCharge;
        this.amountCharge = amountCharge;
    }

    public int getId() {
        return id;
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

    public void setPercentageCharge(float percentageCharge) {
        this.percentageCharge = percentageCharge;
    }

    public float getAmountCharge() {
        return amountCharge;
    }

    public void setAmountCharge(float amountCharge) {
        this.amountCharge = amountCharge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Currency)) return false;

        Currency currency = (Currency) o;

        if (id != currency.id) return false;
        if (Float.compare(currency.percentageCharge, percentageCharge) != 0) return false;
        if (Float.compare(currency.amountCharge, amountCharge) != 0) return false;
        if (name != null ? !name.equals(currency.name) : currency.name != null) return false;
        return !(symbol != null ? !symbol.equals(currency.symbol) : currency.symbol != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (symbol != null ? symbol.hashCode() : 0);
        result = 31 * result + (percentageCharge != +0.0f ? Float.floatToIntBits(percentageCharge) : 0);
        result = 31 * result + (amountCharge != +0.0f ? Float.floatToIntBits(amountCharge) : 0);
        return result;
    }

    @Override
    public String toString() {
        return name + " (" + symbol + ")";
    }

    /**
     * PARCELLING PART
     */

    // Parcelling part
    public Currency(Parcel in) {
        String[] data = new String[3];

        in.readStringArray(data);
        this.id = in.readInt();
        this.name = in.readString();
        this.symbol = in.readString();
        this.percentageCharge = in.readFloat();
        this.amountCharge = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.symbol);
        parcel.writeFloat(this.percentageCharge);
        parcel.writeFloat(this.amountCharge);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Currency createFromParcel(Parcel in) {
            return new Currency(in);
        }

        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };
}
