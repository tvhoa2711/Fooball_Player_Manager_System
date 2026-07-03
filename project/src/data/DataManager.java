package data;

import controller.Cabinet;
import entity.AttendanceRecord;
import entity.MatchRecord;
import entity.PerformanceRecord;
import entity.Player;
import entity.RegularPlayer;
import entity.StarPlayer;
import entity.TrainingSession;
import util.Notification;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data layer class responsible for loading and saving project data to text files.
 */
public class DataManager {
    private Cabinet cabinet;

    // File names for data persistence
    private static final String FILE_PLAYERS = "players.txt";
    private static final String FILE_TRAININGS = "trainings.txt";
    private static final String FILE_ATTENDANCE = "attendance.txt";
    private static final String FILE_MATCHES = "matches.txt";
    private static final String FILE_PERFORMANCES = "performances.txt";

    public DataManager(Cabinet cabinet) {
        this.cabinet = cabinet;
    }

    /**
     * Loads all data from text files upon startup.
     * If files do not exist, it populates mock data and creates the files automatically.
     */
    public void loadData() {
        System.out.println(Notification.DATA_LOAD_START.getMessage());

        File playerFile = new File(FILE_PLAYERS);
        if (!playerFile.exists()) {
            System.out.println("⚠️ Không tìm thấy các file dữ liệu. Đang tạo dữ liệu mẫu mặc định...");
            createMockData();
            saveData();
            return;
        }

        try {
            // 1. Load Players
            loadPlayers();

            // 2. Load Training Sessions
            loadTrainings();

            // 3. Load Attendance
            loadAttendance();

            // 4. Load Matches
            loadMatches();

            // 5. Load Performance Records
            loadPerformances();

            System.out.println(Notification.DATA_LOAD_SUCCESS.getMessage(cabinet.getPlayerManager().getListPlayer().size()));
        } catch (IOException e) {
            System.out.println("❌ Lỗi xảy ra khi đọc dữ liệu từ file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Saves all system data from memory into the text files.
     */
    public void saveData() {
        System.out.println(Notification.DATA_SAVE_START.getMessage());

        try {
            savePlayers();
            saveTrainings();
            saveAttendance();
            saveMatches();
            savePerformances();
            System.out.println(Notification.DATA_SAVE_SUCCESS.getMessage());
        } catch (IOException e) {
            System.out.println("❌ Lỗi xảy ra khi ghi dữ liệu ra file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==========================================
    // PRIVATE LOAD METHODS (FILE READERS)
    // ==========================================

    private void loadPlayers() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(FILE_PLAYERS));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            // Format: playerId|fullName|age|nationality|position|shirtNumber|baseSalary|status|playerType
            String[] tokens = line.split("\\|");
            if (tokens.length < 9) continue;

            String id = tokens[0];
            String name = tokens[1];
            int age = Integer.parseInt(tokens[2]);
            String nationality = tokens[3];
            String position = tokens[4];
            int shirt = Integer.parseInt(tokens[5]);
            double salary = Double.parseDouble(tokens[6]);
            String status = tokens[7];
            String type = tokens[8];

            Player player;
            if (type.equalsIgnoreCase("Star")) {
                player = new StarPlayer(id, name, age, nationality, position, shirt, salary, status);
            } else {
                player = new RegularPlayer(id, name, age, nationality, position, shirt, salary, status);
            }
            cabinet.getPlayerManager().addPlayer(player);
        }
        reader.close();
    }

    private void loadTrainings() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(FILE_TRAININGS));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            // Format: trainingId|date|location|topic
            String[] tokens = line.split("\\|");
            if (tokens.length < 4) continue;

            String id = tokens[0];
            String date = tokens[1];
            String location = tokens[2];
            String topic = tokens[3];

            TrainingSession session = new TrainingSession(id, date, location, topic);
            cabinet.getTrainingManager().addSession(session);
        }
        reader.close();
    }

    private void loadAttendance() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(FILE_ATTENDANCE));
        String line;
        // Map to temporarily hold AttendanceRecord objects by session ID
        Map<String, AttendanceRecord> recordMap = new HashMap<>();

        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            // Format: trainingId|playerId|isPresent
            String[] tokens = line.split("\\|");
            if (tokens.length < 3) continue;

            String trId = tokens[0];
            String plId = tokens[1];
            boolean isPresent = Boolean.parseBoolean(tokens[2]);

            AttendanceRecord record = recordMap.get(trId);
            if (record == null) {
                record = new AttendanceRecord(trId);
                recordMap.put(trId, record);
            }
            record.updateAttendanceStatus(plId, isPresent);
        }
        reader.close();

        // Put all constructed records to TrainingManager
        for (AttendanceRecord ar : recordMap.values()) {
            cabinet.getTrainingManager().recordAttendanceDirectly(ar);
        }
    }

    private void loadMatches() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(FILE_MATCHES));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            // Format: matchId|date|opponentTeam|matchType
            String[] tokens = line.split("\\|");
            if (tokens.length < 4) continue;

            String id = tokens[0];
            String date = tokens[1];
            String opponent = tokens[2];
            String type = tokens[3];

            MatchRecord match = new MatchRecord(id, date, opponent, type);
            cabinet.getMatchManager().addMatch(match);
        }
        reader.close();
    }

    private void loadPerformances() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(FILE_PERFORMANCES));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            // Format: matchId|playerId|goals|assists|yellowCards|redCards|minutesPlayed
            String[] tokens = line.split("\\|");
            if (tokens.length < 7) continue;

            String matchId = tokens[0];
            String playerId = tokens[1];
            int goals = Integer.parseInt(tokens[2]);
            int assists = Integer.parseInt(tokens[3]);
            int yellow = Integer.parseInt(tokens[4]);
            int red = Integer.parseInt(tokens[5]);
            int mins = Integer.parseInt(tokens[6]);

            PerformanceRecord perf = new PerformanceRecord(matchId, playerId, goals, assists, yellow, red, mins);
            cabinet.getMatchManager().addPerformanceToMatch(matchId, perf);
        }
        reader.close();
    }

    // ==========================================
    // PRIVATE SAVE METHODS (FILE WRITERS)
    // ==========================================

    private void savePlayers() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PLAYERS));
        for (Player p : cabinet.getPlayerManager().getListPlayer()) {
            String type = (p instanceof StarPlayer) ? "Star" : "Regular";
            writer.write(String.format("%s|%s|%d|%s|%s|%d|%.0f|%s|%s\n",
                    p.getPlayerId(), p.getFullName(), p.getAge(), p.getNationality(),
                    p.getPosition(), p.getShirtNumber(), p.getBaseSalary(), p.getStatus(), type));
        }
        writer.close();
    }

    private void saveTrainings() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_TRAININGS));
        for (TrainingSession ts : cabinet.getTrainingManager().getListSession()) {
            writer.write(String.format("%s|%s|%s|%s\n",
                    ts.getTrainingId(), ts.getDate(), ts.getLocation(), ts.getTopic()));
        }
        writer.close();
    }

    private void saveAttendance() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_ATTENDANCE));
        for (AttendanceRecord ar : cabinet.getTrainingManager().getListAttendance()) {
            for (Map.Entry<String, Boolean> entry : ar.getAttendance().entrySet()) {
                writer.write(String.format("%s|%s|%b\n",
                        ar.getTrainingId(), entry.getKey(), entry.getValue()));
            }
        }
        writer.close();
    }

    private void saveMatches() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_MATCHES));
        for (MatchRecord mr : cabinet.getMatchManager().getListMatch()) {
            writer.write(String.format("%s|%s|%s|%s\n",
                    mr.getMatchId(), mr.getDate(), mr.getOpponentTeam(), mr.getMatchType()));
        }
        writer.close();
    }

    private void savePerformances() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PERFORMANCES));
        for (MatchRecord mr : cabinet.getMatchManager().getListMatch()) {
            for (PerformanceRecord pr : mr.getPerformances()) {
                writer.write(String.format("%s|%s|%d|%d|%d|%d|%d\n",
                        pr.getMatchId(), pr.getPlayerId(), pr.getGoals(), pr.getAssists(),
                        pr.getYellowCards(), pr.getRedCards(), pr.getMinutesPlayed()));
            }
        }
        writer.close();
    }

    /**
     * Helper to generate initial mock database for testing if files don't exist.
     */
    private void createMockData() {
        // 1. Players
        cabinet.getPlayerManager().addPlayer(new RegularPlayer("PL0001", "Nguyen Van Quyet", 34, "Vietnam", "MF", 10, 15000000.0, "Active"));
        cabinet.getPlayerManager().addPlayer(new StarPlayer("PL0002", "Nguyen Quang Hai", 29, "Vietnam", "MF", 19, 25000000.0, "Active"));
        cabinet.getPlayerManager().addPlayer(new RegularPlayer("PL0003", "Dang Van Lam", 32, "Vietnam", "GK", 35, 18000000.0, "Active"));
        cabinet.getPlayerManager().addPlayer(new StarPlayer("PL0004", "Nguyen Cong Phuong", 31, "Vietnam", "FW", 70, 20000000.0, "Inactive"));

        // 2. Training sessions
        TrainingSession ts1 = new TrainingSession("TR0001", "01/06/2026", "My Dinh Stadium", "Tactics & Chemistry");
        TrainingSession ts2 = new TrainingSession("TR0002", "03/06/2026", "Training Center", "Fitness & Stamina");
        cabinet.getTrainingManager().addSession(ts1);
        cabinet.getTrainingManager().addSession(ts2);

        // Attendance records
        AttendanceRecord ar1 = new AttendanceRecord("TR0001");
        ar1.updateAttendanceStatus("PL0001", true);
        ar1.updateAttendanceStatus("PL0002", true);
        ar1.updateAttendanceStatus("PL0003", false); // Absent
        cabinet.getTrainingManager().recordAttendanceDirectly(ar1);

        // 3. Matches
        MatchRecord mr1 = new MatchRecord("MA0001", "02/06/2026", "Thailand FC", "Friendly");
        MatchRecord mr2 = new MatchRecord("MA0002", "15/06/2026", "Malaysia Utd", "League");
        cabinet.getMatchManager().addMatch(mr1);
        cabinet.getMatchManager().addMatch(mr2);

        // Player Performances
        // Match 1
        cabinet.getMatchManager().addPerformanceToMatch("MA0001", new PerformanceRecord("MA0001", "PL0001", 1, 1, 0, 0, 90)); // 1 goal, 1 assist
        cabinet.getMatchManager().addPerformanceToMatch("MA0001", new PerformanceRecord("MA0001", "PL0002", 0, 2, 1, 0, 80)); // 2 assists, 1 yellow
        cabinet.getMatchManager().addPerformanceToMatch("MA0001", new PerformanceRecord("MA0001", "PL0003", 0, 0, 0, 0, 90)); // GK, clean match

        // Match 2
        cabinet.getMatchManager().addPerformanceToMatch("MA0002", new PerformanceRecord("MA0002", "PL0001", 2, 0, 0, 0, 75)); // 2 goals
        cabinet.getMatchManager().addPerformanceToMatch("MA0002", new PerformanceRecord("MA0002", "PL0002", 1, 1, 0, 0, 90)); // 1 goal, 1 assist
    }
}
