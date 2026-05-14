# ♟️ Chess Agent Project

## Overview

This project is a Java-based chess engine and playable chess application featuring multiple AI search strategies, board evaluation heuristics, and automated bot-vs-bot training modes.

The system supports both human gameplay and autonomous agent competition through a graphical chess interface.

The project explores:

- Classical AI search algorithms
- Chess board evaluation heuristics
- Multi-threaded move generation
- Simulation-based decision making
- Self-improving heuristic tuning

---

# Features

## ♟️ Chess Gameplay

- Full chess rules implementation
- Legal move validation
- Castling
- En passant
- Pawn promotion
- Checkmate and stalemate detection
- Draw repetition detection

---

## 🤖 AI Agents

The engine supports two AI approaches:

### Minimax with Alpha-Beta Pruning

- Depth-limited recursive search
- Alpha-beta pruning optimisation
- Multi-threaded move evaluation
- Heuristic-based board scoring

### Monte Carlo Tree Search (MCTS)

- Randomised simulation-based evaluation
- Probabilistic move selection
- Configurable simulation depth

---

# Evaluation System

The AI evaluates positions using multiple weighted heuristics:

| Heuristic | Description |
|---|---|
| Material | Piece value advantage |
| Center Control | Control of central squares |
| Offence | Attacking opportunities |
| Defence | Piece protection and safety |
| Mobility | Number of available moves |
| Tropism | Pressure around the enemy king |
| King Safety | Safety of king position |
| Pawn Structure | Pawn coordination and weaknesses |

Each heuristic uses adjustable weights:

```java
float mul1[] = {1, 7, 4, 10, 1, 5, 10, 7};
```

This allows experimentation with different play styles and evaluation strategies.

---

# Self-Learning / Training Mode

The project includes an automated bot-vs-bot training system where agents:

- Play repeated matches
- Adjust heuristic weights after wins/losses
- Generate random heuristic configurations
- Log game statistics to file

Training results are written to:

```txt
chess_results.txt
```

Logged information includes:

- Winner
- Heuristic weights
- Consecutive wins
- Number of turns

---

# Project Structure

| Class | Responsibility |
|---|---|
| `Game` | Controls game flow and rules |
| `Agent` | AI logic and move selection |
| `Board` | Board representation and move generation |
| `Piece` subclasses | Chess piece behaviour |
| `ChessBoardGUI` | Graphical interface |

---

# AI Architecture

## Move Selection Flow

1. Generate all legal moves
2. Evaluate candidate positions
3. Search future states using Minimax or MCTS
4. Select the best-scoring move

---

## Minimax Optimisations

- Alpha-beta pruning
- Move ordering
- Parallel move evaluation using Java threads

---

## MCTS Strategy

- Random playout simulations
- Goal-based evaluation thresholds
- Win probability estimation

---

# Technologies Used

- Java
- Swing GUI
- Multi-threading
- Object-Oriented Programming
- Recursive search algorithms

---

# Running the Project

## Requirements

- Java 17+ recommended
- IntelliJ IDEA / Eclipse / VS Code

## Compile and Run

```bash
javac *.java
java Game
```

---

# Game Modes

| Mode | Description |
|---|---|
| `UservBot` | Human vs AI |
| `singleBotvBot` | Single AI match |
| `MultiBotvBot` | Multiple automated AI games |

Game mode can be configured in `Game.java`:

```java
private Mode mode = Mode.UservBot;
```

---

# Concepts Implemented

## Alpha-Beta Pruning

Reduces unnecessary search branches during minimax evaluation.

## Board Evaluation Heuristics

The engine dynamically scores positions rather than relying only on material count.

## Parallel Processing

Each candidate move is evaluated in a separate thread to improve search speed.

## Reinforcement-Inspired Weight Adjustment

Bots adapt heuristic weights based on game outcomes.

---

# Future Improvements

Potential future improvements include:

- Opening book support
- Transposition tables
- Zobrist hashing
- Improved MCTS rollout strategy
- Neural network evaluation
- PGN import/export
- Adjustable AI difficulty
- Time-controlled search

---

# What I Learned

This project improved my understanding of:

- Game AI architecture
- Search algorithms
- Heuristic evaluation systems
- Multi-threading in Java
- Recursive algorithms
- State simulation and rollback systems
- Object-oriented software design

---

# Screenshots

_Add screenshots of the GUI here._

---

# Author

Created by Amara Okonkwo.
