package com.lucas.spring.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {

	@Autowired
	Utils utils;

	String emailToken = "teste12dq";

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	final void testGenerateUserId() {
		String userId = utils.generateUserId(15);
		String userId2 = utils.generateUserId(15);

		assertNotNull(userId);
		assertTrue(userId.length() == 15);

		assertNotNull(userId2);
		assertNotEquals(userId, userId2);
	}

	@Test
	final void testHasTokenNotExpired() {
		String token = utils.gerateEmailVerificationToken(emailToken);
		assertNotNull(token);
		boolean hasTokenExpired = utils.hasTokenExpired(token);
		assertFalse(hasTokenExpired);
	}
	
	@Test
	final void testHasTokenExpired() {
		String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsdWNhc29sc2lAZ21haWwuY29tIiwiZXhwIjoxNTczMTUyNTM3fQ.J05KnCBD9ftJNBTRQndSgpD9DjInoPPSFd_EKKOrMECIa2iBBZWxBOEb4F0ojQRVyAxEALcFZ61YC4l16pE-Yg";
		boolean hasTokenExpired = utils.hasTokenExpired(expiredToken);
		assertTrue(hasTokenExpired);
	}

}
