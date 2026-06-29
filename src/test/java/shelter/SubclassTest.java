package shelter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Dog, Cat, and Rabbit subclasses.
 */
public class SubclassTest {

    @Test
    public void testDogSpeciesIsDog() {
        Dog dog = new Dog("A001", 3, "healthy", "SZ-001", "stray");
        assertEquals("Dog", dog.getSpecies());
    }

    @Test
    public void testCatSpeciesIsCat() {
        Cat cat = new Cat("A002", 2, "healthy", "SZ-002", "surrender");
        assertEquals("Cat", cat.getSpecies());
    }

    @Test
    public void testRabbitSpeciesIsRabbit() {
        Rabbit rabbit = new Rabbit("A003", 1, "healthy", "SZ-003", "transfer");
        assertEquals("Rabbit", rabbit.getSpecies());
    }

    @Test
    public void testDogIsInstanceOfAnimal() {
        Dog dog = new Dog("A001", 3, "healthy", "SZ-001", "stray");
        assertTrue(dog instanceof Animal);
    }

    @Test
    public void testCatIsInstanceOfAnimal() {
        Cat cat = new Cat("A002", 2, "healthy", "SZ-002", "stray");
        assertTrue(cat instanceof Animal);
    }

    @Test
    public void testRabbitIsInstanceOfAnimal() {
        Rabbit rabbit = new Rabbit("A003", 1, "healthy", "SZ-003", "stray");
        assertTrue(rabbit instanceof Animal);
    }

    @Test
    public void testDogFieldsPassedToParent() {
        Dog dog = new Dog("A001", 4, "healthy", "SZ-001", "surrender");
        assertEquals("A001", dog.getId());
        assertEquals(4, dog.getAge());
        assertEquals("SZ-001", dog.getShelterZoneCode());
        assertEquals("surrender", dog.getArrivalType());
    }
}
