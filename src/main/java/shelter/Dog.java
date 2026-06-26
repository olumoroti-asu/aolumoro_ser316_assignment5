package shelter;

/**
 * A dog in the shelter.
 */
public class Dog extends Animal {

    /**
     * Creates a Dog.
     *
     * @param id              unique animal ID
     * @param age             age in years
     * @param healthStatus    initial health status
     * @param shelterZoneCode zone code
     * @param arrivalType     how the animal arrived
     */
    public Dog(String id, int age, String healthStatus, String shelterZoneCode, String arrivalType) {
        super(id, "Dog", age, healthStatus, shelterZoneCode, arrivalType);
    }
}
