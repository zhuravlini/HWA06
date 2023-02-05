package ru.netology.test;

import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.ListOfCardsPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TransferMoneyPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    @Test
    public void shouldTransferMoneySingleOwner() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        var listOfCardsPage = new ListOfCardsPage();
        int initialFirstCardBalance = listOfCardsPage.getFirstCardBalance(); // баланс 1ой карты ДО перевода
        int initialSecondCardBalance = listOfCardsPage.getSecondCardBalance(); // баланс 2ой карты ДО перевода
        listOfCardsPage.firstCardDeposit();
        var transferMoneyPage = new TransferMoneyPage();
        var depositInfo = DataHelper.getDepositInfo();
        transferMoneyPage.depositFromFirstCardToSecond(depositInfo);
        var listOfCardsPage2 = new ListOfCardsPage();
        int firstCardBalance = listOfCardsPage2.getFirstCardBalance(); // баланс 1ой карты ПОСЛЕ перевода
        int secondCardBalance = listOfCardsPage2.getSecondCardBalance(); // баланс 2ой карты ПОСЛЕ перевода
        int firstCardBalanceExpected = initialFirstCardBalance + DataHelper.getDepositInfo().getAmount();
        int secondCardBalanceExpected = initialSecondCardBalance - DataHelper.getDepositInfo().getAmount();
        assertEquals(firstCardBalanceExpected, firstCardBalance);
        assertEquals(secondCardBalanceExpected, secondCardBalance);
    }
}
