package com.popov.fintrack.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("app")
@Validated
@Getter
@Setter
public class AppProperties {

    @NonNull
    private String hostUrl;
}
