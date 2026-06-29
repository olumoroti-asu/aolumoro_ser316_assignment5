package shelter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the Animal class covering fields, status transitions, and observer wiring.
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
    public void testDefaultStatusIsIntake() {
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        assertEquals("intake", animal.getStatus());
    }

    @Test
    public void testSetStatusToAvailable() {
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        animal.setStatus("available");
        assertEquals("available", animal.getStatus());
    }

    @Test
    public void testSetStatusToPending() {
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        animal.setStatus("pending");
        assertEquals("pending", animal.getStatus());
    }

    @Test
    public void testSetStatusToAdopted() {
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        animal.setStatus("adopted");
        assertEquals("adopted", animal.getStatus());
    }

    @Test
    public void testFullStatusTransitionLifecycle() {
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        assertEquals("intake", animal.getStatus());
        animal.setStatus("available");
        assertEquals("available", animal.getStatus());
        animal.setStatus("pending");
        assertEquals("pending", animal.getStatus());
        animal.setStatus("adopted");
        assertEquals("adopted", animal.getStatus());
    }

    @Test
    public void testReturnedAnimalGoesBackToAvailable() {
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        animal.setStatus("pending");
        animal.setStatus("available");
        assertEquals("available", animal.getStatus());
    }

    @Test
    public void testSetHealthStatusUpdates() {
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        animal.setHealthStatus("cleared");
        assertEquals("cleared", animal.getHealthStatus());
    }

    @Test
    public void testObserverNotifiedOnStatusChange() {
        Animal animal = new Animal("A001", "Cat", 2, "healthy", "SZ-001", "stray");
        String[] captured = {null};
        AnimalObserver observer = a -> captured[0] = a.getStatus();
        animal.addObserver(observer);
        animal.setStatus("available");
        assertEquals("available", captured[0]);
    }

    @Test
    public void testMultipleObserversAllNotified() {
        Animal animal = new Animal("A001", "Dog", 5, "healthy", "SZ-001", "surrender");
        int[] count = {0};
        animal.addObserver(a -> count[0]++);
        animal.addObserver(a -> count[0]++);
        animal.setStatus("pending");
        assertEquals(2, count[0]);
    }

    @Test
    public void testObserverReceivesCorrectAnimalReference() {
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        Animal[] received = {null};
        animal.addObserver(a -> received[0] = a);
        animal.setStatus("available");
        assertEquals(animal, received[0]);
    }

    @Test
    public void testFactoryZoneCodeOnFirstAnimal() {
        AnimalFactory factory = new AnimalFactory();
        Animal animal = factory.createAnimal("dog", "A001", 3, "healthy", "stray");
        assertEquals("SZ-001", animal.getShelterZoneCode());
    }

    @Test
    public void testFactoryZoneCodeIncrementsSequentially() {
        AnimalFactory factory = new AnimalFactory();
        Animal a1 = factory.createAnimal("dog",    "A001", 3, "healthy", "stray");
        Animal a2 = factory.createAnimal("cat",    "A002", 2, "healthy", "stray");
        Animal a3 = factory.createAnimal("rabbit", "A003", 1, "healthy", "stray");
        assertEquals("SZ-001", a1.getShelterZoneCode());
        assertEquals("SZ-002", a2.getShelterZoneCode());
        assertEquals("SZ-003", a3.getShelterZoneCode());
    }

    @Test
    public void testAdopterMatchesDogBySpeciesAndAge() {
        Adopter adopter = new Adopter("Maria Santos", "Dog", 1, 5);
        Animal dog = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        assertTrue(adopter.matches(dog));
    }

    @Test
    public void testAdopterDoesNotMatchWrongSpecies() {
        Adopter adopter = new Adopter("Maria Santos", "Dog", 1, 5);
        Animal cat = new Animal("A001", "Cat", 3, "healthy", "SZ-001", "stray");
        assertFalse(adopter.matches(cat));
    }

    @Test
    public void testAdopterDoesNotMatchAgeOutOfRange() {
        Adopter adopter = new Adopter("Maria Santos", "Dog", 1, 3);
        Animal dog = new Animal("A001", "Dog", 6, "healthy", "SZ-001", "stray");
        assertFalse(adopter.matches(dog));
    }

    @Test
    public void testToStringContainsIdSpeciesAndZone() {
        Animal animal = new Animal("A007", "Rabbit", 1, "healthy", "SZ-007", "stray");
        String result = animal.toString();
        assertNotNull(result);
        assertTrue(result.contains("A007"));
        assertTrue(result.contains("Rabbit"));
        assertTrue(result.contains("SZ-007"));
    }
}
