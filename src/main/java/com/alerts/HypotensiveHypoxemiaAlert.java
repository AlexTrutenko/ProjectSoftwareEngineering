package com.alerts;

import com.dataManagement.PatientRecord;
import java.util.List;
import java.util.ArrayList;
/**
 * Checks for hypotensive hypoxemia by evaluating the latest systolic blood pressure
 * and oxygen saturation records for a patient.
 *
 * Only the most recent {@code "SystolicPressure"} and {@code "OxygenSaturation"} records
 * are considered. If the systolic pressure falls below 90 mmHg and the oxygen saturation
 * falls below 92 %, an alert is generated.
 *
 *
 *   Systolic pressure threshold: 90 mmHg
 *   Oxygen saturation threshold: 92 %
 *
 */
public class HypotensiveHypoxemiaAlert implements AlertTriggerCondition {

    /**
     * Evaluates a list of patient records to detect hypotensive hypoxemia.
     *
     * Iterates through all provided {@link PatientRecord}s and captures the
     * most recent values for record types {@code "SystolicPressure"} and
     * {@code "OxygenSaturation"}. If both values breach their respective
     * thresholds (systolic &lt; 90 mmHg and saturation &lt; 92 %), an
     * {@link Alert} is created with the timestamp of the latest reading.
     *
     *
     * @param patientId the unique identifier of the patient being evaluated
     * @param records   the complete list of {@link PatientRecord}s for this patient;
     *                  non-pressure and non-saturation records are ignored
     * @return a {@link List} of {@link Alert}s if hypotensive hypoxemia is detected;
     *         otherwise, an empty list
     */

    @Override
    public List<Alert> evaluate(int patientId, List<PatientRecord> records) {
        double systolicPressure = 0.0;
        double saturation = 0.0;
        long timeStamp = 0;
        for (PatientRecord r : records) {
            if ("SystolicPressure".equals(r.getRecordType())) {
                systolicPressure = r.getMeasurementValue();
                timeStamp = r.getTimestamp();
            }
            if ("OxygenSaturation".equals(r.getRecordType())) {
                saturation = r.getMeasurementValue();
                timeStamp = r.getTimestamp();
            }
        }
        List<Alert> alerts = new ArrayList<>();
        if (systolicPressure < 90 && saturation < 92) {
            String cond = "Hypotensive Hypoxemia: systolic=" + systolicPressure
                    + " mmHg, saturation=" + saturation + "%";
            alerts.add(new Alert(String.valueOf(patientId), cond, timeStamp));
        }
        return alerts;
    }
}
