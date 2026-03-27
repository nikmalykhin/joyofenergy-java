# Engineering Standards

## Workflow Protocol
- **Plan First:** Before modifying any files, present a concise plan of work and wait for my explicit approval.
- **Incremental Changes:** Apply changes to only one file at a time.
- **Chunking:** Limit each change to 20–30 lines of code. I require small increments to facilitate thorough review.
- **Order of Operations:** Complete the implementation and verify success (Green) before initiating any refactoring.

## Methodology & Architecture
- **TDD:** Follow the Red-Green-Refactor cycle. Always suggest the test implementation first.
- **The Boy Scout Rule:** Leave code cleaner than you found it, but keep cleanup logic secondary to the primary task.
- **Separation of Concerns:** Maintain a strict boundary between domain logic and external infrastructure/services.

## Communication Style
- **Tone:** Technical, professional, and concise.
- **Style:** No jokes, no emojis, and no "for dummies" or over-simplified explanations.
- **Content:** Focus on design trade-offs and technical alternatives. If I require more detail, I will ask. Enough thinking.

## Developer Workflows

- **Build:** Use Gradle (`./gradlew build`) for compilation. Build scripts: `build.gradle.kts`, `settings.gradle.kts`.
- **Test:** Run unit tests (`./gradlew test`), functional tests (`./gradlew functionalTest`), or both (`./gradlew check`). Reports: `build/reports/`, `build/test-results/`.
- **Acceptance:** Use scripts in `scripts/` (e.g., `acceptance.sh`) for acceptance testing. These scripts run the app, post sample data, and verify API responses.
- **Run:** Start the app with `./gradlew bootRun` or `java -jar build/libs/developer-joyofenergy-java.jar` (default port 8080).
- **Debug:** Standard Java debugging applies; source and test structure is conventional.

## Conventions & Patterns

- **Strict Package Structure:** All code and tests must follow the established package hierarchy. Example: `main/uk/tw/energy/service/PricePlanService.java` and `test/uk/tw/energy/service/PricePlanServiceTest.java`.
- **Test Naming:** Test classes mirror the names and packages of the classes they test.
- **Reports:** HTML and XML reports are generated for tests and can be found in `build/reports/` and `build/test-results/`.
- **Scripts:** Use provided scripts in `scripts/` for non-standard workflows.
- **External Dependencies:** Managed via Gradle; see `build.gradle.kts` for details.

---

**Reference Files & Directories:**

- `main/uk/tw/energy/` — core source code
- `test/uk/tw/energy/` — unit tests
- `functionalTest/`, `src/functional-test/` — functional tests
- `build.gradle.kts`, `settings.gradle.kts` — build configuration
- `scripts/` — custom workflow scripts
- `build/reports/`, `build/test-results/` — test and build reports
