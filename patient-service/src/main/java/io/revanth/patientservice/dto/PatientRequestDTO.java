package io.revanth.patientservice.dto;


import io.revanth.patientservice.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRequestDTO {
    @NotBlank(message="Name is required")
    @Size(max = 100, message="name cannot exceed 100 characters")
    private String name;

    @NotBlank(message="Email is required")
    @Email(message="Email should be valid")
    private String email;

    @NotBlank(message="Address is required")
    private String address;

    @NotBlank(groups = CreatePatientValidationGroup.class, message="Registered date is required")
    private String registeredDate;

    @NotNull(message="DateofBirth is required")
    private String dateOfBirth;

}
