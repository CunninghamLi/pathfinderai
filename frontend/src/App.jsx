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
        <div className="pf-app">
            <header className="pf-header">
                <div className="pf-header-left">
                    <div className="pf-logo-circle">PF</div>
                    <div>
                        <h1 className="pf-title">PathFinder AI</h1>
                        <p className="pf-subtitle">
                            Describe your skills and let PathFinder suggest real jobs for you.
                        </p>
                    </div>
                </div>
                <div className="pf-header-right">
                    <span className="pf-badge">Student demo</span>
                </div>
            </header>

            <main className="pf-main">
                {/* Left side: input + assistant reply */}
                <section className="pf-panel pf-panel-chat">
                    <h2 className="pf-panel-title">Your skills</h2>
                    <p className="pf-panel-help">
                        Example: <span>Java, Spring Boot, React, Docker</span>
                    </p>

                    <form className="pf-form" onSubmit={handleSubmit}>
                        <label className="pf-label" htmlFor="skillsInput">
                            Tell me what technologies you know:
                        </label>
                        <textarea
                            id="skillsInput"
                            className="pf-textarea"
                            rows={4}
                            value={message}
                            onChange={(e) => setMessage(e.target.value)}
                            placeholder="Java, JavaScript, React, SQL..."
                        />

                        <div className="pf-form-footer">
                            {error && <div className="pf-error">{error}</div>}

                            <button
                                type="submit"
                                className="pf-button"
                                disabled={loading || !message.trim()}
                            >
                                {loading ? "Thinking..." : "Ask PathFinder"}
                            </button>
                        </div>
                    </form>

                    <div className="pf-assistant">
                        <h3 className="pf-assistant-title">Assistant reply</h3>
                        <div className="pf-assistant-box">
                            {reply ? (
                                <p>{reply}</p>
                            ) : (
                                <p className="pf-assistant-placeholder">
                                    I will analyze your skills and summarize what I see here.
                                </p>
                            )}
                        </div>
                    </div>
                </section>

                {/* Right side: job cards */}
                <section className="pf-panel pf-panel-jobs">
                    <h2 className="pf-panel-title">Job recommendations</h2>

                    {jobs.length === 0 ? (
                        <p className="pf-empty-state">
                            No jobs yet. Enter your skills on the left and click
                            <span> Ask PathFinder</span>.
                        </p>
                    ) : (
                        <div className="pf-jobs-grid">
                            {jobs.map((job, index) => (
                                <article key={index} className="pf-job-card">
                                    <h3 className="pf-job-title">{job.title}</h3>
                                    <p className="pf-job-company">{job.company}</p>
                                    <p className="pf-job-location">{job.location}</p>

                                    <p className="pf-job-desc">{job.description}</p>

                                    <a
                                        href={job.url}
                                        target="_blank"
                                        rel="noreferrer"
                                        className="pf-job-link"
                                    >
                                        View job posting
                                    </a>
                                </article>
                            ))}
                        </div>
                    )}
                </section>
            </main>

            <footer className="pf-footer">
                Built with Spring Boot + React · Demo project by Cunningham
            </footer>
        </div>
    );
}

export default App;
