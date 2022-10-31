package com.example.accounts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.accounts.entity.Accounts;
import com.example.accounts.entity.Cards;
import com.example.accounts.entity.CustomerDetails;
import com.example.accounts.entity.Loans;
import com.example.accounts.feign.CardsFeignClient;
import com.example.accounts.feign.LoansFeignClient;
import com.example.accounts.repository.AccountsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountsService {
	
	@Autowired
	private AccountsRepository accountsRepository;
	
	@Autowired
	private LoansFeignClient loansFeignClient;
	
	@Autowired
	private CardsFeignClient cardsFeignClient;

	@Cacheable(value = "customers",key = "#customerId")
	public Accounts findByCustomerId(int customerId) {
		
		log.info("calling db from service");
		 Optional<Accounts> findById = Optional.ofNullable(accountsRepository.findByCustomerId(customerId));
		if(findById.isPresent())
		{
			return findById.get();
		}
		return new Accounts();
	}

	@Cacheable(value = "accounts",key = "#customerId")
	public CustomerDetails getCustomerDetails(int customerId) {
		log.info("calling db from for customerdetails");
		Accounts accounts=null;
		Optional<Accounts> findById = Optional.ofNullable(accountsRepository.findByCustomerId(customerId));
		if(findById.isPresent())
		{
			accounts= findById.get();
		}
		 
		List<Loans> loans = loansFeignClient.getAccountDetails(customerId).getBody();
		List<Cards> cards = cardsFeignClient.getAccountDetails(customerId).getBody();

		System.out.println("getting data from db");
		//Thread.sleep(60000);
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccounts(accounts);
		customerDetails.setLoans(loans);
		customerDetails.setCards(cards);
		return customerDetails;
	}

}
