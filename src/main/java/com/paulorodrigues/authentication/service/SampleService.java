package com.paulorodrigues.sampledemo.test.sample.service;

import com.paulorodrigues.sampledemo.test.sample.model.Sample;
import com.paulorodrigues.sampledemo.test.sample.model.SampleDTO;
import com.paulorodrigues.sampledemo.test.sample.model.SampleFilter;
import com.paulorodrigues.sampledemo.test.sample.repository.SampleRepository;
import com.paulorodrigues.sampledemo.test.util.MessageUtil;
import com.paulorodrigues.sampledemo.test.util.NotFoundException;
import com.paulorodrigues.sampledemo.test.util.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class SampleService {
    @Autowired
    private SampleRepository sampleRepository;
    
    public List<SampleDTO> findAll() {
        return samplesToDTOs(sampleRepository.findAll());
    }

    public Page<Sample> findByFilterPageable(SampleFilter filter, Pageable pageable) {
        return sampleRepository.findByFilterPageable(
                filter.getId(),
                filter.getName(),                
                filter.getStatus(),
                filter.getDescription(),
                filter.getStartDate(),
                filter.getEndDate(),
                pageable);
    }

    public Sample findById(Long sampleId) throws NotFoundException {
        log.info("Finding sample by sampleId={}", sampleId);
        return sampleRepository.findById(sampleId)
                .orElseThrow(
                        () -> new NotFoundException(MessageUtil.getMessage("SAMPLE_NOT_FOUND") + " ID: " + sampleId)
                );
    }

    public List<SampleDTO> findByName (String name) throws ValidationException {
        return samplesToDTOs(sampleRepository.findByName(name));
    }

    public SampleDTO create(Sample sample) throws ValidationException {
        assert sample != null : MessageUtil.getMessage("SAMPLE_IS_NULL");

        log.info("Creating sample name={}", sample.getName());
        return sampleToDTO(save(sample));
    }

    public Sample save(Sample sample) throws ValidationException {
        sample.validation();
        log.info("Saving sample={}", sample);
        return sampleRepository.saveAndFlush(sample);
    }

    public SampleDTO update(Long sampleId, SampleDTO sampleEdited) throws ValidationException, NotFoundException {
        Sample sampleToEdit = findById(sampleId);
        ModelMapper mapper = new ModelMapper();
        sampleToEdit = mapper.map(sampleEdited, Sample.class);
        log.info("Updating sample id={}, name={}", sampleId, sampleToEdit.getName());
        return sampleToDTO(save(sampleToEdit));
    }

    public void delete(Long sampleId) throws NotFoundException {
        Sample sample = findById(sampleId);
        log.info("Deleting sample id={}, name={}", sampleId, sample.getName());
        sampleRepository.delete(sample);
    }

    public Sample sampleFromDTO(SampleDTO sampleDTO) {
        if (Optional.ofNullable(sampleDTO).isEmpty()) {
            return null;
        }
        return Sample.builder()
                .id(sampleDTO.getId())
                .name(sampleDTO.getName())
                .issueDate(sampleDTO.getIssueDate())
                .status(sampleDTO.getStatus())
                .description(sampleDTO.getDescription())
                .build();
    }

    public SampleDTO sampleToDTO(Sample sample) {
        if (Optional.ofNullable(sample).isEmpty()) {
            return null;
        }
        return SampleDTO.builder()
                .id(sample.getId())
                .name(sample.getName())
                .issueDate(sample.getIssueDate())
                .status(sample.getStatus())
                .description(sample.getDescription())
                .build();
    }

    public List<SampleDTO> samplesToDTOs(List<Sample> samples) {
        return samples
                .stream()
                .map(this::sampleToDTO).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public List<Sample> samplesFromDTOs(List<SampleDTO> samplesDTO) {
        List<Sample> result = new ArrayList<>();
        if (!Optional.ofNullable(samplesDTO).isEmpty()) {
            for (SampleDTO sampleDTO : samplesDTO) {
                result.add(sampleFromDTO(sampleDTO));
            }
        }
        return result;
    }
}
