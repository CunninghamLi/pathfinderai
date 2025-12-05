# 🧭 PathFinderAI

PathFinderAI is a simple AI-assisted web app that recommends **real job postings** based on the skills a user enters.  
It analyzes the technologies provided (Java, React, SQL, etc.) and fetches matching jobs from a live public API.

Frontend (React + Vite)  
Backend (Spring Boot, Java 17)  
Live demo: **https://pathfinderai-frontend.vercel.app**

---

## ✨ What it does
- User enters a list of skills
- Backend extracts recognized technologies
- Backend calls a real jobs API (Remotive)
- Frontend displays a clean set of job cards
- Also returns a short summary of detected skills

This project shows:
- API consumption and data mapping  
- Full-stack communication (React → Spring Boot)  
- Clean UI design  
- Deployment to Vercel + Render  
- Use of Docker for backend hosting  

---

## 🛠 Tech Stack
**Frontend:** React, Vite, CSS  
**Backend:** Spring Boot, Java 17, WebClient  
**Deployment:**  
- Frontend on Vercel  
- Backend on Render (Docker)

---

## 🚀 Live Services
- Frontend: https://pathfinderai-frontend.vercel.app  
- Backend: https://pathfinderai-backend.onrender.com  

---

## 📂 Running the project locally

### Backend
```bash
cd backend
./gradlew bootRun
