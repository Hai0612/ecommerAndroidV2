package com.example.ecomapplication.models;

import java.util.List;

public class FCMNotification {
    public Notification notification;
    public Data data;
    public List<String> registration_ids;

    public FCMNotification(Notification notification, Data data, List<String> registration_ids) {
        this.notification = notification;
        this.data = data;
        this.registration_ids = registration_ids;
    }

    public static Notification createNotification(String title, String body) {
        return new Notification(title, body);
    }

    public static Data createData(String title, String body) {
        return new Data(title, body);
    }

    public static class Notification {
        public String title;
        public String body;

        public Notification(String title, String body) {
            this.title = title;
            this.body = body;
        }
    }

    public static class Data {
        public String title;
        public String body;

        public Data(String title, String body) {
            this.title = title;
            this.body = body;
        }
    }
}
