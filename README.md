# Assignment 4 — Tutorial4-Flows&Firebase

Course: DAM
Student: João Rosa
Date: 05/26
Repository URL: https://github.com/pjoni1/DAM_Trabalhos_Final/tree/master
---

1. Introduction
This assignment focused on the development of a complete, modern Android health and fitness application named NutriFlow. The project leverages Jetpack Compose for a declarative UI, Room Database for robust local persistence, Kotlin Coroutines and Flows for reactive programming, and the Google Gemini API for advanced AI-driven multimodal image processing. The goal was to build a comprehensive tracker for daily caloric intake, macronutrients, and physical evolution, enhanced by an AI assistant capable of automatically estimating nutritional values from food photos.

2. System Overview
The project tackles three core engineering challenges:

Modern UI & Reactive State (Jetpack Compose + MVVM): A single-activity architecture entirely built with Jetpack Compose, driven by a centralized NutriFlowViewModel that exposes UI states via StateFlow to ensure seamless recompositions without UI thread blocking.
Local Persistence & Data Modeling (Room DB): A fully offline-capable architecture utilizing Room Database to persistently store user biometric profiles, daily food logs, and evolution vault imagery.
AI Computer Vision Assistant (Gemini API): An integration with Google's latest multimodal LLM (gemini-1.5-flash) to process photos taken by the user's camera, identifying the food and instantly parsing its estimated calories and macronutrients into the app.
3. Architecture and Design
3.1. Reactive State Management (MVVM)
The application strictly follows the Model-View-ViewModel (MVVM) design pattern:

StateFlow Architecture: The NutriFlowViewModel acts as the single source of truth, emitting immutable StateFlow streams for userProfile, mealEntries, currentMealPlan, and mediaEntries. This ensures the UI instantly reacts to any data changes, such as logging a new meal or changing the date offset.
Gamification Logic: Complex business logic, such as the Daily Streak calculation, resides in the ViewModel, evaluating historical data to ensure users are rewarded accurately for hitting their daily caloric goals.
3.2. Data Models and Persistence Layer
Data is structured relationally using Room Database entities:

UserProfile: Stores biometric data (height, weight, age, gender) and fitness goals (deficit, maintenance, surplus) to calculate target calories and macros.
MealEntry: Logs individual food items, storing name, calories, macros, meal category (e.g., Breakfast, Lunch), and timestamp.
MediaEntry: Handles the "Vault" functionality, tracking local URI paths of photos and categorizing them as either Physical Evolution or Meal Diary.
4. Implementation
4.1. AI Assistant & Camera Integration
FileProvider & CameraX: Implemented secure local image capture using FileProvider (ACTION_IMAGE_CAPTURE), resolving permission denials and local pathing issues to temporarily store images in the app's cache.
Multimodal AI Prompting: Sent captured bitmaps to the Gemini API alongside a highly constrained system prompt. The prompt forces the LLM to analyze the food and return a strict, parsable JSON containing foodName, calories, proteins, carbs, and fats.
4.2. Internationalization (I18n)
Dynamic Localization: Built a robust translation system utilizing Android's strings.xml for Portuguese and English. Implemented a dynamic language switcher directly in the app's Top Bar (Settings icon) that automatically updates all labels, dates (SimpleDateFormat tied to app locale), and placeholders without requiring an app restart.
4.3. Dashboards and Vault Gamification
Macro Tracking & Streaks: Engineered an algorithmic streak system that compares daily caloric consumption against the target. If the user meets the target within a threshold, the streak increments; otherwise, it resets seamlessly.
Evolution Vault: Built a persistent visual gallery using horizontal pagers and grids to group user photos chronologically by month, allowing users to track their physical evolution over time.
5. Testing and Validation
AI JSON Parsing Validation: Tested the Gemini API with diverse food images to ensure the response was consistently valid JSON. Handled 404 model errors and transitioned to gemini-1.5-flash.
Streak Edge-Cases: Validated the mathematical logic of the daily streak, testing scenarios where a user misses a day, meets the goal exactly, or exceeds the maximum caloric threshold.
Localization Testing: Confirmed that UI components, date formats, and hardcoded texts dynamically switch between English and Portuguese without layout breaks.
6. Usage Instructions
API Keys Config: Generate a valid Google AI Studio token and include it securely in the code or local.properties to enable the Camera AI functionality.
Permissions: The app requires Camera permissions for food scanning and the Vault. Ensure permissions are granted upon first request.
Environment: Open the project in Android Studio (Ladybug/Koala or newer), sync Gradle, and run on a physical device or emulator running API 26+.
Autonomous Software Engineering Sections
7. Prompting Strategy
For the AI integration and logic refactoring, I employed a "Constraint-Based JSON" strategy.

Methodology: When interacting with the Gemini API for food recognition, I explicitly prompted the model to only return a JSON structure and nothing else. This prevented the LLM from adding conversational text that would break the Kotlin JSON serialization parser.
Collaboration: I used the autonomous coding assistant to troubleshoot streak logic algorithms and to completely map and replace hardcoded UI strings with Android String Resources (R.string) for dynamic internationalization.
8. Autonomous Agent Workflow
The automated assistant (Antigravity IDE) operated as an implementation partner:

Analysis: It analyzed the existing MVVM structure and identified bugs in the Streak calculations and date formatting functions.
Execution: It autonomously traversed the codebase to perform wide-scale refactoring, such as translating hardcoded strings in Compose files to stringResource calls, updating XML values, and fixing FileProvider pathing errors.
9. Verification of AI-Generated Artifacts
Architecture Integrity: Every snippet generated by the AI was reviewed to ensure it maintained the declarative nature of Jetpack Compose. For instance, ensuring that Locale changes triggered recompositions properly using collectAsState().
Security & Paths: AI-suggested fixes for Camera intents were heavily verified to ensure they adhered to Android 11+ Scoped Storage and FileProvider security guidelines.
10. Human vs AI Contribution
Human: 100% manual implementation of the core MVVM architecture, Room Database schemas, Jetpack Compose layouts, baseline navigation, and core feature definitions.
AI: Assisted in debugging the Gemini API connectivity (ModelNotFound errors), algorithmic correction of the Streak Counter, comprehensive internationalization mapping, and drafting this technical report.
11. Ethical and Responsible Use
Risk Mitigation: AI was barred from generating the baseline data architecture to preserve core academic learning goals. It was primarily used to scale features (like translations) and troubleshoot specific Android SDK issues (Camera Intents).
Transparency: This comprehensive report functions as an honest disclosure of AI presence, drawing a strict line between automated troubleshooting/refactoring and the student's original software engineering implementation work.
Development Process
12. Version Control and Commit History
Git was actively used to checkpoint development milestones. The repository history reflects the evolution: initial UI layouts in Compose, followed by Room integration, gamification/streak logic, and finally the integration of the Gemini AI Camera feature and multi-language support.

13. Difficulties and Lessons Learned
AI Serialization Errors: Learned that LLMs can sometimes inject markdown backticks (```json) into their responses, which breaks native Json.decodeFromString. This required robust string sanitization before parsing.
Android FileProvider: Gained deep knowledge into Android's restrictive file system. Setting up xml/file_paths.xml correctly to pass temporary URIs to the Camera application was a significant debugging challenge.
Compose Recomposition Loops: Learned the importance of using remember and collectAsState() correctly to prevent infinite UI recompositions when formatting dates or calculating macro progress bars.
14. Future Improvements
Firebase Synchronization: Expanding the local Room Database to sync seamlessly with Firebase Firestore, allowing cross-device account access.
Barcode Scanner Integration: Adding a traditional Barcode Scanner API as a fallback or complement to the Gemini AI vision, for precise packaged food tracking.
Dynamic Charting: Implementing robust graphical charts using a library like Vico to visualize caloric intake and weight fluctuation over a 6-month period.
15. AI Usage Disclosure (Mandatory)
AI Tools Used:

Antigravity IDE (Gemini-powered Agent): Utilized entirely within the IDE for intelligent code refactoring, internationalization setups, UI bug fixing, and streak algorithm corrections.
Scope of Usage:

Technical Troubleshooting: Resolving FileProvider exceptions, Gemini API Model 404 errors, and fixing date locale bugs.
Architecture Refinement: Wide-scale replacement of hardcoded strings into standardized XML values for PT/EN support.
Confirmation of Responsibility: I explicitly confirm that the foundational architecture, Room database setup, and Compose layout structures were manually engineered 100% by me. AI tools were used for optimization, debugging, and scaling specific features. I have validated every single artifact and remain entirely responsible for the code, behavior, and final contents of this assignment
