package com.webpush.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FirebaseMessagingService {

    private final FirebaseMessaging firebaseMessaging;

    public FirebaseMessagingService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    public String sendNotification(Note note, String token) throws FirebaseMessagingException {
        Message message = getMessage(note, token);
        return firebaseMessaging.send(message);
    }

    public String sendNotificationDynamic(Note note, String token, String clientId) throws FirebaseMessagingException, IOException {
        Message message = getMessage(note, token);
        return FirebaseService.getFirebaseMessaging(clientId).send(message);
    }

    private Message getMessage(Note note, String token) {
        Notification notification = Notification
                .builder()
                .setTitle(note.getSubject())
                .setBody(note.getContent())
                .build();

        Message message = Message
                .builder()
                .setToken(token)
                .setNotification(notification)
                .putAllData(note.getData())
                .build();
        return message;
    }
}