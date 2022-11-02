package com.increff.assure.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@ComponentScan("com.increff.assure")
@PropertySources({ //
        @PropertySource(value = "file:src/main/resources/assure.properties", ignoreResourceNotFound = true) //
})
public class SpringConfig {


}
