package com.example.accounts;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AccountsControllerIntTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Test
	public void getAccountDetailsTest() throws Exception
	{
		ResultActions response=mockMvc.perform(get("/myAccount/{customerId}",1));
		System.out.println("response"+response);
		
		response.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.accountNumber",is(186576453)));
	}

}
