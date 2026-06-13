# Twootr

A simplified Twitter-like messaging system built as a learning exercise from the book *Real-World Software Development* by Richard Warburton & Raoul-Gabriel Urma (Chapter 6).

## Overview

Twootr is a backend-only Java application that models the core mechanics of a social messaging platform: users can register, log on, follow others, send twoots, and receive messages from the people they follow.

## Key Concepts

### Domain Model

| Class | Role |
|---|---|
| `Twootr` | Central application core; manages sessions and twoot delivery |
| `User` | Holds credentials, followers, and the last-seen position |
| `Twoot` | An immutable message with an id, content, position, and sender |
| `Position` | A monotonically increasing counter that tracks message ordering |
| `SenderEndPoint` | Actions available to a logged-in user (send, follow, logoff) |
| `ReceiverEndPoint` | Interface for delivering incoming twoots to a client |
| `UserRepository` / `InMemoryUserRepository` | Persistence abstraction with an in-memory implementation |

### Core Behaviours

- **Registration** — users are stored via `UserRepository`.
- **Authentication** — `logon()` validates credentials and returns a `SenderEndPoint` wrapped in an `Optional`.
- **Follow** — bidirectional relationship: `SenderEndPoint.onFollow()` updates both `follows` and `followers` sets.
- **Send twoot** — twoots are broadcast immediately to all active followers; inactive followers receive a replay on next logon.
- **Replay on logon** — on logon, the system replays all twoots posted after the user's `lastSeenPosition`.
- **Logoff** — removes the session and persists the last-seen position so replays work correctly.

## Architecture

The design uses a **ports-and-adapters** style: `ReceiverEndPoint` is a port (interface) that can be backed by any transport (mock in tests, WebSocket in production). The in-memory repository makes the core testable without a database.

### Architectural Lessons

**Hexagonal Architecture (Ports & Adapters)** — the core domain (`Twootr`, `User`, `Twoot`, `Position`) has zero knowledge of infrastructure. It never mentions HTTP, WebSockets, or databases. Infrastructure concerns plug in at the boundaries through two ports:

- **Inbound port** — `ReceiverEndPoint` abstracts how a twoot reaches the client. In tests it is a Mockito mock; in a real app it would be a WebSocket session. Swapping the transport requires no changes to the domain.
- **Outbound port** — `UserRepository` abstracts persistence. `InMemoryUserRepository` is the test adapter; a SQL or MongoDB adapter could replace it without touching the domain logic.

This boundary means the entire domain can be exercised by fast unit tests with no server or database running, and production adapters can evolve independently of business rules.

## Tech Stack

- Java 11
- Maven
- JUnit Jupiter 5.9
- Mockito 5.11

## Running the Tests

```bash
mvn test
```

### Test Coverage

| Test | What it verifies |
|---|---|
| `shouldBeAbleToAuthenticateUser` | Valid credentials return a present `SenderEndPoint` |
| `shouldNotBeAbleToAuthenticateWithWrongPassword` | Wrong password returns empty `Optional` |
| `shouldSendTwootsToFollowers` | Active followers receive twoots in real time |
| `shouldNotSendTwootsToNonFollowers` | Non-followers never receive twoots |
| `shouldReceiveReplayOfTwootsOnLogon` | Missed twoots are replayed when a follower logs back on |

## Project Structure

```
src/
  main/java/dev/studying/
    Twootr.java               # Application core
    User.java                 # User entity
    Twoot.java                # Message entity
    Position.java             # Message position/ordering
    SenderEndPoint.java       # Outbound user actions
    ReceiverEndPoint.java     # Inbound delivery interface
    UserRepository.java       # Repository interface
    InMemoryUserRepository.java
  test/java/dev/studying/
    TwootrTest.java           # Integration tests with Mockito
```
