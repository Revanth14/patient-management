package io.revanth.patientservice.service;

import io.revanth.patientservice.dto.PatientRequestDTO;
import io.revanth.patientservice.dto.PatientResponseDTO;
import io.revanth.patientservice.exceptions.EmailAlreadyExistsException;
import io.revanth.patientservice.mapper.PatientMapper;
import io.revanth.patientservice.model.Patient;
import io.revanth.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService  {
    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream().map(
                PatientMapper::toPatientResponseDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A Patient with this email exists" + patientRequestDTO.getEmail());
        }
        Patient newPatient = patientRepository.save(
                PatientMapper.toModel(patientRequestDTO)
        );
        return PatientMapper.toPatientResponseDTO(newPatient);
    }

}
