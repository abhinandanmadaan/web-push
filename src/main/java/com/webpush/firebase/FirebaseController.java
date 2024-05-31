package com.webpush.firebase;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class FirebaseController {
    // Send via firebase - approach 2
    @Autowired
    private FirebaseMessagingService firebaseMessagingService;
    private final Map<String, List<String>> firebaseSubscriptions = new HashMap<>();

    // Send via firebase - approach 2
    @PostMapping("/firebase/send-notification")
    public String sendNotification(@RequestBody Note note,
                                   @RequestParam String user) throws FirebaseMessagingException {
        if(firebaseSubscriptions.containsKey(user)) {
            for (String userToken : firebaseSubscriptions.get(user)) {
                firebaseMessagingService.sendNotification(note, userToken);
            }
        }
        return "OK";
    }

    @PostMapping("/firebase/send-notification-dynamic")
    public String sendNotificationDynamic(@RequestBody Note note,
                                   @RequestParam String user, @RequestParam String clientId) throws FirebaseMessagingException, IOException {
        if(firebaseSubscriptions.containsKey(user)) {
            for (String userToken : firebaseSubscriptions.get(user)) {
                firebaseMessagingService.sendNotificationDynamic(note, userToken, clientId);
            }
        }
        return "OK";
    }

    @PostMapping("/register")
    public void subscribe(@RequestParam String token, @RequestParam String user) {
        System.out.println("Subscribed to " + user);
        if (firebaseSubscriptions.containsKey(user)) {
            // Key exists, get the list associated with the key and add data to it
            List<String> dataList = firebaseSubscriptions.get(user);
            dataList.add(token);
        } else {
            // Key doesn't exist, create a new list, add data to it, and put it in the map
            List<String> dataList = new ArrayList<>();
            dataList.add(token);
            firebaseSubscriptions.put(user, dataList);
        }
    }

    @PostMapping("/un-register")
    public void unsubscribe(@RequestParam String token, @RequestParam String user) {
        System.out.println("Unsubscribed " + user);
        firebaseSubscriptions.remove(user);
    }

}
