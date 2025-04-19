package alert_management;

import com.alerts.Alert;
import com.alerts.HypotensiveHypoxemiaAlert;
import com.dataManagement.PatientRecord;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@code HypotensiveHypoxemiaAlert}.
 *
 * This code verifies:
 *
 *   alerts are triggered when BOTH thresholds
 *   for Systolic blood pressure nad
 *   blood oxygen saturation are breached
 *
 */

public class HypotensiveHypoxemiaAlertTest {
    private static PatientRecord rec(int id, double value, String type, long ts) {
        return new PatientRecord(id, value, type, ts);
    }

    // both thresholds are breached
    @Test
    void bothValuesTriggered() {
        HypotensiveHypoxemiaAlert trigger = new HypotensiveHypoxemiaAlert();

        List<PatientRecord> records = List.of(
                rec(1, 85, "SystolicPressure", 1),
                rec(1, 91, "OxygenSaturation", 2)
        );

        List<Alert> alerts = trigger.evaluate(1, records);

        assertTrue(alerts.get(0).getCondition()
                .contains("Hypotensive Hypoxemia"));
    }

    // only systolic blood pressure threshold breached
    @Test
    void onlySystolicLow() {
        HypotensiveHypoxemiaAlert trigger = new HypotensiveHypoxemiaAlert();

        List<Alert> alerts = trigger.evaluate(
                2,
                List.of(
                        rec(2, 80, "SystolicPressure", 10),
                        rec(2, 95, "OxygenSaturation", 11)
                )
        );

        assertTrue(alerts.isEmpty());
    }

    // only blood oxygen saturation threshold breached
    @Test
    void onlySaturationLow() {
        HypotensiveHypoxemiaAlert trigger = new HypotensiveHypoxemiaAlert();

        List<Alert> alerts = trigger.evaluate(
                3,
                List.of(
                        rec(3, 110, "SystolicPressure", 20),
                        rec(3, 90,  "OxygenSaturation", 21)
                )
        );

        assertTrue(alerts.isEmpty());
    }

    // none of them is breached
    @Test
    void bothNormal() {
        HypotensiveHypoxemiaAlert trigger = new HypotensiveHypoxemiaAlert();

        List<Alert> alerts = trigger.evaluate(
                4,
                List.of(
                        rec(4, 120, "SystolicPressure", 30),
                        rec(4, 98,  "OxygenSaturation", 31)
                )
        );

        assertTrue(alerts.isEmpty());
    }
}
