/*
 * Copyright (C) 2021 paulo.rodrigues
 * Profile: <https://github.com/mrpaulo>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.paulorodrigues.authentication.address.service;


import com.paulorodrigues.authentication.address.model.*;
import com.paulorodrigues.authentication.address.repository.AddressRepository;
import com.paulorodrigues.authentication.address.repository.CityRepository;
import com.paulorodrigues.authentication.address.repository.CountryRepository;
import com.paulorodrigues.authentication.address.repository.StateCountryRepository;
import com.paulorodrigues.authentication.exception.InvalidRequestException;
import com.paulorodrigues.authentication.exception.NotFoundException;
import com.paulorodrigues.authentication.user.repository.UserRepository;
import com.paulorodrigues.authentication.util.MessageUtil;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author paulo.rodrigues
 */
@Service
@Transactional
@Log4j2
public class AddressService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;
    
    @Autowired
    private CityRepository cityRepository;
    
    @Autowired
    private StateCountryRepository stateCountryRepository;
    
    @Autowired
    private CountryRepository countryRepository;

    public Address findById(Long addressId) throws NotFoundException {
        /*
       Should be this, but I got a nullPointer on Address - findById - should throw an exception test
          return addressRepository.findById(addressId)
               .orElseThrow(
                        () -> new LibraryStoreBooksException(MessageUtil.getMessage("ADDRESS_NOT_FOUND") + " ID: " + addressId)
                );
         */
        log.info("Finding address by addressId={}", addressId);
        Optional<Address> address = addressRepository.findById(addressId);
        if (address == null || !address.isPresent()) {
            log.error("Address not found by addressId={}", addressId);
            throw new NotFoundException(MessageUtil.getMessage("ADDRESS_NOT_FOUND") + " ID: " + addressId);
        }
        return address.get();
    }

    public AddressDTO create(Address address) throws InvalidRequestException {
        log.info("Creating address name={}", address.getName());
        return toDTO(save(address));
    }

    public Address save(Address address) throws InvalidRequestException {
        address.addressValidation();
        address.persistAt();
        log.info("Saving address={}", address);
        return addressRepository.saveAndFlush(address);
    }

    public AddressDTO edit(Long addressId, AddressDTO addressDetail) throws InvalidRequestException, NotFoundException {
        Address addressToEdit = findById(addressId);
        String createBy = addressToEdit.getCreateBy();
        var createAt = addressToEdit.getCreateAt();
        ModelMapper modelMapper = new ModelMapper();
        addressToEdit = modelMapper.map(addressDetail, Address.class);
        addressToEdit.setCreateBy(createBy);
        addressToEdit.setCreateAt(createAt);
        addressToEdit.setId(addressId);
        log.info("Updating address id={}, name={}", addressId, addressToEdit.getName());
        return toDTO(save(addressToEdit));
    }

    public void delete(Long addressId) throws NotFoundException {
        Address addressToDelete = findById(addressId);
        log.info("Deleting address id={}, name={}", addressId, addressToDelete.getName());
        userRepository.deleteAddressReference(addressId);
        addressRepository.delete(addressToDelete);
    }

    public AddressDTO toDTO(Address address) {
        if (address == null) {
            return null;
        }
        return AddressDTO.builder()
                .id(address.getId())
                .fmtAddress(address.formatAddress())
                .build();
    }

    public Address getAddressFromDTO (AddressDTO dto) {
        try {
            return dto != null ? findById(dto.getId()) : null;
        } catch (Exception e) {
            log.error("Exception on getAddressFromDTO addressDTO={}, message={}", dto, e.getMessage());
            return null;
        }
    }

    public City getCityFromDTO (CityDTO dto) {
        try {
            if(dto == null){
                return null;
            }
            return Optional.of(cityRepository.findById(dto.getId())).get().orElse(null);
        } catch (Exception e) {
            log.error("Exception on getCityFromDTO CityDTO={}, message={}", dto, e.getMessage());
            return null;
        }
    }

    public Country getCountryFromDTO (CountryDTO dto) {
        try {
            if(dto == null){
                return null;
            }
            return Optional.of(countryRepository.findById(dto.getId())).get().orElse(null);
        } catch (Exception e) {
            log.error("Exception on getCountryFromDTO CountryDTO={}, message={}", dto, e.getMessage());
            return null;
        }
    }

    public List<Map<String, String>> getLogradouros() {
        return Stream.of(TipoLogradouro.values()).map(temp -> {
            Map<String, String> obj = new HashMap<>();
            obj.put("value", temp.getName());
            obj.put("label", temp.getDescription());
            return obj;
        }).collect(Collectors.toList());
    }

     public List<Country> getAllCountries(){
        return countryRepository.findAll();
    }
     
    public List<StateCountry> getAllStates(Long countryId){
        Optional<Country> country = countryRepository.findById(countryId);
        return country.map(value -> stateCountryRepository.findByCountry(value)).orElse(null);
    }
    
    public List<City> getAllCities(Long countryId, Long stateId){
        Country country = countryRepository.findById(countryId).orElse(null);
        StateCountry state = stateCountryRepository.findById(stateId).orElse(null);
        if(country == null || state == null){
            return null;
        }
        return cityRepository.findByCountryAndState(country, state);
    }

    public List<Address> findByName(String name) {
        return addressRepository.findByName(name);
    }
}
