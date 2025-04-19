package alert_management;

import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.dataManagement.DataStorage;
import com.dataManagement.Patient;
import com.dataManagement.PatientRecord;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@code AlertGenerator}.
 *
 * This code verifies:
 *
 *   alerts are handled for each type of alert
 *   for the method evaluateData.
 *
 */

class AlertGeneratorEvaluateTest {

    private static PatientRecord rec(int id, double value, String type, long ts) {
        return new PatientRecord(id, value, type, ts);
    }

    //alert for systolic
    @Test
    void systolicAlert() {
        DataStorage storage = new DataStorage();
        long ts = System.currentTimeMillis() - 1_000;     // 1 s ago
        storage.addPatientData(1, 190, "SystolicPressure", ts);  // >180 mmHg

        AlertGenerator generator = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);

        generator.evaluateData(patient);

        List<Alert> alerts = generator.getGeneratedAlerts();
        assertEquals(1, alerts.size(), "Exactly one alert expected");
        assertTrue(alerts.get(0).getCondition()
                .contains("Critical threshold breached for Systolic"));
    }

    //no alerts
    @Test
    void normalData_generatesNoAlert() {
        DataStorage storage = new DataStorage();
        long ts = System.currentTimeMillis() - 1_000;

        // Normal systolic, normal oxygen saturation, normal ECG series
        storage.addPatientData(2, 120, "SystolicPressure", ts);
        storage.addPatientData(2, 97,  "OxygenSaturation", ts);

        // Fill five benign ECG points so ECGDataAlert has enough window
        for (int i = 0; i < 5; i++) {
            storage.addPatientData(2, 10 + i, "ECG", ts + i * 100);
        }

        AlertGenerator generator = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);

        generator.evaluateData(patient);

        assertTrue(generator.getGeneratedAlerts().isEmpty(),
                "No alerts should be generated for normal data");
    }

    //alert for saturation
    @Test
    void lowSaturationAlert() {
        DataStorage storage = new DataStorage();
        long t = System.currentTimeMillis() - 1_000;
        storage.addPatientData(10, 91, "OxygenSaturation", t);   // < 92 %

        AlertGenerator gen = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);
        gen.evaluateData(patient);

        assertTrue(gen.getGeneratedAlerts()
                .stream()
                .anyMatch(a -> a.getCondition().contains("Low Oxygen Saturation")));
    }

    //ecg alert
    @Test
    void ecgPeakAlert() {
        DataStorage storage = new DataStorage();
        long t0 = System.currentTimeMillis() - 10_000;

        // five baseline values
        storage.addPatientData(11, 10, "ECG", t0);
        storage.addPatientData(11, 11, "ECG", t0 + 100);
        storage.addPatientData(11, 12, "ECG", t0 + 200);
        storage.addPatientData(11, 13, "ECG", t0 + 300);
        storage.addPatientData(11, 11, "ECG", t0 + 400);

        // sixth value is 30, well above 1.5 × avg(~11.4)
        storage.addPatientData(11, 30, "ECG", t0 + 500);

        AlertGenerator gen = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);
        gen.evaluateData(patient);

        assertTrue(gen.getGeneratedAlerts()
                .stream()
                .anyMatch(a -> a.getCondition().contains("abnormally high ECG")));
    }

    //Hypotensive‑hypoxemia  alert
    @Test
    void hypotensiveHypoxemiaAlert() {
        DataStorage storage = new DataStorage();
        long t = System.currentTimeMillis() - 500;
        storage.addPatientData(12, 85, "SystolicPressure",  t);  // < 90
        storage.addPatientData(12, 91, "OxygenSaturation", t+1); // < 92

        AlertGenerator gen = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);
        gen.evaluateData(patient);

        assertTrue(gen.getGeneratedAlerts()
                .stream()
                .anyMatch(a -> a.getCondition().contains("Hypotensive Hypoxemia")));
    }

    //pressed button
    @Test
    void alertButtonPressedAlert() {
        DataStorage storage = new DataStorage();
        long t = System.currentTimeMillis() - 200;
        storage.addPatientData(13, 1.0, "AlertButton", t);   // 1.0 → pressed

        AlertGenerator gen = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);
        gen.evaluateData(patient);

        assertTrue(gen.getGeneratedAlerts()
                .stream()
                .anyMatch(a -> a.getCondition().contains("Alert button pressed")));
    }
}
