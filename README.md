# Assignment 1 — Tutorial1-HelloWorld

Course: DAM
Student: João Rosa
Date: 03/26
Repository URL: https://github.com/pjoni1/DAM_Trabalhos_Final/tree/master
---
## 1. Introduction
This assignment served as an introduction to the Android development ecosystem and the Kotlin programming language. The main objectives were to configure the development environment (IntelliJ IDEA and Android Studio), understand basic Kotlin syntax, and create a functional application featuring adaptive layouts and customized graphical resources.
---
## 2. System Overview
The solution is divided into three main developmental stages, showcasing a progression from pure logic to complex UI management:

  -Kotlin Fundamental Exercises: A suite of console-based programs focusing on data types, control flow (using when expressions), and robust error handling with try-catch blocks. Key features include a multi-base calculator (Decimal/Hexadecimal) and functional sequence generation.

  -Android Studio Foundational Tasks: Initial implementation of Android components within the Android Studio IDE. These exercises focused on understanding the Activity lifecycle, Project Structure (Manifests, Java/Kotlin sources, and Resources), and basic View interaction.

  -Anti-Gravity Fuel Application: The final project component consisting of a gas price monitor. It features a custom "Anti-Gravity" theme and implements a responsive UI that adapts to different screen orientations (Portrait and Landscape) using specific resource qualifiers and ConstraintLayout.
  
---
## 3. Architecture and Design

The solution follows the standard Android architectural components:

  -Logic Layer: Kotlin files implementing OOP principles (Classes and Objects).
  
  -Presentation Layer: XML-based layouts using ConstraintLayout for a responsive UI.
  
  -Resource Organization: Separation of concerns using res/values for strings/colors and res/layout for UI structure, including specific qualifiers for landscape mode
  
---
## 4. Implementation
The implementation was divided into three distinct modules, focusing on core programming logic and mobile interface design.
### 4.1. Kotlin Fundamentals (Ex. 1-3)

  -Console Calculator: Developed a multi-base calculator using when expressions. It performs arithmetic, boolean, and bitwise operations. To handle the hexadecimal requirement, Float.toBits() and Integer.toHexString() were used to extract the raw memory representation.

  -Physics Simulator: Used generateSequence to calculate ball bounces. 

  -Robustness: Integrated a centralized try-catch block to manage InputMismatchException, preventing crashes during invalid user input.

### 4.2. Android Studio & Anti-Gravity Fuel App

  -UI Architecture: Utilized ConstraintLayout to build the "Anti-Gravity Fuel" interface. The layout presents fuel prices (Gasoline/Diesel) for multiple cities.

  -Adaptability: Created specialized resource qualifiers (e.g., layout-land) to provide a custom experience when the device is rotated, ensuring no overlapping of graphical elements.

  -Resource Management: All text labels and color schemes were offloaded to strings.xml and colors.xml to follow Android best practices for maintainability
  
---
## 5. Testing and Validation
Functional Testing: The calculator was tested with edge cases such as division by zero and extremely large integers to verify bitwise shift behavior.

Input Stress Test: Intentionally entered non-numeric characters into the console to confirm that the try-catch exception handling logic gracefully prompts the error message.

Layout Validation: Used the Android Studio Emulator to verify the "Anti-Gravity" theme across different screen densities. Confirmed that the landscape orientation correctly displays all city data without clipping.

Git Integrity: Verified the commit history through the terminal to ensure all incremental changes were correctly tracked after the initial repository mapping fix.

---
## 6. Usage Instructions
Environment Setup: Ensure IntelliJ IDEA and Android Studio are installed.

Kotlin Exercises: Navigate to the dam package. Right-click on the desired exercise file (e.g., exer_2.kt) and select "Run".

Android Application: Open the AntiGravityFuel project in Android Studio. Wait for the Gradle sync to complete and click the "Run" button (Shift + F10) to deploy to an emulator or physical device.

Requirements: Android API level 30 or higher is recommended for the best experience.

---
# Autonomous Software Engineering Sections - only for [AC OK, AI OK] sections
## 7. Prompting Strategy
The prompting strategy followed a dual-path approach, utilizing both reactive, user-written prompts for technical troubleshooting and a highly structured, role-based "Master Prompt" for the autonomous agent workflow.

  Reactive Technical Prompting (User-Written): Specific prompts were authored to resolve environment-specific "blockers" and syntax questions encountered during the development of the Kotlin exercises. These prompts were direct and targeted at immediate problem-solving.

  Example: "The directory is registered as a Git root, but no repositories were found. How do I fix this mapping in the IntelliJ settings?" or "How can I format a list of Doubles to display only two decimal places in Kotlin?"

  Structured Agentic Prompting (Anti-Gravity Framework): For the development of the final mobile application, a sophisticated prompting technique was used to define the AI as an autonomous software engineering agent. This prompt provided high-level context, specific goals, and strict constraints before any execution began.  

  Strategy Evolution: The strategy evolved from using AI as a simple "search engine" for fixing Git and IDE errors to utilizing it as a high-level architectural consultant. By providing a detailed "Master Prompt" in the Anti-Gravity environment, the interaction shifted from simple code-snippet requests to a professional "Plan-Review-Execute" workflow.
  
---
## 8. Autonomous Agent Workflow
The AI acted strictly as a Technical Assistant and Troubleshooting Consultant, not as a coder for the logic exercises. The workflow was as follows:

  -Infrastructure Support: Resolving the mapping issues between the project directory and the Git repository when the IDE failed to detect the VCS.

  -UI/UX Design Consultation: Providing conceptual guidance on how to organize the "Anti-Gravity" theme and ensuring the responsiveness of the fuel price display.

  -Documentation & Formatting: Assisting in the translation and professional structuring of this report to ensure technical accuracy in the final delivery.
  
---
## 9. Verification of AI-Generated Artifacts
Verification was focused on environment fixes and UI suggestions:

  -Environment Verification: Each Git command suggested by the AI was tested in the terminal to ensure it correctly re-linked the repository without data loss.

  -Manual UI Review: Suggestions regarding layout orientation were manually implemented and adjusted in the Android Studio Layout Editor to match the specific "Anti-Gravity" visual requirements.

  -Fact-Checking: Any technical explanation provided by the AI for the report was cross-referenced with official Android and Kotlin documentation.
  
---
## 10. Human vs AI Contribution
Human: Performed the installation, wrote the core Kotlin exercises, designed the UI manually in the editor, and conducted all emulator tests.
AI: Provided conceptual explanations and helped resolve environment-specific configuration errors.
---
## 11. Ethical and Responsible Use
Risk Mitigation: To avoid the risk of over-reliance, AI was strictly excluded from the logic development and coding phases of the Kotlin exercises and Android tasks. This ensured that the core learning objectives of the course were met through manual implementation.

Handling Limitations: AI suggestions regarding Git repository fixes were carefully cross-referenced with official documentation to ensure that no "black-box" commands were executed without understanding their impact on the local file system.

Bias and Accuracy: I recognized that AI can provide outdated or overly complex UI patterns. Consequently, every suggestion regarding the "Anti-Gravity" app's design was manually filtered and adjusted to match the specific requirements of the assignment's rubric and standard Android best practices.

Transparency: This report serves as a full disclosure of AI involvement, clearly separating autonomous technical troubleshooting from the student’s original software development work.

---
# Development Process
## 12. Version Control and Commit History
Git was used to track progress. The history shows a clear evolution: initial environment setup, Kotlin logic implementation, Android UI design, and finally, resource optimization
---
## 13. Difficulties and Lessons Learned
Git Root Mapping: The main technical hurdle was the IntelliJ VCS synchronization. The project was registered as a root but wouldn't detect commits initially. This was resolved by manually re-linking the directory in the IDE settings and using the terminal to force the tracking of files.

ConstraintLayout Chains: Aligning multiple elements symmetrically in the Anti-Gravity app required learning how to use chains and guidelines effectively to avoid overlapping components during screen rotation.

Android Lifecycle: Understanding how resources are loaded based on qualifiers (layout vs layout-land) was essential to ensure a smooth transition between orientations.

---
## 14. Future Improvements
Real-time API Integration: Currently, the fuel prices are static. A future iteration could integrate a public API (like Preços dos Combustíveis Online) to fetch real-time data.

Persistent Storage: Implementing a Room Database to allow users to save their "Favorite Cities" and track price history over time.

Enhanced Navigation: Adding a RecyclerView to the Anti-Gravity app to support an unlimited number of cities with a smooth scrolling experience.

---
## 15. AI Usage Disclosure (Mandatory)
AI Tools Used: * Gemini 3 Flash: Used via the standard interface for environment troubleshooting and report structuring.

  -Gemini 3.1 Pro: Utilized specifically within the "Anti-Gravity" development environment for advanced UI design consultation and layout optimization.

Scope of Usage: * Technical Troubleshooting: Solving Git root directory mapping errors and IDE configuration issues in IntelliJ IDEA.

  -UI/UX Refinement: Providing conceptual advice for the "Anti-Gravity" fuel app's layout, specifically for responsive design and resource qualifiers.

  -Documentation Support: Assistance in translating, formatting, and professionalizing the English technical report.

Confirmation of Responsibility: I confirm that no AI was used to generate the core logic or code for the Kotlin and Android Studio exercises, which were developed 100% manually. I have reviewed and verified all technical suggestions provided by the AI tools and remain solely responsible for the final artifacts and content of this assignment.
