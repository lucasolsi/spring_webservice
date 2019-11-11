package com.lucas.spring.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucas.spring.entity.AddressEntity;
import com.lucas.spring.entity.UserEntity;
import com.lucas.spring.repositories.AddressRepository;
import com.lucas.spring.repositories.UserRepository;
import com.lucas.spring.service.AddressService;
import com.lucas.spring.shared.dto.AddressDto;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	AddressRepository addressRepo;

	@Override
	public List<AddressDto> getAddresses(String userId) {
		List<AddressDto> returnValue = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = userRepo.findByUserId(userId);
		Iterable<AddressEntity> addresses = addressRepo.findAllByUserDetails(userEntity);

		for (AddressEntity addressEntity : addresses) {
			returnValue.add(modelMapper.map(addressEntity, AddressDto.class));
		}

		return returnValue;
	}

	@Override
	public AddressDto getAddress(String addressId) {
		AddressDto returnValue = null;

		AddressEntity addressEntity = addressRepo.findByAddressId(addressId);
		if (addressEntity != null) {
			returnValue = new ModelMapper().map(addressEntity, AddressDto.class);
		}
		return returnValue;
	}

}
