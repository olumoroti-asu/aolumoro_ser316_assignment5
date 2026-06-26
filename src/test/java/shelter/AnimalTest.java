package shelter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for the Animal class.
 */
public class AnimalTest {

    @Test
    public void testAnimalFieldsStoredCorrectly() {
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        assertEquals("A001", animal.getId());
        assertEquals("Dog", animal.getSpecies());
        assertEquals(3, animal.getAge());
        assertEquals("healthy", animal.getHealthStatus());
        assertEquals("SZ-001", animal.getShelterZoneCode());
        assertEquals("stray", animal.getArrivalType());
    }

    @Test
    public void testAnimalDefaultStatusIsIntake() {
        Animal animal = new Animal("A002", "Cat", 2, "healthy", "SZ-002", "surrender");
        assertEquals("intake", animal.getStatus());
    }

    @Test
    public void testSetStatusUpdatesStatus() {
        Animal animal = new Animal("A003", "Rabbit", 1, "healthy", "SZ-003", "stray");
        animal.setStatus("available");
        assertEquals("available", animal.getStatus());
    }

    @Test
    public void testSetHealthStatusUpdatesHealthStatus() {
        Animal animal = new Animal("A004", "Dog", 4, "healthy", "SZ-004", "transfer");
        animal.setHealthStatus("cleared");
        assertEquals("cleared", animal.getHealthStatus());
    }

    @Test
    public void testObserverNotifiedOnStatusChange() {
        Animal animal = new Animal("A005", "Cat", 2, "healthy", "SZ-005", "stray");
        String[] captured = {null};
        AnimalObserver observer = a -> captured[0] = a.getStatus();
        animal.addObserver(observer);
        animal.setStatus("available");
        assertEquals("available", captured[0]);
    }

    @Test
    public void testMultipleObserversAllNotified() {
        Animal animal = new Animal("A006", "Dog", 5, "healthy", "SZ-006", "surrender");
        int[] callCount = {0};
        animal.addObserver(a -> callCount[0]++);
        animal.addObserver(a -> callCount[0]++);
        animal.setStatus("pending");
        assertEquals(2, callCount[0]);
    }

    @Test
    public void testToStringContainsKeyFields() {
        Animal animal = new Animal("A007", "Rabbit", 1, "healthy", "SZ-007", "stray");
        String result = animal.toString();
        assertNotNull(result);
        assertEquals(true, result.contains("A007"));
        assertEquals(true, result.contains("Rabbit"));
        assertEquals(true, result.contains("SZ-007"));
    }
}
