package shelter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for the Pet Shelter Management System.
 * Runs a 7-day simulation using the Factory and Observer patterns.
 */
public class Main {

    /**
     * Converts a cycle number (1-10) to a Roman numeral string.
     *
     * @param n cycle number
     * @return Roman numeral string
     */
    static String toRoman(int n) {
        String[] romans = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        if (n >= 1 && n <= 10) {
            return romans[n - 1];
        }
        return String.valueOf(n);
    }

    /**
     * Registers all three observers on the given animal and adds it to the master list.
     *
     * @param animal       the animal to register
     * @param vet          the veterinarian observer
     * @param counselor    the adoption counselor observer
     * @param care         the care scheduler observer
     * @param allAnimals   the master list of all animals
     */
    static void register(Animal animal, Veterinarian vet, AdoptionCounselor counselor,
                         CareScheduler care, List<Animal> allAnimals) {
        animal.addObserver(vet);
        animal.addObserver(counselor);
        animal.addObserver(care);
        allAnimals.add(animal);
    }

    /**
     * Main simulation. Runs 7 daily cycles covering all 6 requirements.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        System.out.println("=== Pet Shelter Management System ===");
        System.out.println();

        // --- Setup ---
        AnimalFactory factory = new AnimalFactory();
        Veterinarian vet = new Veterinarian("Dr. Patel");
        AdoptionCounselor counselor = new AdoptionCounselor("Alex Kim");
        CareScheduler care = new CareScheduler();
        List<Animal> allAnimals = new ArrayList<>();

        System.out.println("Staff credentials (ISMem):");
        System.out.println("  " + vet.getName() + ": " + vet.getCredeniaHash());
        System.out.println("  " + counselor.getName() + ": " + counselor.getCredeniaHash());
        System.out.println();

        // R1.1 — 5 starting animals with ID, species, age, health, shelterZoneCode
        Animal a1 = factory.createAnimal("dog",    "A001", 3, "healthy", "surrender");
        Animal a2 = factory.createAnimal("cat",    "A002", 5, "healthy", "stray");
        Animal a3 = factory.createAnimal("rabbit", "A003", 2, "healthy", "surrender");
        Animal a4 = factory.createAnimal("dog",    "A004", 7, "healthy", "transfer");
        Animal a5 = factory.createAnimal("cat",    "A005", 1, "healthy", "stray");

        for (Animal a : new Animal[]{a1, a2, a3, a4, a5}) {
            register(a, vet, counselor, care, allAnimals);
        }

        // R3.1 — Adopters with species and age-range preferences
        Adopter adopter1 = new Adopter("Maria Santos", "Dog",    1, 5);
        Adopter adopter2 = new Adopter("James Lee",    "Cat",    1, 6);
        Adopter adopter3 = new Adopter("Nina Patel",   "Rabbit", 1, 4);
        counselor.registerAdopter(adopter1);
        counselor.registerAdopter(adopter2);
        counselor.registerAdopter(adopter3);

        // Track first two matched animals for completion on Day 4
        Animal pendingA1 = null;
        Animal pendingA2 = null;

        // ===== SIMS I — Day 1: intake processing for 3 of 5 starting animals =====
        System.out.println("SIMS " + toRoman(1) + " - Day 1: Initial intake");
        System.out.println("  Arrivals: A001 (Dog/SZ-001), A002 (Cat/SZ-002), A003 (Rabbit/SZ-003),"
                + " A004 (Dog/SZ-004), A005 (Cat/SZ-005)");
        // R2.1 — Vet clears animals from intake; logs MedicalLogEntry each time
        vet.clearAnimal(a1);
        vet.clearAnimal(a2);
        vet.clearAnimal(a3);
        // R3.1 — Counselor tries to match waiting adopters to available animals
        counselor.runMatching();
        // R4.1 — Basic care runs every cycle for all non-adopted animals
        care.runDailyCare();
        System.out.println();

        // ===== SIMS II — Day 2: remaining animals cleared, first new arrival =====
        System.out.println("SIMS " + toRoman(2) + " - Day 2: Remaining intake + new stray");
        vet.clearAnimal(a4);
        vet.clearAnimal(a5);
        // R1.2 — New animal arrives (stray)
        Animal a6 = factory.createAnimal("dog", "A006", 2, "healthy", "stray");
        register(a6, vet, counselor, care, allAnimals);
        System.out.println("  New arrival: " + a6);
        counselor.runMatching();
        // Capture the first two pending animals for Day 4 completion
        for (Animal a : allAnimals) {
            if ("pending".equals(a.getStatus()) && pendingA1 == null) {
                pendingA1 = a;
            } else if ("pending".equals(a.getStatus()) && pendingA2 == null) {
                pendingA2 = a;
            }
        }
        care.runDailyCare();
        System.out.println();

        // ===== SIMS III — Day 3: vet off shift, new surrender arrives =====
        System.out.println("SIMS " + toRoman(3) + " - Day 3: Vet off shift, new surrender");
        // R2.1 — Staff availability can change (vet goes off shift)
        vet.setAvailable(false);
        System.out.println("  [Vet:" + vet.getName() + "] Off shift — no exams today");
        // R1.2 — Another new animal arrives (surrender)
        Animal a7 = factory.createAnimal("cat", "A007", 4, "healthy", "surrender");
        register(a7, vet, counselor, care, allAnimals);
        System.out.println("  New arrival: " + a7 + " (awaiting vet clearance)");
        counselor.runMatching();
        care.runDailyCare();
        System.out.println();

        // ===== SIMS IV — Day 4: vet returns, adoptions finalized =====
        System.out.println("SIMS " + toRoman(4) + " - Day 4: Vet returns, adoptions complete");
        vet.setAvailable(true);
        System.out.println("  [Vet:" + vet.getName() + "] Back on shift");
        vet.clearAnimal(a6);
        vet.clearAnimal(a7);
        // R3.3 — Complete the two successful adoptions matched earlier
        if (pendingA1 != null) {
            counselor.completeAdoption(pendingA1, adopter1);
        }
        if (pendingA2 != null) {
            counselor.completeAdoption(pendingA2, adopter2);
        }
        counselor.runMatching();
        care.runDailyCare();
        System.out.println();

        // ===== SIMS V — Day 5: adoption return, re-matching =====
        System.out.println("SIMS " + toRoman(5) + " - Day 5: Adoption returned");
        // R3.3 — One adoption fails and the animal is returned
        System.out.println("  Nina Patel returns A003 — could not keep the rabbit");
        counselor.returnAnimal(a3);
        counselor.runMatching();
        care.runDailyCare();
        System.out.println();

        // ===== SIMS VI — Day 6: new arrival, re-matched rabbit =====
        System.out.println("SIMS " + toRoman(6) + " - Day 6: New arrival, continued matching");
        // R1.2 — Another new animal (stray rabbit)
        Animal a8 = factory.createAnimal("rabbit", "A008", 3, "healthy", "stray");
        register(a8, vet, counselor, care, allAnimals);
        System.out.println("  New arrival: " + a8);
        vet.clearAnimal(a8);
        counselor.runMatching();
        care.runDailyCare();
        System.out.println();

        // ===== SIMS VII — Day 7: final adoptions, end-of-week summary =====
        System.out.println("SIMS " + toRoman(7) + " - Day 7: Final adoptions and summary");
        // Complete Nina's adoption — find first available rabbit
        for (Animal a : allAnimals) {
            if ("pending".equals(a.getStatus()) && "Rabbit".equals(a.getSpecies())
                    && !adopter3.isAdopted()) {
                counselor.completeAdoption(a, adopter3);
                break;
            }
        }
        care.runDailyCare();
        System.out.println();

        // --- End of week summary ---
        System.out.println("=== End of Week Summary ===");
        for (Animal a : allAnimals) {
            System.out.println("  " + a);
        }
        System.out.println();

        long adopted   = allAnimals.stream().filter(a -> "adopted".equals(a.getStatus())).count();
        long available = allAnimals.stream().filter(a -> "available".equals(a.getStatus())).count();
        long pending   = allAnimals.stream().filter(a -> "pending".equals(a.getStatus())).count();
        long intake    = allAnimals.stream().filter(a -> "intake".equals(a.getStatus())).count();
        System.out.println("Total animals processed : " + allAnimals.size());
        System.out.println("Adopted                 : " + adopted);
        System.out.println("Pending                 : " + pending);
        System.out.println("Available               : " + available);
        System.out.println("Still in intake         : " + intake);
    }
}
