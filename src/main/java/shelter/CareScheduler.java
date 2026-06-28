package shelter;

import java.util.ArrayList;
import java.util.List;

/**
 * Observes animals and tracks which ones need daily care.
 * Performs feeding and cleaning for all non-adopted animals.
 */
public class CareScheduler implements AnimalObserver {

    /** Animals currently tracked for care. */
    private List<Animal> careList;

    /** Creates a CareScheduler with an empty care list. */
    public CareScheduler() {
        this.careList = new ArrayList<>();
    }

    /**
     * Reacts to status changes.
     * Adds animals that become available or pending, removes adopted animals.
     *
     * @param animal the animal whose status changed
     */
    @Override
    public void onStatusChange(Animal animal) {
        if ("available".equals(animal.getStatus()) || "pending".equals(animal.getStatus())) {
            if (!careList.contains(animal)) {
                careList.add(animal);
            }
        } else if ("adopted".equals(animal.getStatus())) {
            careList.remove(animal);
        }
    }

    /**
     * Performs feeding and cleaning for all animals in the care list.
     */
    public void runDailyCare() {
        for (Animal animal : careList) {
            System.out.println("  [Care] Fed and cleaned " + animal.getId()
                    + " (" + animal.getSpecies() + ") in zone " + animal.getShelterZoneCode());
        }
    }

    /** @return all animals currently on the care list */
    public List<Animal> getCareList() {
        return careList;
    }
}
