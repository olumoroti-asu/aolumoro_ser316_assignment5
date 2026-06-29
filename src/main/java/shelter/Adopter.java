package shelter;

/**
 * Represents a person looking to adopt an animal.
 */
public class Adopter {

    /** Adopter's name. */
    private String name;

    /** Preferred species (e.g., "Dog", "Cat", "Rabbit"). */
    private String preferredSpecies;

    /** Minimum preferred age. */
    private int minAge;

    /** Maximum preferred age. */
    private int maxAge;

    /** Whether this adopter has already adopted an animal. */
    private boolean adopted;

    /**
     * Creates a new Adopter.
     *
     * @param name             adopter's name
     * @param preferredSpecies preferred species
     * @param minAge           minimum preferred age
     * @param maxAge           maximum preferred age
     */
    public Adopter(String name, String preferredSpecies, int minAge, int maxAge) {
        this.name = name;
        this.preferredSpecies = preferredSpecies;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.adopted = false;
    }

    /**
     * Returns true if this adopter's preferences match the given animal.
     *
     * @param animal the animal to check
     * @return true if species and age range match
     */
    public boolean matches(Animal animal) {
        boolean speciesMatch = animal.getSpecies().equalsIgnoreCase(preferredSpecies);
        boolean ageMatch = animal.getAge() >= minAge && animal.getAge() <= maxAge;
        return speciesMatch && ageMatch;
    }

    /** @return adopter's name */
    public String getName() {
        return name;
    }

    /** @return preferred species */
    public String getPreferredSpecies() {
        return preferredSpecies;
    }

    /** @return whether this adopter has adopted */
    public boolean isAdopted() {
        return adopted;
    }

    /**
     * Marks this adopter as having completed an adoption.
     *
     * @param adopted true if adoption completed
     */
    public void setAdopted(boolean adopted) {
        this.adopted = adopted;
    }
}
