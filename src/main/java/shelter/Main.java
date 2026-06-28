package shelter;

/**
 * Smoke test entry point for Day 2.
 * Creates a few animals, attaches all observers, and verifies observer output fires correctly.
 */
public class Main {

    /**
     * Runs a quick smoke test wiring up animals, staff, and observers.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        System.out.println("=== Day 2 Smoke Test ===");
        System.out.println();

        AnimalFactory factory = new AnimalFactory();
        Veterinarian vet = new Veterinarian("Dr. Patel");
        AdoptionCounselor counselor = new AdoptionCounselor("Alex Kim");
        CareScheduler careScheduler = new CareScheduler();

        // Create 3 animals and attach all observers
        Animal a1 = factory.createAnimal("dog", "A001", 3, "healthy", "surrender");
        Animal a2 = factory.createAnimal("cat", "A002", 5, "healthy", "stray");
        Animal a3 = factory.createAnimal("rabbit", "A003", 2, "healthy", "surrender");

        for (Animal a : new Animal[]{a1, a2, a3}) {
            a.addObserver(vet);
            a.addObserver(counselor);
            a.addObserver(careScheduler);
        }

        // Register one adopter
        Adopter adopter = new Adopter("Maria Santos", "Dog", 1, 5);
        counselor.registerAdopter(adopter);

        // Vet clears animals — observers should fire for each status change
        System.out.println("--- Vet clears A001 ---");
        vet.clearAnimal(a1);

        System.out.println();
        System.out.println("--- Vet clears A002 ---");
        vet.clearAnimal(a2);

        System.out.println();
        System.out.println("--- Counselor runs matching ---");
        counselor.runMatching();

        System.out.println();
        System.out.println("--- Care scheduler runs daily care ---");
        careScheduler.runDailyCare();

        System.out.println();
        System.out.println("--- Counselor completes adoption for A001 ---");
        counselor.completeAdoption(a1, adopter);

        System.out.println();
        System.out.println("--- Care after adoption (A001 should be gone) ---");
        careScheduler.runDailyCare();

        System.out.println();
        System.out.println("--- Simulate return of A002 ---");
        counselor.returnAnimal(a2);

        System.out.println();
        System.out.println("--- Care after return (A002 back on list) ---");
        careScheduler.runDailyCare();
    }
}
