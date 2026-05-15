package com.seanergy.oa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "file-watcher")
@Getter
@Setter
public class FileWatcherConfig {

    private String watchDir;
    private String[] supportedExtensions = {"pdf", "jpg", "jpeg", "png"};
}
