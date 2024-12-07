import { useState } from "react";
import axios from "axios";

const PushNotificationSender = () => {
    const [email, setEmail] = useState("");
    const [title, setTitle] = useState("");
    const [body, setBody] = useState("");
    const [error, setError] = useState(null);
    const [successMessage, setSuccessMessage] = useState("");

    const sendPushNotification = async () => {
        try {
            const response = await axios.post(
                "http://localhost:8080/api/push/send",
                { email, title, body },
                {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
                    },
                }
            );
            setSuccessMessage(response.data);
            setError(null);
        } catch (err) {
            const errorMessage = err.response?.data?.message || "Failed to send push notification";
            setError(errorMessage);
            setSuccessMessage("");
            console.error("Axios Error: ", err);
        }
    };

    return (
        <div>
            <h2>Send Push Notification</h2>
            <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="User Email"
            />
            <input
                type="text"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="Notification Title"
            />
            <textarea
                value={body}
                onChange={(e) => setBody(e.target.value)}
                placeholder="Notification Body"
            />
            <button onClick={sendPushNotification}>Send Notification</button>

            {error && <div style={{ color: "red" }}>{error}</div>}
            {successMessage && <div style={{ color: "green" }}>{successMessage}</div>}
        </div>
    );
};

export default PushNotificationSender;
