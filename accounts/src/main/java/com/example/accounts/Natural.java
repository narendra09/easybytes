package com.example.accounts;

public class Natural {
	public static void main(String[] args) {
		
			print(1,15);
		
	}
	
	public static void print(int i,int n)
	{
	if(i<=n)
	{
		System.out.println(i);
		i++;
		print(i,n);
	}
	}

}
