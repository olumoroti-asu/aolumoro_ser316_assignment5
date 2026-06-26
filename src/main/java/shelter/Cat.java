package shelter;

/**
 * A cat in the shelter.
 */
public class Cat extends Animal {

    /**
     * Creates a Cat.
     *
     * @param id              unique animal ID
     * @param age             age in years
     * @param healthStatus    initial health status
     * @param shelterZoneCode zone code
     * @param arrivalType     how the animal arrived
     */
    public Cat(String id, int age, String healthStatus, String shelterZoneCode, String arrivalType) {
        super(id, "Cat", age, healthStatus, shelterZoneCode, arrivalType);
    }
}
