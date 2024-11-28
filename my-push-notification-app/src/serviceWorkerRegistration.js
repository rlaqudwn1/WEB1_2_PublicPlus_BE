// serviceWorkerRegistration.js
export function register() {
    if ("serviceWorker" in navigator) {
        window.addEventListener("load", () => {
            const swUrl = process.env.PUBLIC_URL + "/firebase-messaging-sw.js"; // 서비스 워커 파일 경로

            navigator.serviceWorker
                .register(swUrl)
                .then((registration) => {
                    console.log("Service Worker 등록 성공: ", registration);
                })
                .catch((error) => {
                    console.error("Service Worker 등록 실패: ", error);
                });
        });
    }
}
