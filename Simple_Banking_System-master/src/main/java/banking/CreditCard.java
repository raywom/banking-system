package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreditCard {
    private String number;
    private int pin;
    private double balance;
    private int maxBalance = -50000;
    public int customerId;

    public void addBalanceToDBCredit(double amount) {
        String addBalanceQuery = "UPDATE credit_card SET balance = balance + ? WHERE number = ?";
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(Bank.url);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(addBalanceQuery);
            preparedStatement.setString(1, String.valueOf(amount));
            preparedStatement.setString(2, number);
            try {
                preparedStatement.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteCardFromDBCredit() {
        if (balance >= 0) {
            String deleteCardQuery = "DELETE FROM credit_card WHERE number = ?";
            SQLiteDataSource dataSource = new SQLiteDataSource();
            dataSource.setUrl(Bank.url);
            boolean returnValue = false;

            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(deleteCardQuery);
                preparedStatement.setString(1, number);
                try {
                    preparedStatement.executeUpdate();
                    returnValue = true;
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return returnValue;
        }
        return false;
    }

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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getMaxBalance() {
        return maxBalance;
    }

    public void setMaxBalance(int maxBalance) {
        this.maxBalance = maxBalance;
    }
}
