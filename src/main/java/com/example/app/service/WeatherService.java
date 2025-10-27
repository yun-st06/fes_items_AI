// src/main/java/com/example/app/service/WeatherService.java
package com.example.app.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;

    @Value("${openweather.api.key}")
    private String apiKey;
    @Value("${openweather.api.base-url}")
    private String baseUrl;
    @Value("${openweather.api.lang:ja}")
    private String lang;
    @Value("${openweather.api.units:metric}")
    private String units;

    public WeatherService(RestTemplate weatherRestTemplate) {
        this.restTemplate = weatherRestTemplate;
    }

    public WeatherResult getCurrentByLatLon(double lat, double lon) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/weather")
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("appid", apiKey)
                .queryParam("lang", lang)
                .queryParam("units", units)
                .build(true).toUri();
        try {
            ResponseEntity<OpenWeatherResponse> res =
                    restTemplate.getForEntity(uri, OpenWeatherResponse.class);
            var body = res.getBody();
            if (body == null || body.main == null || body.weather == null || body.weather.length == 0) {
                return WeatherResult.error("天気情報を取得できませんでした。");
            }
            var w0 = body.weather[0];
            return WeatherResult.ok(new WeatherInfo(
                    body.name != null ? body.name : "開催地",
                    w0.description != null ? w0.description : "天気不明",
                    w0.icon != null ? w0.icon : "01d",
                    body.main.temp, body.main.feels_like, body.main.humidity,
                    body.wind != null ? body.wind.speed : 0.0
            ));
        } catch (RestClientException e) {
            return WeatherResult.error("天気情報の取得に失敗しました。通信環境をご確認ください。");
        }
    }

    /* ====== DTOs ====== */
    public static class OpenWeatherResponse {
        public Weather[] weather;
        public Main main;
        public Wind wind;
        public String name;

        public static class Weather { public String main; public String description; public String icon; }
        public static class Main { public double temp; public double feels_like; public int humidity; }
        public static class Wind { public double speed; }
    }

    public static class WeatherInfo {
        public final String city; public final String description; public final String icon;
        public final double temp; public final double feelsLike; public final int humidity; public final double windSpeed;
        public WeatherInfo(String city, String description, String icon,
                           double temp, double feelsLike, int humidity, double windSpeed) {
            this.city = city; this.description = description; this.icon = icon;
            this.temp = temp; this.feelsLike = feelsLike; this.humidity = humidity; this.windSpeed = windSpeed;
        }
    }

    public static class WeatherResult {
        public final WeatherInfo info; public final String error;
        private WeatherResult(WeatherInfo info, String error){ this.info=info; this.error=error; }
        public static WeatherResult ok(WeatherInfo i){ return new WeatherResult(i,null); }
        public static WeatherResult error(String m){ return new WeatherResult(null,m); }
        public boolean hasInfo(){ return info!=null; }
        public boolean hasError(){ return error!=null && !error.isBlank(); }
    }
}

