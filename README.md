# network-acl-simulator

A Java application that simulates network packet filtering using Cisco-style Standard and Extended Access Control Lists (ACLs).

---

## Overview

This project models network traffic evaluation against configured access-list rule sets. It parses Cisco IOS-style ACL configuration files, converts IPv4 addresses and wildcard masks into numerical values for bitwise comparison, and evaluates simulated network packets sequentially to determine whether each packet is **permitted** or **denied** based on first-match semantics and implicit deny rules.

---

## Features

* **Standard ACL Evaluation (`StandardACLMain.java`):**
  * Evaluates packets based strictly on source IPv4 addresses.
  * Supports explicit source addresses with wildcard masks (e.g., `0.0.0.0`) and the `any` keyword.
* **Extended ACL Evaluation (`ExtendedACLMain.java`):**
  * Evaluates packets based on source IP, destination IP, protocol rules, and port conditions.
  * Supports single port filtering (`eq <port>`) and port range filtering (`range <start>-<end>`).
  * Supports wildcard masking for both source and destination subnets as well as `any` keywords.
* **Bitwise Address Matching:**
  * Converts 32-bit IPv4 dotted-quad addresses to `long` integers and applies inverted wildcard masks via bitwise operations for exact subnet matching.
* **Default Implicit Deny:**
  * Automatically denies any packet that does not match an explicit permitting rule in the list.

---

## Project Structure

```text
network-acl-simulator/
├── ExtendedACLMain.java      # Driver program for Extended ACL simulation
├── StandardACLMain.java      # Driver program for Standard ACL simulation
├── ExtendedRule.java         # Data model and matching logic for Extended ACL rules
├── StandardRule.java         # Data model and matching logic for Standard ACL rules
├── Packet.java               # Packet representation (source IP, destination IP, port)
├── extended_rules.txt        # Extended ACL rule configurations
├── extended_input.txt        # Extended test packet streams
├── standard_rules.txt        # Standard ACL rule configurations
└── standard_input.txt        # Standard test packet streams
```

---

## File Formats

### Standard ACL Rules & Packets

* **Rule Format (`standard_rules.txt`):**
  ```text
  access-list <id> <permit|deny> <source-ip> <wildcard-mask>
  access-list <id> <permit|deny> any
  ```
* **Packet Format (`standard_input.txt`):**
  ```text
  <source-ip>
  ```

### Extended ACL Rules & Packets

* **Rule Format (`extended_rules.txt`):**
  ```text
  access-list <id> <permit|deny> <protocol> <source-ip> <source-mask> <dest-ip> <dest-mask>
  access-list <id> <permit|deny> <protocol> <source-ip> <source-mask> <dest-ip> <dest-mask> eq <port>
  access-list <id> <permit|deny> <protocol> <source-ip> <source-mask> <dest-ip> <dest-mask> range <start-port>-<end-port>
  access-list <id> <permit|deny> <protocol> any <dest-ip> <dest-mask> eq <port>
  access-list <id> <permit|deny> <protocol> any any
  ```
* **Packet Format (`extended_input.txt`):**
  ```text
  <source-ip> <dest-ip>
  <source-ip> <dest-ip> <port>
  ```

---

## How to Run

### 1. Compile the Source Code

```bash
javac *.java
```

### 2. Run the Standard ACL Simulator

```bash
java StandardACLMain
```

**Sample Output:**
```text
Packet from 192.168.30.30 denied
Packet from 192.168.30.31 permitted
Packet from 10.5.5.1 permitted
Packet from 172.16.0.100 permitted
```

### 3. Run the Extended ACL Simulator

```bash
java ExtendedACLMain
```

**Sample Output:**
```text
Packet from 192.168.10.5 to 192.168.30.20 on port 21 denied
Packet from 192.168.10.5 to 192.168.30.20 on port 80 permitted
Packet from 192.168.20.10 to 192.168.30.30 on port 25 denied
Packet from 192.168.20.10 to 192.168.30.30 on port 443 permitted
```