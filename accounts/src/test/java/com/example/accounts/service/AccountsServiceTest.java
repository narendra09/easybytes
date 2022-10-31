package com.example.accounts.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.accounts.entity.Accounts;
import com.example.accounts.repository.AccountsRepository;

@ExtendWith(MockitoExtension.class)
public class AccountsServiceTest {

	@Mock
	private AccountsRepository accountsRepository;

	@InjectMocks
	private AccountsService accountsService;
	
	

	@Test
	public void testFindByCustomerId() {
		Accounts accounts = Accounts.builder().customerId(1).accountNumber(1234).build();

		Mockito.when(accountsRepository.findByCustomerId(accounts.getCustomerId())).thenReturn(accounts);

		Accounts acc=accountsService.findByCustomerId(accounts.getCustomerId());
		
		assertThat(acc).isNotNull();
		assertThat(acc.getCustomerId()).isEqualTo(1);
	}
}
