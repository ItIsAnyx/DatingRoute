import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client'

let stompClient = null;

export function connect() {
  const token = localStorage.getItem('access_token');

  if (!token) {
    console.error('No access token found. Cannot connect to WebSocket.');
    return Promise.reject(new Error('Authentication token not found.'));
  }

  stompClient = new Client({
    webSocketFactory: () => new SockJS('http://localhost:8081/ws'),
    connectHeaders: {
      Authorization: `Bearer ${token}`
    },
    debug: (str) => {
      console.log('STOMP Debug:', str);
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
  });

  return new Promise((resolve, reject) => {
    stompClient.onConnect = (frame) => {
      console.log('✅ WebSocket connected:', frame);
      resolve(stompClient);
    };

    stompClient.onStompError = (frame) => {
      console.error('❌ WebSocket connection error:', frame.headers['message']);
      reject(frame);
    };

    stompClient.onDisconnect = () => {
      console.log('🔌 WebSocket disconnected');
    };

    stompClient.activate();
  });
}

export function subscribeToChat(chatId, callback) {
  if (!stompClient || !stompClient.connected) {
    console.error('WebSocket is not connected. Cannot subscribe.');
    return null;
  }
  
  const subscription = stompClient.subscribe(`/topic/chat/${chatId}`, (message) => {
    const messageBody = JSON.parse(message.body);
    console.log('📩 Received message for chat', chatId, ':', messageBody);
    callback(messageBody);
  });
  
  console.log(`🔔 Subscribed to /topic/chat/${chatId}`);
  return subscription;
}

export function sendMessage(chatId, text) {
  if (!stompClient || !stompClient.connected) {
    console.error('WebSocket is not connected. Cannot send message.');
    return;
  }

  const destination = `/app/chat/${chatId}/sendMessage`;
  const body = JSON.stringify({
    message: text
  });

  stompClient.publish({
    destination,
    body
  });

  console.log('📤 Message sent to', destination, ':', body);
}

export function disconnect() {
  if (stompClient) {
    stompClient.deactivate();
  }
}

export function isConnected() {
  return stompClient && stompClient.connected;
}