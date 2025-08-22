package com.tivic.manager.triagem.serializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CalendarDeserializerWithoutTimezone extends JsonDeserializer<GregorianCalendar> {

    @Override
    public GregorianCalendar deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(sdf.parse(p.getText()));
            return calendar;
        } catch (ParseException e) {
            throw new IOException("Erro ao desserializar a data: " + p.getText(), e);
        }
    }
}
