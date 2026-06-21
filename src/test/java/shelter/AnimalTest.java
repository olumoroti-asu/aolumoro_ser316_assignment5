package shelter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnimalTest {

    @Test
    public void testSampleAddition() {
        int result = 2 + 3;
        assertEquals(5, result);
    }

    @Test
    public void testSampleString() {
        String name = "PetShelter";
        assertTrue(name.startsWith("Pet"));
    }
}
