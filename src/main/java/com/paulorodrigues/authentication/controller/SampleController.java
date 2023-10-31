package com.paulorodrigues.authentication.controller;


import com.paulorodrigues.authentication.exception.NotFoundException;
import com.paulorodrigues.authentication.exception.UnknownErrorException;
import com.paulorodrigues.authentication.exception.ValidationException;
import com.paulorodrigues.authentication.util.ConstantsUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping(ConstantsUtil.SAMPLE_V1_BASE_API)
public class SampleController {

    @Autowired
    private SampleService sampleService;

    @GetMapping(ConstantsUtil.FIND_ALL_PATH)
    public ResponseEntity<List<SampleDTO>> findAll() {
        try {
            List<SampleDTO> samples = sampleService.findAll();
            List<SampleDTO> samplesSorted = samples.stream().sorted(Comparator.comparing(SampleDTO::getName)).collect(Collectors.toList());
            return ResponseEntity.ok().body(samplesSorted);
        } catch (Exception e) {
            log.error("Exception on getAll message={}", e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    @PostMapping(ConstantsUtil.FIND_PAGEABLE_PATH)
    public ResponseEntity<Page<Sample>> findByFilterPageable(@RequestBody SampleFilter filter, HttpServletRequest req, HttpServletResponse res) {
        try {
            Pageable pageable = PageRequest.of(filter.getCurrentPage() > 0 ? filter.getCurrentPage() - 1 : 0, filter.getRowsPerPage(), Sort.by(filter.getSortColumn()));
            Page<Sample> samples = sampleService.findByFilterPageable(filter, pageable);

            return ResponseEntity.ok().body(samples);
        } catch (Exception e) {
            log.error("Exception on findPageable message={}", e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    @GetMapping(ConstantsUtil.FIND_BY_ID_PATH)
    public ResponseEntity<Sample> findById(@PathVariable(value = "id") Long sampleId) throws NotFoundException {
        try {
            return ResponseEntity.ok().body(sampleService.findById(sampleId));
        } catch (Exception e) {
            log.error("Exception on getById sampleId={}, message={}", sampleId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    @GetMapping(ConstantsUtil.FIND_BY_NAME_PATH)
    public ResponseEntity<List<SampleDTO>> findByName(@PathVariable(value = "name") String sampleName) throws UnknownErrorException, ValidationException {
        try {
            return ResponseEntity.ok().body(sampleService.findByName(sampleName));
        } catch (Exception e) {
            log.error("Exception on getByName sampleName={}, message={}", sampleName, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    @PostMapping()
    public ResponseEntity<SampleDTO> create(@RequestBody Sample sample) throws ValidationException {
        try {
            return new ResponseEntity<>(sampleService.create(sample), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Exception on create sample={}, message={}", sample, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    @PutMapping(ConstantsUtil.UPDATE_PATH)
    public ResponseEntity<SampleDTO> update(@PathVariable(value = "id") Long sampleId, @RequestBody SampleDTO sampleDTO) throws NotFoundException, ValidationException {
        try {
            return ResponseEntity.ok().body(sampleService.update(sampleId, sampleDTO));
        } catch (Exception e) {
            log.error("Exception on getByName sampleId={}, message={}", sampleId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    @DeleteMapping(ConstantsUtil.DELETE_PATH)
    public Map<String, Long> delete(@PathVariable(value = "id") Long sampleId) throws NotFoundException {
        try {
            sampleService.delete(sampleId);
            Map<String, Long> response = new HashMap<>();
            response.put("id", sampleId);
            return response;
        } catch (Exception e) {
            log.error("Exception on getByName sampleId={}, message={}", sampleId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }
}
