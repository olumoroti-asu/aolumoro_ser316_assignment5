package shelter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the AnimalFactory class.
 */
public class AnimalFactoryTest {

    @Test
    public void testCreateDogReturnsDog() {
        AnimalFactory factory = new AnimalFactory();
        Animal animal = factory.createAnimal("dog", "A001", 3, "healthy", "stray");
        assertTrue(animal instanceof Dog);
        assertEquals("Dog", animal.getSpecies());
    }

    @Test
    public void testCreateCatReturnsCat() {
        AnimalFactory factory = new AnimalFactory();
        Animal animal = factory.createAnimal("cat", "A002", 2, "healthy", "surrender");
        assertTrue(animal instanceof Cat);
        assertEquals("Cat", animal.getSpecies());
    }

    @Test
    public void testCreateRabbitReturnsRabbit() {
        AnimalFactory factory = new AnimalFactory();
        Animal animal = factory.createAnimal("rabbit", "A003", 1, "healthy", "transfer");
        assertTrue(animal instanceof Rabbit);
        assertEquals("Rabbit", animal.getSpecies());
    }

    @Test
    public void testFirstAnimalGetsZoneSZ001() {
        AnimalFactory factory = new AnimalFactory();
        Animal animal = factory.createAnimal("dog", "A001", 3, "healthy", "stray");
        assertEquals("SZ-001", animal.getShelterZoneCode());
    }

    @Test
    public void testZoneCodeIncrementsEachAnimal() {
        AnimalFactory factory = new AnimalFactory();
        Animal a1 = factory.createAnimal("dog", "A001", 3, "healthy", "stray");
        Animal a2 = factory.createAnimal("cat", "A002", 2, "healthy", "stray");
        Animal a3 = factory.createAnimal("rabbit", "A003", 1, "healthy", "stray");
        assertEquals("SZ-001", a1.getShelterZoneCode());
        assertEquals("SZ-002", a2.getShelterZoneCode());
        assertEquals("SZ-003", a3.getShelterZoneCode());
    }

    @Test
    public void testZoneCodeFormatIsThreeDigits() {
        AnimalFactory factory = new AnimalFactory();
        Animal animal = factory.createAnimal("dog", "A001", 3, "healthy", "stray");
        assertTrue(animal.getShelterZoneCode().matches("SZ-\\d{3}"));
    }

    @Test
    public void testCreateAnimalCaseInsensitive() {
        AnimalFactory factory = new AnimalFactory();
        Animal dog = factory.createAnimal("DOG", "A001", 3, "healthy", "stray");
        Animal cat = factory.createAnimal("CAT", "A002", 2, "healthy", "stray");
        assertEquals("Dog", dog.getSpecies());
        assertEquals("Cat", cat.getSpecies());
    }

    @Test
    public void testCreatedAnimalStartsInIntake() {
        AnimalFactory factory = new AnimalFactory();
        Animal animal = factory.createAnimal("dog", "A001", 3, "healthy", "stray");
        assertEquals("intake", animal.getStatus());
    }
}
