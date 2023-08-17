package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");

        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig  .custom()
                                                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                                                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                                                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                                                        .build())
                                                        .build();
             CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();
            String jsonString = EntityUtils.toString(entity);

            ObjectMapper mapper = new ObjectMapper();
            List<Fact> list = mapper.readValue(jsonString, new TypeReference<>() {});
            List<Fact> filteredList = list.stream()
                    .filter(value -> value.getUpvotes() != 0 && value.getUpvotes() > 0)
                    .toList();
            filteredList
                    .forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}