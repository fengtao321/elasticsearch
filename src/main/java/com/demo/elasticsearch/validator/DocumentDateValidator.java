package com.demo.elasticsearch.validator;

import com.demo.elasticsearch.dto.DocumentDto;
import com.demo.elasticsearch.metadata.DocumentDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DocumentDateValidator implements ConstraintValidator<DocumentDate, String> {
    @Override
    public boolean isValid(String dateStr, ConstraintValidatorContext context) {
        DocumentDto.dateFormat.setLenient(false);
        try {
            DocumentDto.dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
