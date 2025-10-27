package com.example.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WeatherConfig {

    @Value("${openweather.timeout.millis:3000}")
    private int timeoutMillis;

    @Bean
    public RestTemplate weatherRestTemplate() {
        var f = new SimpleClientHttpRequestFactory();
        f.setConnectTimeout(timeoutMillis);
        f.setReadTimeout(timeoutMillis);
        return new RestTemplate(f);
    }
}
