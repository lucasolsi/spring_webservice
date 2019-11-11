package com.lucas.spring.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.lucas.spring.entity.AddressEntity;
import com.lucas.spring.entity.UserEntity;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

	@Autowired
	UserRepository userRepo;

	static boolean recordsCreated = false;
	
	String userId = "912f4f5";

	@BeforeEach
	void setUp() throws Exception {
		if (!recordsCreated) {
			createRecords();
		}
	}

	@Test
	void testGetVerifiedUsers() {
		Pageable pageableRequest = PageRequest.of(0, 2);
		Page<UserEntity> pages = userRepo.findAllUsersWithConfirmedEmailAddress(pageableRequest);

		assertNotNull(pages);

		List<UserEntity> userRecords = pages.getContent();
		assertNotNull(userRecords);
		assertTrue(userRecords.size() == 1);
	}

	@Test
	void testFindUserByFirstName() {
		String firstName = "Repository";
		List<UserEntity> foundUsers = userRepo.findUserByFirstName(firstName);
		assertNotNull(foundUsers);
		assertTrue(foundUsers.size() == 2);

		UserEntity user = foundUsers.get(0);
		assertTrue(user.getFirstName().equals(firstName));
	}

	@Test
	void testFindUserByLastName() {
		String lastName = "Test";
		List<UserEntity> foundUsers = userRepo.findUserByLastName(lastName);
		assertNotNull(foundUsers);
		assertTrue(foundUsers.size() == 2);

		UserEntity user = foundUsers.get(0);
		assertTrue(user.getLastName().equals(lastName));
		assertEquals(user.getLastName(), foundUsers.get(1).getLastName());
	}

	@Test
	void testFindUsersByKeyword() {
		String keyword = "ory";
		List<UserEntity> foundUsers = userRepo.findUsersByKeyword(keyword);
		assertNotNull(foundUsers);
		assertTrue(foundUsers.size() == 2);

		UserEntity user = foundUsers.get(0);
		assertTrue(user.getFirstName().contains(keyword));
	}

	@Test
	void testupdateUserEmailVerificationStatus() {
		boolean emailVerificationStatus = false;
		userRepo.updateUserEmailVerificationStatus(emailVerificationStatus, userId);

		UserEntity storedUser = userRepo.findByUserId(userId);
		boolean storedEmailVerifStatus = storedUser.getEmailVerificationStatus();
		
		assertTrue(storedEmailVerifStatus == emailVerificationStatus);
	}

	@Test
	void testfindUserEntityByUserId(){
		UserEntity userEntity = userRepo.findUserEntityByUserId(userId);

		assertNotNull(userEntity);
		assertTrue(userEntity.getUserId().equals(userId));
	}

	private void createRecords() {
		UserEntity userEntity = new UserEntity();
		userEntity.setFirstName("Repository");
		userEntity.setLastName("Test");
		userEntity.setUserId("912f4f5");
		userEntity.setEncrycptedPassword("ttt");
		userEntity.setEmail("email@email.com");
		userEntity.setEmailVerificationStatus(true);

		// Second User
		UserEntity userEntity2 = new UserEntity();
		userEntity2.setFirstName("Repository");
		userEntity2.setLastName("Test");
		userEntity2.setUserId("lhfto5");
		userEntity2.setEncrycptedPassword("ttt");
		userEntity2.setEmail("email2@email.com");
		userEntity2.setEmailVerificationStatus(false);

		AddressEntity addressEntity = new AddressEntity();
		addressEntity.setType("shipping");
		addressEntity.setAddressId("test01");
		addressEntity.setCity("New City");
		addressEntity.setCountry("Somewhere");
		addressEntity.setPostalCode("120124-009");
		addressEntity.setStreetName("12 Hurricane Street");

		AddressEntity addressEntity2 = new AddressEntity();
		addressEntity2.setType("shipping");
		addressEntity2.setAddressId("test02");
		addressEntity2.setCity("New City");
		addressEntity2.setCountry("Somewhere");
		addressEntity2.setPostalCode("120124-009");
		addressEntity2.setStreetName("12 Hurricane Street");

		List<AddressEntity> addresses = new ArrayList<>();
		addresses.add(addressEntity);

		List<AddressEntity> addresses2 = new ArrayList<>();
		addresses2.add(addressEntity2);

		userEntity.setAddresses(addresses);
		userEntity2.setAddresses(addresses2);

		userRepo.save(userEntity);
		userRepo.save(userEntity2);

		recordsCreated = true;
	}

}
