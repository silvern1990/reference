package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.test.TestDAO;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	TestDAO testDAO;

	@Test
	void contextLoads() {
		try{
			testDAO.selectQuery();
			System.out.println("------------tttttttttt------------");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
