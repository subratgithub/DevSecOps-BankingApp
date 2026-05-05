package com.example.bankapp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankappApplicationTests {

	@Test
	void testApplicationClass() {
		// Test that the application class can be instantiated
		BankappApplication app = new BankappApplication();
		assertNotNull(app);
	}
	
	@Test
	void testApplicationName() {
		String expectedName = "BankappApplication";
		BankappApplication app = new BankappApplication();
		assertEquals(expectedName, app.getClass().getSimpleName());
	}

}
