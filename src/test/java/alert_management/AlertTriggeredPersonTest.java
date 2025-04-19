package alert_management;

import com.alerts.Alert;
import com.alerts.AlertTriggeredPerson;
import com.dataManagement.PatientRecord;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class AlertTriggeredPersonTest {
    private static PatientRecord rec(int id, double value, long ts) {
        return new PatientRecord(id, value, "AlertButton", ts);
    }

    // button pressed
    @Test
    void pressed_triggersPressedAlert() {
        AlertTriggeredPerson trigger = new AlertTriggeredPerson();

        List<Alert> alerts = trigger.evaluate(
                1,
                List.of(rec(1, 1.0, 100))
        );

        assertEquals(1, alerts.size());
        assertTrue(alerts.get(0).getCondition().contains("pressed"));
    }

    //button is not pressed
    @Test
    void released_triggersReleasedAlert() {
        AlertTriggeredPerson trigger = new AlertTriggeredPerson();

        List<Alert> alerts = trigger.evaluate(
                2,
                List.of(rec(2, 0.0, 200))
        );

        assertEquals(1, alerts.size());
        assertTrue(alerts.get(0).getCondition().contains("released"));
    }

    //if record type is not 1 or 0
    @Test
    void nonButtonRecords_noAlert() {
        AlertTriggeredPerson trigger = new AlertTriggeredPerson();

        // Record type is something unrelated
        PatientRecord other = new PatientRecord(3, 90, "HeartRate", 300);

        List<Alert> alerts = trigger.evaluate(3, List.of(other));

        assertTrue(alerts.isEmpty());
    }

    //checks multiple actions
    @Test
    void multipleEvents_produceMultipleAlerts() {
        AlertTriggeredPerson trigger = new AlertTriggeredPerson();

        List<PatientRecord> series = List.of(
                rec(4, 1.0, 10),   // pressed
                rec(4, 0.0, 20),   // released
                rec(4, 1.0, 30)    // pressed again
        );

        List<Alert> alerts = trigger.evaluate(4, series);

        assertEquals(3, alerts.size());
        assertEquals("Alert button pressed",  alerts.get(0).getCondition());
        assertEquals("Alert button released", alerts.get(1).getCondition());
        assertEquals("Alert button pressed",  alerts.get(2).getCondition());
    }
}
