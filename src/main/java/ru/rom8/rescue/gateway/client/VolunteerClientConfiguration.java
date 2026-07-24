package ru.rom8.rescue.gateway.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class VolunteerClientConfiguration {

    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    VolunteerClient volunteerClient(
            RestClient.Builder restClientBuilder,
            @Value("${clients.volunteer-service.base-url}") String baseUrl
    ) {
        RestClient restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();

        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return proxyFactory.createClient(VolunteerClient.class);
    }
}
