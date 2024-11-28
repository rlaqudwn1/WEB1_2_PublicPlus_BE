// /src/services/authService.js

import axios from "axios";

// 로그인 API 요청
const login = async (email, password, fcmToken) => {
    try {
        const response = await axios.post("/api/login", {
            email,
            password,
            fcmToken,
        });
        return response.data;  // 서버에서 받은 데이터 (JWT, FCM Token 등)
    } catch (error) {
        throw new Error("로그인 실패");
    }
};

export default { login };
