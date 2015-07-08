package com.webQ.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.webQ.service"})
public class SpringRootConfig {
}