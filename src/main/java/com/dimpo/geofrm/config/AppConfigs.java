package com.dimpo.geofrm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app-configs")
@Getter
@Setter
public class AppConfigs {

	private String outFilesLocation;

}
