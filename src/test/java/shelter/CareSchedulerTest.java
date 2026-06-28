package shelter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the CareScheduler class.
 */
public class CareSchedulerTest {

    @Test
    public void testCareListEmptyAtStart() {
        CareScheduler scheduler = new CareScheduler();
        assertTrue(scheduler.getCareList().isEmpty());
    }

    @Test
    public void testAnimalAddedWhenStatusBecomesAvailable() {
        CareScheduler scheduler = new CareScheduler();
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        animal.addObserver(scheduler);
        animal.setStatus("available");
        assertTrue(scheduler.getCareList().contains(animal));
    }

    @Test
    public void testAnimalAddedWhenStatusBecomesPending() {
        CareScheduler scheduler = new CareScheduler();
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        animal.addObserver(scheduler);
        animal.setStatus("pending");
        assertTrue(scheduler.getCareList().contains(animal));
    }

    @Test
    public void testAnimalRemovedWhenStatusBecomesAdopted() {
        CareScheduler scheduler = new CareScheduler();
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        animal.addObserver(scheduler);
        animal.setStatus("available");
        animal.setStatus("adopted");
        assertFalse(scheduler.getCareList().contains(animal));
    }

    @Test
    public void testAnimalNotAddedTwiceForSameStatus() {
        CareScheduler scheduler = new CareScheduler();
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        animal.addObserver(scheduler);
        animal.setStatus("available");
        // Trigger again manually
        scheduler.onStatusChange(animal);
        assertEquals(1, scheduler.getCareList().size());
    }

    @Test
    public void testMultipleAnimalsTracked() {
        CareScheduler scheduler = new CareScheduler();
        Animal a1 = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        Animal a2 = new Animal("A002", "Cat", 2, "healthy", "SZ-002", "stray");
        a1.addObserver(scheduler);
        a2.addObserver(scheduler);
        a1.setStatus("available");
        a2.setStatus("available");
        assertEquals(2, scheduler.getCareList().size());
    }

    @Test
    public void testCareListSizeDecreasesAfterAdoption() {
        CareScheduler scheduler = new CareScheduler();
        Animal a1 = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        Animal a2 = new Animal("A002", "Cat", 2, "healthy", "SZ-002", "stray");
        a1.addObserver(scheduler);
        a2.addObserver(scheduler);
        a1.setStatus("available");
        a2.setStatus("available");
        a1.setStatus("adopted");
        assertEquals(1, scheduler.getCareList().size());
        assertTrue(scheduler.getCareList().contains(a2));
    }

    @Test
    public void testIntakeStatusDoesNotAddToList() {
        CareScheduler scheduler = new CareScheduler();
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        animal.addObserver(scheduler);
        // status starts as intake — never triggers observer add
        assertTrue(scheduler.getCareList().isEmpty());
    }

    @Test
    public void testImplementsAnimalObserver() {
        CareScheduler scheduler = new CareScheduler();
        assertTrue(scheduler instanceof AnimalObserver);
    }

    @Test
    public void testReturnedAnimalReappearsInCareList() {
        CareScheduler scheduler = new CareScheduler();
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        animal.addObserver(scheduler);
        animal.setStatus("available");
        animal.setStatus("pending");
        // Still in list as pending, confirm size
        assertEquals(1, scheduler.getCareList().size());
        // Now simulate return to available
        animal.setStatus("available");
        assertEquals(1, scheduler.getCareList().size());
        assertTrue(scheduler.getCareList().contains(animal));
    }
}
