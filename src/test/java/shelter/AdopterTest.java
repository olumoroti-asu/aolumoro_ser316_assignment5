package shelter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the Adopter class.
 */
public class AdopterTest {

    @Test
    public void testAdopterFieldsStoredCorrectly() {
        Adopter adopter = new Adopter("Maria Santos", "Dog", 1, 5);
        assertEquals("Maria Santos", adopter.getName());
        assertEquals("Dog", adopter.getPreferredSpecies());
    }

    @Test
    public void testAdopterDefaultIsNotAdopted() {
        Adopter adopter = new Adopter("Maria Santos", "Dog", 1, 5);
        assertFalse(adopter.isAdopted());
    }

    @Test
    public void testSetAdoptedUpdatesFlag() {
        Adopter adopter = new Adopter("Maria Santos", "Dog", 1, 5);
        adopter.setAdopted(true);
        assertTrue(adopter.isAdopted());
    }

    @Test
    public void testMatchesTrueWhenSpeciesAndAgeMatch() {
        Adopter adopter = new Adopter("Maria Santos", "Dog", 1, 5);
        Animal dog = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        assertTrue(adopter.matches(dog));
    }

    @Test
    public void testMatchesFalseWhenSpeciesDiffers() {
        Adopter adopter = new Adopter("James Lee", "Cat", 1, 5);
        Animal dog = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        assertFalse(adopter.matches(dog));
    }

    @Test
    public void testMatchesFalseWhenAgeTooLow() {
        Adopter adopter = new Adopter("Maria Santos", "Dog", 4, 8);
        Animal dog = new Animal("A001", "Dog", 2, "healthy", "SZ-001", "stray");
        assertFalse(adopter.matches(dog));
    }

    @Test
    public void testMatchesFalseWhenAgeTooHigh() {
        Adopter adopter = new Adopter("Maria Santos", "Dog", 1, 3);
        Animal dog = new Animal("A001", "Dog", 5, "healthy", "SZ-001", "stray");
        assertFalse(adopter.matches(dog));
    }

    @Test
    public void testMatchesTrueAtMinAgeBoundary() {
        Adopter adopter = new Adopter("Maria Santos", "Dog", 3, 6);
        Animal dog = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        assertTrue(adopter.matches(dog));
    }

    @Test
    public void testMatchesTrueAtMaxAgeBoundary() {
        Adopter adopter = new Adopter("Maria Santos", "Dog", 1, 5);
        Animal dog = new Animal("A001", "Dog", 5, "healthy", "SZ-001", "stray");
        assertTrue(adopter.matches(dog));
    }

    @Test
    public void testMatchesIsCaseInsensitiveForSpecies() {
        Adopter adopter = new Adopter("Nina Patel", "rabbit", 1, 4);
        Animal rabbit = new Animal("A001", "Rabbit", 2, "healthy", "SZ-001", "stray");
        assertTrue(adopter.matches(rabbit));
    }
}
