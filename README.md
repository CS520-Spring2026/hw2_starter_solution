# hw2_starter_solution

# CS520 Spring 2026 - Homework 2 Solution

## Overview and Goal
In this assignment, you will work with an existing implementation of an Expense Tracker application. This application applies the MVC architecture pattern.
Rather than building brand-new user features, your task is to understand, analyze, and restructure the system using sound software engineering principles.

Treat this as a realistic on-boarding task: you are joining a team with shipped software, and your responsibility is to improve understandability, modularity, extensibility readiness, and testability while preserving current behavior. 

## Getting Started
1. Clone the repository: `git clone https://github.com/CS520-Spring2026/hw2_starter_solution`
2. Read this `README.md` file.
3. Build, test, and run the application using the commands below.
4. Explore source code in `src/` and tests in `test/`.

We'll use the ant build tool (`https://ant.apache.org/manual/installlist.html`) to build and run the application.

## Optional Working Files
You may draft your answers in local markdown files (for example under `docs/`) while working, but these files are optional and are not required for grading.
All written graded content must appear in `HW2_answers.pdf`.

## Build and Run

The Expense Tracker application has the following structure:
- `bin/`: Contains the generated class files
- `jdoc/`: Contains the generated javadoc folders/files
- `lib/`: Contains the third party software jar files
- `src/`: Contains the Java folders and source files
- `test/`: Contains the JUnit test suite source files
- `build.xml`: Is the ant build tool input file
- `build/`: Contains the ant build tool output files
- `coverage_report/`: Contains the test coverage report

The build requirements are:
- JDK 21+: Generate API doc (javadoc), compile (javac), run (java)
- Ant 1.10.15+: Build and run the application and test suite(s)

From the root directory (containing the build.xml file):

1. Build app: `ant compile`

<!-- 2. Run the app: `java -cp bin ExpenseTrackerApp` -->
2. Run the app:
   - On Windows: `java -cp "bin;lib\xchart-3.8.8.jar" ExpenseTrackerApp`
   - On Linux/macOS: `java -cp "bin:lib/xchart-3.8.8.jar" ExpenseTrackerApp`

3. Build and run tests: `ant test` (See the build/TEST-*.txt files for more details.)

4. Generate the test coverage report: `ant coverage.report` (See `coverage_report/index.html` for more details.)

5. Generate Javadoc: `ant document`

6. Perform linting `ant lint`

7. Clean generated artifacts (e.g., class files, javadoc files): `ant clean`

# Architecture

The Expense Tracker application applies the MVC architecture pattern as follows:
* model package: Contains the data model represented as a list of transactions
* view package: Contains the visualizations of the model and supports user interactions
* controller package: Contains the application logic to process the user interactions

# Features

* **Add Transaction:**
  Enter a valid amount and category, then click **Add Transaction**.
  The valid transaction appears in the list, and the total cost updates automatically.

* **Delete Transaction:**
  Select a valid transaction from the list.
  In the 'Edit' menu, select the 'Delete' menu item.
  The valid transaction disappears from the list, and the total cost updates automatically.

* **Save the Transaction List:**
  In the 'File' menu, select the 'Save As...' menu item
  In the Save dialog, first select a valid output file and then click the 'Save' button

* **Open a Transaction List:**
  In the 'File' menu, select the "Open File..." menu item
  In the Open dialog, first select a valid input file and then click the 'Open' button

* **Analyze:**
  First select the 'Analysis' tab.
  In that tab, select the analysis time window (e.g., 'Last week')
  In the tab, click the 'Analyze' button
  If there are transactions in that time window, displays the analysis results. If not, displays an error message.

## Solution

**Program Comprehension**
The three required IDE screenshots are already present in the starter solution folder:

1. Type hierarchy for `CSVExporter`

   <img src="screenshots/typehierarchy_csvexporter.png" alt="Type hierarchy for CSVExporter" width="320" />

2. Call hierarchy for `CSVExporter`

   <img src="screenshots/callhierarchy_csvexporter.png" alt="Call hierarchy for CSVExporter" width="320" />

3. References to `COMMA_SEPARATOR` in `CSVConstants`

   <img src="screenshots/reference_to_commaseparator.png" alt="References to COMMA_SEPARATOR" width="320" />

The type hierarchy shows that `CSVExporter` is a concrete exporter strategy implementing `TransactionExporter`, which clarifies its role in the design. 
The call hierarchy shows where export behavior is triggered, which helps trace control flow from the UI/controller into the model/export layer. 
The constant-reference view shows where CSV formatting concerns are centralized and reused, which improves maintainability and reduces duplication.


**Documentation**

1. The README was updated to describe the analysis/data-visualization feature and to reflect on testing and XChart usage: [README.md](/c:/Users/mtasmin/Downloads/CS520/sp26/hw2_starter_solution/README.md:88).  
2. API comments were added for the new strategy abstraction and related methods, for example in [TransactionExporter.java](/c:/Users/mtasmin/Downloads/CS520/sp26/hw2_starter_solution/src/model/TransactionExporter.java:4) and [CSVExporter.java](/c:/Users/mtasmin/Downloads/CS520/sp26/hw2_starter_solution/src/model/CSVExporter.java:7).  
3. Generated Javadoc is present under [jdoc](</c:/Users/mtasmin/Downloads/CS520/sp26/hw2_starter_solution/jdoc>).

**Git Log**
The repository history is incremental and task-oriented. The main commits correspond to importing the starter, extending model tests, adding the visualization feature, updating README documentation, refining coverage, and adding the end-to-end tests.

**Extensibility: Data Visualization**

**Strategy Design Pattern**
The extensibility work is centered on separating the export abstraction from its CSV implementation.

The strategy interface is [TransactionExporter.java](/c:/Users/mtasmin/Downloads/CS520/sp26/hw2_starter_solution/src/model/TransactionExporter.java:7). It defines the common operation:
`String exportTransactions(List<Transaction> txns, String filename);`

The current concrete strategy is [CSVExporter.java](/c:/Users/mtasmin/Downloads/CS520/sp26/hw2_starter_solution/src/model/CSVExporter.java:10), which implements `TransactionExporter`. This design improves extensibility because new exporters such as `JSONExporter` or `XMLExporter` can be added without changing clients to depend on a CSV-specific type. The controller can work against the strategy interface rather than a single file format implementation.

The `AnalysisPanelView` also uses the XChart library to strategize the chart or charts used in the data visualization.

The new data-visualization feature is implemented in:
- [AnalysisPanelView.java](/c:/Users/mtasmin/Downloads/CS520/sp26/hw2_starter_solution/src/view/AnalysisPanelView.java:32)
- [DataVizUtils.java](/c:/Users/mtasmin/Downloads/CS520/sp26/hw2_starter_solution/src/view/DataVizUtils.java:18)
- [ExpenseTrackerController.java](/c:/Users/mtasmin/Downloads/CS520/sp26/hw2_starter_solution/src/controller/ExpenseTrackerController.java:18)

`AnalysisPanelView` handles the visualization UI, chart creation, and error display. `DataVizUtils` computes the category summary for the selected time window. The controller wires the Analyze button to `performDataAnalysis()`.

The XChart library also applies the Template Method design pattern. For example, [`CategoryChart`](https://javadoc.scijava.org/XChart/org/knowm/xchart/CategoryChart.html) extends the `Chart` base class.

**Reflection on XChart**

We used the XChart library for the data visualization in the analysis tab.

Here are some aspects that made it easier to use:
* Deployability: Installed as a single jar file located in `lib/xchart-3.8.8.jar`.
* Understandability: It provides documentation, examples, and API documentation, which made it easier to learn the chart-building workflow.
* UI integration: The chart can be displayed directly in Swing through `XChartPanel`, which fit the existing application structure.
* ...

Here are other aspects that made it harder to use:
* There were many chart choices to evaluate before selecting one that matched the expense-category summary.
* Each chart supports many styling options through the `Styler` API, which increases flexibility but also increases setup complexity.
* The chart data had to be transformed into the exact XChart series format before it could be rendered.
* ...

**Testability**

* Have modular application code (e.g., model, view, controller)
* Use automated testing tools (e.g., ant, junit)
* Have different levels of testing (e.g., unit, end-to-end)
* Have independent test cases (e.g., unit test cases for model)
* ...

**Unit Test Suite**
The model-related tests are in [ExpenseTrackerTest.java](/c:/Users/mtasmin/Downloads/CS520/sp26/hw2_starter_solution/test/ExpenseTrackerTest.java:51). They cover:
- initial model state
- invalid transaction amount
- invalid transaction category
- add transaction
- remove transaction with invalid low/high IDs
- successful remove transaction
- export/import round-trip

These tests follow the common test template by checking setup/preconditions, invoking the unit under test, and then checking postconditions. Helper methods such as `checkTransaction(...)` and `testAddTransactionHelper(...)` reduce duplication while keeping assertions explicit.

The coverage requirement for `ExpenseTrackerModel` and `Transaction` is satisfied. In the JaCoCo report:
- `ExpenseTrackerModel`: 100% coverage
- `Transaction`: 100% coverage

This evidence is in the generated report under [coverage_report/model/index.html](</c:/Users/mtasmin/Downloads/CS520/sp26/hw2_starter_solution/coverage_report/model/index.html>). Overall project coverage is 86%, while the specific required classes exceed the threshold.

Coverage screenshot:

<img src="screenshots/coverage_report.png" alt="Coverage report screenshot" width="360" />

**End-to-End Test Suite**
The two required end-to-end data-visualization tests are implemented in:
- `testAnalyzeHasNoTransactionsE2E()`: [ExpenseTrackerTest.java](/c:/Users/mtasmin/Downloads/CS520/sp26/hw2_starter_solution/test/ExpenseTrackerTest.java:211)
- `testAnalyzeHasTransactionsE2E()`: [ExpenseTrackerTest.java](/c:/Users/mtasmin/Downloads/CS520/sp26/hw2_starter_solution/test/ExpenseTrackerTest.java:236)

Test runner screenshot:

<img src="screenshots/testrunner_alltestcasepassing.png" alt="Test runner showing all test cases passing" width="360" />

**Usability: AI Assistant**

Here is one possible AI assistant design:
* The user could interact with an AI assistant through a chat box in the application.
* The assistant could answer questions about the user's expenses, summarize trends, or help extend the `ExpenseTrackerApp`.
* Another option would be to use an AI library to extend the `ExpenseTrackerApp` with a feature such as text-to-speech, a machine learning predictive model, or an LLM chatbot.

A possible Java library to use would be the OpenAI Java SDK.
* It would allow the application to connect to an LLM service.
* It could support a chatbot-style assistant for answering user questions about spending data.
* It could also help developers extend the application with AI-assisted features.

One risk or challenge is that the AI assistant could produce incorrect or misleading output.
* A user might trust an incorrect spending summary or recommendation.
* A developer would need to validate the AI output and handle errors carefully.
