// /src/App.js

import React from "react";
import FcmTokenButton from "./components/FcmTokenButton";
import TopicManager from "./components/TopicManager";
import LoginFCMComponent from "./components/LoginFCMComponent";
import Calendar from "./components/Calendar"; // FullCalendar 컴포넌트 추가

const App = () => {
    return (
        <div>
            <h1>Public Sports Facility</h1>
            <LoginFCMComponent />
            <FcmTokenButton />
            <TopicManager />
            <Calendar /> {/* FullCalendar 추가 */}
        </div>
    );
};

export default App;
