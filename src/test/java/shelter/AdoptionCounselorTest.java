package shelter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the AdoptionCounselor class.
 */
public class AdoptionCounselorTest {

    @Test
    public void testCounselorNameStoredCorrectly() {
        AdoptionCounselor counselor = new AdoptionCounselor("Alex Kim");
        assertEquals("Alex Kim", counselor.getName());
    }

    @Test
    public void testCounselorDefaultAvailableIsTrue() {
        AdoptionCounselor counselor = new AdoptionCounselor("Alex Kim");
        assertTrue(counselor.isAvailable());
    }

    @Test
    public void testSetAvailableUpdatesFlag() {
        AdoptionCounselor counselor = new AdoptionCounselor("Alex Kim");
        counselor.setAvailable(false);
        assertFalse(counselor.isAvailable());
    }

    @Test
    public void testMatchingPoolEmptyAtStart() {
        AdoptionCounselor counselor = new AdoptionCounselor("Alex Kim");
        assertTrue(counselor.getMatchingPool().isEmpty());
    }

    @Test
    public void testOnStatusChangeAddsAnimalWhenAvailable() {
        AdoptionCounselor counselor = new AdoptionCounselor("Alex Kim");
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        animal.addObserver(counselor);
        animal.setStatus("available");
        assertTrue(counselor.getMatchingPool().contains(animal));
    }

    @Test
    public void testOnStatusChangeRemovesAnimalWhenPending() {
        AdoptionCounselor counselor = new AdoptionCounselor("Alex Kim");
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        animal.addObserver(counselor);
        animal.setStatus("available");
        animal.setStatus("pending");
        assertFalse(counselor.getMatchingPool().contains(animal));
    }

    @Test
    public void testOnStatusChangeRemovesAnimalWhenAdopted() {
        AdoptionCounselor counselor = new AdoptionCounselor("Alex Kim");
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        animal.addObserver(counselor);
        animal.setStatus("available");
        animal.setStatus("adopted");
        assertFalse(counselor.getMatchingPool().contains(animal));
    }

    @Test
    public void testAnimalNotAddedToPoolTwice() {
        AdoptionCounselor counselor = new AdoptionCounselor("Alex Kim");
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        animal.addObserver(counselor);
        animal.setStatus("available");
        // Manually trigger again to test guard
        counselor.onStatusChange(animal);
        assertEquals(1, counselor.getMatchingPool().size());
    }

    @Test
    public void testRunMatchingSetsAnimalToPending() {
        AdoptionCounselor counselor = new AdoptionCounselor("Alex Kim");
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        animal.addObserver(counselor);
        animal.setStatus("available");
        Adopter adopter = new Adopter("Maria Santos", "Dog", 1, 5);
        counselor.registerAdopter(adopter);
        counselor.runMatching();
        assertEquals("pending", animal.getStatus());
    }

    @Test
    public void testRunMatchingSkipsAlreadyAdoptedAdopter() {
        AdoptionCounselor counselor = new AdoptionCounselor("Alex Kim");
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        animal.addObserver(counselor);
        animal.setStatus("available");
        Adopter adopter = new Adopter("Maria Santos", "Dog", 1, 5);
        adopter.setAdopted(true);
        counselor.registerAdopter(adopter);
        counselor.runMatching();
        assertEquals("available", animal.getStatus());
    }

    @Test
    public void testCompleteAdoptionSetsAnimalToAdopted() {
        AdoptionCounselor counselor = new AdoptionCounselor("Alex Kim");
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        Adopter adopter = new Adopter("Maria Santos", "Dog", 1, 5);
        counselor.completeAdoption(animal, adopter);
        assertEquals("adopted", animal.getStatus());
    }

    @Test
    public void testCompleteAdoptionMarksAdopterAsAdopted() {
        AdoptionCounselor counselor = new AdoptionCounselor("Alex Kim");
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        Adopter adopter = new Adopter("Maria Santos", "Dog", 1, 5);
        counselor.completeAdoption(animal, adopter);
        assertTrue(adopter.isAdopted());
    }

    @Test
    public void testReturnAnimalSetsStatusBackToAvailable() {
        AdoptionCounselor counselor = new AdoptionCounselor("Alex Kim");
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        animal.setStatus("pending");
        counselor.returnAnimal(animal);
        assertEquals("available", animal.getStatus());
    }

    @Test
    public void testGetCredeniaHashIs64Characters() {
        AdoptionCounselor counselor = new AdoptionCounselor("Alex Kim");
        assertEquals(64, counselor.getCredeniaHash().length());
    }

    @Test
    public void testGetCredeniaHashIsNotNull() {
        AdoptionCounselor counselor = new AdoptionCounselor("Alex Kim");
        assertNotNull(counselor.getCredeniaHash());
    }

    @Test
    public void testImplementsISMem() {
        AdoptionCounselor counselor = new AdoptionCounselor("Alex Kim");
        assertTrue(counselor instanceof ISMem);
    }

    @Test
    public void testImplementsAnimalObserver() {
        AdoptionCounselor counselor = new AdoptionCounselor("Alex Kim");
        assertTrue(counselor instanceof AnimalObserver);
    }
}
