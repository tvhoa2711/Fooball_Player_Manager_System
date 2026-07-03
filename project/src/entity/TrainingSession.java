package entity;

import util.Notification;

/**
 * Entity representing a training session in the club.
 * Fully encapsulated with static validation helpers and final setters.
 */
public class TrainingSession {
    private String trainingId;
    private String date; // dd/MM/yyyy
    private String location;
    private String topic;

    public TrainingSession(String trainingId, String date, String location, String topic) {
        setTrainingId(trainingId);
        setDate(date);
        setLocation(location);
        setTopic(topic);
    }

    // ==========================================
    // STATIC VALIDATION HELPERS
    // ==========================================

    public static boolean isValidTrainingId(String id) {
        return id != null && id.trim().matches("^TR\\d{4}$");
    }

    public static boolean isValidDate(String date) {
        return date != null && date.trim().matches("^\\d{2}/\\d{2}/\\d{4}$");
    }

    public static boolean isValidLocation(String location) {
        return location != null && !location.trim().isEmpty();
    }

    public static boolean isValidTopic(String topic) {
        return topic != null && !topic.trim().isEmpty();
    }

    // ==========================================
    // GETTERS & FINAL SETTERS
    // ==========================================

    public String getTrainingId() { return trainingId; }
    
    public final void setTrainingId(String trainingId) {
        if (!isValidTrainingId(trainingId)) {
            throw new IllegalArgumentException(Notification.VAL_TRAINING_ID.getMessage());
        }
        this.trainingId = trainingId.trim();
    }

    public String getDate() { return date; }
    
    public final void setDate(String date) {
        if (!isValidDate(date)) {
            throw new IllegalArgumentException(Notification.VAL_TRAINING_DATE.getMessage());
        }
        this.date = date.trim();
    }

    public String getLocation() { return location; }
    
    public final void setLocation(String location) {
        if (!isValidLocation(location)) {
            throw new IllegalArgumentException(Notification.VAL_TRAINING_LOCATION.getMessage());
        }
        this.location = location.trim();
    }

    public String getTopic() { return topic; }
    
    public final void setTopic(String topic) {
        if (!isValidTopic(topic)) {
            throw new IllegalArgumentException(Notification.VAL_TRAINING_TOPIC.getMessage());
        }
        this.topic = topic.trim();
    }

    @Override
    public String toString() {
        return String.format("Session ID: %-8s | Date: %-10s | Location: %-15s | Topic: %-20s",
                trainingId, date, location, topic);
    }
}
