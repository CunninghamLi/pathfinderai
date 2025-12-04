import { useState } from "react";
import "./App.css";

function App() {
    const [message, setMessage] = useState("");
    const [reply, setReply] = useState("");
    const [jobs, setJobs] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!message.trim()) return;

        setLoading(true);
        setError("");
        setReply("");
        setJobs([]);

        try {
            const res = await fetch("http://localhost:8080/api/chat", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ message }),
            });

            if (!res.ok) {
                throw new Error("Backend returned status " + res.status);
            }

            const data = await res.json();

            setReply(data.reply || "");
            setJobs(Array.isArray(data.jobs) ? data.jobs : []);
        } catch (err) {
            console.error(err);
            setError("Could not reach backend. Is Spring Boot running on port 8080?");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="app">
            <header className="app-header">
                <h1>PathFinder AI</h1>
                <p>Describe your skills and I will suggest jobs.</p>
            </header>

            <main className="app-main">
                <form onSubmit={handleSubmit} className="chat-form">
                    <label htmlFor="message">Tell me about your skills or what you want:</label>
                    <textarea
                        id="message"
                        rows={4}
                        placeholder="Example: I know Java, Spring Boot, React, and SQL and I want a junior backend job in Montreal."
                        value={message}
                        onChange={(e) => setMessage(e.target.value)}
                    />

                    <button type="submit" disabled={loading}>
                        {loading ? "Thinking..." : "Ask PathFinder"}
                    </button>
                </form>

                {error && <div className="error-box">{error}</div>}

                {reply && (
                    <section className="reply-section">
                        <h2>Assistant reply</h2>
                        <p style={{ whiteSpace: "pre-wrap" }}>{reply}</p>
                    </section>
                )}

                {jobs.length > 0 && (
                    <section className="jobs-section">
                        <h2>Job recommendations</h2>
                        <ul className="job-list">
                            {jobs.map((job, idx) => (
                                <li key={idx} className="job-card">
                                    <h3>{job.title}</h3>
                                    <p>
                                        <strong>Company:</strong> {job.company}
                                    </p>
                                    <p>
                                        <strong>Location:</strong> {job.location}
                                    </p>
                                    <p className="job-description">
                                        {job.description?.slice(0, 250)}
                                        {job.description && job.description.length > 250 ? "..." : ""}
                                    </p>
                                    {job.url && (
                                        <a href={job.url} target="_blank" rel="noreferrer">
                                            View job posting
                                        </a>
                                    )}
                                </li>
                            ))}
                        </ul>
                    </section>
                )}
            </main>
        </div>
    );
}

export default App;
