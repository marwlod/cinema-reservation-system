package io.github.kkw.api.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class StringToInstantConverter implements Converter<String, Instant> {
    @Override
    public Instant convert(String source) {
        if (source.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$")) {
            return Instant.parse(source + "T00:00:00Z");
        } else {
            return Instant.parse(source);
        }
    }
}
