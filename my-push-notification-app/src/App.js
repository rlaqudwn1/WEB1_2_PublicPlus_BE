// src/App.js
import React from "react";
import FcmTokenButton from "./components/FcmTokenButton";
import TopicManager from "./components/TopicManager";
import Calendar from "./components/Calendar"; // FullCalendar 컴포넌트 추가
import PushNotificationSender from "./components/PushNotificationSender"; // 푸시 알림 전송 컴포넌트 추가
import LoginFCMComponent from "./components/LoginFCMComponent";
const App = () => {
    return (
        <div>
            <h1>Public Sports Facility</h1>

            {/* 푸시 알림 전송 컴포넌트 */}
            <PushNotificationSender />

            {/* FCM 토큰 버튼 컴포넌트 */}
            <FcmTokenButton />

            {/* TopicManager 컴포넌트 */}
            <TopicManager />

            {/* FullCalendar 컴포넌트 */}
            <Calendar />
            <LoginFCMComponent/>
        </div>
    );
};

export default App;
