package entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Entity representing the attendance record of a training session.
 * Designed to hold the training session ID and the present/absent map of player IDs.
 */
public class AttendanceRecord {
    private String trainingId;
    private Map<String, Boolean> attendance; // Key: playerId, Value: true (Present), false (Absent)

    public AttendanceRecord(String trainingId) {
        this.trainingId = trainingId;
        this.attendance = new HashMap<>();
    }

    public String getTrainingId() { 
        return trainingId; 
    }
    
    public void setTrainingId(String trainingId) {
        this.trainingId = trainingId;
    }

    public Map<String, Boolean> getAttendance() { 
        return attendance; 
    }
    
    public void setAttendance(Map<String, Boolean> attendance) {
        this.attendance = attendance;
    }

    /**
     * Records the presence or absence of a player.
     * Single responsibility: stores the mapping without input format validation.
     */
    public void updateAttendanceStatus(String playerId, boolean isPresent) {
        if (playerId != null) {
            this.attendance.put(playerId.trim(), isPresent);
        }
    }

    @Override
    public String toString() {
        return "Attendance for Session: " + trainingId + " | Total Players tracked: " + attendance.size();
    }
}
