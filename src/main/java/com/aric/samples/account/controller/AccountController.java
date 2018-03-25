/**
 *
 */
package com.aric.samples.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aric.samples.account.model.Account;
import com.aric.samples.account.service.AccountService;

/**
 * @author dursun
 *
 */
@RestController()
public class AccountController {
	@Autowired
	private AccountService service;

	@RequestMapping("/query")
	public List<Account> query(@RequestParam(value = "firstName", required = true) final String firstName,
			@RequestParam(value = "lastName", required = true) final String lastName) {
		return service.findAccountsByFirstNameAndLastName(firstName, lastName);
	}

	@RequestMapping("/deposit")
	public Account deposit(@RequestParam(value = "id", required = true) final Long accountId,
			@RequestParam(value = "amount", required = true) final Double amount) {
		return service.deposit(accountId, amount);
	}

	@RequestMapping("/transfer")
	public Account transfer(@RequestParam(value = "senderId", required = true) final Long senderAccountId,
			@RequestParam(value = "receiverId", required = true) final Long receiverAccountId,
			@RequestParam(value = "amount", required = true) final Double amount) {
		return service.transfer(senderAccountId, receiverAccountId, amount);
	}
}
