package shelter;

/**
 * Interface for all staff members in the shelter.
 * All staff must be able to produce a credential hash.
 */
public interface ISMem {

    /**
     * Returns a SHA-256 credential hash for this staff member.
     *
     * @return SHA-256 hash string
     */
    String getCredeniaHash();
}
