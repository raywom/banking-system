package banking;

public class User {
    private String name;
    private String surname;
    private String phoneNumber;
    private String password;
    private boolean isCreditCard;
    private boolean isDebitCard;
    private boolean isMortgageCard;

    private Card card;
    private Boolean isLoggedIn;

    public Card getCard() {
        return card;
    }

    public void logIn(Card targetCard) {
        if (targetCard != null) {
            card = Bank.pullCardFromDB(targetCard.getCardNumber());
            isLoggedIn = true;
        }
    }

    public void logOut() {
        card = null;
        isLoggedIn = false;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public User() {
        card = null;
        isLoggedIn = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isCreditCard() {
        return isCreditCard;
    }

    public void setCreditCard(boolean creditCard) {
        isCreditCard = creditCard;
    }

    public boolean isDebitCard() {
        return isDebitCard;
    }

    public void setDebitCard(boolean debitCard) {
        isDebitCard = debitCard;
    }

    public boolean isMortgageCard() {
        return isMortgageCard;
    }

    public void setMortgageCard(boolean mortgageCard) {
        isMortgageCard = mortgageCard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
