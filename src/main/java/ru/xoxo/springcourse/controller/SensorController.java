package ru.xoxo.springcourse.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.xoxo.springcourse.dto.SensorDTO;
import ru.xoxo.springcourse.model.Sensor;
import ru.xoxo.springcourse.service.SensorService;
import ru.xoxo.springcourse.utill.Errors;
import ru.xoxo.springcourse.utill.SensorErrorResponse;
import ru.xoxo.springcourse.utill.SensorNotCreatedException;
import ru.xoxo.springcourse.utill.SensorValidator;



@RestController
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService sensorService;
    private final ModelMapper modelMapper;
    private final SensorValidator sensorValidator;

    @Autowired
    public SensorController(SensorService sensorService, ModelMapper modelMapper, SensorValidator sensorValidator) {
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
        this.sensorValidator = sensorValidator;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> createSensor(@RequestBody @Valid SensorDTO sensorDTO, BindingResult result) {

        Sensor sensor = convertToSensor(sensorDTO);
        sensorValidator.validate(sensor, result);
        if(result.hasErrors()) {
            throw new SensorNotCreatedException(Errors.errorToClient(result));
        }
        sensorService.saveSensor(sensor);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorNotCreatedException exception) {
        SensorErrorResponse response
                = new SensorErrorResponse(exception.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }
}
