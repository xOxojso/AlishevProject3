package ru.xoxo.springcourse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.xoxo.springcourse.model.Measurement;
import ru.xoxo.springcourse.model.Sensor;
import ru.xoxo.springcourse.repository.MeasurementRepository;
import ru.xoxo.springcourse.utill.SensorNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MeasurementService {
    private final MeasurementRepository measurementRepository;
    private final SensorService sensorService;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, SensorService sensorService) {
        this.measurementRepository = measurementRepository;
        this.sensorService = sensorService;
    }

    @Transactional
    public void saveMeasurement(Measurement measurement) {
        enrichMeasurement(measurement);
        Optional<Sensor> optionalSensor = sensorService.findByName(measurement.getSensor().getName());
        if(optionalSensor.isPresent()) {
            measurement.setSensor(optionalSensor.get());
            measurementRepository.save(measurement);
        } else {
            throw new SensorNotFoundException("Sensor with that name not found");
        }
    }

    public List<Measurement> getAllMeasurements() {
        return measurementRepository.findAll();
    }

    public Long getRainyDays() {
        return getAllMeasurements().stream().filter(Measurement::isRaining).count();
    }

    private void enrichMeasurement(Measurement measurement) {
        measurement.setMeasurementDateTime(LocalDateTime.now());
    }
}
