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
package com.paulorodrigues.authentication.address.controller;


import com.paulorodrigues.authentication.address.model.*;
import com.paulorodrigues.authentication.address.service.AddressService;
import com.paulorodrigues.authentication.exception.InvalidRequestException;
import com.paulorodrigues.authentication.exception.NotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.paulorodrigues.authentication.util.ConstantsUtil.*;

/**
 *
 * @author paulo.rodrigues
 */
@Log4j2
@RestController
@CrossOrigin(origins = {"*"})
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ok success"),
        @ApiResponse(responseCode = "201", description = "Address created"),
        @ApiResponse(responseCode = "400", description = "Validation Error Response"),
        @ApiResponse(responseCode = "401", description = "Full Authentication Required or Invalid access token"),
        @ApiResponse(responseCode = "403", description = "Insufficient scope"),
        @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "500", description = "Something Unexpected Happened"),})
@RequestMapping(ADDRESSES_V1_BASE_API)
public class AddressController {

    @Autowired
    private AddressService addressService;

    @ApiOperation(value = "Get the address by id",
            notes = "It returns the address given an Id")
    @GetMapping(FIND_BY_ID_PATH)
    public ResponseEntity<Address> getById(@PathVariable(value = "id") Long addressId) throws NotFoundException {
        try {
            return ResponseEntity.ok().body(addressService.findById(addressId));
        } catch (Exception e) {
            log.error("Exception on getById addressId={}, message={}", addressId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    @GetMapping(FIND_BY_NAME_PATH)
    public ResponseEntity<List<Address>> getByName(@PathVariable(value = "name") String addressName) {
        try {
            return ResponseEntity.ok().body(addressService.findByName(addressName));
        } catch (Exception e) {
            log.error("Exception on getByName addressName={}, message={}", addressName, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    @PostMapping()
    public ResponseEntity<AddressDTO> create(@RequestBody Address address) throws InvalidRequestException {
        try {
            return new ResponseEntity<>(addressService.create(address), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Exception on create address={}, message={}", address, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    @PutMapping(UPDATE_PATH)
    public ResponseEntity<AddressDTO> update(@PathVariable(value = "id") Long addressId, @RequestBody AddressDTO addressDTO) throws InvalidRequestException, NotFoundException {
        try {
            return ResponseEntity.ok(addressService.edit(addressId, addressDTO));
        } catch (Exception e) {
            log.error("Exception on update addressId={}, message={}", addressId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    @DeleteMapping(DELETE_PATH)
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long addressId) throws NotFoundException {
        try {
            addressService.delete(addressId);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;
        } catch (Exception e) {
            log.error("Exception on delete addressId={}, message={}", addressId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }
    
    @GetMapping(GET_LOGRADOUROS)
    public ResponseEntity<List<Map<String, String>>> getLogradouros() {
        try {
            return ResponseEntity.ok().body(addressService.getLogradouros());
        } catch (Exception e) {
            log.error("Exception on getETypePublicPlace message={}", e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }
    
    @GetMapping(GET_CITIES_PATH)
    public ResponseEntity<List<City>> getAllCities(@PathVariable(value = "country") Long countryId, @PathVariable(value = "state") Long stateId) {
        try {
            return ResponseEntity.ok().body(addressService.getAllCities(countryId, stateId));
        } catch (Exception e) {
            log.error("Exception on getAllCities countryId={}, stateId={}, message={}", countryId, stateId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }
    
    @GetMapping(GET_STATES_PATH)
    public ResponseEntity<List<StateCountry>> getAllStates(@PathVariable(value = "country") Long countryId) {
        try {
            return ResponseEntity.ok().body(addressService.getAllStates(countryId));
        } catch (Exception e) {
            log.error("Exception on getAllStates countryId={}, message={}", countryId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }
    
    @GetMapping(GET_COUNTRIES_PATH)
    public ResponseEntity<List<Country>> getAllCountries() {
        try {
            return ResponseEntity.ok().body(addressService.getAllCountries());
        } catch (Exception e) {
            log.error("Exception on getAllCountries message={}", e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }
}
