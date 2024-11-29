import axios from "axios";

const API_URL = process.env.REACT_APP_BACKEND_URL;

const fcmService = {
    subscribeToTopic: async (topic) => {
        const response = await axios.post(`${API_URL}/subscribe`, { topic });
        return response.data;
    },

    sendMessageToTopic: async (topic, message) => {
        const response = await axios.post(`${API_URL}/send`, {
            topic,
            message,
        });
        return response.data;
    },
};

export default fcmService;
