package com.lucas.spring.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import static org.mockito.ArgumentMatchers.*;

import com.lucas.spring.entity.AddressEntity;
import com.lucas.spring.model.response.UserRest;
import com.lucas.spring.service.impl.UserServiceImpl;
import com.lucas.spring.shared.dto.AddressDto;
import com.lucas.spring.shared.dto.UserDto;

class UserControllerTest {

	@InjectMocks
	UserController userController;

	@Mock
	UserServiceImpl userService;

	UserDto userDto;

	// User
	String firstName = "Teste";
	String lastName = "Unitário";
	final String userId = "i58rpthr4";
	String encryptedPassword = "1231ht5js";
	String userEmail = "teste@teste.com";
	String password = "senha";

	// Addresses
	String shippingType = "shipping";
	String billingType = "billing";
	String addressId = "12kdasjv50";
	String city = "Cidade";
	String country = "Brazil";
	String postalCode = "999991-911";
	String streetName = "Rua das Casas, 38";

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		userDto = new UserDto();
		userDto.setFirstName(firstName);
		userDto.setLastName(lastName);
		userDto.setEmail(userEmail);
		userDto.setEmailVerificationStatus(Boolean.FALSE);
		userDto.setEmailVerificationToken(null);
		userDto.setAddresses(getAddressesDto());
		userDto.setEncryptedPassword(encryptedPassword);
		userDto.setUserId(userId);
	}

	@Test
	final void testGetUser() {
		when(userService.getUserByUserId(anyString())).thenReturn(userDto);

		UserRest userRest = userController.getUser(userId);
		
		assertNotNull(userRest);
		assertEquals(userId, userRest.getUserId());
		assertEquals(userDto.getFirstName(), userRest.getFirstName());
		assertEquals(userDto.getLastName(), userRest.getLastName());
		assertTrue(userDto.getAddresses().size() == userRest.getAddresses().size());
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

	@SuppressWarnings("unused")
	private List<AddressEntity> getAddressesEntity() {
		List<AddressDto> addresses = getAddressesDto();

		Type listType = new TypeToken<List<AddressEntity>>() {
		}.getType();

		return new ModelMapper().map(addresses, listType);
	}

}
