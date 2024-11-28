// /src/App.js

import React from "react";
import FcmTokenButton from "./components/FcmTokenButton";
import TopicManager from "./components/TopicManager";
import LoginFCMComponent from "./components/LoginFCMComponent";

const App = () => {
    return (
        <div>
            <h1>Public Sports Facility</h1>
            <LoginFCMComponent />
            <FcmTokenButton />
            <TopicManager />
        </div>
    );
};

export default App;
