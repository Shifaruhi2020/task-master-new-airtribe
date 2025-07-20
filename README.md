# 🧩 TaskMaster

**TaskMaster** is a collaborative task and project management backend application built with **Spring Boot**, **JWT authentication**, **Redis**, and **MySQL**. It enables users to create tasks, manage projects, collaborate with team members, and securely authenticate using JWTs.

---

## 📚 Table of Contents

- [✨ Features](#-features)  
- [🧰 Tech Stack](#-tech-stack)  
- [🔐 Authentication Flow](#-authentication-flow)  
- [📁 Project Structure](#-project-structure)  
- [⚙️ Setup Instructions](#️-setup-instructions)  
- [📬 API Overview](#-api-overview)  
- [👩‍💻 Author](#-author)  
- [📄 License](#-license)

---

## ✨ Features

- ✅ User Registration with Email Verification  
- ✅ Login with JWT Token Generation  
- ✅ Logout using Redis (Token Blacklisting)  
- ✅ Task Creation, Assignment, and Completion  
- ✅ Comment and Attach Files to Tasks  
- ✅ Create and Manage Projects  
- ✅ Invite Team Members to Projects  
- ✅ Filter and Search Tasks  
- ✅ Persistent Storage using MySQL  
- ✅ Token Blacklist with Redis

---

## 🧰 Tech Stack

| Layer               | Technology             |
|--------------------|------------------------|
| Backend Framework  | Spring Boot            |
| Security           | Spring Security + JWT  |
| Database           | MySQL                  |
| Token Blacklisting | Redis                  |
| ORM                | JPA (Hibernate)        |
| File Uploads       | Spring Multipart       |

---


## 🔐 Authentication Flow

```text
[REGISTER] → [VERIFY EMAIL] → [LOGIN → JWT Token Issued] → [ACCESS PROTECTED APIs]  
              ↓
       [LOGOUT → JWT stored in Redis blacklist]  

```
---

## 📁 Project Structure 

```text

src/main/java/org/airtribe/TaskMaster
├── configuration/          # Security config, filters
├── controller/             # REST API controllers
├── entity/                 # JPA entity classes
├── repository/             # Spring Data repositories
├── service/                # Business logic
├── util/                   # JWT utilities
└── TaskMasterApplication.java

```

---

## 📬 API Overview

🔐 Auth Endpoints

| Endpoint              | Method | Description                  |
| --------------------- | ------ | ---------------------------- |
| `/register`           | POST   | Register new user            |
| `/verifyRegistration` | GET    | Email verification           |
| `/signin`             | POST   | User login (returns JWT)     |
| `/auth/logout`        | POST   | Blacklists token using Redis |

------

🧩 Task Endpoints

| Endpoint                          | Method | Description                  |
| --------------------------------- | ------ | ---------------------------- |
| `/tasks/create`                   | POST   | Create a new task            |
| `/tasks/my-tasks`                 | GET    | Get tasks created by user    |
| `/tasks/{taskId}/complete`        | PUT    | Mark task as completed       |
| `/tasks/{taskId}/assign/{userId}` | PUT    | Assign task to a user        |
| `/tasks/filter`                   | GET    | Filter tasks by status, etc. |
| `/tasks/search`                   | GET    | Search tasks by keyword      |
| `/tasks/{taskId}/comments`        | POST   | Add comment to task          |
| `/tasks/{taskId}/comments`        | GET    | Get all comments on task     |
| `/tasks/{taskId}/attachments`     | POST   | Upload file to task          |
| `/tasks/{taskId}/attachments`     | GET    | Get uploaded files for task  |

----

📁 Project & Team Endpoints

| Endpoint                               | Method | Description                     |
| -------------------------------------- | ------ | ------------------------------- |
| `/project/create`                      | POST   | Create a new project            |
| `/project/{projectId}`                 | GET    | Get project details             |
| `/project/{projectId}/invite/{userId}` | POST   | Invite user to join the project |
