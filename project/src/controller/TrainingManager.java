package controller;

import entity.AttendanceRecord;
import entity.Player;
import entity.TrainingSession;
import util.Notification;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class that manages training sessions and player attendance.
 */
public class TrainingManager {
    private List<TrainingSession> listSession;
    private List<AttendanceRecord> listAttendance;

    public TrainingManager() {
        this.listSession = new ArrayList<>();
        this.listAttendance = new ArrayList<>();
    }

    public List<TrainingSession> getListSession() {
        return listSession;
    }

    public List<AttendanceRecord> getListAttendance() {
        return listAttendance;
    }

    public boolean addSession(TrainingSession session) {
        if (session == null) {
            System.out.println(Notification.SESSION_NULL.getMessage());
            return false;
        }
        if (searchSessionById(session.getTrainingId()) != null) {
            System.out.println(Notification.DUPLICATE_SESSION_ID.getMessage(session.getTrainingId()));
            return false;
        }
        listSession.add(session);
        return true;
    }

    public TrainingSession searchSessionById(String id) {
        if (id == null) return null;
        for (TrainingSession ts : listSession) {
            if (ts.getTrainingId().equalsIgnoreCase(id.trim())) {
                return ts;
            }
        }
        return null;
    }

    public void displayAllSessions() {
        if (listSession.isEmpty()) {
            System.out.println(Notification.SESSION_LIST_EMPTY.getMessage());
            return;
        }
        System.out.println("==================================== DANH SÁCH BUỔI TẬP ====================================");
        for (TrainingSession ts : listSession) {
            System.out.println(ts);
        }
        System.out.println("==========================================================================================");
    }

    /**
     * Records or updates training attendance (Task S7).
     * If attendance already exists, it updates status based on original snapshot players only (BR18).
     * If not, it creates a new record using the provided list of currently active players (BR16).
     */
    public AttendanceRecord recordAttendance(String trainingId, List<String> absentPlayerIds, List<Player> activePlayersAtTime) {
        AttendanceRecord record = searchAttendanceBySessionId(trainingId);
        
        if (record != null) {
            // Overwrite attendance using the existing snapshot player list (BR18)
            Map<String, Boolean> oldMap = record.getAttendance();
            Map<String, Boolean> newMap = new HashMap<>();
            for (String pid : oldMap.keySet()) {
                // If player ID is in the absent list, mark false. Otherwise, mark true.
                newMap.put(pid, !absentPlayerIds.contains(pid));
            }
            record.setAttendance(newMap);
        } else {
            // Create a new attendance record based on current active players list (BR16)
            record = new AttendanceRecord(trainingId);
            for (Player p : activePlayersAtTime) {
                String pid = p.getPlayerId();
                record.updateAttendanceStatus(pid, !absentPlayerIds.contains(pid));
            }
            listAttendance.add(record);
        }
        return record;
    }

    /**
     * Method to overwrite/add raw AttendanceRecord (used mainly by DataManager when loading files)
     */
    public boolean recordAttendanceDirectly(AttendanceRecord record) {
        if (record == null) return false;
        AttendanceRecord existing = searchAttendanceBySessionId(record.getTrainingId());
        if (existing != null) {
            listAttendance.remove(existing);
        }
        listAttendance.add(record);
        return true;
    }

    public AttendanceRecord searchAttendanceBySessionId(String sessionId) {
        if (sessionId == null) return null;
        for (AttendanceRecord ar : listAttendance) {
            if (ar.getTrainingId().equalsIgnoreCase(sessionId.trim())) {
                return ar;
            }
        }
        return null;
    }

    public void displayAttendance(String sessionId) {
        AttendanceRecord ar = searchAttendanceBySessionId(sessionId);
        if (ar == null) {
            System.out.println(Notification.ATTENDANCE_NOT_FOUND.getMessage(sessionId));
            return;
        }
        System.out.println("=================== ĐIỂM DANH BUỔI TẬP: " + sessionId + " ===================");
        int presentCount = 0;
        int absentCount = 0;
        for (Map.Entry<String, Boolean> entry : ar.getAttendance().entrySet()) {
            boolean present = entry.getValue();
            if (present) presentCount++;
            else absentCount++;
            System.out.println("Player ID: " + entry.getKey() + " | Trạng thái: " + (present ? "Có mặt (Present)" : "Vắng mặt (Absent)"));
        }
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Tóm tắt: Có mặt (Present): " + presentCount + " | Vắng mặt (Absent): " + absentCount);
        System.out.println("==========================================================================");
    }

    /**
     * Delete: Remove a training session and its associated attendance record.
     */
    public boolean deleteSession(String id) {
        TrainingSession session = searchSessionById(id);
        if (session == null) {
            System.out.println(Notification.SESSION_NOT_FOUND.getMessage(id));
            return false;
        }

        listSession.remove(session);
        // Clean up associated attendance record
        AttendanceRecord ar = searchAttendanceBySessionId(id);
        if (ar != null) {
            listAttendance.remove(ar);
        }
        System.out.println(Notification.SUCCESS_DELETE_SESSION.getMessage(id));
        return true;
    }

    /**
     * Auto-generates the next Training ID by finding the current max numeric suffix and incrementing.
     */
    public String generateNextTrainingId() {
        int maxNum = 0;
        for (TrainingSession ts : listSession) {
            try {
                int num = Integer.parseInt(ts.getTrainingId().substring(2));
                if (num > maxNum) maxNum = num;
            } catch (NumberFormatException e) {
                // Skip malformed IDs
            }
        }
        return String.format("TR%04d", maxNum + 1);
    }

    /**
     * Returns a copy of the session list sorted by the numeric part of the Training ID.
     */
    public List<TrainingSession> getSessionsSortedById() {
        List<TrainingSession> sorted = new ArrayList<>(listSession);
        sorted.sort((a, b) -> {
            int numA = Integer.parseInt(a.getTrainingId().substring(2));
            int numB = Integer.parseInt(b.getTrainingId().substring(2));
            return Integer.compare(numA, numB);
        });
        return sorted;
    }
}
