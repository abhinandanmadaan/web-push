package com.webpush.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class FirebaseService {

    private static final String CONFIG_PATH = "src/main/resources/";
    private static final ConcurrentHashMap<String, FirebaseApp> firebaseApps = new ConcurrentHashMap<>();

    public static FirebaseMessaging getFirebaseMessaging(String clientId) {
        firebaseApps.computeIfAbsent(clientId, id -> initializeFirebaseApp(id));
        return FirebaseMessaging.getInstance(firebaseApps.get(clientId));
    }

    private static FirebaseApp initializeFirebaseApp(String clientId) {
        try {
            FileInputStream serviceAccount = new FileInputStream(CONFIG_PATH + "web-push-" + clientId + ".json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            return FirebaseApp.initializeApp(options, clientId);
        } catch (IOException e) {
            throw new RuntimeException("Error initializing FirebaseApp for client: " + clientId, e);
        }
    }
}