import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";

// 서비스 워커 등록
import { register } from "./serviceWorkerRegistration"; // 서비스 워커 등록을 위한 함수 호출

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
    <React.StrictMode>
        <App />
    </React.StrictMode>
);

// 서비스 워커 등록 (Firebase용 서비스 워커는 public 폴더에 있어야 함)
register(); // Firebase 서비스 워커를 등록합니다.
