package com.mikky.corebanking.audit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuditApplicationTests {

	private static final String API_V1 = "/api/v1/audit";
    private static final String LOGS_URI = "/logs";

	@BeforeEach
	void cleanDatabase() {
		
	}

	@Test
	void shouldLoadAuditSuccessfully() {}

}
