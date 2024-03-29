package com.example.accounts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.accounts.config.AccountsServiceConfig;
import com.example.accounts.config.Properties;
import com.example.accounts.entity.Accounts;
import com.example.accounts.entity.Cards;
import com.example.accounts.entity.CustomerDetails;
import com.example.accounts.entity.Loans;
import com.example.accounts.feign.CardsFeignClient;
import com.example.accounts.feign.LoansFeignClient;
import com.example.accounts.service.AccountsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;



@RestController
@Slf4j
public class AccountsController {
	
	@Autowired
	private AccountsService accountsService;
	
	
	@Autowired
	private LoansFeignClient loansFeignClient;
	
	
	@Autowired
	AccountsServiceConfig accountsConfig;

	@GetMapping("/myAccount/{customerId}")
	public ResponseEntity<Accounts> getAccountDetails(@PathVariable int customerId) {
		Accounts accounts = accountsService.findByCustomerId(customerId);
		log.info("Accounts Json data {}",accounts);
		
		return  new ResponseEntity<Accounts>(accounts, HttpStatus.OK) ;
		

	}
	
	@GetMapping("/account/properties")
	public String getPropertyDetails() throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Properties properties = new Properties(accountsConfig.getMsg(), accountsConfig.getBuildVersion(),
				accountsConfig.getMailDetails(), accountsConfig.getActiveBranches());
		String jsonStr = ow.writeValueAsString(properties);
		return jsonStr;
	}
	
	@GetMapping("/myCustomerDetails/{customerId}")
	@CircuitBreaker(name = "detailsForCustomerSupportApp",fallbackMethod ="myCustomerDetailsFallBack")
	public  ResponseEntity<CustomerDetails> myCustomerDetails(@PathVariable int customerId) throws InterruptedException {
		
		log.info("inside account controller");
	
		CustomerDetails customerDetails = accountsService.getCustomerDetails(customerId);
		log.info("end of account controller");

		return new ResponseEntity<CustomerDetails>(customerDetails,HttpStatus.OK);

	}
	
	public  ResponseEntity<CustomerDetails> myCustomerDetailsFallBack(int customerId,Throwable t) {
		Accounts accounts = accountsService.findByCustomerId(customerId);
		List<Loans> loans = loansFeignClient.getAccountDetails(customerId).getBody();
		

		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccounts(accounts);
		customerDetails.setLoans(loans);

		
		return new ResponseEntity<CustomerDetails>(customerDetails,HttpStatus.OK);

	}


}
