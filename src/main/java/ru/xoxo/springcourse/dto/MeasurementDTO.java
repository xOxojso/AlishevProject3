package ru.xoxo.springcourse.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public class MeasurementDTO {
    @Column(name = "value")
    @NotEmpty
    @Min(value = -100, message = "min value: -100")
    @Max(value = 100, message = "max value: 100")
    private double value;
    @NotEmpty
    @Column(name = "raining")
    private boolean raining;
    @NotEmpty
    @ManyToOne
    @JoinColumn(name = "sensor_id", referencedColumnName = "id")
    private SensorDTO sensor;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isRaining() {
        return raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }
}
