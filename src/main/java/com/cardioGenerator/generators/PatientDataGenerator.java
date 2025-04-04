package com.cardioGenerator.generators;

import com.cardioGenerator.outputs.OutputStrategy;

public interface PatientDataGenerator {
    void generate(int patientId, OutputStrategy outputStrategy);
}
