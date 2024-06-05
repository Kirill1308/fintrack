package com.popov.fintrack.utills;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.popov.fintrack.exception.InvalidCurrencyException;

import java.io.IOException;
import java.util.Currency;

public class CurrencyCodeDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String currencyCode = jsonParser.getText().toUpperCase();
        try {
            Currency.getInstance(currencyCode);
            return currencyCode;
        } catch (IllegalArgumentException e) {
            throw new InvalidCurrencyException("Invalid currency code: " + currencyCode);
        }
    }
}
