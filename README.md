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

## Concerns / Questions

1. **Observer vs just calling methods directly:** For a small simulation, the Observer pattern adds a bit of extra structure. I think it's worth it here because there are genuinely multiple things that react to the same event (status change), but I want to make sure I'm not over-complicating simple cases where only one thing cares.

2. **How many animals arrive per cycle:** The assignment says new animals "can" arrive, not "must" arrive. I plan to have 1 new animal arrive on most cycles and skip a couple of days to make the simulation feel realistic rather than mechanical.

3. **Adoption failure logic:** I want to simulate one returned adoption. The simplest approach is to mark the adoption as failed after a set number of cycles and put the animal back to `available`. I need to decide whether the Observer handles this automatically or if the main loop manages it.

4. **Zone code counter:** Every animal needs a `SZ-XXX` zone code. I plan to keep a simple counter in `AnimalFactory` that increments each time a new animal is created, formatted as a 3-digit number. Need to decide where this counter lives so it doesn't reset mid-simulation.

5. **Keeping output readable:** Each cycle prints quite a bit - intakes, care events, adoptions. I want to make sure the output is clear and easy to follow without being so long that it's hard to grade. I'll keep each event to one line.
