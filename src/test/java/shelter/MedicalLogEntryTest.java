package shelter;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the MedicalLogEntry class.
 */
public class MedicalLogEntryTest {

    @Test
    public void testFieldsStoredCorrectly() {
        UUID token = UUID.randomUUID();
        MedicalLogEntry entry = new MedicalLogEntry(token, "A001", "Intake exam passed");
        assertEquals(token, entry.getValidationToks());
        assertEquals("A001", entry.getAnimalId());
        assertEquals("Intake exam passed", entry.getNote());
    }

    @Test
    public void testValidationToksIsNotNull() {
        UUID token = UUID.randomUUID();
        MedicalLogEntry entry = new MedicalLogEntry(token, "A001", "Vaccination given");
        assertNotNull(entry.getValidationToks());
    }

    @Test
    public void testTwoEntriesHaveDifferentTokensWhenCreatedWithDifferentUUIDs() {
        UUID token1 = UUID.randomUUID();
        UUID token2 = UUID.randomUUID();
        MedicalLogEntry entry1 = new MedicalLogEntry(token1, "A001", "Exam");
        MedicalLogEntry entry2 = new MedicalLogEntry(token2, "A002", "Exam");
        assertTrue(!entry1.getValidationToks().equals(entry2.getValidationToks()));
    }

    @Test
    public void testToStringContainsAnimalIdAndNote() {
        UUID token = UUID.randomUUID();
        MedicalLogEntry entry = new MedicalLogEntry(token, "A003", "Cleared for adoption");
        String result = entry.toString();
        assertTrue(result.contains("A003"));
        assertTrue(result.contains("Cleared for adoption"));
    }
}
