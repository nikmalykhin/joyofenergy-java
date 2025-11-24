
# Copilot Instructions for joyofenergy-java

## Workflow Rules

1. **Two-Phase Workflow**:  
	 - **Phase 1:** Propose a detailed plan for any significant change or feature.  
	 - **Phase 2:** Write code only after plan approval.  
	 *Why?* Prevents rushed implementations and enforces "Measure Twice, Code Once".

2. **Package Structure**:  
	 - All code must use the `uk.tw.energy` package structure.  
	 *Why?* This corrects past mistakes and maintains consistency.

3. **Small Steps**:  
	 - Keep methods under 20 lines when possible.  
	 - Break logic into private helper methods.  
	 *Why?* Supports maintainability and prepares for "Sized Steps" methodology.

4. **Baseline Safety**:  
	 - Every new feature must include at least one "Happy Path" unit test.  
	 *Why?* Ensures new code is immediately runnable and safe.

## Architecture Overview

- **Domain**:  
	- Core business logic is in `main/uk/tw/energy/domain/`.
- **Controllers**:  
	- API endpoints are in `main/uk/tw/energy/controller/`.
- **Services**:  
	- Application logic and integrations are in `main/uk/tw/energy/service/`.
- **Generators**:  
	- Data generation utilities are in `main/uk/tw/energy/generator/`.

## Developer Workflows

- **Build**:  
	- Use `./gradlew build` to compile and test.
- **Run**:  
	- Use `./gradlew bootRun` or `java -jar build/libs/developer-joyofenergy-java.jar` (after build).
- **Testing**:  
	- Unit tests: `./gradlew test`  
	- Functional tests: `./gradlew functionalTest`  
	- All checks: `./gradlew check`
- **API Testing**:  
	- Use `curl` commands from README to interact with endpoints.

## Project-Specific Patterns

- **Smart Meter IDs**:  
	- Use IDs from the README for test data and API calls.
- **No Zero Readings**:  
	- Power readings are always non-zero for simplicity.
- **Price Plan Logic**:  
	- Price plan comparisons and recommendations are exposed via `/price-plans/compare-all/{smartMeterId}` and `/price-plans/recommend/{smartMeterId}`.

## Key Files & Directories

- `README.md`: Architecture, API, and workflow reference.
- `.github/copilot-instructions.md`: AI agent rules.
- `main/uk/tw/energy/`: Source code, organized by domain, controller, service, generator.
- `test/uk/tw/energy/`: Unit tests.
- `functionalTest/uk/tw/energy/`: Functional tests.

## Conventions

- **Gradle Wrapper**:  
	- Always use `./gradlew` for builds and tests.
- **Java Version**:  
	- Project requires Java 21.
