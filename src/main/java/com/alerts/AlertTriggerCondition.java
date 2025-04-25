package com.alerts;

import com.dataManagement.PatientRecord;
import java.util.List;

/**
 * Contract for any alert‐generating condition. Implementations of this
 * interface define specific health or event criteria to evaluate
 * a patient’s records and produce alerts.
 *
 * This design adheres to the SOLID principles:
 *
 *   (Single Responsibility): Each implementation
 *       focuses on a single alert condition.
 *   (Open/Closed): New alert types can be added
 *       without modifying existing code, by implementing this interface.
 *   (Liskov Substitution):All implementations
 *       can be used interchangeably where {@code AlertTriggerCondition}
 *       is expected.
 *   (Interface Segregation): Clients depend only on
 *       this minimal interface rather than large, monolithic classes.
 *   (Dependency Inversion):High-level modules
 *       (e.g., {@code AlertGenerator}) depend on this abstraction,
 *       not on concrete implementations.

 */

public interface AlertTriggerCondition {
    List<Alert> evaluate(int patientId, List<PatientRecord> records);
}


