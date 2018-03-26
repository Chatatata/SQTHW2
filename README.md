# SQTHW2

Software Quality and Testing, Assignment 2.

Implements *Bank Account* application with an HTTP API (Spring Framework) using JDBC (Spring Data).

## Components

The application consists of controller, model, repository and service type of components.

### Controller classes

#### `AccountController`

A `@RestController` used to broke RPC over HTTP instances to the required service calls.

##### Resolved mocks for dependencies

###### `AccountService`

```java
Mockito.when(this.accountService.findAccountsByFirstNameAndLastName(COMMON_FIRST_NAME, COMMON_LAST_NAME)).thenReturn(Collections.singletonList(this.firstAccount));
Mockito.when(this.accountService.deposit(this.firstAccount.getId(), DEPOSIT_AMOUNT)).thenReturn(this.nilAccount);
Mockito.when(this.accountService.transfer(this.firstAccount.getId(), this.secondAccount.getId(), TRANSFER_AMOUNT)).thenReturn(this.nilAccount);
```

### Service classes

#### `AccountService`

A single-bean contained service managing repository calls.

##### Resolved mocks for dependencies

###### `AccountRepository`

```java
Mockito.when(accountRepository.findByOwnerFirstNameAndOwnerLastName(COMMON_FIRST_NAME, COMMON_LAST_NAME)).thenReturn(Collections.singletonList(firstAccount));
Mockito.when(accountRepository.findOne(FIRST_USER_IDENTIFIER)).thenReturn(firstAccount);
Mockito.when(accountRepository.findOne(SECOND_USER_IDENTIFIER)).thenReturn(secondAccount);
```