package data_management;

import com.dataManagement.Patient;
import com.dataManagement.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@code Pation}.
 *
 * This code verifies:
 *
 *   method getRecords works.
 *
 */

class PatientGetRecordsTest {

    private static final long T0 = 1_700_000_000_000L;
    private static final long T1 = T0 + 1_000;
    private static final long T2 = T0 + 2_000;

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient(77);
        patient.addRecord(10.0, "HeartRate",       T0);
        patient.addRecord(120.0, "SystolicPressure", T1);
        patient.addRecord(97.0, "OxygenSaturation",  T2);
    }

    //Method filters correctly and doesn’t include neighbours
    @Test
    void windowInMiddle_returnsSubset() {
        List<PatientRecord> recs = patient.getRecords(T0 + 500, T1 + 500);
        assertEquals(1, recs.size());
        assertEquals(T1, recs.get(0).getTimestamp());
    }

    //Boundaries are inclusive at the start.
    @Test
    void startBoundary_inclusive() {
        List<PatientRecord> recs = patient.getRecords(T0, T0);
        assertEquals(1, recs.size());
        assertEquals(T0, recs.get(0).getTimestamp());
    }

    //	All stored records are returned.
    @Test
    void fullRange_returnsAll() {
        List<PatientRecord> recs = patient.getRecords(T0, T2);
        assertEquals(3, recs.size());
    }

    //Returns an empty list, not null and no exception.
    @Test
    void noOverlap_returnsEmptyList() {
        List<PatientRecord> recs = patient.getRecords(T2 + 1_000, T2 + 2_000);
        assertTrue(recs.isEmpty());
    }

    //Returns an empty list, not null and no exception.
    @Test
    void endBeforeStart_returnsEmpty() {
        List<PatientRecord> recs = patient.getRecords(T2, T0);
        assertTrue(recs.isEmpty());
    }
}
