package com.tivic.manager.triagem.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class CalendarSerializerWithoutTimezone extends JsonSerializer<GregorianCalendar> {

    @Override
    public void serialize(GregorianCalendar value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = sdf.format(value.getTime());
        gen.writeString(formattedDate);
    }
}
