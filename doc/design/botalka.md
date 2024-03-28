# Design Document of 'botalka' service

## Overall Description

Service manages homework statements and submissions. It provides a way to
create a homework event post and schedule it to be published at specified
time. Also, it supports gradual homework development for teacher by working
with drafts. It stores each homework description and attached files. After
publication, it receives submissions and drives an interaction between teacher
and student.

I propose to have a Telegram Bot UI.

## Functional Requirements

**FR1**. Teacher must be able to create a homework statement draft.

**FR2**. Homework statement must contain a title, text description and
         deadline.

**FR3**. Homework statement can have attached pdf files.

**FR4**. Homework publication can be scheduled to a given time moment.

**FR5**. Service must store scheduled homeworks as well as drafts.

**FR6**. Service must send a notification when homework is published.

**FR7**. Homework statement must be readonly after publication for simplicity.

**FR8**. Student must be able to add submissions for the homework with
         text and attachments such as pdf document.

**FR9**. Teacher must be able to view submissions.

**FR10**. Teacher must be able to send a submission text feedback.

**FR11**. Service must send a notification to teacher when submission is sent.

**FR12**. Service must send a notification to student when feedback is sent.

**FR13**. Student must not be able to send submission after homework deadline.

**FR14**. Student must not be able to send submission after acceptance.

## Deployment Diagram

```mermaid
C4Context 
  title System Context diagram for Botalka

  Enterprise_Boundary(telegram-boundary, "Telegram") {
    System(telegram, "Telegram", "Instant Messenger.")
  }

  Enterprise_Boundary(lms-boundary, "LMS") {
    System(botalka, "Botalka Service", "Manages homeworks.")
    System(frontend, "LMS Frontend", "Telegram Bot Application.")
    Person(user, "User", "Teacher or Student.")
  }

  BiRel(user, telegram, "")
  BiRel(frontend, telegram, "")
  Rel(frontend, botalka, "")
  Rel(botalka, telegram, "")
```

## Entity Relationship Diagram

```mermaid
erDiagram
  user {
    integer id          PK
    string  alias       UN
  }

  student {
    integer user_id PK, FK
  }

  teacher {
    integer user_id PK, FK
  }

  homework {
    integer   id          PK
    string    title
    string    description
    integer   weight
    timestamp post_moment "nullable"
  }
  
  homework_workspace {
    integer   homework_id PK, FK
    integer   student_id  PK, FK
  }

  homework_submission {
    integer   id          PK
    integer   homework_id FK
    integer   student_id  FK
    string    comment
    timestamp moment
  }

  homework_feedback {
    integer   id            PK
    integer   homework_id   FK
    integer   student_id    FK
    integer   teacher_id    FK
    string    comment
    integer   score         "nullable"
    timestamp moment
  }

  student ||--o{ homework_submission : submit
  homework_submission }o--|| homework_workspace : for
  teacher ||--o{ homework_feedback : answer
  homework_feedback }o--|| homework_workspace : for
  user ||--o{ student : is
  user ||--o{ teacher : is
```

## HTTP API

```kotlin
object BotalkaHttpApi {
  val `POST  /homework`: (Homework.Draft) -> Homework
  val `GET   /homework/list`: () -> List<Homework.Header>
  val `POST  /homework/{}/workspace/{}/submission`: (Workspace.Id, Submission.Draft) -> Submission
  val `POST  /homework/{}/workspace/{}/feedback`: (Workspace.Id, Feedback.Draft) -> Feedback
  val `GET   /homework/{}/workspace/{}`: (Workspace.Id) -> Workspace
  val `GET   /homework/{}/result`: (Homework.Id) -> List<Homework.Result>
}
```

## Produced Events

```kotlin
object BotalkaEvent {
  class HomeworkPosted(homework: Homework)
  class SubmissionAdded(submission: Submission)
  class FeedbackAdded(feedback: Feedback)
}
```
