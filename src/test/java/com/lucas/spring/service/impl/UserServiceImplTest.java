package com.lucas.spring.service.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.lucas.spring.entity.AddressEntity;
import com.lucas.spring.entity.UserEntity;
import com.lucas.spring.repositories.UserRepository;
import com.lucas.spring.shared.AmazonEmailService;
import com.lucas.spring.shared.Utils;
import com.lucas.spring.shared.dto.AddressDto;
import com.lucas.spring.shared.dto.UserDto;

class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userService;

	@Mock
	UserRepository userRepo;

	@Mock
	Utils utils;

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Mock
	AmazonEmailService amazonMail;

	// User attributes
	String firstName = "Teste";
	String lastName = "Unitário";
	String userId = "i58rpthr4";
	String encryptedPassword = "1231ht5js";
	String userEmail = "teste@teste.com";
	String password = "senha";

	// Addresses attributes
	String shippingType = "shipping";
	String billingType = "billing";
	String addressId = "12kdasjv50";
	String city = "Cidade";
	String country = "Brazil";
	String postalCode = "999991-911";
	String streetName = "Rua das Casas, 38";

	UserEntity userEntity;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName(firstName);
		userEntity.setLastName(lastName);
		userEntity.setUserId(userId);
		userEntity.setEncrycptedPassword(encryptedPassword);
		userEntity.setEmail(userEmail);
		userEntity.setEmailVerificationToken("aaaaaaaaa");
		userEntity.setAddresses(getAddressesEntity());
	}

	@Test
	void testGetUser() {

		when(userRepo.findByEmail(anyString())).thenReturn(userEntity);

		UserDto userDto = userService.getUser(userEmail);

		assertNotNull(userDto);
		assertEquals(firstName, userDto.getFirstName());
	}

	@Test
	final void testGetUser_UsernameNotFoundException() {
		when(userRepo.findByEmail(anyString())).thenReturn(null);

		assertThrows(UsernameNotFoundException.class, () -> {
			userService.getUser(userEmail);
		});
	}

	@Test
	final void testCreateUser() {
		when(userRepo.findByEmail(anyString())).thenReturn(null);
		when(utils.generateAddressId(anyInt())).thenReturn(addressId);
		when(utils.generateUserId(anyInt())).thenReturn(userId);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
		when(userRepo.save(any(UserEntity.class))).thenReturn(userEntity);
		Mockito.doNothing().when(amazonMail).verifyEmail(any(UserDto.class));
		
		UserDto userDto = new UserDto();
		userDto.setAddresses(getAddressesDto());
		userDto.setEmail(userEmail);
		userDto.setFirstName(firstName);
		userDto.setLastName(lastName);
		userDto.setPassword(password);
		
		UserDto storedUserDetails = userService.createUser(userDto);

		assertNotNull(storedUserDetails);
		assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
		assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
		assertNotNull(storedUserDetails.getUserId());
		assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());
		verify(utils,times(storedUserDetails.getAddresses().size())).generateAddressId(10);
		verify(bCryptPasswordEncoder, times(1)).encode(password);
		verify(userRepo,times(1)).save(any(UserEntity.class));
	}

	private List<AddressDto> getAddressesDto() {
		AddressDto addressDto = new AddressDto();
		addressDto.setType(billingType);
		addressDto.setAddressId(addressId);
		addressDto.setCity(city);
		addressDto.setCountry(country);
		addressDto.setPostalCode(postalCode);
		addressDto.setStreetName(streetName);

		AddressDto shippingAddressDto = new AddressDto();
		shippingAddressDto.setType(shippingType);
		shippingAddressDto.setAddressId(addressId);
		shippingAddressDto.setCity(city);
		shippingAddressDto.setCountry(country);
		shippingAddressDto.setPostalCode(postalCode);
		shippingAddressDto.setStreetName(streetName);

		List<AddressDto> addresses = new ArrayList<>();
		addresses.add(shippingAddressDto);
		addresses.add(addressDto);

		return addresses;
	}

	private List<AddressEntity> getAddressesEntity() {
		List<AddressDto> addresses = getAddressesDto();

		Type listType = new TypeToken<List<AddressEntity>>() {
		}.getType();

		return new ModelMapper().map(addresses, listType);
	}

}
