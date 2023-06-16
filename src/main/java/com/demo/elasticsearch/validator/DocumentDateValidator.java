package com.demo.elasticsearch.validator;

import com.demo.elasticsearch.dto.DocumentDto;
import com.demo.elasticsearch.metadata.DocumentDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DocumentDateValidator implements ConstraintValidator<DocumentDate, Date> {
    @Override
    public boolean isValid(Date date, ConstraintValidatorContext context) {
        return date.getYear()>2000;
    }
}
