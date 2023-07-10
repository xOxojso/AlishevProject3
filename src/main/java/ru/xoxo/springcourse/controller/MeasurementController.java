package ru.xoxo.springcourse.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.xoxo.springcourse.dto.MeasurementDTO;
import ru.xoxo.springcourse.model.Measurement;
import ru.xoxo.springcourse.service.MeasurementService;
import ru.xoxo.springcourse.utill.Errors;
import ru.xoxo.springcourse.utill.MeasurementErrorResponse;
import ru.xoxo.springcourse.utill.MeasurementNotCreatedException;
import ru.xoxo.springcourse.utill.MeasurementValidator;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {
    private final MeasurementService measurementService;
    private final MeasurementValidator measurementValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementController(MeasurementService measurementService, MeasurementValidator measurementValidator, ModelMapper modelMapper) {
        this.measurementService = measurementService;
        this.measurementValidator = measurementValidator;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> saveMeasurement(@RequestBody @Valid MeasurementDTO measurementsDTO,
                                                      BindingResult result) {
        Measurement measurement = convertToMeasurement(measurementsDTO);
        measurementValidator.validate(measurement, result);
        if(result.hasErrors()) {
            throw new MeasurementNotCreatedException(Errors.errorToClient(result));
        }
        measurementService.saveMeasurement(measurement);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping()
    public List<MeasurementDTO> getAllMeasurement() {
        return measurementService.getAllMeasurements().stream()
                .map(this::convertToMeasurementDTO).collect(Collectors.toList());
    }

    @GetMapping("/rainyDaysCount")
    public Long getRainyDays() {
        return measurementService.getRainyDays();
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementNotCreatedException exception) {
        MeasurementErrorResponse response
                = new MeasurementErrorResponse(exception.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Measurement convertToMeasurement(MeasurementDTO MeasurementsDTO) {
        return modelMapper.map(MeasurementsDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }
}
