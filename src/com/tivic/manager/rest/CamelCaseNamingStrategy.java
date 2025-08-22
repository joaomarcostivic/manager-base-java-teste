package com.tivic.manager.rest;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;


@SuppressWarnings("serial")
public class CamelCaseNamingStrategy extends PropertyNamingStrategy {
    @Override
    public String nameForField(@SuppressWarnings("rawtypes") MapperConfig config, AnnotatedField field, String defaultName) {
        return convert(defaultName);
    }

    @Override
    public String nameForGetterMethod(@SuppressWarnings("rawtypes") MapperConfig config, AnnotatedMethod method, String defaultName) {
        return convert(defaultName);
    }

    @Override
    public String nameForSetterMethod(@SuppressWarnings("rawtypes") MapperConfig config, AnnotatedMethod method, String defaultName) {
        return convert(defaultName);
    }

    public String convert(String defaultName) {
        char[] arr = defaultName.toCharArray();
        StringBuilder nameToReturn = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == '_') {
                nameToReturn.append(Character.toUpperCase(arr[i + 1]));
                i++;
            } else {
                nameToReturn.append(arr[i]);
            }
        }
        return nameToReturn.toString();
    }
}