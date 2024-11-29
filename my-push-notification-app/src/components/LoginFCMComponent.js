import { useEffect, useState } from "react";
import { messaging } from "../firebase"; // firebase.js에서 messaging import
import { getToken } from "firebase/messaging";
import axios from "axios";

const LoginFCMComponent = () => {
    const [userEmail, setUserEmail] = useState("");
    const [password, setPassword] = useState("");
    const [fcmToken, setFcmToken] = useState(null);
    const [error, setError] = useState(null);

    // FCM 토큰을 가져오는 함수
    const getFcmToken = async () => {
        try {
            // FCM 토큰 받기
            const token = await getToken(messaging, { vapidKey: process.env.REACT_APP_VAPID_KEY });
            if (token) {
                console.log("FCM Token:", token);
                setFcmToken(token); // 받은 토큰을 state에 저장
            } else {
                console.error("No registration token available.");
                setError("No FCM token available.");
            }
        } catch (err) {
            console.error("Error getting FCM token:", err);
            setError("Failed to get FCM token.");
        }
    };

    // 컴포넌트가 마운트되면 FCM 토큰 요청
    useEffect(() => {
        getFcmToken();
    }, []);

    // 로그인 처리 및 FCM 토큰 저장
    const handleLogin = async () => {
        try {
            // 로그인 시 FCM 토큰을 포함하여 요청
            const response = await axios.post("http://localhost:8080/api/user/login", {
                userEmail,
                password,
                fcmToken, // FCM 토큰 추가
            });

            const { accessToken } = response.data;

            // 로그인 성공 후 처리
            console.log("Login successful:", accessToken);
        } catch (err) {
            setError(err.response?.data?.message || "Login failed");
        }
    };

    return (
        <div>
            <h2>Login and Get FCM Token</h2>
            <div>
                <input
                    type="email"
                    value={userEmail}
                    onChange={(e) => setUserEmail(e.target.value)}
                    placeholder="Email"
                    required
                />
            </div>
            <div>
                <input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Password"
                    required
                />
            </div>
            <button onClick={handleLogin}>Login</button>

            {error && <div style={{ color: "red" }}>{error}</div>}

            {fcmToken && <div>FCM Token: {fcmToken}</div>}
        </div>
    );
};

export default LoginFCMComponent;
