package shelter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * An adoption counselor who matches adopters to available animals.
 * Implements ISMem and AnimalObserver.
 */
public class AdoptionCounselor implements ISMem, AnimalObserver {

    /** Staff member name. */
    private String name;

    /** Whether this counselor is currently on shift. */
    private boolean available;

    /** Animals currently in the matching pool. */
    private List<Animal> matchingPool;

    /** Adopters waiting for a match. */
    private List<Adopter> waitingAdopters;

    /**
     * Creates an AdoptionCounselor.
     *
     * @param name staff member name
     */
    public AdoptionCounselor(String name) {
        this.name = name;
        this.available = true;
        this.matchingPool = new ArrayList<>();
        this.waitingAdopters = new ArrayList<>();
    }

    /**
     * Adds an adopter to the waiting list.
     *
     * @param adopter the adopter to register
     */
    public void registerAdopter(Adopter adopter) {
        waitingAdopters.add(adopter);
    }

    /**
     * Reacts to animal status changes.
     * Adds the animal to the matching pool when available,
     * removes it when pending or adopted.
     *
     * @param animal the animal whose status changed
     */
    @Override
    public void onStatusChange(Animal animal) {
        if ("available".equals(animal.getStatus())) {
            if (!matchingPool.contains(animal)) {
                matchingPool.add(animal);
            }
        } else if ("pending".equals(animal.getStatus()) || "adopted".equals(animal.getStatus())) {
            matchingPool.remove(animal);
        }
    }

    /**
     * Tries to match each waiting adopter to an available animal.
     * Sets animal status to pending when a match is found.
     */
    public void runMatching() {
        for (Adopter adopter : waitingAdopters) {
            if (adopter.isAdopted()) {
                continue;
            }
            for (Animal animal : matchingPool) {
                if ("available".equals(animal.getStatus()) && adopter.matches(animal)) {
                    System.out.println("  [Counselor:" + name + "] Matched " + adopter.getName()
                            + " with " + animal.getId() + " (" + animal.getSpecies() + ")");
                    animal.setStatus("pending");
                    break;
                }
            }
        }
    }

    /**
     * Completes an adoption for the given animal and adopter.
     *
     * @param animal  the animal being adopted
     * @param adopter the adopter completing the adoption
     */
    public void completeAdoption(Animal animal, Adopter adopter) {
        animal.setStatus("adopted");
        adopter.setAdopted(true);
        System.out.println("  [Counselor:" + name + "] Adoption complete: " + adopter.getName()
                + " adopted " + animal.getId());
    }

    /**
     * Returns an animal from a failed adoption back to available.
     *
     * @param animal the animal being returned
     */
    public void returnAnimal(Animal animal) {
        System.out.println("  [Counselor:" + name + "] Adoption returned: " + animal.getId()
                + " is back to available");
        animal.setStatus("available");
    }

    /**
     * Returns SHA-256 hash of this staff member's name as credential hash.
     *
     * @return SHA-256 hex string
     */
    @Override
    public String getCredeniaHash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(name.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            return "hash-error";
        }
    }

    /** @return this counselor's name */
    public String getName() {
        return name;
    }

    /** @return true if the counselor is on shift */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Sets whether the counselor is on shift.
     *
     * @param available true if on shift
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /** @return animals currently in the matching pool */
    public List<Animal> getMatchingPool() {
        return matchingPool;
    }
}
