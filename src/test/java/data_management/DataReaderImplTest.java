package data_management;

import com.dataManagement.DataReaderImpl;
import com.dataManagement.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class DataReaderImplTest {


    private static final class RecordingStorage extends DataStorage {
        static final class Call {
            final int    patientId;
            final double value;
            final String type;
            final long   timestamp;

            Call(int patientId, double value, String type, long timestamp) {
                this.patientId = patientId;
                this.value     = value;
                this.type      = type;
                this.timestamp = timestamp;
            }
        }

        private final List<Call> calls = new ArrayList<>();

        @Override
        public void addPatientData(int patientId,
                                   double measurementValue,
                                   String recordType,
                                   long timestamp) {
            calls.add(new Call(patientId, measurementValue, recordType, timestamp));
        }

        int size()                { return calls.size();      }
        Call get(int index)       { return calls.get(index);  }
        boolean isEmpty()         { return calls.isEmpty();   }
    }



    @TempDir Path tmp;
    private RecordingStorage storage;

    @BeforeEach
    void setUp() {
        storage = new RecordingStorage();
    }

    /* ===========================  Happy paths  =========================== */

    @Test
    void readsSingleRow_correctlyParsesEachField() throws IOException {
        Path f = tmp.resolve("single.csv");
        Files.writeString(
                f,
                "patientId,measurementValue,recordType,timestamp%n" +
                        "42,37.5,HR,1617973123000%n".formatted(),
                StandardCharsets.UTF_8);

        new DataReaderImpl(f.toString()).readData(storage);

        assertEquals(1, storage.size());
        RecordingStorage.Call c = storage.get(0);
        assertAll(
                () -> assertEquals(42, c.patientId),
                () -> assertEquals(37.5, c.value),
                () -> assertEquals("HR", c.type),
                () -> assertEquals(1617973123000L, c.timestamp)
        );
    }

    @Test
    void readsMultipleRows_headerOrderIrrelevant() throws IOException {
        Path f = tmp.resolve("multi.csv");
        Files.writeString(
                f,
                """
                timestamp,recordType,patientId,measurementValue
                1700000000000,TEMP,1,36.6
                1700000001000,TEMP,1,36.7
                """,
                StandardCharsets.UTF_8);

        new DataReaderImpl(f.toString()).readData(storage);

        assertEquals(2, storage.size(), "Both data lines must be forwarded");
    }

    @Test
    void headerOnly_fileProducesNoCalls() throws IOException {
        Path f = tmp.resolve("header.csv");
        Files.writeString(f,
                "patientId,measurementValue,recordType,timestamp%n",
                StandardCharsets.UTF_8);

        new DataReaderImpl(f.toString()).readData(storage);

        assertTrue(storage.isEmpty());
    }

    @Test
    void emptyFile_safeNoop() throws IOException {
        Path f = tmp.resolve("empty.csv");
        Files.createFile(f);

        new DataReaderImpl(f.toString()).readData(storage);

        assertTrue(storage.isEmpty());
    }

    /* ========================  Failure scenarios  ======================== */

    @Nested
    class FailureScenarios {

        @Test
        void nonExistentFile_throwsIOException() {
            Path missing = tmp.resolve("not_here.csv");
            assertThrows(IOException.class,
                    () -> new DataReaderImpl(missing.toString()).readData(storage));
        }

        @Test
        void pathIsDirectory_throwsIOException() throws IOException {
            Path dir = Files.createDirectory(tmp.resolve("folder"));
            assertThrows(IOException.class,
                    () -> new DataReaderImpl(dir.toString()).readData(storage));
        }

        @Test
        void invalidNumericValue_throwsNumberFormatException() throws IOException {
            Path f = tmp.resolve("badNumber.csv");
            Files.writeString(
                    f,
                    """
                    patientId,measurementValue,recordType,timestamp
                    1,abc,TEMP,1700000000000
                    """,
                    StandardCharsets.UTF_8);

            assertThrows(NumberFormatException.class,
                    () -> new DataReaderImpl(f.toString()).readData(storage));
        }

        @Test
        void missingRequiredColumn_causesArrayBounds() throws IOException {
            Path f = tmp.resolve("missingColumn.csv");
            Files.writeString(
                    f,
                    """
                    patientId,measurementValue,recordType
                    1,55,BP_SYS
                    """,
                    StandardCharsets.UTF_8);

            assertThrows(ArrayIndexOutOfBoundsException.class,
                    () -> new DataReaderImpl(f.toString()).readData(storage));
        }
    }
}
