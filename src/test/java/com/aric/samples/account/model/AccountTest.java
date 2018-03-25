package com.aric.samples.account.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

public class AccountTest {
    private static final AtomicLong COMMON_IDENTIFIER = new AtomicLong(0);
    private static final double COMMON_BALANCE = .00f;
    private static final double COMMON_BIGGER_BALANCE = 5.00f;
    private static final double VALID_DEPOSIT_AMOUNT = 1.00f;
    private static final double VALID_WITHDRAW_AMOUNT = 1.00f;
    private static final double NEGATIVE_VALUE = -5.00f;
    private static final double ZERO = .0f;
    private static final String COMMON_FIRST_NAME = "first name";
    private static final String COMMON_LAST_NAME = "last name";
    private static final long COMMON_TCKN = 11111111111L;

    private Account account;

    @Before
    public void setUp() throws Exception {
        this.account = new Account();

        this.account.setBalance(COMMON_BALANCE);
    }

    @Test
    public void itRetrievesId() throws Exception {
        final Long givenLong = COMMON_IDENTIFIER.incrementAndGet();

        account.setId(givenLong);

        assertEquals(account.getId(), givenLong.longValue());
    }

    @Test
    public void itRetrievesBalance() throws Exception {
        final double givenBalance = COMMON_BALANCE;

        account.setBalance(givenBalance);

        assertTrue(account.getBalance() == givenBalance);
    }

    @Test
    public void itRetrievesTckn() throws Exception {
        final long tckn = COMMON_TCKN;

        account.setOwnerTckn(tckn);

        assertEquals(account.getOwnerTckn(), tckn);
    }

    @Test
    public void itRetrievesFirstName() throws Exception {
        final String firstName = COMMON_FIRST_NAME;

        account.setOwnerFirstName(firstName);

        assertEquals(account.getOwnerFirstName(), firstName);
    }

    @Test
    public void itRetrievesLastName() throws Exception {
        final String lastName = COMMON_LAST_NAME;

        account.setOwnerLastName(lastName);

        assertEquals(account.getOwnerLastName(), lastName);
    }

    @Test
    public void itDepositsPositiveAmountOfMoney() throws Exception {
        final double initialBalance = account.getBalance();

        account.deposit(VALID_DEPOSIT_AMOUNT);

        assertTrue(account.getBalance() ==COMMON_BALANCE + VALID_DEPOSIT_AMOUNT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itThrowsWhenZeroAmountOfMoneyIsGivenToDeposit() throws IllegalArgumentException {
        account.deposit(ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itThrowsWhenNegativeAmountOfMoneyIsGivenToDeposit() throws IllegalArgumentException {
        account.deposit(NEGATIVE_VALUE);
    }

    @Test
    public void itWithdrawsWhenExpectedAmountIsLeft() throws Exception {
        account.setBalance(COMMON_BIGGER_BALANCE);

        account.withdraw(VALID_WITHDRAW_AMOUNT);

        assertTrue(account.getBalance() == COMMON_BIGGER_BALANCE - VALID_WITHDRAW_AMOUNT);
    }

    @Test
    public void itWithdrawsWhenExactlyExpectedAmountIsLeft() throws Exception {
        account.setBalance(COMMON_BIGGER_BALANCE);

        account.withdraw(COMMON_BIGGER_BALANCE);

        assertTrue(account.getBalance() == ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itThrowsWhenMoreThanBalanceIsRequestedToWithdraw() throws IllegalArgumentException {
        account.setBalance(ZERO);

        account.withdraw(VALID_WITHDRAW_AMOUNT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itThrowsWhenNegativeAmountIsWithdrawn() throws IllegalArgumentException {
        account.setBalance(COMMON_BIGGER_BALANCE);

        account.withdraw(NEGATIVE_VALUE);
    }
}