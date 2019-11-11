package com.lucas.spring.service;

import java.util.List;

import com.lucas.spring.shared.dto.AddressDto;

public interface AddressService {

	List<AddressDto> getAddresses(String userId);

	AddressDto getAddress(String addressId);
	
}
