import { useState } from "react";
import { getToken } from "firebase/messaging";
import { messaging } from "../firebase";

const FcmTokenButton = () => {
    const [fcmToken, setFcmToken] = useState("");
    const [error, setError] = useState(null);

    const handleGetToken = async () => {
        try {
            const vapidKey = process.env.REACT_APP_VAPID_KEY;
            const token = await getToken(messaging, { vapidKey });

            if (!token) throw new Error("FCM Token을 받을 수 없습니다.");
            setFcmToken(token);
            console.log("FCM Token:", token);
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <div>
            <h2>FCM 토큰 발급</h2>
            <button onClick={handleGetToken}>토큰 발급받기</button>
            {fcmToken && <p>{fcmToken}</p>}
            {error && <p style={{ color: "red" }}>{error}</p>}
        </div>
    );
};

export default FcmTokenButton;
