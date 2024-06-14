package com.hms.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.Timestamp;
import java.util.Date;

@ResponseStatus(value = HttpStatus.CONFLICT) // Used to set the status code of an HTTP response
@Getter // Generates Getters for all non-transient fields
@Setter // Generates Setters for all non-transient fields
@AllArgsConstructor // Generates a constructor with all fields as formal parameters
@NoArgsConstructor // Generates a constructor with no formal parameters
public class DuplicateResourceException extends RuntimeException {
    private Date timestamp;
    private String resourceName;
    private String fieldName;
    private String fieldValue;
    public DuplicateResourceException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("Resource '%s' with field:value '%s':'%d' already exists.", resourceName, fieldName, fieldValue));
        this.timestamp = new Timestamp(new Date().getTime());
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String generateErrorMessage() {
        return String.format("Resource '%s' with field:value '%s':'%d' already exists.", resourceName, fieldName, fieldValue);
    }
}
