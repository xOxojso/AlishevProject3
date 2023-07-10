package ru.xoxo.springcourse.utill;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class Errors {
    public static String errorToClient(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        fieldErrors.forEach(error -> errorMessage.append(error.getField())
                .append(" - ")
                .append(error.getDefaultMessage())
                .append(";"));
        return errorMessage.toString();
    }
}
