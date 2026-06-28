package shelter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A veterinarian who clears animals from intake to available.
 * Implements ISMem and AnimalObserver.
 */
public class Veterinarian implements ISMem, AnimalObserver {

    /** Staff member name. */
    private String name;

    /** Whether this vet is currently on shift. */
    private boolean available;

    /** Medical log entries recorded by this vet. */
    private List<MedicalLogEntry> medLog;

    /**
     * Creates a Veterinarian.
     *
     * @param name staff member name
     */
    public Veterinarian(String name) {
        this.name = name;
        this.available = true;
        this.medLog = new ArrayList<>();
    }

    /**
     * Clears an animal from intake by updating its health and setting status to available.
     * Logs a MedicalLogEntry for the intake exam.
     *
     * @param animal the animal to clear
     */
    public void clearAnimal(Animal animal) {
        animal.setHealthStatus("cleared");
        MedicalLogEntry entry = new MedicalLogEntry(UUID.randomUUID(),
                animal.getId(), "Intake exam passed - cleared for adoption");
        medLog.add(entry);
        System.out.println("  [Vet:" + name + "] " + entry);
        animal.setStatus("available");
    }

    /**
     * Reacts to status changes. Logs a note when an animal becomes available.
     *
     * @param animal the animal whose status changed
     */
    @Override
    public void onStatusChange(Animal animal) {
        if ("available".equals(animal.getStatus())) {
            System.out.println("  [Vet:" + name + "] Noted: " + animal.getId()
                    + " is now available in zone " + animal.getShelterZoneCode());
        }
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

    /** @return this vet's name */
    public String getName() {
        return name;
    }

    /** @return true if the vet is on shift */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Sets whether the vet is on shift.
     *
     * @param available true if on shift
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /** @return all medical log entries recorded by this vet */
    public List<MedicalLogEntry> getMedLog() {
        return medLog;
    }
}
