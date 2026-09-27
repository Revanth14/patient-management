package io.revanth.patientservice.service;

import io.revanth.patientservice.dto.PatientRequestDTO;
import io.revanth.patientservice.dto.PatientResponseDTO;
import io.revanth.patientservice.exception.EmailAlreadyExistsException;
import io.revanth.patientservice.exception.PatientNotFoundException;
import io.revanth.patientservice.grpc.BillingServiceGrpcClient;
import io.revanth.patientservice.mapper.PatientMapper;
import io.revanth.patientservice.model.Patient;
import io.revanth.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    private final BillingServiceGrpcClient billingServiceGrpcClient;

    public PatientService(PatientRepository patientRepository,  BillingServiceGrpcClient billingServiceGrpcClient) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream()
                .map(PatientMapper::toDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Patient with email already exists"+patientRequestDTO.getEmail());
        }
        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));

        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString()
                ,newPatient.getName(), newPatient.getEmail());

        return PatientMapper.toDTO(newPatient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                ()-> new PatientNotFoundException("Patient not found with id: "+ id)
        );
        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),id)) {
            throw new EmailAlreadyExistsException("Patient with email already exists"+patientRequestDTO.getEmail());
        }

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);


    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }

}












