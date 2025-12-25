package com.mikky.corebanking.audit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Date;
import java.util.List;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@SpringBootTest
@AutoConfigureMockMvc
class AuditApplicationTests {

	private static final String API_V1 = "/api/v1/audit";
	private static final String LOGS_URI = "/logs";

	@Autowired
	MockMvc mock;

	@Test
	void shouldLoadAuditSuccessfully() throws Exception {
		var queryAuditLogRequest = get(API_V1 + LOGS_URI)
				.header("Authorization",
						"Bearer " + this.generateAuthTokenWithPermissions(List.of("ROLE_ADMIN"),
								List.of("PERMISSION_CAN_READ_AUDIT")))
				.contentType(MediaType.APPLICATION_JSON);

		this.mock.perform(queryAuditLogRequest).andExpect(status().isOk());
	}

	private String generateAuthTokenWithPermissions(List<String> roles, List<String> permissions) {
		return Jwts.builder()
				.setSubject("sampleuser@samplemail.com")
				.claim("roles", roles)
				.claim("permissions", permissions)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 3000))
				.signWith(SignatureAlgorithm.HS512, "test-secret-key")
				.compact();
	}

}
