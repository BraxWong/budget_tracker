# Project TODO List

## 1. User Authentication
- [ ] Implement user login and registration functionality using Spring Security.
  - Currently completed without Spring Security; will revisit for integration.
- [ ] Implement JWT (JSON Web Tokens) for secure session management.
  - Recognized as an industry standard for authentication; will explore implementation at a later date.

## 2. Database Integration
- [x] Choose a relational database (e.g., PostgreSQL or MySQL) or a NoSQL database (e.g., MongoDB).
  - **Status:** Completed
- [x] Use Spring Data JPA for easier database interactions and CRUD operations.
  - **Status:** Completed

## 3. Budget Entries
- [ ] Create a `BudgetEntry` entity to represent individual entries in the budget, including fields such as:
  - Amount
  - Category
  - Date
  - Description
- [ ] Implement RESTful endpoints for CRUD operations on budget entries:
  - [ ] **Create:** Add new entries.
  - [ ] **Read:** Retrieve entries for the current month.
  - [ ] **Update:** Modify existing entries.
  - [ ] **Delete:** Remove entries.

## 4. Monthly Budget Tracking
- [ ] Create endpoints to view the total savings, expenses, and balance for the current month.
- [ ] Implement filtering options (e.g., by category or date).

## 5. User Settings Management
- [x] Allow users to set monthly saving goals.
    - **Status:** Completed
- [x] Create an endpoint to view and update these settings.
    - **Status:** Completed

## 6. File Import/Export
- [ ] Implement functionality to import budget entries from a CSV or JSON file.
- [ ] Create endpoints to export the current month’s budget data to a file.

## 7. Analytics and Reports
- [ ] Add endpoints that provide analytics on spending habits, including:
  - [ ] Monthly spending by category.
  - [ ] Trends over several months.

## 8. Error Handling
- [ ] Implement global exception handling using `@ControllerAdvice` to manage errors gracefully.
  - Currently managed as the development cycle progresses.

## 9. Unit and Integration Testing
- [ ] Write unit tests for service and controller layers using JUnit and Mockito.
- [ ] Create integration tests to verify interactions with the database.

## 10. Documentation
- [ ] Use Swagger/OpenAPI to generate API documentation automatically.