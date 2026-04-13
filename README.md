# MVVM Vinyl Shop

Desktop JavaFX application for managing a small vinyl lending library: browse records, add new ones, and drive **reserve**, **borrow**, **return**, and **remove** flows against a shared in-memory model. The project is structured as **MVVM** with explicit **State** objects for lending rules and **JavaBeans `PropertyChangeSupport`** for model notifications.

---

## Overview

This is a single-process, GUI-first assignment-style project. There is **no** client–server layer, **no** sockets, and **no** HTTP API in the repository—the app talks to `VinylModelManager` in the same JVM.

The JavaFX UI is loaded from **`listView.fxml`** (main list and actions) and **`addView.fxml`** (add form). Navigation between screens is handled by **`ViewHandler`**, which swaps scenes on the primary stage (window title: **“Vinyl Library”**).

---

## Key features

- **CRUD-style catalog**: pre-seeded classic albums in `VinylTest.main`, plus **add** via the add screen (`VinylAddViewModel` → `VinylModel.add`).
- **Lending operations** (on the selected vinyl): **reserve**, **borrow**, **return**, **remove**, with feedback via JavaFX **`Alert`** dialogs.
- **Dual selection UI**: `ListView` and `ComboBox` both bound to the same observable list and selection in `listViewController`.
- **State-specific rules**: each vinyl delegates `reserve` / `borrow` / `returnVinyl` / `remove` to a **`VinylState`** implementation (`AvailableState`, `ReservedState`, `BorrowedState`, `BorrowedAndReservedState`).
- **“Pending removal”** semantics: `remove` on a busy record marks the vinyl; when it becomes available again, `VinylModelManager` can drop it from the list (see `returnVinyl` / `remove` in the model manager).
- **Background stress simulation** (optional demo noise): `VinylTest` starts one **thread per simulated person** that repeatedly calls the model while the UI runs (see **Concurrency** below).

---

## Architecture

| Layer | Package / role |
|--------|----------------|
| **Model** | `dk.via.pro2.MVVMVinylShop.model` — domain types (`Vinyl`, `Person`), lending **`VinylState`** hierarchy, **`VinylModel`** interface, and **`VinylModelManager`** (in-memory list + `PropertyChangeSupport`). |
| **View** | `dk.via.pro2.MVVMVinylShop.view` — FXML-backed **`listViewController`** and **`AddViewController`**, plus **`ViewHandler`** for loading FXML and opening views. Controllers wire JavaFX controls to view models; they do not own business rules. |
| **ViewModel** | `dk.via.pro2.MVVMVinylShop.viewmodel` — **`VinylListViewModel`** (list actions, selection, listens to model changes), **`VinylAddViewModel`** (form properties + `addVinyl`), created through **`ViewModelFactory`**. |

Supporting contract in the root package: **`PropertyChangeSubject`** (`addListener` / `removeListener` with `PropertyChangeListener`), implemented by the model surface via **`VinylModel`**.

---

## Design patterns

### MVVM

- **Model**: `VinylModel` / `VinylModelManager` expose data and operations.
- **ViewModel**: `VinylListViewModel` and `VinylAddViewModel` expose JavaFX-friendly state (`ObservableList`, `StringProperty`, etc.) and command-style methods the view calls.
- **View**: FXML + controllers; `ViewHandler` centralizes FXML loading and navigation.

### Observer / property change notifications

- `VinylModelManager` owns a **`PropertyChangeSupport`** instance and fires **`vinylListChanged`** after mutating the vinyl list or underlying vinyl state (see `firePropertyChange` calls).
- `VinylListViewModel` implements **`PropertyChangeListener`** and, on notification, schedules **`refreshVinyls`** on the JavaFX application thread with **`Platform.runLater`** so UI updates stay thread-safe.

### State pattern

- `Vinyl` holds a **`VinylState`** (`lendingState`) and forwards lending operations to it.
- Concrete states **`AvailableState`**, **`ReservedState`**, **`BorrowedState`**, and **`BorrowedAndReservedState`** encapsulate valid transitions and error conditions (`IllegalStateException` messages propagate to the UI).

---

## Concurrency and multithreading

`VinylTest.main` does three things before `launch(args)`:

1. Builds **`VinylModelManager`** and adds **10** hard-coded `Vinyl` instances and **5** named `Person` instances.
2. Registers a **`PropertyChangeListener`** on the model that prints a snapshot of all vinyls to **standard output** whenever the model fires a change.
3. Starts **5** long-lived **`Thread`** instances—one per person—each looping forever (until interrupted), picking a random vinyl and calling **`reserve`**, **`borrow`**, **`returnVinyl`**, or related paths based on that person’s relationship to the record, then sleeping **`100000` ms** between iterations.

`VinylModelManager` synchronizes its public mutators and **`getVinyls()`** with **`synchronized`**, so concurrent simulator threads and the JavaFX thread contend through the same locks. The UI path refreshes via `Platform.runLater` as described above.

There is **no** explicit thread pool, executor service, or shutdown hook for those background threads in the current code.

---

## UI and user workflows

**Main list (`listView.fxml` + `listViewController`)**

- Browse vinyls in a **`ListView`** or **`ComboBox`** (same underlying list).
- Buttons (identified by button text in code): **Reserve**, **Borrow**, **Return**, **Remove**, **Add new Vinyl** — each triggers the corresponding **`VinylListViewModel`** method (except add, which opens the add view).
- Outcome messages are shown in an **`Alert`**.

**Add screen (`addView.fxml` + `AddViewController`)**

- Three **`TextArea`** fields are discovered in document order and **bidirectionally bound** to `VinylAddViewModel` string properties.
- **Add** validates the year as digits, calls **`addVinyl()`**, and returns to the list on success; **Back** resets the form and returns to the list.

**Identity in the GUI**

- The interactive user is a fixed **`Person("GUI", "User")`** inside `VinylListViewModel`, separate from the five simulator `Person` threads.

---

## Project structure

```
Pro2Assignment/
├── Pro2Assignment.iml          # IntelliJ module (source root: src/)
├── .idea/
│   └── misc.xml                # languageLevel / JDK 21 (see Technologies)
└── src/dk/via/pro2/MVVMVinylShop/
    ├── VinylTest.java          # Application entry + seed data + background threads
    ├── PropertyChangeSubject.java
    ├── model/
    │   ├── Vinyl.java
    │   ├── Person.java
    │   ├── VinylState.java
    │   ├── AvailableState.java
    │   ├── ReservedState.java
    │   ├── BorrowedState.java
    │   ├── BorrowedAndReservedState.java
    │   ├── VinylModel.java
    │   └── VinylModelManager.java
    ├── view/
    │   ├── ViewHandler.java
    │   ├── listView.fxml
    │   ├── listViewController.java
    │   ├── addView.fxml
    │   └── AddViewController.java
    └── viewmodel/
        ├── ViewModelFactory.java
        ├── VinylListViewModel.java
        └── VinylAddViewModel.java
```

## Technologies

- **Java**: project metadata in **`.idea/misc.xml`** targets **JDK 21** (`languageLevel="JDK_21"`, `project-jdk-name="liberica-21"`).
- **JavaFX**: UI, FXML, properties, collections (`javafx.*` imports throughout view and viewmodel).
- **JavaBeans**: `java.beans.PropertyChangeListener` / `PropertyChangeSupport` in the model.
- **IntelliJ module**: `Pro2Assignment.iml` declares a project library entry **`gson-2.13.2`**. **Note:** there are **no** `import` statements for Gson (or other third-party libraries) under `src/` in the current codebase—the application logic does not use Gson at runtime unless you add code that does.

---

## Setup and run (IntelliJ)

This repo is an **IntelliJ IDEA Java module**, not a Maven/Gradle project. A practical workflow:

1. **Open** the project folder in IntelliJ (the `.iml` + `.idea` layout is already present).
2. Ensure a **JDK 21** is selected for the project/module (match `.idea/misc.xml` or use your own JDK 21).
3. Configure **JavaFX** for the module (SDK or libraries your course uses). The sources depend on JavaFX; the repository does not vendor JavaFX JARs.
4. Create or adjust a **Run configuration**:
   - **Main class:** `dk.via.pro2.MVVMVinylShop.VinylTest`
   - **Module:** `Pro2Assignment` (or your renamed module, if you change it)
5. Run **`VinylTest`**.

Because there is no committed Maven/Gradle classpath, **exact command-line `javac` / `java` invocations are not documented here**—they depend on where JavaFX is installed on your machine.

---

## Main entry point

| Item | Value |
|------|--------|
| **Class** | `dk.via.pro2.MVVMVinylShop.VinylTest` |
| **Extends** | `javafx.application.Application` |
| **`main`** | Seeds `VinylModelManager`, attaches console listener, starts background threads, then **`launch(args)`**. |
| **`start(Stage)`** | Builds `ViewModelFactory` + `ViewHandler` and shows the JavaFX UI on the seeded model. |

---

## Seeded demo data and runtime behavior

- **Vinyls** (titles / artists / years) are created inline in **`VinylTest.main`** (10 well-known albums).
- **Persons** Alice, Bob, Charlie, Diana, and Erik are created with **`new Person(first, last)`**; static **`Person.nextId`** assigns increasing IDs.
- The **property-change listener** prints **`[MODEL UPDATE]`** blocks to the console listing each vinyl’s title, **`getStateLabel()`**, and who is borrowing or reserving it.
- Background threads log attempts and outcomes to the console; combined with the **100 second** sleep, most of the visible activity during normal use is still **GUI-driven** unless you wait for simulator ticks.

---

## Limitations

- **In-memory only**: restarting the app loses all data; there is no persistence layer in code.
- **Identity / lookup**: `VinylModelManager.findVinyl` matches by **title + artist** only—duplicate title/artist pairs are ambiguous.
- **Simulator threads** run **indefinitely** and are not joined or stopped on window close.
- **`VinylListViewModel`** declares **`errorMessageProperty()`** returning **`null`** (and assigns `errorMessage = null`); list actions rely on **`Alert`** return strings instead—do not expect a bound error label without further implementation.
- **`VinylAddViewModel`** imports **`IntegerProperty` / `SimpleIntegerProperty`** but does not use them in the shown fields (minor dead API surface).
- **No automated tests** are present in the repository (no `src/test` tree or test runners checked in).

---

## Possible future improvements

- Graceful **shutdown** of simulator threads on application exit.
- **Persistence** (file or database) if requirements grow beyond a demo.
- **Build tool** (Maven or Gradle) with a documented JavaFX dependency for reproducible CLI builds.
- **Tests** for state transitions and `VinylModelManager` edge cases (duplicate records, removal while borrowed, etc.).
- Richer **user model** in the GUI (pick among persons instead of a single hard-coded GUI user).

---


