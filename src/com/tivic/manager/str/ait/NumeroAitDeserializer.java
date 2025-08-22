package com.tivic.manager.str.ait;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class NumeroAitDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser jasonParser, DeserializationContext context) throws IOException {
        String rawValue = jasonParser.getValueAsString();
        return Integer.valueOf(rawValue.replaceAll("[^\\d.]", ""));
    }
}
