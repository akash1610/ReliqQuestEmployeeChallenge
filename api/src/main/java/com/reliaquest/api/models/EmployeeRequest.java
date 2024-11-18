package com.reliaquest.api.models;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    @NotBlank(message = "Employee name must not be empty")
    private String name;

    @NotNull(message = "Employee salary must not be null")
    @Positive(message = "Employee Salary must be positive")
    private Integer salary;

    @NotBlank(message = "Employee title must not be empty")
    private String title;

    @NotNull(message = "Employee age must not be null")
    @Min(value = 16, message = "The minimum employee age should be at least 16")
    @Max(value = 75, message = "The maximum employee age should be at most 75")
    private Integer age;
}
