package banking;

import org.sqlite.SQLiteDataSource;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        User user = new User();
        Banker banker = new Banker();

        String fileName = "card.s3db";


        if (fileName.equals("")) {
            System.out.println("db filename is missing!\nAnykey: >");
            scanner.nextLine();
        } else {
            Bank.url = "jdbc:sqlite:" + fileName;
            if (!Files.isRegularFile(Paths.get(fileName))) {
                createCardsDatabase();
            }
        }

        mainMenu:
        while (true) {
            System.out.println("who you are?");
            System.out.println("1)client");
            System.out.println("2)banker");
            int who = scanner.nextInt();

            System.out.println("1)login");
            if (who == 1)
                System.out.println("2)registration");

            choiceOfInitialAction(who, scanner.nextInt(), user, banker);

            if (who == 1) {
                System.out.print("1. Create an account\r\n" +
                        "2. Log into account\r\n" +
                        "0. Exit\r\n" +
                        ">");

                switch (scanner.nextInt()) {
                    case 1:
                        printNewAccount(Bank.createAccount());
                        user.setDebitCard(true);
                        break;
                    case 2:
                        System.out.println("1) Credit card");
                        System.out.println("2) Mortgage card");
                        System.out.println("3) card off");

                        switch (scanner.nextInt()){
                            case 1:
                                user.logIn(showLogInDialogueForCredit());
                                if (user.isLoggedIn()) {
                                    System.out.println("\n\rYou have successfully logged in!\n\r");
                                    int returnTo = showCardDialogue(user.getCreditCard());
                                    user.logOut();
                                    System.out.println("\n\rYou have successfully logged out!\n\r");
                                    if (returnTo == 0) break mainMenu;
                                }
                                break;
                            case 2:
                                user.logIn(showLogInDialogueForMortgage());
                                if (user.isLoggedIn()) {
                                    System.out.println("\n\rYou have successfully logged in!\n\r");
                                    int returnTo = showCardDialogue(user.getMortgageCard());
                                    user.logOut();
                                    System.out.println("\n\rYou have successfully logged out!\n\r");
                                    if (returnTo == 0) break mainMenu;
                                }
                                break;
                            case 3:
                                user.logIn(showLogInDialogue());
                                if (user.isLoggedIn()) {
                                    System.out.println("\n\rYou have successfully logged in!\n\r");
                                    int returnTo = showCardDialogue(user.getCard());
                                    user.logOut();
                                    System.out.println("\n\rYou have successfully logged out!\n\r");
                                    if (returnTo == 0) break mainMenu;
                                }
                                break;
                        }
                        break;
                    case 0:
                        break mainMenu;
                    default:
                        System.out.println("Wrong number!\r\nTry again...\r\n\n");
                }
            }else{
                System.out.print("1. create a credit card\r\n" +
                        "2. create a mortgage card\r\n" +
                        "0. Exit\r\n" +
                        ">");
                switch (scanner.nextInt()) {
                    case 1:
                        System.out.println("Enter phone customer");
                        int idCustomer = scanner.nextInt();
                        printNewAccount(Bank.createCreditCard(idCustomer));
                        break;
                    case 2:
                        System.out.println("Enter phone customer");
                        idCustomer = scanner.nextInt();
                        printNewAccount(Bank.CreateMortgageCard(idCustomer));
                        break;
                    case 0:
                        break mainMenu;
                    default:
                        System.out.println("Wrong number!\r\nTry again...\r\n\n");
                }
            }
        }
        System.out.println("\r\nBye!");
    }


    public static void createCardsDatabase() {

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(Bank.url);

        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS debit_card(" +
                        "id INTEGER," +
                        "number TEXT," +
                        "pin TEXT," +
                        "balance INTEGER DEFAULT 0" +
                        ")");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void printNewAccount(MortgageCard card) {
        System.out.println("\n\rYour card has been created\n\rYour card number:");
        System.out.printf("%016d%n", card.getNumber());
        System.out.println("Your card PIN:");
        System.out.printf("%04d%n%n", card.getPin());
    }

    public static void printNewAccount(CreditCard card) {
        System.out.println("\n\rYour card has been created\n\rYour card number:");
        System.out.printf("%016d%n", card.getNumber());
        System.out.println("Your card PIN:");
        System.out.printf("%04d%n%n", card.getPin());
    }

    public static void printNewAccount(Card card) {
        System.out.println("\n\rYour card has been created\n\rYour card number:");
        System.out.printf("%016d%n", card.getCardNumber());
        System.out.println("Your card PIN:");
        System.out.printf("%04d%n%n", card.getPin());
    }

    public static MortgageCard showLogInDialogueForMortgage() {
        System.out.print("\n\rEnter your card number:\n\r>");
        String cardNumber = scanner.nextLine();
        System.out.print("Enter your PIN:\n\r>");
        int pin = scanner.nextInt();

        MortgageCard card = new MortgageCard();
        card.setNumber(cardNumber);
        card.setPin(pin);

        if (Bank.getCardsFromDB().contains(card)) {
            return card;
        } else {
            System.out.println("\n\rWrong card number or PIN!\r\n");
            return null;
        }
    }

    public static CreditCard showLogInDialogueForCredit() {
        System.out.print("\n\rEnter your card number:\n\r>");
        String cardNumber = scanner.nextLine();
        System.out.print("Enter your PIN:\n\r>");
        int pin = scanner.nextInt();

        CreditCard card = new CreditCard();
        card.setNumber(cardNumber);
        card.setPin(pin);

        if (Bank.getCardsFromDB().contains(card)) {
            return card;
        } else {
            System.out.println("\n\rWrong card number or PIN!\r\n");
            return null;
        }
    }


    public static Card showLogInDialogue() {
        System.out.print("\n\rEnter your card number:\n\r>");
        long cardNumber = scanner.nextLong();
        System.out.print("Enter your PIN:\n\r>");
        long pin = scanner.nextLong();

        Card card = new Card().setCardNumber(cardNumber).setPin(pin);

        if (Bank.getCardsFromDB().contains(card)) {
            return card;
        } else {
            System.out.println("\n\rWrong card number or PIN!\r\n");
            return null;
        }
    }

    public static int showCardDialogue(Card card) {
        while(true) {
            System.out.print("\n1. Balance" +
                    "\n2. Add income" +
                    "\n3. Do transfer" +
                    "\n4. Close account" +
                    "\n5. Log out" +
                    "\n0. Exit" +
                    "\n>");

            switch (scanner.nextInt()) {
                case 2:
                    showAddIncomeDialogue(card);
                case 1:
                    System.out.println("\nBalance: " + card.getBalanceFromDB() + "\n");
                    break;
                case 3:
                    doTransferDialogue(card);
                    break;
                case 4:
                    card.deleteCardFromDB();
                    System.out.println("\nThe account has been closed!");
                    break;
                case 5:
                    return 2;
                case 0:
                    return 0;
                default:
                    System.out.println("Wrong number!\r\nTry again...\r\n\n");
            }
        }
    }

    public static int showCardDialogue(MortgageCard card) {
        while(true) {
            System.out.print(
                    "\n5. Log out" +
                    "\n0. Exit" +
                    "\n>");
            switch (scanner.nextInt()) {
                case 5:
                    return 2;
                case 0:
                    return 0;
                default:
                    System.out.println("Wrong number!\r\nTry again...\r\n\n");
            }
        }
    }

    public static int showCardDialogue(CreditCard card) {
        while(true) {
            System.out.print("\n1. Balance" +
                    "\n2. Add income" +
                    "\n3. Close account" +
                    "\n4. Log out" +
                    "\n0. Exit" +
                    "\n>");

            switch (scanner.nextInt()) {
                case 2:
                    showAddIncomeDialogue(card);
                case 1:
                    System.out.println("\nBalance: " + card.getBalance() + "\n");
                    break;
                case 3:
                    card.deleteCardFromDBCredit();
                    System.out.println("\nThe account has been closed!");
                    break;
                case 4:
                    return 2;
                case 0:
                    return 0;
                default:
                    System.out.println("Wrong number!\r\nTry again...\r\n\n");
            }
        }
    }

    public static void showAddIncomeDialogue(Card card) {
        double amount;
        while(true) {
            System.out.print("Enter the amount:\n>");
            amount = scanner.nextDouble();

            if (amount >= 0) break;
            else {
                System.out.println("Wrong number!\r\nTry again...\r\n\n");
            }
        }
        card.addBalanceToDB(amount);
    }

    public static void showAddIncomeDialogue(CreditCard card) {
        double amount;
        while(true) {
            System.out.print("Enter the amount:\n>");
            amount = scanner.nextDouble();

            if (amount >= 0) break;
            else {
                System.out.println("Wrong number!\r\nTry again...\r\n\n");
            }
        }
        card.addBalanceToDBCredit(amount);
    }



    public static void doTransferDialogue(Card card) {
        long targetCardNumber;
        double moneyAmount = 0;


        targetCardNumber = showEnterTargetCardNumberDialogue(card);
        if (targetCardNumber != 0) {
            moneyAmount = showEnterMoneyAmountDialogue(card);
        }

        if ((targetCardNumber != 0) && (moneyAmount != 0)) {
            // transaction
            Card targetCard;
            targetCard = Bank.pullCardFromDB(targetCardNumber);
            card.addBalanceToDB(-moneyAmount);
            targetCard.addBalanceToDB(moneyAmount);

            System.out.println("\nThe funds are transferred successfully.");
        }
    }


    public static long showEnterTargetCardNumberDialogue(Card card) {
        long targetCardNumber;

        while(true) {
            System.out.print("\nEnter number of the card to which the funds are to be transferred," +
                    "\nor 0 to cancel the operation:\n>");
            targetCardNumber = scanner.nextLong();

            if (targetCardNumber == 0) {
                break;
            }
            if (targetCardNumber == card.getCardNumber()) {
                System.out.println("You can't transfer money to the same account!");
                targetCardNumber = 0;
                break;
            }
            if (!Bank.isPassedLuhn(targetCardNumber)) {
                System.out.println("Probably you made mistake in the card number. Please try again!");
                continue;
            }
            if (!Bank.cardIsInDB(targetCardNumber)) {
                System.out.println("Such a card does not exist.");
                targetCardNumber = 0;
                break;
            }
            break;
        }
        return targetCardNumber;
    }

    public static void choiceOfInitialAction(int who, int what, User user, Banker banker){
        if (who == 1) {
            if (what == 1) {
                System.out.println("Enter your name");
                user.setName(scanner.nextLine());
                System.out.println("Enter your surname");
                user.setSurname(scanner.nextLine());
                System.out.println("Enter your phone");
                user.setPhoneNumber(scanner.nextLine());
                Bank.addCustomer(user);
            } else if (what == 2) {
                System.out.println("Enter your phone");
                user = Bank.userSearch(scanner.nextLine());
            }
        }
        else {
            if (what == 1){
                System.out.println("Enter your login");
                String login = scanner.nextLine();
                System.out.println("Enter your password");
                String password = scanner.nextLine();
                banker = Bank.bankerSearch(login, password);
            }
        }
    }


    public static double showEnterMoneyAmountDialogue(Card card) {
        double moneyAmount;

        while (true){
            System.out.print("\nEnter how much money you want to transfer," +
                    "\nor 0 to cancel the operation:\n>");
            moneyAmount = scanner.nextDouble();

            if (moneyAmount < 0) {
                System.out.println("Please, enter positive number.");
                continue;
            }
            if (moneyAmount > card.getBalanceFromDB()) {
                System.out.println("Not enough money!");
                moneyAmount = 0;
                break;
            }
            break;
        }
        return moneyAmount;
    }
}

