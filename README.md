# Queues Management Application

A Java-based simulation project for managing queues using **threads** and **synchronization mechanisms**, implemented as part of *Fundamental Programming Techniques - Assignment 2*.

## 📌 Project Overview
This project simulates the behavior of multiple queues serving clients, aiming to **minimize waiting time**.  
It models real-world scenarios where customers arrive, wait in line, are served, and leave the system.

The application:
- Generates a set of clients, each with:
  - **ID** (1 to N)
  - **Arrival time**
  - **Service time**
- Assigns each client to the queue with the **minimum waiting time** at their arrival.
- Runs a simulation over a specified interval using **multithreading**:
  - One thread per queue.
  - One thread managing simulation time and client distribution.
- Logs the state of queues and clients over time.
- Computes statistics such as:
  - **Average waiting time**
  - **Average service time**
  - **Peak hour** of queue activity

## ✨ Features
- Random client generation within configurable bounds.
- Real-time simulation of multiple queues running in parallel.
- Thread-safe queue operations using synchronization mechanisms.
- Graphical User Interface (JavaFX) for:
  - Setting simulation parameters
  - Displaying queue evolution in real time
- Logs of events saved to a `.txt` file.

## ⚙️ Input Parameters
The user can configure the following through the UI:
- `N` – number of clients
- `Q` – number of queues
- `t_simulation_max` – maximum simulation time
- `[arrival_min, arrival_max]` – bounds for client arrival times
- `[service_min, service_max]` – bounds for client service times
