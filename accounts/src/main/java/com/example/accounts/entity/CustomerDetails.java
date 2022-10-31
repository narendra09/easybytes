package com.example.accounts.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerDetails implements Serializable{
	
	private Accounts accounts;
	private List<Loans> loans;
	private List<Cards> cards;
	
	

}