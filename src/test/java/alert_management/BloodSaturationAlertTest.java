package alert_management;

import com.alerts.Alert;
import com.alerts.BloodSaturationAlert;
import com.dataManagement.PatientRecord;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@code BloodSaturationAlert}.
 *
 * This code verifies:
 *
 *   an alert is triggered when saturation falls below 92%,
 *   an alert is triggered when there is a drop of 5%
 *   or more within 10 minutes interval.
 *
 */

public class BloodSaturationAlertTest {
    private static PatientRecord rec(int id, double satPercent, long timestamp) {
        return new PatientRecord(id, satPercent, "OxygenSaturation", timestamp);
    }

    //Test for Low Saturation
    @Test
    void lowSaturation_triggersAlert() {

        BloodSaturationAlert trigger = new BloodSaturationAlert();

        List<PatientRecord> records = List.of(
                rec(1, 95, 0),      // it should be fine
                rec(1, 30, 1)       // below threshold
        );
        List<Alert> alerts = trigger.evaluate(1, records);

        assertTrue(alerts.get(0).getCondition()
                .contains("Low Oxygen Saturation:"));
    }

    //Test for Rapid Drop:
    @Test
    void rapidDropWithinTenMinutes_triggersAlert() {

        BloodSaturationAlert trigger = new BloodSaturationAlert();

        long t0 = 0;
        long t1 = 7 * 60 * 1000;   // 7 minutes later (within 10‑minute window)

        List<PatientRecord> records = List.of(
                rec(2, 100, t0),
                rec(2, 93, t1)
        );
        List<Alert> alerts = trigger.evaluate(2, records);


        assertTrue(alerts.get(0).getCondition()
                .contains("Rapid Oxygen Saturation Drop"));
    }
}
