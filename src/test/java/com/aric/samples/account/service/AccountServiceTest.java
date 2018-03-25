package com.aric.samples.account.service;

import com.aric.samples.account.model.Account;
import com.aric.samples.account.repository.AccountRepository;
import com.aric.samples.account.service.AccountServiceTest.AccountServiceTestContextConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AccountServiceTestContextConfiguration.class})
public class AccountServiceTest {
    @Configuration
    @ComponentScan(basePackages = {
            "com.aric.samples.account.service",
            "com.aric.samples.account.repository"
    })
    static class AccountServiceTestContextConfiguration {
        @Bean
        public AccountService accountService() {
            return new AccountService();
        }

        @Bean
        public AccountRepository accountRepository() {
            return Mockito.mock(AccountRepository.class);
        }
    }

    private static final String COMMON_FIRST_NAME = "first name";
    private static final String COMMON_LAST_NAME = "last name";
    private static final Long FIRST_USER_IDENTIFIER = 0L;
    private static final Long SECOND_USER_IDENTIFIER = 1L;
    private static final Double INITIAL_BALANCE = 50.00d;
    private static final Double DEPOSIT_AMOUNT = 5.00d;
    private static final Double TRANSFER_AMOUNT = 5.00d;
    private static final Double NEGATIVE_VALUE = -5.00d;
    private static final Double ZERO = 0.00d;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    private Account firstAccount;
    private Account secondAccount;

    @Before
    public void setUp() throws Exception {
        this.firstAccount = new Account();
        this.firstAccount.setOwnerFirstName(COMMON_FIRST_NAME);
        this.firstAccount.setOwnerLastName(COMMON_LAST_NAME);
        this.firstAccount.setId(FIRST_USER_IDENTIFIER);
        this.firstAccount.setBalance(INITIAL_BALANCE);

        this.secondAccount = new Account();
        this.secondAccount.setId(SECOND_USER_IDENTIFIER);

        Mockito.when(accountRepository.findByOwnerFirstNameAndOwnerLastName(COMMON_FIRST_NAME, COMMON_LAST_NAME)).thenReturn(Collections.singletonList(firstAccount));
        Mockito.when(accountRepository.findOne(FIRST_USER_IDENTIFIER)).thenReturn(firstAccount);
        Mockito.when(accountRepository.findOne(SECOND_USER_IDENTIFIER)).thenReturn(secondAccount);
    }

    @After
    public void tearDown() throws Exception {
        Mockito.reset(accountRepository);
    }

    @Test
    public void returnsFindResultOfRepository() throws Exception {
        List<Account> accounts = accountService.findAccountsByFirstNameAndLastName(COMMON_FIRST_NAME, COMMON_LAST_NAME);

        assertEquals(accounts, Collections.singletonList(firstAccount));
    }

    @Test
    public void accountQueryCallsRepositoryFunction() {
        List<Account> accounts = accountService.findAccountsByFirstNameAndLastName(COMMON_FIRST_NAME, COMMON_LAST_NAME);

        Mockito.verify(accountRepository, VerificationModeFactory.times(1)).findByOwnerFirstNameAndOwnerLastName(COMMON_FIRST_NAME, COMMON_LAST_NAME);
    }

    @Test
    public void depositSavesWithValidMoneyAmount() throws Exception {
        accountService.deposit(this.firstAccount.getId(), DEPOSIT_AMOUNT);

        Mockito.verify(accountRepository, VerificationModeFactory.times(1)).save(this.firstAccount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void depositRethrowsIfAmountIsNegative() throws Exception {
        accountService.deposit(this.firstAccount.getId(), NEGATIVE_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void depositRethrowsIfAmountIsZero() throws Exception {
        accountService.deposit(this.firstAccount.getId(), ZERO);
    }

    @Test
    public void depositCallsRepositoryFindFunction() {
        accountService.deposit(this.firstAccount.getId(), DEPOSIT_AMOUNT);

        Mockito.verify(accountRepository, VerificationModeFactory.times(1)).findOne(this.firstAccount.getId());
    }

    @Test
    public void transferWithdrawalsCorrectAmountOfMoney() throws Exception {
        accountService.transfer(this.firstAccount.getId(), this.secondAccount.getId(), TRANSFER_AMOUNT);

        assertTrue(this.firstAccount.getBalance() == INITIAL_BALANCE - TRANSFER_AMOUNT);
    }

    @Test
    public void transferDepositsCorrectAmountOfMoney() throws Exception {
        accountService.transfer(this.firstAccount.getId(), this.secondAccount.getId(), TRANSFER_AMOUNT);

        assertTrue(this.secondAccount.getBalance() == INITIAL_BALANCE + TRANSFER_AMOUNT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void transferRethrowsIfAmountIsZero() throws Exception {
        accountService.transfer(this.firstAccount.getId(), this.secondAccount.getId(), ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void transferRethrowsIfAmountIsNegative() throws Exception {
        accountService.transfer(this.firstAccount.getId(), this.secondAccount.getId(), NEGATIVE_VALUE);
    }

    @Test
    public void transferCallsRepositoryFindFunctionForSender() {
        accountService.transfer(this.firstAccount.getId(), this.secondAccount.getId(), TRANSFER_AMOUNT);

        Mockito.verify(accountRepository, VerificationModeFactory.times(1)).findOne(this.firstAccount.getId());
    }

    @Test
    public void transferCallsRepositoryFindFunctionForReceiver() {
        accountService.transfer(this.firstAccount.getId(), this.secondAccount.getId(), TRANSFER_AMOUNT);

        Mockito.verify(accountRepository, VerificationModeFactory.times(1)).findOne(this.secondAccount.getId());
    }

    @Test
    public void transferSavesFirstAccount() throws Exception {
        accountService.transfer(this.firstAccount.getId(), this.secondAccount.getId(), TRANSFER_AMOUNT);

        Mockito.verify(accountRepository, VerificationModeFactory.times(1)).save(this.firstAccount);
    }

    @Test
    public void transferSavesSecondAccount() throws Exception {
        accountService.transfer(this.firstAccount.getId(), this.secondAccount.getId(), TRANSFER_AMOUNT);

        Mockito.verify(accountRepository, VerificationModeFactory.times(1)).save(this.secondAccount);
    }
}