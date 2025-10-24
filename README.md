# Smart Todo List (JavaFX)

## Overview
Smart Todo List is a desktop task manager built in Java using JavaFX. It enables students to create, edit, categorize, search, and filter tasks through an intuitive graphical interface. The project is developed as part of the Integrated Software Development Workshop 2025/2026.

## Features
- Create, update, and delete tasks with title, description, due date, category, and priority.
- Mark tasks as complete/incomplete and track status changes.
- Search bar for keyword-based lookup; quick filters by category, priority, or status.
- Two GUI implementations:
    - Hand-crafted JavaFX scenes.
    - SceneBuilder-authored interface (≥30% of GUI).
- Persistent storage (JSON file) loaded at startup and saved on change.

## Technology Stack
- **Language:** Java 17+
- **Framework:** JavaFX 17+
- **UI Tools:** SceneBuilder (for FXML-based views), CSS for styling
- **Build Tool:** Maven or Gradle (team choice)
- **Testing:** JUnit 5, TestFX (optional for UI tests)

## Getting Started
1. Install Java 17+ (ensure `JAVA_HOME` is set).
2. Install JavaFX SDK and configure IDE (IntelliJ IDEA recommended).
3. Clone the repository:
   ```bash
   git clone <repo-url>
   cd SmartTodoList
