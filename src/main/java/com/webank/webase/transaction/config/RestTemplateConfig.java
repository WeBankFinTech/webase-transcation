/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.webank.webase.transaction.config;

import java.util.concurrent.TimeUnit;
import lombok.Data;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Restful request template configuration
 */
@Data
@Configuration
public class RestTemplateConfig {
    /**
     * new RestTemplate.
     * 
     * @param factory object
     * @return
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    /**
     * init httpRequestFactory.
     * 
     * @return
     */
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        PoolingHttpClientConnectionManager pollingConnectionManager =
                new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS);
        // max connection
        pollingConnectionManager.setMaxTotal(1000);
        pollingConnectionManager.setDefaultMaxPerRoute(100);
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(pollingConnectionManager);
        // add Keep-Alive
        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
        HttpClient httpClient = httpClientBuilder.build();
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        clientHttpRequestFactory.setReadTimeout(100000);
        clientHttpRequestFactory.setConnectTimeout(100000);
        return clientHttpRequestFactory;
    }
}
