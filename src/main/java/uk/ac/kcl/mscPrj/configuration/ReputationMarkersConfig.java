package uk.ac.kcl.mscPrj.configuration;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "reputation-markers")
public class ReputationMarkersConfig {
    private Map<String, Integer> markers = new HashMap<>();
}