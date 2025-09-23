# CS 157A Personal Database Application

In this semester-long project, teams of four will design and build a complete database application end-to-end. You'll model a real domain (≥5 base tables), implement it in MySQL Workbench, write non-trivial SQL (joins, subqueries, aggregates, indexes, transactions), and integrate it with a small Java console app using JDBC (no web/GUI). The work is delivered in phases with short demos, and each member's role and concrete contributions must be defined and documented before every presentation.

_This spec is a living document, minor clarifications may be added as we progress._

## Teams

- You will work in groups of 4 (randomly assigned)
- Group lists are posted on Canvas
- All membership changes require instructor approval
- Teams must define and document each member's role before every submission or presentation and should be included in the submission
- Roles may rotate across phases, but every student must own clearly identifiable work

## Tech Stack

- **Database**: MySQL (Workbench)
- **App**: Java (console), IDE, JDBC, DB config files
- **No web required**

## AI Collaboration for Project

Use AI as a teammate for brainstorming, critique, and debugging, not as an answer key. You must adapt ideas to your domain or context and prove your own understanding. This process will be part of the grade.

### What you must turn in

**ai_log.md** or **.txt**: for each AI session, include:

- Prompt used on AI (paste it)
- AI idea (3 bullets)
- We adopted/changed/rejected (3+ bullets) and contextualized + why
- What AI got wrong/generic, how you fixed/validated it

## Project Submission Phases

### Phase A: Parts 1 & 2

**Window**: Tuesday and Thursday, Sept 22–26 (both class days, mixed with usual lecture)

**Deliverables**:

- Part 1: Domain + E/R diagram + assumptions
- Part 2: Relational schema (tables, types, PK/FK, constraints) + brief normalization notes

**Phase A Deliverables**:

- PDF File of ERD Diagrams and explanations of the diagram
- PDF File of Schema mappings and explanations
- Text File of Documentation of how you incorporated AI

### Phase B: Parts 3 & 4

**Window**: Tuesday and Thursday, Nov 3–7 (both class days, mixed with usual lecture)

**Deliverables**:

- Part 3: MySQL scripts to create & populate the DB
- Part 4: Query suite (joins, subqueries, updates, indexes, and one transaction demo (commit/rollback))

### Phase C: Parts 5, 6 & 7 (Final)

**Window**: Tuesday and Thursday, Dec 1–5 (both class days - complete week given to submissions)

**Deliverables**:

- Part 5: Java console app using JDBC (PreparedStatements only, transactional workflow, input validation, etc)
- Part 6: At least one VIEW and one TRIGGER or stored routine, with a tiny test
- Part 7: Wrap-up: reproducible build/run instructions and a short reflection

---

_These are the current specifications. As we progress, I may add clarifications or small adjustments and announce on CANVAS and update the document._

## Detailed Part Requirements

### Part 1: Pick a domain + E/R design

- **Minimum schema size**: at least 5 base tables or more (not counting views)
- Choose a real domain you care about (e.g., advising, events, library, housing)
- Draw an E/R diagram with entities, relationships, cardinalities, and key choices rationale and description
- State assumptions/business rules

#### Minimum Requirements for ERD Diagrams

**Entity sets**:

- At least five or more entities (strong entities) relevant to the application domain
- At least one or more weak entities, clear distinction between strong entities and weak entities

**Attributes**:

- Each entity has at least one or more attributes (beyond the key)
- Composite keys / multi-attribute keys correctly underlined if needed in your diagram

**Relationships**:

- Relationships between entity sets shown as diamonds
- Participation (total vs partial) clearly indicated where needed

**Multiplicity constraints**:

- Each relationship must specify how many instances can participate (1–1, 1–M, M–M, notation)

**Special constructs where relevant**:

- Weak entities with identifying relationships
- ISA hierarchies (triangles) if the domain suggested specialization

**Faithfulness to domain**:

- Entities, attributes, and relationships should reflect real-world objects and connections in the project topic
- Avoid redundancy
- Keep diagrams reasonably simple (no invented intermediate entities without purpose)

### Part 2: Translate E/R to Relational schema

- Convert the E/R to relations (tables)
- Specify PKs, FKs, and constraints; justify any surrogate keys
- Brief normalization notes applicable to your database

### Part 3: Implement & seed in MySQL (Workbench)

- Create the schema in MySQL WorkBench
- Load an initial dataset that's rich enough to support interesting queries
- Submit all the schema creating statements and queries
- Evidence it runs clean

### Part 4: Queries, updates, indexes, transactions

Prepare a query suite that demonstrates:

- Multi-table joins (inner + at least one outer join)
- Subqueries (scalar, IN/EXISTS, and a FROM-subquery)
- Aggregates with GROUP BY/HAVING
- Updates/inserts/deletes
- Transactions with START TRANSACTION … COMMIT/ROLLBACK
- Add at least two indexes and explain why (and show a quick EXPLAIN before/after or timing note)

### Part 5: Java integration (console app, JDBC)

- Build a menu-driven console app using JDBC (no web/GUI)
- PreparedStatement everywhere (no string concat)
- At least one transactional workflow (e.g., multi-table insert with rollback on failure)
- Input validation and helpful error messages
- App reads DB config from a properties file

### Part 6: Views, constraints, triggers (MySQL)

- Add at least one view (e.g., a convenient reporting view)
- Add one of: a trigger or a stored routine (procedure/function) that enforces a rule or automates a task
- Include any CHECK constraints you need
- Demonstrate each feature with a tiny test

### Part 7: Wrap-up & reproducibility

Ensure a grader can build and run from scratch:

- Run `create_and_populate.sql`
- Run a subset of `queries.sql`
- Compile/run your Java app with `app.properties`
- Reflect briefly on limitations & future work

## Quality Checklist

- **Data model**: sensible PK/FK choices, integrity constraints, normalization rationale
- **SQL depth**: joins, subqueries (incl. EXISTS/IN and FROM-subquery), aggregates, DML
- **Correctness & safety**: prepared statements, transactions with rollback, error handling
- **DB features**: views + triggers/routines used meaningfully
- **Reproducibility**: one-command rebuild; properties-driven config; clear README

_No website or GUI needed: MySQL Workbench + console Java (JDBC) is the intended stack._
