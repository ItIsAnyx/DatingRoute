// src/services/websocketService.js
import { Client } from 'stompjs';
import SockJS from 'sockjs-client';

let stompClient = null;

export function connect() {
  const socket = new SockJS('http://localhost:8081/ws');
  // Используем Stomp.over, а не Client.over
  stompClient = Stomp.over(socket); 

  return new Promise((resolve, reject) => {
    stompClient.connect({}, (frame) => {
      console.log('Connected: ' + frame);
      resolve(stompClient);
    }, (error) => {
      console.error('WebSocket connection error:', error);
      reject(error);
    });
  });
}

export function subscribeToChat(chatId, callback) {
  if (!stompClient) {
    console.error('WebSocket is not connected');
    return;
  }
  // Подписываемся на общий топик, как в примере бэкенда
  // Важно: бэкенд должен фильтровать сообщения и присылать только те,
  // что относятся к текущему чату.
  return stompClient.subscribe('/topic/public', (message) => {
    const messageBody = JSON.parse(message.body);
    console.log('Received message:', messageBody);
    callback(messageBody);
  });
}

export function sendMessage(chatId, text, isTest = false) {
  if (!stompClient || !stompClient.connected) {
    console.error('WebSocket is not connected');
    return;
  }

  const destination = '/app/sendMessage'; // /app - префикс по умолчанию
  const body = JSON.stringify({
    chat_id: chatId,
    message: text,
    test: isTest
  });

  stompClient.send(destination, {}, body);
}

export function disconnect() {
  if (stompClient && stompClient.connected) {
    stompClient.disconnect();
  }
}