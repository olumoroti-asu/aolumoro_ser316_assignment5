package shelter;

/**
 * A rabbit in the shelter.
 */
public class Rabbit extends Animal {

    /**
     * Creates a Rabbit.
     *
     * @param id              unique animal ID
     * @param age             age in years
     * @param healthStatus    initial health status
     * @param shelterZoneCode zone code
     * @param arrivalType     how the animal arrived
     */
    public Rabbit(String id, int age, String healthStatus,
                  String shelterZoneCode, String arrivalType) {
        super(id, "Rabbit", age, healthStatus, shelterZoneCode, arrivalType);
    }
}
