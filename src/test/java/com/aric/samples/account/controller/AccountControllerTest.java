package com.aric.samples.account.controller;

import com.aric.samples.account.controller.AccountControllerTest.AccountControllerTestContextConfiguration;
import com.aric.samples.account.model.Account;
import com.aric.samples.account.service.AccountService;
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
@ContextConfiguration(classes = {AccountControllerTestContextConfiguration.class})
public class AccountControllerTest {
    @Configuration
    @ComponentScan(basePackages = {
            "com.aric.samples.account.controller",
            "com.aric.samples.account.service"
    })
    static class AccountControllerTestContextConfiguration {
        @Bean
        public AccountService accountService() {
            return Mockito.mock(AccountService.class);
        }

        @Bean
        public AccountController accountController() {
            return new AccountController();
        }
    }

    private static final String COMMON_FIRST_NAME = "first name";
    private static final String COMMON_LAST_NAME = "last name";
    private static final Long FIRST_USER_IDENTIFIER = 0L;
    private static final Long SECOND_USER_IDENTIFIER = 1L;
    private static final Long NIL_USER_IDENTIFIER = 2L;
    private static final Double INITIAL_BALANCE = 50.00d;
    private static final Double DEPOSIT_AMOUNT = 5.00d;
    private static final Double TRANSFER_AMOUNT = 5.00d;
    private static final Double NEGATIVE_VALUE = -5.00d;
    private static final Double ZERO = 0.00d;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountController accountController;

    private Account firstAccount;
    private Account secondAccount;
    private Account nilAccount;

    @Before
    public void setUp() throws Exception {
        this.firstAccount = new Account();
        this.firstAccount.setOwnerFirstName(COMMON_FIRST_NAME);
        this.firstAccount.setOwnerLastName(COMMON_LAST_NAME);
        this.firstAccount.setId(FIRST_USER_IDENTIFIER);
        this.firstAccount.setBalance(INITIAL_BALANCE);

        this.secondAccount = new Account();
        this.secondAccount.setId(SECOND_USER_IDENTIFIER);

        this.nilAccount = new Account();
        this.nilAccount.setId(NIL_USER_IDENTIFIER);

        Mockito.when(this.accountService.findAccountsByFirstNameAndLastName(COMMON_FIRST_NAME, COMMON_LAST_NAME)).thenReturn(Collections.singletonList(this.firstAccount));
        Mockito.when(this.accountService.deposit(this.firstAccount.getId(), DEPOSIT_AMOUNT)).thenReturn(this.nilAccount);
        Mockito.when(this.accountService.transfer(this.firstAccount.getId(), this.secondAccount.getId(), TRANSFER_AMOUNT)).thenReturn(this.nilAccount);
    }

    @After
    public void tearDown() throws Exception {
        Mockito.reset(accountService);
    }

    @Test
    public void callsServiceFinder() throws Exception {
        List<Account> accounts = this.accountController.query(COMMON_FIRST_NAME, COMMON_LAST_NAME);

        Mockito.verify(this.accountService, VerificationModeFactory.times(1)).findAccountsByFirstNameAndLastName(COMMON_FIRST_NAME, COMMON_LAST_NAME);
        assertEquals(accounts, Collections.singletonList(this.firstAccount));
    }

    @Test
    public void callsServiceDeposit() throws Exception {
        this.accountController.deposit(this.firstAccount.getId(), DEPOSIT_AMOUNT);

        Mockito.verify(this.accountService, VerificationModeFactory.times(1)).deposit(this.firstAccount.getId(), DEPOSIT_AMOUNT);
    }

    @Test
    public void callsServiceTransfer() throws Exception {
        this.accountController.transfer(this.firstAccount.getId(), this.secondAccount.getId(), TRANSFER_AMOUNT);

        Mockito.verify(this.accountService, VerificationModeFactory.times(1)).transfer(this.firstAccount.getId(), this.secondAccount.getId(), TRANSFER_AMOUNT);
    }
}