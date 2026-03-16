# Feature Porting Prompt (Pagination)

## Objective
Port the **pagination implementation** from the **Helol Compose project**
D:\Android\AAIT\Helool\data\src\main\java\com\aait\data\paging\NotificationsPagingSource
D:\Android\AAIT\Helool\data\src\main\java\com\aait\data\util\PaginationHelper
D:\Android\AAIT\Helool\app\src\main\java\com\aait\helool\util\paging
into:
`D:\Android\AAIT\base-2026`

Use Helol as a reference and follow the same pattern — **do NOT copy blindly**.

---

## Scope
Work ONLY on:

- Pagination logic
- Load-more behavior
- Paging state handling
- UI state updates
- Integration with existing Compose screens

### Do NOT:
- Modify unrelated modules or features
- Perform large refactors
- Introduce unnecessary architecture

---

## Architecture Rules
Follow **Compose-first principles**:

- State hoisting
- Unidirectional data flow
- Lifecycle-aware logic

### Guidelines
- Prefer reusable pagination components.
- Keep the implementation modular.
- Avoid over-engineering.
- Match the existing project architecture.

---

## Workflow
1. Study the pagination implementation in Helol.
2. Extract the core logic and state management.
3. Adapt it to fit the target project's architecture.
4. Implement incrementally.
5. Validate loading states, errors, and end-of-list behavior.

---

## Git Rules
- Edit **ONE file at a time**.
- **Commit and don`t push after EVERY file edit.**
- Keep commits small, focused, and descriptive.
