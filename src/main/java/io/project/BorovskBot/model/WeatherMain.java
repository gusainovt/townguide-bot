package io.project.BorovskBot.model;

import java.math.BigDecimal;

public class WeatherMain {
    private BigDecimal temp;
    private BigDecimal humidity;

    private BigDecimal feels_like;


    public WeatherMain() {
    }

    public BigDecimal getTemp() {
        return temp;
    }

    public BigDecimal getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(BigDecimal feels_like) {
        this.feels_like = feels_like;
    }

    public void setTemp(BigDecimal temp) {
        this.temp = temp;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return "WeatherMain{" +
                "temp=" + temp +
                ", humidity=" + humidity +
                '}';
    }
}
