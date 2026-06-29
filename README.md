# Assignment 5a/b – Pet Shelter Management System

## Screencast

> **Link:** *(add your screencast URL here once uploaded)*

---

## Project Overview

This project simulates a basic pet shelter. The shelter takes in animals (dogs, cats, rabbits, etc.), assigns staff to care for them, matches adopters to animals, and tracks what happens to each animal over a 7-day simulation.

Each day (cycle) the shelter processes new intakes, staff do care tasks, and adoption counselors try to match waiting adopters with available animals. The goal is to show how design patterns can organize this kind of simulation without making the code overly complicated.

---

## Design Patterns - In My Own Words

### 1. Factory Pattern

The Factory pattern is a way of putting all object creation in one place instead of scattering `new` calls everywhere. In this project, `AnimalFactory` has a single `createAnimal(type, id, age, healthStatus, arrivalType)` method. You call it with a string like `"dog"` and it decides which subclass to instantiate (`Dog`, `Cat`, or `Rabbit`), generates the next `SZ-XXX` zone code automatically, and returns a fully set-up `Animal` object ready to use.

Without this pattern, every part of the code that needed a new animal would have to call `new Dog(...)` or `new Cat(...)` directly, know the right zone code format, and remember to keep the counter consistent. Putting all of that in one class means there is only one place to fix if anything changes.

### 2. Observer Pattern

The Observer pattern lets one object (the subject) automatically notify a list of other objects (the observers) whenever something changes, without the subject needing to know anything about who is listening or what they will do.

In this project, `Animal` is the subject. It holds a list of `AnimalObserver` objects. Every time `setStatus()` is called on an animal, it loops through that list and calls `onStatusChange(this)` on each observer. The three observers are `Veterinarian`, `AdoptionCounselor`, and `CareScheduler`. Each one reacts differently to the same event - the counselor updates its matching pool, the care scheduler updates its care list, and the vet logs a note when the animal becomes available. The `Animal` class does not know or care what any of them do; it just fires the notification and moves on.

---

## Integration Strategy

The two patterns work together like this:

1. When a new animal arrives, `AnimalFactory` creates the right animal object and assigns it a `shelterZoneCode`. The animal starts in `intake` status.
2. The `Animal` class has a list of observers. When it is created, a `CareScheduler` observer and an `AdoptionCounselor` observer are registered on it.
3. When a vet clears the animal and its status changes to `available`, the observers are notified automatically - the `AdoptionCounselor` adds it to the matching pool and the `CareScheduler` sets up its daily feeding/cleaning tasks.
4. When an adopter is matched, the status changes to `pending`, which notifies observers again. The counselor stops trying to match this animal.
5. If the adoption fails, the status goes back to `available` and the observers react accordingly.

The Factory handles *creating* animals consistently, and the Observer handles *reacting* to what happens to them over time. Together they cover the full animal lifecycle without the main simulation loop needing to micromanage every detail.

---

## Requirements Focus

These are the 6 functional requirements I plan to fulfill:

- **R1.1** – Start with at least 5 animals. Each animal tracks ID, species, age, health status, and shelter zone code (`SZ-001` through `SZ-005` for the starting animals).
- **R1.2** – One or two new animals arrive each cycle as stray or surrender. The `AnimalFactory` creates them with the next available zone code.
- **R1.3** – Each animal has a status (`intake`, `available`, `pending`, `adopted`) that changes as the simulation runs. Observers react to each change.
- **R2.1** – Two staff roles: `Veterinarian` and `AdoptionCounselor`. The vet clears animals from intake; the counselor handles matching and adoption paperwork.
- **R3.1** – Adopters arrive with preferences (preferred species and age range). Each cycle the counselor checks available animals and tries to find a match.
- **R3.3** – At least one adoption plays out fully - one successful adoption and one returned animal are included in the 7-day run.
- **R4.1** – Basic care (feeding and cleaning) happens every cycle for all animals not yet adopted. The `CareScheduler` observer handles this automatically when an animal becomes available.

*Note: The simulation itself (running 7 cycles with output prefixed `SIMS I`, `SIMS II`, etc.) is a required part of the assignment but does not count toward the 6 requirements above.*

---

## Build Environment

| Tool | Version |
|---|---|
| JDK | Java 18.0.2.1 |
| JUnit | 5.10.0 |
| Checkstyle | 10.12.4 |
| SpotBugs | 6.0.9 (plugin), 4.8.3 (annotations) |
| Gradle | 8.x |

---

## Concerns / Questions

1. **Observer vs just calling methods directly:** For a small simulation, the Observer pattern adds a bit of extra structure. I think it's worth it here because there are genuinely multiple things that react to the same event (status change), but I want to make sure I'm not over-complicating simple cases where only one thing cares.

2. **How many animals arrive per cycle:** The assignment says new animals "can" arrive, not "must" arrive. I plan to have 1 new animal arrive on most cycles and skip a couple of days to make the simulation feel realistic rather than mechanical.

3. **Adoption failure logic:** I want to simulate one returned adoption. The simplest approach is to mark the adoption as failed after a set number of cycles and put the animal back to `available`. I need to decide whether the Observer handles this automatically or if the main loop manages it.

4. **Zone code counter:** Every animal needs a `SZ-XXX` zone code. I plan to keep a simple counter in `AnimalFactory` that increments each time a new animal is created, formatted as a 3-digit number. Need to decide where this counter lives so it doesn't reset mid-simulation.

5. **Keeping output readable:** Each cycle prints quite a bit - intakes, care events, adoptions. I want to make sure the output is clear and easy to follow without being so long that it's hard to grade. I'll keep each event to one line.

---

## Pattern Selection Rationale

### Why Factory Pattern?

**The problem it solves:**
The shelter needs to create many different types of animals throughout the simulation - dogs, cats, and rabbits - each of which is a different subclass of `Animal`. Without a factory, every time the simulation needed a new animal it would have to call `new Dog(...)`, `new Cat(...)`, or `new Rabbit(...)` directly. That means every call site must know which subclass to use, must format the zone code correctly (`SZ-001`, `SZ-002`, etc.), and must keep the counter in sync across the whole program. If the zone code format changed, every call site would need to be updated.

**Why it is a good fit for this domain:**
A real animal shelter receives animals of unknown types at intake - a stray could be any species. The `AnimalFactory` models this naturally. The intake logic just says "a dog arrived" and calls `createAnimal("dog", ...)`. It does not need to know that `Dog` is a subclass of `Animal`, how to format `SZ-006`, or that the counter was last at 5. All of that is encapsulated in one place. This also reflects how shelters actually work: there is one intake process regardless of species. The factory is that single intake process in code form.

**Why it is a good fit for the technical requirements:**
The assignment required `shelterZoneCode` in `SZ-XXX` format with a counter that persists across the entire simulation. Managing that counter in the factory as a private instance variable on the single factory object used throughout `Main` is the cleanest solution. The counter never resets, never duplicates, and is never accessible from outside the factory - which is exactly the encapsulation the pattern provides.

---

### Why Observer Pattern?

**The problem it solves:**
Several different parts of the system need to react whenever an animal's status changes. When an animal becomes `available`, the adoption counselor needs to add it to the matching pool, the care scheduler needs to add it to the daily care list, and the vet needs to log a note. When it becomes `adopted`, the care scheduler needs to remove it. Without the Observer pattern, `setStatus()` would have to manually call methods on the counselor, the care scheduler, and the vet every time it ran - tightly coupling `Animal` to every staff class and making it impossible to add new reactions without editing `Animal` itself.

**Why it is a good fit for this domain:**
An animal shelter is fundamentally event-driven. When an animal's situation changes, many different people and systems react: staff update care schedules, counselors update waiting lists, records are updated. The Observer pattern reflects this directly. The `Animal` is the source of truth for its own status. The staff classes - `AdoptionCounselor`, `CareScheduler`, `Veterinarian` - each implement `AnimalObserver` and register themselves on the animals they care about. When the animal's status changes, all of them are notified automatically, just as in a real shelter where a status board update notifies everyone in the building.

**Why it is a good fit for the technical requirements:**
The assignment required multiple staff roles (R2.1) reacting to animal status changes (R1.3), care running every cycle (R4.1), and matching happening automatically when animals become available (R3.1). All of these are fundamentally reactions to the same event - a status change. The Observer pattern lets each requirement be handled by a separate class, each of which manages its own logic independently. The simulation loop stays clean because it does not manually wire every event to every handler; the observers do that wiring themselves at registration time.

---

## Requirements to Pattern Mapping

| Requirement | Description | Pattern |
|---|---|---|
| R1.1 | 5 starting animals with ID, species, age, health, shelterZoneCode | Factory |
| R1.2 | New animals arrive each cycle as stray or surrender | Factory |
| R1.3 | Animal status changes trigger automatic reactions | Observer |
| R2.1 | Veterinarian and AdoptionCounselor staff roles | Observer |
| R3.1 | Adopters matched to animals by preferences each cycle | Observer |
| R3.3 | One successful adoption and one returned adoption | Observer |
| R4.1 | Daily feeding and cleaning for all non-adopted animals | Observer |

---

## Trade-off Analysis

### Factory Pattern - chosen over direct constructors

**Alternative considered:** Calling `new Dog(...)`, `new Cat(...)`, `new Rabbit(...)` directly at every point in the simulation where a new animal is needed, passing the zone code manually each time.

**Pros of the alternative (direct constructors):**
- Simpler overall structure - no extra `AnimalFactory` class, fewer files.
- Every object creation is visible inline in `Main`, making it obvious what is being created.
- No indirection - the reader does not need to look up what `createAnimal("dog", ...)` actually returns.

**Cons of the alternative (direct constructors):**
- The zone code counter (`SZ-001`, `SZ-002`, ...) would have to be managed manually in `Main` or as a static variable, both of which are fragile. A static variable is global state; managing it in `Main` means `Main` is responsible for a detail that has nothing to do with running the simulation.
- Every call site must know the correct subclass name. If a new species is added, every part of the code that might create that species needs to be updated.
- If the zone code format ever changes (e.g. to `ZONE-001`), every single `new Dog(...)` call in the codebase breaks.
- It becomes easy to accidentally create two animals with the same zone code or skip a number.

**Why Factory was chosen:**
The factory encapsulates all three concerns - which subclass to create, what zone code to assign, and how to format it - in one place. The rest of the code just calls `factory.createAnimal("dog", ...)` and trusts the result. The counter is a private field on the factory instance, so it is impossible to accidentally reset or duplicate it. Adding a `Bird` class later requires only one new subclass and one extra branch in `createAnimal` - nothing in `Main` changes. The factory also makes it straightforward to add more setup logic later (e.g. assigning a random temperament) without touching call sites.

---

### Observer Pattern - chosen over direct method calls in the simulation loop

**Alternative considered:** After every `setStatus()` call in `Main`, manually call the appropriate methods on each staff class. For example: after `animal.setStatus("available")`, call `counselor.addToMatchingPool(animal)`, `careScheduler.addToCareList(animal)`, and `vet.logNote(animal)` explicitly.

**Pros of the alternative (direct calls):**
- Completely transparent - reading `Main` shows exactly what happens after every status change.
- No indirection - no observer interface to understand, no registration step.
- Easier to write for a very small simulation with only a few status changes.

**Cons of the alternative (direct calls):**
- `Main` becomes tightly coupled to every staff class. It needs to import and know the API of `AdoptionCounselor`, `CareScheduler`, and `Veterinarian` just to call them after every status change.
- If a status change happens in multiple places (e.g. `returnAnimal()` also calls `setStatus("available")`), all the manual follow-up calls have to be duplicated or centralized, which creates more coupling.
- Adding a fourth observer - for example a `ShelterReportLogger` - requires editing `Main` and every place that changes status, rather than just registering the new observer once.
- It is easy to forget one of the manual calls, especially for less common transitions like `pending` → `available` on a return. The Observer pattern makes missing a reaction impossible - if you are registered, you are notified.

**Why Observer was chosen:**
The Observer pattern inverts the dependency: instead of `Main` knowing about all staff classes, each staff class knows about `Animal` through the `AnimalObserver` interface. `Main` registers observers once at startup and then never has to think about them again. When `setStatus("available")` is called anywhere - whether from `clearAnimal()`, `returnAnimal()`, or `Main` directly - all registered observers fire automatically. This also makes the code easier to test: each observer can be tested independently by just calling `onStatusChange()` with a mock animal, without running the whole simulation.

---

## Design Evolution and Process

### Initial Plan (Deliverable A)

The initial design in Deliverable A outlined the following structure:
- An `AnimalFactory` with a `createAnimal(type, ...)` method producing subclasses of `Animal`.
- An `Animal` class with a list of observers and a separate `notify()` method to call after status changes.
- Two observers: `AdoptionCounselor` and `Veterinarian`.
- A 7-day simulation loop with `SIMS I` through `SIMS VII` output.

The initial design also left several open questions: where the zone code counter lives, whether adoption returns are handled by observers or the main loop, and how many animals arrive per cycle.

### What Changed During Implementation

**1. `notify()` merged into `setStatus()`**

The original plan had a separate `notify()` method on `Animal` that would be called after each status update. During implementation it became obvious this was a footgun - you could call `status = "available"` and forget to call `notify()`, and nothing would break visibly until a test caught the missing observer notification. Merging both into `setStatus()` made it impossible to update the status without notifying observers, removing an entire category of bugs.

**2. `CareScheduler` added as a third observer**

The original plan only registered `AdoptionCounselor` and `Veterinarian` as observers. When implementing R4.1 (daily care), the first instinct was to loop over all animals in `Main` and call a care method. But this required `Main` to keep track of which animals were eligible for care. Adding `CareScheduler` as a third observer meant it maintained its own care list automatically - animals are added when they become `available` or `pending`, and removed when they are `adopted`. This eliminated a whole tracking concern from `Main`.

**3. Adoption return handled in the main loop, not by an observer**

The initial plan considered an observer-driven return: some mechanism that would automatically detect a failed adoption after a number of cycles and trigger `returnAnimal()`. In practice, this added complexity without benefit for a scripted 7-day simulation. The final version simply calls `counselor.returnAnimal(a3)` on Day 5 in `Main`. This is honest about what the simulation is doing - it is a scripted story, not an autonomous system - and is much easier to read and debug.

**4. Zone counter as a factory instance variable**

The initial concern was whether the counter would reset mid-simulation. Three options were considered: a static field on `AnimalFactory`, a static field on `Animal`, or an instance variable on the factory. Static fields create shared mutable state that is hard to reset between tests. An instance variable on the single factory object in `Main` solved the problem cleanly - the counter persists for the whole simulation because the same factory instance is used throughout, and it resets naturally when a new `AnimalFactory` is created in tests.

### What Would Be Done Differently

If starting over, the `ISMem` interface would be extracted earlier. Both `Veterinarian` and `AdoptionCounselor` duplicate the exact same SHA-256 hashing logic in `getCredeniaHash()`. An abstract base class `StaffMember` implementing `ISMem` with the shared hashing logic would have eliminated that duplication. The decision to keep them separate was made to avoid introducing an extra layer of abstraction, but the duplication is a real maintenance cost.

### Planning Notes

Initial class diagram sketch (text form):

```
Animal  ──────────────────────────────────────────────
  fields: id, species, age, healthStatus,             |
          shelterZoneCode, status, arrivalType         |
  observers: List<AnimalObserver>                     |
  setStatus(newStatus) → notifies all observers        |
                                                      |
AnimalFactory                    AnimalObserver (interface)
  zoneCounter: int                onStatusChange(Animal)
  createAnimal(type, ...) → Dog/Cat/Rabbit       |
                                     ┌─────────────────┤
                              Veterinarian  AdoptionCounselor  CareScheduler
                              implements    implements          implements
                              ISMem +       ISMem +             AnimalObserver
                              AnimalObserver AnimalObserver
```

The sketch drove the decision to make `Animal` hold the observer list directly rather than using a separate `EventBus` or `Subject` class, keeping the structure as simple as possible.

---

## Resources Used

**1. *Head First Design Patterns* - Freeman & Robson (O'Reilly)**

This book was the primary reference for understanding the GoF Observer and Factory patterns. The Observer chapter showed the specific problem with tight coupling between subjects and observers, and the Factory chapter clarified the difference between a Simple Factory (what this project uses) and the full Factory Method pattern. Reading both chapters confirmed that `Animal` holding the observer list directly - rather than going through a separate subject class - was the correct simplified structure for this scale of project. The book's emphasis on "program to interfaces, not implementations" directly influenced the decision to make `AnimalObserver` an interface rather than an abstract class.

**2. Oracle Java SE 18 Documentation - `java.security.MessageDigest`**

Referenced for the SHA-256 implementation in `getCredeniaHash()` in both `Veterinarian` and `AdoptionCounselor`. The documentation showed the correct way to get a `MessageDigest` instance by algorithm name, digest a byte array, and convert the resulting bytes to a hex string. This influenced the specific loop used: `String.format("%02x", b)` for each byte to ensure each byte always produces exactly two hex characters, keeping the output a consistent 64-character string regardless of leading zero bytes.

**3. Oracle Java SE 18 Documentation - `java.util.UUID`**

Referenced for `UUID.randomUUID()` used in `MedicalLogEntry`. The documentation confirmed that `randomUUID()` generates a cryptographically strong pseudo-random UUID and that each call is guaranteed to produce a unique value with overwhelming probability. This was the reason it was chosen for `validationToks` - the assignment required a UUID parameter, and `randomUUID()` is the standard way to generate one that is meaningfully unique per log entry.

**4. Gradle JaCoCo Plugin documentation (docs.gradle.org)**

Referenced for configuring the `jacoco` plugin, the `jacocoTestReport` task, and the `classDirectories` exclusion pattern used to exclude `Main.class` from coverage calculation. The documentation showed the `afterEvaluate` block pattern needed to filter class directories after the build has been evaluated, which was necessary because the exclusion needs to run after the test classes are compiled. Without this reference, it would not have been clear that a simple `exclude` on the plugin block is insufficient - the exclusion must be applied to the report task's `classDirectories`.

---

## Challenges Faced

**1. Observer notification order causing stale state reads**

All three observers fire in the order they were registered when `setStatus()` is called. During early testing, it was noticed that the `Veterinarian` observer was reading the animal's status to check if it was `"available"` - but at the time the observer fires, the status has already been updated by `setStatus()`. This was fine, but required verifying explicitly that `this.status = newStatus` happens *before* the observer loop, not after. If the order were reversed - notify first, then update - every observer would read the old status. The current implementation in `Animal.setStatus()` updates the field first, then iterates the observer list, making the new status visible to all observers when they call `animal.getStatus()`.

**2. Preventing duplicate entries in matching pool and care list on status revisits**

When an animal is returned from a failed adoption, its status goes back to `"available"`. This triggers `onStatusChange()` on the `AdoptionCounselor` and `CareScheduler` again, which would normally add the animal to their lists a second time - creating duplicate care runs and duplicate match attempts. This was caught by tracing the simulation output and noticing animals being fed twice per cycle after a return. The fix was an `if (!list.contains(animal))` guard in both `onStatusChange()` methods before adding the animal. Simple and effective, but it required tracing the full sequence of status transitions to realize the observer would fire more than once for the same animal.

**3. Making `runMatching()` skip already-matched adopters correctly**

Early versions of `runMatching()` did not check `adopter.isAdopted()` before trying to match. This caused already-completed adopters to match again in the next cycle if a new available animal appeared. For example, Maria Santos would match with A001 on Day 1, but on Day 2 she would match again with A004. The fix was adding `if (adopter.isAdopted()) { continue; }` at the top of the adopter loop. The `isAdopted` flag on `Adopter` was specifically added for this reason - it is set by `completeAdoption()` and checked by `runMatching()` to skip adopters who are done.

**4. Zone code counter not persisting across factory calls in early design**

An early approach kept the counter as a local variable inside `createAnimal()`, which meant every animal got `SZ-001`. Moving it to a field on the `AnimalFactory` instance fixed this, but raised the question of what happens if a new `AnimalFactory` is created mid-simulation. The solution was to create exactly one `AnimalFactory` at the start of `Main` and reuse it for all eight animals across the simulation. This made the counter naturally persistent without any static state or singleton workaround.

---

## Usage of AI

All Javadoc comments across the source files in this project were generated by AI.
