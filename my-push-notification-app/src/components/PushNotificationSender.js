// src/components/PushNotificationSender.js
import { useState } from "react";
import axios from "axios";

const PushNotificationSender = () => {
    const [email, setEmail] = useState("");
    const [title, setTitle] = useState("");
    const [body, setBody] = useState("");
    const [error, setError] = useState(null);
    const [successMessage, setSuccessMessage] = useState("");

    // 푸시 알림 전송 함수
    const sendPushNotification = async () => {
        try {
            const response = await axios.post("http://localhost:8080/api/push/send", {
                email,
                title,
                body,
            });

            // 전송 성공 메시지 처리
            setSuccessMessage(response.data);
            setError(null);
        } catch (err) {
            // 에러 처리
            setError(err.response?.data || "Failed to send push notification");
            setSuccessMessage("");
        }
    };

    return (
        <div>
            <h2>Send Push Notification</h2>
            <div>
                <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="User Email"
                    required
                />
            </div>
            <div>
                <input
                    type="text"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    placeholder="Notification Title"
                    required
                />
            </div>
            <div>
                <textarea
                    value={body}
                    onChange={(e) => setBody(e.target.value)}
                    placeholder="Notification Body"
                    required
                />
            </div>
            <button onClick={sendPushNotification}>Send Notification</button>

            {error && <div style={{ color: "red" }}>{error}</div>}
            {successMessage && <div style={{ color: "green" }}>{successMessage}</div>}
        </div>
    );
};

export default PushNotificationSender;
