package com.paulorodrigues.authentication.person.controller;


import com.paulorodrigues.authentication.exception.InvalidRequestException;
import com.paulorodrigues.authentication.exception.NotFoundException;
import com.paulorodrigues.authentication.exception.ValidationException;
import com.paulorodrigues.authentication.person.model.Person;
import com.paulorodrigues.authentication.person.model.PersonDTO;
import com.paulorodrigues.authentication.person.model.PersonRequest;
import com.paulorodrigues.authentication.person.model.PersonResponse;
import com.paulorodrigues.authentication.person.service.PersonService;
import com.paulorodrigues.authentication.util.ConstantsUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping(ConstantsUtil.PEOPLE_V1_BASE_API)
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping(ConstantsUtil.FIND_ALL_PATH)
    public ResponseEntity<List<PersonDTO>> findAll() {
        try {
            List<PersonDTO> peopleSorted = personService.findAll()
                    .stream()
                    .sorted(Comparator.comparing(PersonDTO::getFirstName))
                    .toList();
            return ResponseEntity.ok().body(peopleSorted);
        } catch (Exception e) {
            log.error("Exception on getAll message={}", e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    @PostMapping(ConstantsUtil.FIND_PAGEABLE_PATH)
    public ResponseEntity<PersonResponse> findByQueryPageable(@RequestBody PersonRequest personRequest) {
        try {
            return ResponseEntity.ok().body(personService.findByQueryPageable(personRequest));
        } catch (Exception e) {
            log.error("Exception on findPageable message={}", e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    @GetMapping(ConstantsUtil.FIND_BY_ID_PATH)
    public ResponseEntity<Person> findById(@PathVariable(value = "id") Long personId) throws NotFoundException {
        try {
            return ResponseEntity.ok().body(personService.findById(personId));
        } catch (Exception e) {
            log.error("Exception on getById personId={}, message={}", personId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    @PostMapping(ConstantsUtil.FIND_BY_NAME_PAGEABLE_PATH)
    public ResponseEntity<PersonResponse> findByName(@RequestBody PersonRequest personRequest) throws ValidationException {
        try {
            return ResponseEntity.ok().body(personService.findByName(personRequest));
        } catch (Exception e) {
            log.error("Exception on getByName personName={}, message={}", personRequest.getQuery().getName(), e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    @PostMapping()
    public ResponseEntity<PersonDTO> create(@RequestBody Person person) throws ValidationException, InvalidRequestException {
        try {
            return new ResponseEntity<>(personService.create(person), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Exception on create person={}, message={}", person, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    @PutMapping(ConstantsUtil.UPDATE_PATH)
    public ResponseEntity<PersonDTO> update(@PathVariable(value = "id") Long personId, @RequestBody PersonDTO personDTO) throws NotFoundException, ValidationException, InvalidRequestException {
        try {
            return ResponseEntity.ok().body(personService.update(personId, personDTO));
        } catch (Exception e) {
            log.error("Exception on getByName personId={}, message={}", personId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    @DeleteMapping(ConstantsUtil.DELETE_PATH)
    public Map<String, Long> delete(@PathVariable(value = "id") Long personId) throws NotFoundException {
        try {
            personService.delete(personId);
            Map<String, Long> response = new HashMap<>();
            response.put("id", personId);
            return response;
        } catch (Exception e) {
            log.error("Exception on getByName personId={}, message={}", personId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }
}
