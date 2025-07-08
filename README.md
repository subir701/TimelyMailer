# 📧 TimelyMailer — Email Scheduler (Phase 1)

**TimelyMailer** is an email scheduling platform that allows users to schedule and automate email delivery. It solves the problem of manual, repetitive email sending by providing a simple, reliable system that ensures emails are delivered at the right time.

---

## 🚀 Features (Phase 1)

- ✅ **Schedule Emails:** Send emails at a specific date & time.
- ✅ **Multiple Recipients:** Send one email to many.
- ✅ **Edit Scheduled Emails:** Modify recipients, content, and schedule.
- ✅ **Automatic Retry:** Retries sending emails on temporary failures (SMTP/internet issues).
- ✅ **Frontend Dashboard:** Manage emails easily with a web interface.
- ✅ **Basic Unit Testing:** With **JUnit** and **Mockito**.
- ✅ **Gradle Build & Dockerized Backend**

---

## 🖥️ Technologies Used
| Backend                      | Frontend                     |
|------------------------------|------------------------------|
| Java 17 + Spring Boot        | ReactJS (TypeScript)         |
| Spring Data JPA + PostgreSQL | Tailwind CSS + Vite          |
| JUnit, Mockito (Testing)     |                              |
| Gradle, Lombok, Docker       |                              |

---

## 📂 Project Structure
TimelyMailer/
│
├── TimelyMailer_Backend/ # Backend Source Code (Spring Boot)
│ └── Dockerfile # Docker Configuration
│
├── TimelyMailer_Frontend/ # Frontend Source Code (React + Vite)
│
├── railway.json # Railway Deployment Config
└── README.md # Project Documentation (this file)

---

## 📑 API Endpoints (Backend)
| Method | Endpoint                   | Description                    |
|--------|-----------------------------|--------------------------------|
| POST   | `/api/emails`               | Schedule a new email           |
| GET    | `/api/emails`               | Get all scheduled emails       |
| GET    | `/api/emails/{emailId}`     | Get email by ID                |
| PUT    | `/api/emails/{emailId}`     | Update a scheduled email       |
| DELETE | `/api/emails/{emailId}`     | Delete a scheduled email       |
| GET    | `/api/emails/{emailId}/receivers` | Get recipients of an email |

---

## ⚙️ Setup Instructions

### 1️⃣ Backend Setup (Spring Boot)
```bash
cd TimelyMailer_Backend
./gradlew build
docker build -t timelymailer-backend .
docker run -p 8080:8080 timelymailer-backend
```

**Environment Variables:**

SPRING_MAIL_* → Your SMTP credentials.
---
##2️⃣ Frontend Setup (React + Vite + Tailwind)
```bash
cd TimelyMailer_Frontend/timely-mailer-dashboard
npm install
npm run dev
```
The app will be available at: http://localhost:8080
---
## 🎯 Who Is It For?

- **✅ Job Seekers →** Send cold emails at optimal times.

- **✅ Event Organizers, HRs, Teachers →** Send announcements automatically.
---

## 🧩 What Problem Does It Solve?
- Eliminates manual emailing to multiple people.

- No need for the user to be online at the scheduled time.

- Automates repetitive email tasks.
  
---

##🔭 Future Plans (Next Phases)

- Multi-user system with authentication.

- Email marketing & promotional campaigns.

- Analytics: Delivery reports, open rates.

- Templates for faster email composing.

- Public API for third-party integrations.
