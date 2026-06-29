package shelter;

/**
 * Factory for creating Animal objects.
 * Keeps a counter so every new animal gets a unique SZ-XXX zone code.
 */
public class AnimalFactory {

    /** Counter for generating zone codes. Starts at 1. */
    private int zoneCounter;

    /** Creates a new AnimalFactory starting at zone SZ-001. */
    public AnimalFactory() {
        this.zoneCounter = 1;
    }

    /**
     * Creates an animal of the given type with auto-assigned zone code.
     *
     * @param type         "dog", "cat", or "rabbit" (case-insensitive)
     * @param id           unique animal ID
     * @param age          age in years
     * @param healthStatus initial health status
     * @param arrivalType  how the animal arrived
     * @return a new Animal subclass instance
     */
    public Animal createAnimal(String type, String id, int age,
                               String healthStatus, String arrivalType) {
        String zoneCode = String.format("SZ-%03d", zoneCounter);
        zoneCounter++;

        if (type.equalsIgnoreCase("dog")) {
            return new Dog(id, age, healthStatus, zoneCode, arrivalType);
        } else if (type.equalsIgnoreCase("cat")) {
            return new Cat(id, age, healthStatus, zoneCode, arrivalType);
        } else {
            return new Rabbit(id, age, healthStatus, zoneCode, arrivalType);
        }
    }
}
