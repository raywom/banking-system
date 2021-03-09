package banking;

public class MortgageCard {
    private String number;
    private int pin;
    private int customerId;
    private double monthPay;
    private int monthLeft;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getMonthPay() {
        return monthPay;
    }

    public void setMonthPay(double monthPay) {
        this.monthPay = monthPay;
    }

    public int getMonthLeft() {
        return monthLeft;
    }

    public void setMonthLeft(int monthLeft) {
        this.monthLeft = monthLeft;
    }
}
