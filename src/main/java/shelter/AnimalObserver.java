package shelter;

/**
 * Observer interface for reacting to animal status changes.
 */
public interface AnimalObserver {

    /**
     * Called whenever an animal's status changes.
     *
     * @param animal the animal whose status changed
     */
    void onStatusChange(Animal animal);
}
