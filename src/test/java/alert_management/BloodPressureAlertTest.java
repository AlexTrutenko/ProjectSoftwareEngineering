package alert_management;

import com.alerts.Alert;
import com.alerts.BloodPressureAlert;
import com.dataManagement.PatientRecord;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@code BloodPressureAlert}.
 *
 * This code verifies:
 *
 *   detection of an increasing blood‑pressure trend,
 *   detection of a decreasing trend, and
 *   alerts are triggered for reading above the threshold breaches for each blood‑pressure type
 *       (systolic and diastolic).
 *
 */

class BloodPressureAlertTest {

    private static PatientRecord rec(int id, double value, String type, long ts) {
        return new PatientRecord(id, value, type, ts);
    }

    //increasing trend for systolic
    @Test
    void systolic_increasingTrend() {
        BloodPressureAlert trigger = new BloodPressureAlert(true);   // systolic

        List<PatientRecord> records = List.of(
                rec(1, 100, "SystolicPressure", 1),
                rec(1, 112, "SystolicPressure", 2),
                rec(1, 125, "SystolicPressure", 3)
        );

        List<Alert> alerts = trigger.evaluate(1, records);

        assertTrue(alerts.get(0).getCondition().contains("increasing trend"));
    }

    //decreasing trend for systolic
    @Test
    void systolic_decreasingTrend() {
        BloodPressureAlert trigger = new BloodPressureAlert(true);   // systolic

        List<PatientRecord> records = List.of(
                rec(1, 160, "SystolicPressure", 1),
                rec(1, 145, "SystolicPressure", 2),
                rec(1, 129, "SystolicPressure", 3)
        );

        List<Alert> alerts = trigger.evaluate(1, records);

        assertTrue(alerts.get(0).getCondition().contains("decreasing trend"));
    }

    //incresing trend for diastolic
    @Test
    void diastolic_increasingTrend() {
        BloodPressureAlert trigger = new BloodPressureAlert(false);   // diastolic

        List<PatientRecord> records = List.of(
                rec(1, 80, "DiastolicPressure", 1),
                rec(1, 95, "DiastolicPressure", 2),
                rec(1, 110, "DiastolicPressure", 3)
        );

        List<Alert> alerts = trigger.evaluate(1, records);

        assertTrue(alerts.get(0).getCondition().contains("increasing trend"));
    }

    //decreasing trend for diastolic
    @Test
    void diastolic_decreasingTrend() {
        BloodPressureAlert trigger = new BloodPressureAlert(false);  // diastolic

        List<PatientRecord> records = List.of(
                rec(2, 110, "DiastolicPressure", 1),
                rec(2, 95,  "DiastolicPressure", 2),
                rec(2, 80,  "DiastolicPressure", 3)
        );

        List<Alert> alerts = trigger.evaluate(2, records);

        assertTrue(alerts.get(0).getCondition().contains("decreasing trend"));
    }

    //high thresholds for systolic
    @Test
    void systolic_above180() {
        BloodPressureAlert trigger = new BloodPressureAlert(true); // systolic

        List<Alert> alerts = trigger.evaluate(
                3,
                List.of(rec(3, 185, "SystolicPressure", 10))
        );

        assertTrue(alerts.get(0).getCondition()
                .contains("Critical threshold breached for Systolic"));
    }

    //low thresholds for systolic
    @Test
    void systolic_below90() {
        BloodPressureAlert trigger = new BloodPressureAlert(true); // systolic

        List<Alert> alerts = trigger.evaluate(4,
                List.of(rec(4, 85, "SystolicPressure", 10))
        );

        assertTrue(alerts.get(0).getCondition()
                .contains("Critical threshold breached for Systolic"));
    }

    //high thresholds for diastolic
    @Test
    void diastolic_above120() {
        BloodPressureAlert trigger = new BloodPressureAlert(false); // diastolic

        List<Alert> alerts = trigger.evaluate(
                5,
                List.of(rec(5, 130, "DiastolicPressure", 10))
        );

        assertTrue(alerts.get(0).getCondition()
                .contains("Critical threshold breached for Diastolic"));
    }

    //low thresholds for diastolic
    @Test
    void diastolic_below60() {
        BloodPressureAlert trigger = new BloodPressureAlert(false); // diastolic

        List<Alert> alerts = trigger.evaluate(
                6,
                List.of(rec(6, 55, "DiastolicPressure", 10))
        );

        assertTrue(alerts.get(0).getCondition()
                .contains("Critical threshold breached for Diastolic"));
    }
}
