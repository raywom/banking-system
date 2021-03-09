package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class Bank {
    static final Random generator = new Random();
    private static ArrayList<Card> cards = new ArrayList<>();
    public static String url;

    public static ArrayList<Card> getCardsFromDB() {
        // return all cards in db & copy them to Bank.cards
        cards.clear();
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = con.prepareStatement("SELECT number, pin, balance FROM debit_card;")) {
                try (ResultSet cardFromDb = preparedStatement.executeQuery()) {
                    while (cardFromDb.next()) {
                        Card card;
                        // Retrieve column values
                        long cardNumber = Long.parseLong(cardFromDb.getString("number"));
                        long pin = Long.parseLong(cardFromDb.getString("pin"));
                        double balance = cardFromDb.getInt("balance");
                        card = new Card().setCardNumber(cardNumber).setPin(pin).setBalance(balance);
                        cards.add(card);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }

    public static Banker bankerSearch(String login, String password){
        String pullCardQuery = "SELECT * FROM Banker WHERE login LIKE ? and password like ?";
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        Banker banker = new Banker();

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(pullCardQuery);
            preparedStatement.setString(1, login);
            preparedStatement.setString(1, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Retrieve card values
                banker.setName(resultSet.getString("name"));
                banker.setSurname(resultSet.getString("surname"));
                banker.setPhoneNumber(resultSet.getString("phone_number"));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return banker;
    }

    public static User userSearch(String phone){
        String pullCardQuery = "SELECT * FROM customer WHERE phone_number LIKE ?";
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        User user = new User();

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(pullCardQuery);
            preparedStatement.setString(1, phone);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Retrieve card values
                user.setName(resultSet.getString("name"));;
                user.setSurname(resultSet.getString("surname"));;
                user.setPhoneNumber(resultSet.getString("phone_number"));;
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return user;
    }

    public static MortgageCard pullCardFromDBMortgageCard (String targetCardNumber) {
        String pullCardQuery = "SELECT * FROM mortgage_card WHERE number LIKE ?";
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        MortgageCard card = null;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(pullCardQuery);
            preparedStatement.setString(1, targetCardNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Retrieve card values
                String cardNumber = resultSet.getString("number");
                int pin = resultSet.getInt("pin");
                card = new MortgageCard();
                card.setNumber(cardNumber);
                card.setPin(pin);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return card;
    }

    public static Card pullCardFromDB(Long targetCardNumber) {
        String pullCardQuery = "SELECT * FROM debit_card WHERE number LIKE ?";
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        Card card = null;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(pullCardQuery);
            preparedStatement.setString(1, targetCardNumber.toString());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Retrieve card values
                long cardNumber = Long.parseLong(resultSet.getString("number"));
                long pin = Long.parseLong(resultSet.getString("pin"));
                double balance = resultSet.getInt("balance");
                card = new Card().setCardNumber(cardNumber).setPin(pin).setBalance(balance);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return card;
    }

    public static CreditCard pullCardFromDBCreditCard(String targetCardNumber) {
        String pullCardQuery = "SELECT * FROM credit_card WHERE number LIKE ?";
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        CreditCard card = null;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(pullCardQuery);
            preparedStatement.setString(1, targetCardNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Retrieve card values
                String cardNumber = resultSet.getString("number");
                int pin = Integer.parseInt(resultSet.getString("pin"));
                double balance = resultSet.getInt("balance");
                card = new CreditCard();
                card.setPin(pin);
                card.setNumber(cardNumber);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return card;
    }


    public static boolean cardIsInDB(Long targetCardNumber) {
        String pullCardQuery = "SELECT * FROM debit_card WHERE number LIKE ?";
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        boolean retVal = false;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(pullCardQuery);
            preparedStatement.setString(1, targetCardNumber.toString());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                retVal = resultSet.isBeforeFirst();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public static void setIsCard(int what){

    }

    public static MortgageCard CreateMortgageCard (int customerId){
        long accountNumber = generateAccountNumber();
        int pin = generator.nextInt(10_000);

        MortgageCard card = new MortgageCard();
        card.setNumber(""+accountNumber);
        card.setPin(pin);
        card.setCustomerId(customerId);

        addMortgageCard(card);

        return card;
    }

    public static CreditCard createCreditCard(int customerId){
        long accountNumber = generateAccountNumber();
        int pin = generator.nextInt(10_000);

        CreditCard card = new CreditCard();
        card.setNumber(""+accountNumber);
        card.setPin(pin);
        card.customerId = customerId;

        addCreditCard(card);

        return card;
    }

    public static Card createAccount() {
        // long accountNumber =  (long) (generator.nextInt(1_000_000_000)) * 10 + 4_000_000_000_000_000L;
        long accountNumber = generateAccountNumber();
        long pin = generator.nextInt(10_000);

        Card card = new Card().setCardNumber(accountNumber).setPin(pin);
        cards.add(card);

        addCardToDb(card);

        return card;
    }

    public static void addCustomer(User user){
        String insertCardQuery = "INSERT INTO customer (name, surname, phone_number) VALUES (?, ?, ?)";
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement psInsertCard = connection.prepareStatement(insertCardQuery);

            psInsertCard.setString(1, user.getName());
            psInsertCard.setString(2, user.getSurname());
            psInsertCard.setString(3, user.getPhoneNumber());

            psInsertCard.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static long generateAccountNumber() {
        long accountNumber = (long) (generator.nextInt(1_000_000_000)) * 10 + 4_000_000_000_000_000L;
        /*
        String s = "123456789";
        s.charAt(i)
        */
        char[] charBuffer = Long.toString(accountNumber).toCharArray();
        char[] charBuffer1 = new char[charBuffer.length];

        for (int i = 0; i < charBuffer.length; i++) {
            charBuffer1[i] = charBuffer[charBuffer.length - 1 - i];
        }
        charBuffer = charBuffer1;

        // find the sum
        int sum = 0;
        for (int i = 0; i < charBuffer1.length; i++) {
            int digit = Character.getNumericValue(charBuffer[i]);
            // Modify each odd rank
            if (((i + 1) % 2) == 0) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
                charBuffer[i] = Character.forDigit(digit, 10);
            }
            sum += digit;
        }

        // correct accountNumber
        int lastDigit = 0;
        if ((sum % 10) != 0) {
            lastDigit = 10 - (sum % 10);
        }
        accountNumber += lastDigit;
        return accountNumber;
    }


    public static boolean isPassedLuhn(long num) {
        char[] charBuffer = Long.toString(num).toCharArray();
        char[] charBuffer1 = new char[charBuffer.length];

        for (int i = 0; i < charBuffer.length; i++) {
            charBuffer1[i] = charBuffer[charBuffer.length - 1 - i];
        }
        charBuffer = charBuffer1;

        // find the sum
        int sum = 0;
        for (int i = 0; i < charBuffer1.length; i++) {
            int digit = Character.getNumericValue(charBuffer[i]);
            // Modify each odd rank
            if (((i + 1) % 2) == 0) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
                charBuffer[i] = Character.forDigit(digit, 10);
            }
            sum += digit;
        }

        // correct accountNumber?
        return sum % 10 == 0;
    }

    public static void addMortgageCard(MortgageCard card) {
        String insertCardQuery = "INSERT INTO credit_card (card_number, pin, customer_id) VALUES (?, ?, ?, ?)";
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement psInsertCard = connection.prepareStatement(insertCardQuery);

            psInsertCard.setString(1, card.getNumber());
            psInsertCard.setString(2, "" + card.getPin());
            psInsertCard.setString(4, ""+ card.getCustomerId());

            psInsertCard.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addCreditCard(CreditCard card) {
        String insertCardQuery = "INSERT INTO credit_card (card_number, pin, balance, max_credit, customer_id) VALUES (?, ?, ?, ?, ?)";
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement psInsertCard = connection.prepareStatement(insertCardQuery);

            psInsertCard.setString(1, card.getNumber());
            psInsertCard.setString(2, "" + card.getPin());
            psInsertCard.setString(3, ""+ card.getBalance());
            psInsertCard.setString(4, ""+ card.getMaxBalance());
            psInsertCard.setString(5, ""+ card.customerId);

            psInsertCard.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addCardToDb(Card card) {
        String insertCardQuery = "INSERT INTO card (number, pin, balance) VALUES (?, ?, ?)";
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement psInsertCard = connection.prepareStatement(insertCardQuery);

            psInsertCard.setString(1, card.getCardNumber().toString());
            psInsertCard.setString(2, card.getPin().toString());
            psInsertCard.setString(3, card.getBalance().toString());

            psInsertCard.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
