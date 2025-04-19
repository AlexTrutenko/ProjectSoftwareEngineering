package alert_management;

import com.alerts.Alert;
import com.alerts.ECGDataAlert;
import com.dataManagement.PatientRecord;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@code ECGDataAlert}.
 *
 * This code verifies:
 *
 *   alerts are triggered when peaks above certain
 *   values happen,
 *   alerts are not triggered when
 *   values below certain.
 *
 */

public class ECGDataAlertTest {
    private static PatientRecord rec(int id, double value, long ts){
        return new PatientRecord(id, value, "ECG", ts);
    }

    // peak > THRESHOLD_FACTOR * average
    @Test
    void abnormalPeak() {
        ECGDataAlert trigger = new ECGDataAlert();

        List<PatientRecord> records = List.of(
                rec(1, 10, 1),
                rec(1, 11, 2),
                rec(1, 12, 3),
                rec(1, 13, 4),
                rec(1, 11, 5),
                rec(1, 30, 6)
        );

        List<Alert> alerts = trigger.evaluate(1, records);

        assertTrue(alerts.get(0).getCondition()
                .contains("Measured abnormally high ECG record!"));
    }

    //no peak values, so no alert
    @Test
    void normalSeries() {
        ECGDataAlert trigger = new ECGDataAlert();

        List<PatientRecord> records = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            records.add(rec(2, 10 + i, i));
        }

        List<Alert> alerts = trigger.evaluate(2, records);

        assertTrue(alerts.isEmpty());
    }

    //high values, but no alert
    @Test
    void relativelyLowValues() {
        ECGDataAlert trigger = new ECGDataAlert();

        List<PatientRecord> records = List.of(
                rec(3, 50, 1),
                rec(3, 55, 2),
                rec(3, 60, 3),
                rec(3, 70, 4)
        );

        List<Alert> alerts = trigger.evaluate(3, records);

        assertTrue(alerts.isEmpty());
    }
}
