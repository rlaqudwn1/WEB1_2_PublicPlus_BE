import { useState } from "react";
import fcmService from "../services/fcmService";

const TopicManager = () => {
    const [topic, setTopic] = useState("");
    const [message, setMessage] = useState("");
    const [response, setResponse] = useState("");

    const handleSubscribe = async () => {
        try {
            const res = await fcmService.subscribeToTopic(topic);
            setResponse(`Subscribed to topic: ${topic}`);
        } catch (err) {
            setResponse(`Error subscribing: ${err.message}`);
        }
    };

    const handleSendMessage = async () => {
        try {
            const res = await fcmService.sendMessageToTopic(topic, message);
            setResponse("Message sent successfully!");
        } catch (err) {
            setResponse(`Error sending message: ${err.message}`);
        }
    };

    return (
        <div>
            <h2>토픽 관리</h2>
            <div>
                <label>토픽:</label>
                <input
                    type="text"
                    value={topic}
                    onChange={(e) => setTopic(e.target.value)}
                />
                <button onClick={handleSubscribe}>구독</button>
            </div>
            <div>
                <label>메시지:</label>
                <textarea
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                />
                <button onClick={handleSendMessage}>메시지 전송</button>
            </div>
            {response && <p>{response}</p>}
        </div>
    );
};

export default TopicManager;
