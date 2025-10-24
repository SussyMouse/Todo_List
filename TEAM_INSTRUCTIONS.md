# Team Instructions – Smart Todo List (JavaFX)

## Team & Roles
- **Member A – Core Logic Lead**
    - Owns task domain model, CRUD operations, file persistence, and overall architecture.
- **Member B – GUI Lead**
    - Builds JavaFX UI (manual + SceneBuilder components), manages styling, and user flows.
- **Member C – Quality & Integration Lead**
    - Handles testing strategy, integration, documentation, and release readiness.

Roles are primary ownerships; all members collaborate and provide peer reviews.

## Communication & Meetings
- **Stand-up (15 min):** Mon/Wed/Fri at 09:00 via Discord.
- **Design Sync:** Thursdays 20:00 during Week 1–2 for architectural updates.
- **Weekend Review:** Sundays 18:00 to assess progress and adjust plan.

Use shared Kanban board (e.g., Trello/Jira) to track tasks, blockers, and deadlines.

## Workflow & Branching
1. Create issue cards for every feature/bug aligned with rubric items.
2. Use feature branches (`feature/<summary>`). One task per branch.
3. Submit PRs with:
    - Summary, screenshots/GIF of UI changes.
    - Testing notes (manual/automated).
    - Request review from at least one teammate.
4. Merge only after CI/tests pass and one approval is received.

## Development Guidelines
- **Language/Tools:** Java 17+, JavaFX 17+, SceneBuilder (for one GUI version).
- **Code Style:** Follow Google Java Style; run formatter before committing.
- **Documentation:** Javadoc for public classes/methods; inline comments for complex logic.
- **Error Handling:** Validate user inputs, show friendly alerts for invalid operations.
- **Data Persistence:** Store tasks in JSON (preferred) or serialized format, loaded at startup.

## Testing & Quality
- Unit tests for task management logic (JUnit 5).
- Manual test matrix covering add/edit/delete, search, filter, category, status.
- Regression check before release; record results in `/docs/testing-report.md`.

## Deliverables Checklist
- Java source with comments and documentation.
- SceneBuilder `.fxml` files and any stylesheet assets.
- README with setup, usage, and team credits.
- Videos:
    - ≤2 min code walkthrough (Member A voice-over recommended).
    - ≤5 min GUI demonstration (Member B leading, Member C assisting).
- Academic honesty statement; cite any external references.

## Submission Protocol
- Tag final commit `release-v1.0`.
- Archive project (`zip`) including src, resources, videos, and documentation.
- Ensure all members’ names and student IDs appear in README and submission form.
