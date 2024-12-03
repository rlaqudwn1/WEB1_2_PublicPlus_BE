// /src/utils/tokenUtil.js

// JWT 토큰을 로컬스토리지에 저장
export const saveJwtToken = (token) => {
    localStorage.setItem("jwtToken", token);
};

// JWT 토큰을 로컬스토리지에서 가져오기
export const getJwtToken = () => {
    return localStorage.getItem("jwtToken");
};

// FCM 토큰을 로컬스토리지에 저장
export const saveFcmToken = (token) => {
    localStorage.setItem("fcmToken", token);
};

// FCM 토큰을 로컬스토리지에서 가져오기
export const getFcmToken = () => {
    return localStorage.getItem("fcmToken");
};
