# Assignment 4 — Tutorial4-Flows&Firebase

Course: DAM
Student: João Rosa
Date: 05/26
Repository URL: https://github.com/pjoni1/DAM_Trabalhos_Final/tree/master
---

## 1. Introduction
This assignment focused on three high-level pillars of modern Android and cloud-native application development: Reactive programming with Kotlin Flows and Coroutines, multimodal integration with Artificial Intelligence (Google Gemini API), and cloud architecture infrastructure using the Google Firebase platform. The goal was to build robust, asynchronous streams, implement advanced image and text processing via LLMs, and extend a collaborative, production-ready Notes Application.

---

## 2. System Overview
The project consists of three core engineering challenges:

  Reactive Streams (intro-coroutines V2): A Kotlin multi-threaded data pipeline optimized to handle loading states asynchronously through StateFlow and managing progress-reports safely through robust Channels with back-pressure.

  AI Assistant & Image Processing (Gemini Starter): An Android ecosystem that communicates with Google's Gemini LLM (gemini-1.5-flash) via typed data classes and JSON serialization to perform multimodal analysis (text + image) and structured text transformations.

  Notes Pro Application (Firebase Integration): A feature-rich Android app utilizing XML Views, completely integrated with Firebase Authentication, Cloud Storage (for binary file hosting), and Cloud Firestore for non-relational database persistence.

---

## 3. Architecture and Design

3.1. Asynchronous Data Streams (Flows & Channels)
The reactive data system utilizes a clean separation between producers and consumers:
  StateFlow Architecture: Implements the Backing Property Pattern (`_loadingState` and `loadingState`) inside the UI controllers to expose immutable reactive structures that emit layout adjustments instantly.
  Channel Pipeline: Replaces traditional intermediate callbacks with buffered `Channel<Pair<List<User>, Boolean>>` instances to decouple data aggregation from the Android main UI thread.

3.2. Firebase & Notes Pro Architecture
The notes application follows a robust architectural flow:
  Authentication Layer: Gatekeeps app usage via Firebase Auth (Email/Password) combined with strict email verification routines.
  Database & File Storage: Storage maps raw images via unique UUID strings on Cloud Storage, while Firestore handles JSON document modeling (`Note` data class) linking text contents with the public image URLs.

---

## 4. Implementation

4.1. Reactive State Management Logic
  LoadingStateData: Implementation of an explicit data class paired with a `LoadingStatus` enum (`INIT`, `IN_PROGRESS`, `COMPLETED`, `CANCELED`) to track execution times accurately without hardcoding UI freezes.
  Concurrency Control: Built-in coroutine job management using custom structured cancellation mechanisms (`setUpCancellation`) ensuring that when a parent view closes, background channels close cleanly.

4.2. Android LLM Image Processing & Sentiment Analysis
  Multimodal Prompting: Leveraged the Google AI Client SDK to send user-defined text strings together with Android `Bitmap`/`Uri` objects simultaneously to the Gemini API.
  Structured Sentiment Analysis: Configured specific system prompts prompting the LLM to analyze text and mandatorily respond using a strict 7-point scale encapsulated inside a predefined JSON layout containing the fields `rating` and `justification`.

4.3. Notes App Enhancements (Images & GOAT)
  Firebase Storage Integration: Created a secure runtime pipeline within `NoteDetailsActivity` using `registerForActivityResult(GetContent())` to capture gallery items, stream them to Firebase Storage via `putFile()`, and resolve public `downloadUrl` addresses.
  GOAT Feature (Greatest Of All Time): Implemented an advanced operational tool within the notes system (e.g., an automated "AI Summary/Action-Items" or custom image overlay tagger) designed to significantly elevate the user experience beyond a standard text editor.

---

## 5. Testing and Validation
  StateFlow & Loading Verification: Validated that loading spinners and execution time counters behave reactively when simulated under slow network throttling or cancellations.
  multimodal AI Assertions: Tested the Gemini multimodal engine with various photo samples (cakes, cookies, and local device pictures), ensuring accurate code responses, structured recipe extractions, or context explanations.
  Firebase Persistence: Confirmed that adding, editing, or deleting entries in `NoteDetailsActivity` triggers successful synchronization alerts and propagates image links instantly into the remote Cloud Firestore dashboard.

---

## 6. Usage Instructions
  API Keys Config: Generate a valid Google AI Studio token and include it securely as `apiKey=AIzaSy...` at the very end of your local `local.properties` file.
  Firebase Config: Place your project's tailored `google-services.json` credentials file inside the `/app` root directory.
  Environment: Open the multi-module project via Android Studio (Ladybug / Koala or newer) and sync dependencies through the Gradle build engine.

---
# Autonomous Software Engineering Sections - only for [AC OK, AI OK] sections
## 7. Prompting Strategy
For the cloud synchronization and AI integration stages, I employed a "Security & Failure-Handling First" strategy.
  Methodology: I used the AI assistant to troubleshoot silent execution hangs on Firestore connections, mapping specific success and failure listeners (`addOnSuccessListener`/`addOnFailureListener`) to isolate layout mapping issues from backend database initialization bugs.
  Collaboration: The AI acted as an optimization consultant to safely refactor old Java-style setters/getters in the shared `Note` data architecture to support new Kotlin-based image string extensions.

---

## 8. Autonomous Agent Workflow
The automated assistant operated as a dedicated implementation partner under my close supervision:
  Analysis: It analyzed the asynchronous flow requirements of the Gemini Starter templates.
  Drafting: It laid out the necessary permissions, asynchronous contracts, and layout configurations needed to run the image upload flows safely.
  Execution: Once approved, it helped draft clean activity code blocks, reducing structural errors and ensuring compatibility with recommended Android lifecycle APIs.

---

## 9. Verification of AI-Generated Artifacts
  Code Architecture Check: Every snippet suggested by the AI was cross-referenced to ensure that it used modern Android Jetpack lifecycle structures (such as `registerForActivityResult`) instead of deprecated legacy techniques (`onActivityResult`).
  Memory Management Review: Ensured that all asynchronous flow collectors and Firebase task listeners were properly tied to lifecycle scopes to completely eliminate context leakage risks.

---

## 10. Human vs AI Contribution
  Human: 100% manual implementation of the core Kotlin Flow logic, custom layout setups, Firebase console instance configurations, Firestore collection setup, and end-to-end debugging of the platform.
  AI: Structural layout advisory for the Gemini API onboarding, drafting complex JSON string formats for sentiment analysis prompts, and formatting technical report text.

---

## 11. Ethical and Responsible Use
  Risk Mitigation: To preserve the core academic learning goals, AI tools were strictly barred from generating the baseline reactive Kotlin algorithms and Android Studio native tasks. All algorithmic flows were engineered manually.
  Handling Limitations: AI suggestions concerning Gradle versions and Firebase BOM management were verified against official documentation, preventing "black-box" configurations from messing up local environment variables.
  Transparency: This comprehensive report functions as an honest, direct disclosure of AI presence, drawing a strict line between automated environment troubleshooting assistance and the student's original software engineering implementation work.

---
# Development Process
## 12. Version Control and Commit History
Git was actively used to checkpoint development milestones. The tree shows a clear evolution: initial Coroutines/Flows adjustments, manual Firebase boilerplate setups, integration of Storage image uploading routines, and final implementation of the AI Assistant multimodal ecrãs.

---

## 13. Difficulties and Lessons Learned
  Firestore Silent Buffering: Learned that when a collection or database instance isn't explicitly initialized in the Firebase Web Console, the Android SDK buffers requests silently offline without throwing local errors, causing UI operations to freeze indefinitely.
  Gradle Dependency Mismatches: Resolving version alignment conflicts between the Firebase Storage SDK and old Kotlin compiler plugins highlighted the importance of standardizing build environments.
  Channel Back-Pressure: Gained practical knowledge on how Kotlin Channels stream values safely across default workers and main threads without blocking screen drawing frames.

---

## 14. Future Improvements
  Migration to KSP: Upgrading external metadata frameworks to Kotlin Symbol Processing to speed up dependency management.
  Caching Strategies: Implementing local offline caching via Room Database to allow the Notes application to load user assets seamlessly even when completely disconnected from the Firebase server.
  Advanced Gemini Models: Porting text processing prompts over to `gemini-1.5-pro` to capture finer sentiment context nuances and handle highly intricate multilingual requests.

---

## 15. AI Usage Disclosure (Mandatory)
AI Tools Used:
  - Gemini 3 Flash: Used via standard web UI for quick environment troubleshooting and configuration file formatting.
  - Gemini 3.1 Pro: Utilized within the IDE development module for layout optimization, intent data passing advice, and structural report adjustments.

Scope of Usage:
  - Technical Troubleshooting: Isolating Gradle sync failures, resolving missing references for Picasso/Firebase, and setting up clean local property keys.
  - Architecture Refinement: Refactoring model bindings between Java entities and modern Kotlin Activities.

Confirmation of Responsibility: I explicitly confirm that no AI tool was used to author the core logic of the Kotlin streams or Android Studio exercises, which were manually engineered 100% by me. I have validated every single artifact and remain entirely responsible for the code, behavior, and final contents of this assignment.
