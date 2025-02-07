package com.example.demo.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String field;
    private String fieldMatch;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        this.field = constraintAnnotation.first();
        this.fieldMatch = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        Object field = new BeanWrapperImpl(value).getPropertyValue(this.field);
        Object fieldMatch = new BeanWrapperImpl(value).getPropertyValue(this.fieldMatch);
        return Objects.equals(field, fieldMatch);
    }
}
