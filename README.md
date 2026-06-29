# Assignment 5a/b – Pet Shelter Management System

## Project Overview

This project simulates a basic pet shelter. The shelter takes in animals (dogs, cats, rabbits, etc.), assigns staff to care for them, matches adopters to animals, and tracks what happens to each animal over a 7-day simulation.

Each day (cycle) the shelter processes new intakes, staff do care tasks, and adoption counselors try to match waiting adopters with available animals. The goal is to show how design patterns can organize this kind of simulation without making the code overly complicated.

---

## Planned Design Patterns

### 1. Factory Pattern

I will use a Factory to create Animal objects. Instead of calling `new Dog(...)` or `new Cat(...)` directly all over the code, there will be one `AnimalFactory` class with a `createAnimal(type, ...)` method. This keeps animal creation in one place and makes it easy to add new species later without touching the rest of the code.

**Why it fits here:** The shelter constantly takes in new animals of different types (R1). Having a factory means the intake logic doesn't need to know the details of every animal class - it just calls `AnimalFactory.createAnimal("dog", ...)` and gets back a ready-to-use animal with its zone code and starting status already set.

### 2. Observer Pattern

I will use an Observer pattern so that when an animal's status changes, interested parties are automatically notified. For example, when an animal moves from `intake` to `available`, the adoption counselor is notified and can start matching it. The `Animal` class will hold a list of observers and call `notify()` on each when its status changes.

**Why it fits here:** There are multiple things that need to react to animal status changes - staff assignments, care scheduling, and adoption matching (R1, R2, R3, R4). Instead of having one big method that manually calls everything when a status changes, the Observer pattern keeps each piece of logic separate and only triggers when relevant.

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

### Factory Pattern — chosen over direct constructors

**Alternative considered:** Calling `new Dog(...)`, `new Cat(...)`, `new Rabbit(...)` directly wherever a new animal is needed.

**Pros of direct constructors:** Simpler — no extra class, fewer files.

**Cons of direct constructors:** Every place that creates an animal also has to know the species, assign a zone code, and set the starting status manually. If zone code format changes, every call site breaks. Also makes it easy to forget to register observers.

**Why Factory:** All animal creation goes through one place. Zone codes auto-increment and are always in the right format. Adding a new species like `Bird` only requires one new class and one extra branch in the factory — nothing else in the codebase needs to change.

### Observer Pattern — chosen over direct method calls

**Alternative considered:** After each status change, manually call the relevant staff methods in the simulation loop (e.g. `counselor.addToPool(animal)` after `animal.setStatus("available")`).

**Pros of direct calls:** Easy to trace — you can see exactly what happens after each status change just by reading the loop.

**Cons of direct calls:** The main loop becomes tightly coupled to every staff class. Adding a new observer (e.g. a `NotificationService`) means editing the simulation loop. Status changes in multiple places each need the same manual calls added, making bugs easy to introduce.

**Why Observer:** Each observer manages its own reaction. `Animal.setStatus()` just fires the notification — it does not know or care what happens next. The counselor, vet, and care scheduler each respond independently. This keeps the main loop clean and makes it easy to add new observers without touching existing code.

---

## Design Evolution

### What changed from the initial plan

The initial plan in Deliverable A described the Observer pattern with the `Animal` class holding a list of observers and calling a `notify()` method. The final implementation matches this closely, but a few things changed during development:

- **`notify()` was renamed to `setStatus()`** — combining the status update and notification into one method made more sense than having a separate notify call after every setter. This reduced the chance of forgetting to notify.
- **`CareScheduler` was added as a third observer** — the original plan only mentioned `AdoptionCounselor` and `Veterinarian` as observers. Adding `CareScheduler` as an observer meant the care list updates automatically on every status change with no extra logic in the simulation loop.
- **Adoption return was handled in the main loop rather than by observers** — the initial plan considered having the observer handle failed adoptions automatically. In the final version the main loop explicitly calls `counselor.returnAnimal()` on Day 5, which is simpler and easier to follow for a simulation with a scripted storyline.
- **The zone counter stays inside `AnimalFactory`** — the original concern was whether the counter would reset mid-simulation. Keeping it as an instance variable on the single factory object used throughout the simulation solved this cleanly.

### Hardest part

Getting the observer wiring right so that the matching pool and care list stayed consistent across status transitions was the most challenging part. When an animal goes from `available` to `pending`, both the counselor and the care scheduler need to react correctly — the counselor removes it from the matching pool, but the care scheduler keeps it on the care list (pending animals still need feeding). Getting those two reactions right without them interfering with each other required careful thought about what each observer's `onStatusChange` should do.

---

## Resources Used

- *Head First Design Patterns* (Freeman & Robson) — used to understand the GoF Observer and Factory patterns and confirm the structure followed the specification correctly.
- Oracle Java documentation — referenced for `MessageDigest` SHA-256 usage in `getCredeniaHash()` and `UUID.randomUUID()` in `MedicalLogEntry`.
- Gradle documentation (gradle.org) — referenced for JaCoCo plugin configuration and the `jacocoTestReport` task setup.

---

## Challenges Faced

1. **Observer notification order** — all three observers (`Veterinarian`, `AdoptionCounselor`, `CareScheduler`) are registered on every animal. When `clearAnimal()` calls `setStatus("available")`, all three fire in registration order. The vet's observer prints a note, then the counselor adds the animal to the matching pool. This order matters — if the counselor ran first before the status was actually set, it would miss the animal. Resolved by ensuring `setStatus` updates the field first, then fires observers.

2. **Preventing duplicates in the matching pool and care list** — when an animal's status changes back to `available` after a return, the observer fires again. Without a guard, the animal would be added to both lists a second time. Resolved by checking `if (!list.contains(animal))` before adding in both `AdoptionCounselor` and `CareScheduler`.

3. **Zone code counter persistence** — early on it was unclear where the counter should live so it does not reset between calls. Keeping it as a private instance variable on the single `AnimalFactory` instance created in `Main` meant it persists naturally for the whole simulation without needing any static state.

---

## Usage of AI

All Javadoc comments across the source files in this project were generated by AI.
