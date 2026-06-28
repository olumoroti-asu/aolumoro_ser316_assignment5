package shelter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the Veterinarian class.
 */
public class VeterinarianTest {

    @Test
    public void testVetNameStoredCorrectly() {
        Veterinarian vet = new Veterinarian("Dr. Patel");
        assertEquals("Dr. Patel", vet.getName());
    }

    @Test
    public void testVetDefaultAvailableIsTrue() {
        Veterinarian vet = new Veterinarian("Dr. Patel");
        assertTrue(vet.isAvailable());
    }

    @Test
    public void testSetAvailableUpdatesFlag() {
        Veterinarian vet = new Veterinarian("Dr. Patel");
        vet.setAvailable(false);
        assertFalse(vet.isAvailable());
    }

    @Test
    public void testClearAnimalSetsStatusToAvailable() {
        Veterinarian vet = new Veterinarian("Dr. Patel");
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        vet.clearAnimal(animal);
        assertEquals("available", animal.getStatus());
    }

    @Test
    public void testClearAnimalSetsHealthStatusToCleared() {
        Veterinarian vet = new Veterinarian("Dr. Patel");
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        vet.clearAnimal(animal);
        assertEquals("cleared", animal.getHealthStatus());
    }

    @Test
    public void testClearAnimalAddsMedLogEntry() {
        Veterinarian vet = new Veterinarian("Dr. Patel");
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        vet.clearAnimal(animal);
        assertEquals(1, vet.getMedLog().size());
    }

    @Test
    public void testMedLogEntryBelongsToCorrectAnimal() {
        Veterinarian vet = new Veterinarian("Dr. Patel");
        Animal animal = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        vet.clearAnimal(animal);
        assertEquals("A001", vet.getMedLog().get(0).getAnimalId());
    }

    @Test
    public void testMedLogGrowsWithMultipleClearCalls() {
        Veterinarian vet = new Veterinarian("Dr. Patel");
        Animal a1 = new Animal("A001", "Dog", 3, "healthy", "SZ-001", "stray");
        Animal a2 = new Animal("A002", "Cat", 2, "healthy", "SZ-002", "stray");
        vet.clearAnimal(a1);
        vet.clearAnimal(a2);
        assertEquals(2, vet.getMedLog().size());
    }

    @Test
    public void testGetCredeniaHashIsNotNull() {
        Veterinarian vet = new Veterinarian("Dr. Patel");
        assertNotNull(vet.getCredeniaHash());
    }

    @Test
    public void testGetCredeniaHashIs64Characters() {
        Veterinarian vet = new Veterinarian("Dr. Patel");
        assertEquals(64, vet.getCredeniaHash().length());
    }

    @Test
    public void testTwoVetsWithDifferentNamesHaveDifferentHashes() {
        Veterinarian vet1 = new Veterinarian("Dr. Patel");
        Veterinarian vet2 = new Veterinarian("Dr. Smith");
        assertFalse(vet1.getCredeniaHash().equals(vet2.getCredeniaHash()));
    }

    @Test
    public void testImplementsISMem() {
        Veterinarian vet = new Veterinarian("Dr. Patel");
        assertTrue(vet instanceof ISMem);
    }

    @Test
    public void testImplementsAnimalObserver() {
        Veterinarian vet = new Veterinarian("Dr. Patel");
        assertTrue(vet instanceof AnimalObserver);
    }
}
