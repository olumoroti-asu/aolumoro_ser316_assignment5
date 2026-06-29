package shelter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an animal in the shelter.
 * Holds all core attributes and notifies observers when status changes.
 */
public class Animal {

    /** Unique animal ID. */
    private String id;

    /** Species of the animal (e.g., dog, cat, rabbit). */
    private String species;

    /** Age in years. */
    private int age;

    /** Health status description. */
    private String healthStatus;

    /** Shelter zone code in format SZ-XXX. */
    private String shelterZoneCode;

    /** Current lifecycle status: intake, available, pending, adopted. */
    private String status;

    /** How the animal arrived: stray, surrender, transfer. */
    private String arrivalType;

    /** List of observers watching this animal. */
    private List<AnimalObserver> observers;

    /**
     * Creates a new Animal with the given attributes.
     *
     * @param id             unique animal ID
     * @param species        species name
     * @param age            age in years
     * @param healthStatus   initial health status
     * @param shelterZoneCode zone code in SZ-XXX format
     * @param arrivalType    how the animal arrived
     */
    public Animal(String id, String species, int age, String healthStatus,
                  String shelterZoneCode, String arrivalType) {
        this.id = id;
        this.species = species;
        this.age = age;
        this.healthStatus = healthStatus;
        this.shelterZoneCode = shelterZoneCode;
        this.arrivalType = arrivalType;
        this.status = "intake";
        this.observers = new ArrayList<>();
    }

    /**
     * Registers an observer on this animal.
     *
     * @param observer the observer to add
     */
    public void addObserver(AnimalObserver observer) {
        observers.add(observer);
    }

    /**
     * Changes the animal's status and notifies all observers.
     *
     * @param newStatus the new status value
     */
    public void setStatus(String newStatus) {
        this.status = newStatus;
        for (AnimalObserver observer : observers) {
            observer.onStatusChange(this);
        }
    }

    /**
     * Updates the animal's health status.
     *
     * @param healthStatus new health status description
     */
    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    /** @return the animal's ID */
    public String getId() {
        return id;
    }

    /** @return the species */
    public String getSpecies() {
        return species;
    }

    /** @return age in years */
    public int getAge() {
        return age;
    }

    /** @return current health status */
    public String getHealthStatus() {
        return healthStatus;
    }

    /** @return shelter zone code */
    public String getShelterZoneCode() {
        return shelterZoneCode;
    }

    /** @return current lifecycle status */
    public String getStatus() {
        return status;
    }

    /** @return how the animal arrived */
    public String getArrivalType() {
        return arrivalType;
    }

    @Override
    public String toString() {
        return id + " (" + species + ", age=" + age + ", zone=" + shelterZoneCode
                + ", status=" + status + ", health=" + healthStatus + ")";
    }
}
