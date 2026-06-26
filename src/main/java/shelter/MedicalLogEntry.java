package shelter;

import java.util.UUID;

/**
 * Records a single medical event for an animal.
 */
public class MedicalLogEntry {

    /** Token used to validate this log entry. */
    private UUID validationToks;

    /** The animal this entry belongs to. */
    private String animalId;

    /** Description of the medical action performed. */
    private String note;

    /**
     * Creates a new MedicalLogEntry.
     *
     * @param validationToks UUID token for this entry
     * @param animalId       ID of the animal
     * @param note           description of the medical action
     */
    public MedicalLogEntry(UUID validationToks, String animalId, String note) {
        this.validationToks = validationToks;
        this.animalId = animalId;
        this.note = note;
    }

    /** @return the validation token */
    public UUID getValidationToks() {
        return validationToks;
    }

    /** @return the animal ID this entry belongs to */
    public String getAnimalId() {
        return animalId;
    }

    /** @return the medical note */
    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return "[MedLog] animal=" + animalId + " | " + note + " | token=" + validationToks;
    }
}
