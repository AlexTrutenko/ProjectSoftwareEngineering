package alert_management;

import com.alerts.Alert;
import com.alerts.ECGDataAlert;
import com.dataManagement.PatientRecord;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class ECGDataAlertTest {
    private static PatientRecord rec(int id, double value, long ts) {
        return new PatientRecord(id, value, "ECG", ts);
    }

    // peak > THRESHOLD_FACTOR * average
    @Test
    void abnormalPeak_triggersAlert() {
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
                .contains("abnormally high ECG"));
    }

    //no peak values, so no alert
    @Test
    void normalSeries_noAlert() {
        ECGDataAlert trigger = new ECGDataAlert();

        List<PatientRecord> records = new ArrayList<>();
        for (int i = 0; i < 10; i++) {         // values 10 … 19
            records.add(rec(2, 10 + i, i));
        }

        List<Alert> alerts = trigger.evaluate(2, records);

        assertTrue(alerts.isEmpty());
    }

    //high values, but no alert
    @Test
    void fewerThanWindowSize_noAlert() {
        ECGDataAlert trigger = new ECGDataAlert();

        List<PatientRecord> records = List.of(
                rec(3, 50, 1),
                rec(3, 55, 2),
                rec(3, 60, 3),
                rec(3, 70, 4)    // big jump but only 4 samples
        );

        List<Alert> alerts = trigger.evaluate(3, records);

        assertTrue(alerts.isEmpty());
    }
}
