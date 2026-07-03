package project;

import controller.Cabinet;
import entity.AttendanceRecord;
import entity.MatchRecord;
import entity.PerformanceRecord;
import entity.Player;
import entity.RegularPlayer;
import entity.StarPlayer;
import entity.TrainingSession;
import util.Menu;
import util.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Main Runner class of the Football Player Management System.
 * Fully aligned with the 15 Task functional specifications (S1 to S15).
 */
public class Project {
    private static final Cabinet cabinet = new Cabinet();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Load initial data through DataManager (creates files with mock data if not existing)
        cabinet.getDataManager().loadData();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = Menu.readInt("Choose an option: ", 1, 5);
            switch (choice) {
                case 1:
                    runPlayerMenu();
                    break;
                case 2:
                    runTrainingMatchMenu();
                    break;
                case 3:
                    runContractSalaryMenu();
                    break;
                case 4:
                    runReportMenu();
                    break;
                case 5:
                    runExitMenu();
                    break;
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n======================================");
        System.out.println("  FOOTBALL PLAYER MANAGEMENT SYSTEM ");
        System.out.println("======================================");
        System.out.println("1. Manage Players");
        System.out.println("2. Training and Match Management");
        System.out.println("3. Contract and Salary Management");
        System.out.println("4. Reports");
        System.out.println("5. Exit");
        System.out.println("--------------------------------------");
    }

    // ==========================================
    // 1. MANAGE PLAYERS SUBMENU (S1 - S5)
    // ==========================================
    private static void runPlayerMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----------- MANAGE PLAYERS -----------");
            System.out.println("1. S1 - Add Player");
            System.out.println("2. S2 - Update Player");
            System.out.println("3. S3 - View All Players");
            System.out.println("4. S4 - Search Players");
            System.out.println("5. S5 - View Player Details");
            System.out.println("6. Back to Main Menu");
            System.out.println("--------------------------------------");
            int choice = Menu.readInt("Choose an option (1-6): ", 1, 6);
            switch (choice) {
                case 1:
                    handleS1AddPlayer();
                    break;
                case 2:
                    handleS2UpdatePlayer();
                    break;
                case 3:
                    handleS3ViewAllPlayers();
                    break;
                case 4:
                    handleS4SearchPlayers();
                    break;
                case 5:
                    handleS5ViewPlayerDetails();
                    break;
                case 6:
                    back = true;
                    break;
            }
        }
    }

    private static void handleS1AddPlayer() {
        System.out.println("\n----------- ADD PLAYER -----------");
        String id = cabinet.getPlayerManager().generateNextPlayerId();
        System.out.println("Auto-generated Player ID: " + id);
        String name = Menu.readString("Full Name: ", "^[\\p{L}\\s]+$", "Tên chỉ chứa chữ cái và khoảng trắng!");
        int age = Menu.readInt("Age (16-45): ", 16, 45); // BR4 age must be 16-45
        String nationality = Menu.readNonEmptyString("Nationality: ");
        
        String position;
        while (true) {
            position = Menu.readNonEmptyString("Position (Goalkeeper/Defender/Midfielder/Forward): ");
            // Map inputs to GK, DF, MF, FW to store cleanly
            String posLower = position.trim().toLowerCase();
            if (posLower.equals("goalkeeper") || posLower.equals("gk")) {
                position = "GK";
                break;
            } else if (posLower.equals("defender") || posLower.equals("df")) {
                position = "DF";
                break;
            } else if (posLower.equals("midfielder") || posLower.equals("mf")) {
                position = "MF";
                break;
            } else if (posLower.equals("forward") || posLower.equals("fw")) {
                position = "FW";
                break;
            }
            System.out.println("Lỗi: Vị trí phải thuộc một trong các loại Goalkeeper (GK), Defender (DF), Midfielder (MF), Forward (FW)!");
        }

        int shirt = Menu.readInt("Shirt Number (1-99): ", 1, 99);
        double salary = Menu.readDouble("Base Salary (VND): ", 1, Double.MAX_VALUE);

        System.out.println("Player Type:");
        System.out.println("1. Regular Player");
        System.out.println("2. Star Player");
        int typeChoice = Menu.readInt("Choose Player Type: ", 1, 2);

        String status;
        while (true) {
            status = Menu.readNonEmptyString("Status (Active/Inactive): ").trim();
            if (status.equalsIgnoreCase("Active") || status.equalsIgnoreCase("Inactive")) {
                status = status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();
                break;
            }
            System.out.println("Lỗi: Trạng thái phải là Active hoặc Inactive!");
        }

        System.out.println("[1] Submit [2] Cancel");
        int opt = Menu.readInt("Choose an option: ", 1, 2);
        if (opt == 1) {
            Player player = (typeChoice == 1)
                    ? new RegularPlayer(id, name, age, nationality, position, shirt, salary, status)
                    : new StarPlayer(id, name, age, nationality, position, shirt, salary, status);

            if (cabinet.getPlayerManager().addPlayer(player)) {
                cabinet.getDataManager().saveData();
                System.out.println("Output:\nPlayers added successfully.");
            } else {
                System.out.println("Output:\nFailed to add player.");
            }
        } else {
            System.out.println("Output:\nAction cancelled.");
        }
    }

    private static void handleS2UpdatePlayer() {
        System.out.println("\n----------- UPDATE PLAYER -----------");
        String id = Menu.readString("Enter Player ID: ", "^PL\\d{4}$", "ID dạng PLXXXX!");
        Player p = cabinet.getPlayerManager().searchPlayerById(id);
        if (p == null) {
            System.out.println(Notification.PLAYER_NOT_FOUND.getMessage(id));
            return;
        }

        // Display current info
        String typeStr = (p instanceof StarPlayer) ? "Star Player" : "Regular Player";
        System.out.println("Current Information:");
        System.out.format("Name: %s | Position: %s | No.%d\n", p.getFullName(), p.getPosition(), p.getShirtNumber());
        System.out.format("Base Salary: %,.0f | Type: %s | Status: %s\n", p.getBaseSalary(), typeStr, p.getStatus());

        // Read new inputs
        String position;
        while (true) {
            position = Menu.readNonEmptyString("Enter new Position (Goalkeeper/Defender/Midfielder/Forward): ");
            String posLower = position.trim().toLowerCase();
            if (posLower.equals("goalkeeper") || posLower.equals("gk")) {
                position = "GK";
                break;
            } else if (posLower.equals("defender") || posLower.equals("df")) {
                position = "DF";
                break;
            } else if (posLower.equals("midfielder") || posLower.equals("mf")) {
                position = "MF";
                break;
            } else if (posLower.equals("forward") || posLower.equals("fw")) {
                position = "FW";
                break;
            }
            System.out.println("Lỗi: Vị trí không hợp lệ!");
        }

        int shirt = Menu.readInt("Enter new Shirt Number (1-99): ", 1, 99);
        double salary = Menu.readDouble("Enter new Base Salary: ", 1, Double.MAX_VALUE);

        String status;
        while (true) {
            status = Menu.readNonEmptyString("Enter new Status (Active/Inactive): ").trim();
            if (status.equalsIgnoreCase("Active") || status.equalsIgnoreCase("Inactive")) {
                status = status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();
                break;
            }
            System.out.println("Lỗi: Trạng thái phải là Active hoặc Inactive!");
        }

        System.out.println("[1] Update [2] Cancel");
        int opt = Menu.readInt("Choose an option: ", 1, 2);
        if (opt == 1) {
            // Player type cannot be changed after creation
            if (cabinet.getPlayerManager().updatePlayer(id, p.getFullName(), p.getAge(), p.getNationality(), position, shirt, salary, status)) {
                cabinet.getDataManager().saveData();
                System.out.println("Output:\nThe player updated successfully.");
            } else {
                System.out.println("Output:\nFailed to update player information.");
            }
        } else {
            System.out.println("Output:\nAction cancelled.");
        }
    }

    private static void handleS3ViewAllPlayers() {
        System.out.println("\n----------- PLAYER LIST -----------");
        List<Player> list = cabinet.getPlayerManager().getPlayersSortedById();
        if (list.isEmpty()) {
            System.out.println(Notification.PLAYER_LIST_EMPTY.getMessage());
        } else {
            System.out.format("%-6s %-20s %-4s %-10s %-6s %-16s %-8s\n", "ID", "Name", "Age", "Position", "Shirt", "Type", "Status");
            System.out.println("-----------------------------------------------------------------------");
            for (Player p : list) {
                String typeStr = (p instanceof StarPlayer) ? "Star Player" : "Regular Player";
                System.out.format("%-6s %-20s %-4d %-10s %-6d %-16s %-8s\n",
                        p.getPlayerId(), p.getFullName(), p.getAge(), p.getPosition(), p.getShirtNumber(), typeStr, p.getStatus());
            }
            System.out.println("-----------------------------------------------------------------------");
        }
        System.out.println("Press ENTER to return...");
        scanner.nextLine();
    }

    private static void handleS4SearchPlayers() {
        System.out.println("\n----------- SEARCH PLAYERS -----------");
        System.out.println("Search by:");
        System.out.println("1. Name");
        System.out.println("2. Position");
        System.out.println("3. Nationality");
        System.out.println("4. Status");
        int type = Menu.readInt("Choose search type: ", 1, 4);
        String keyword = Menu.readNonEmptyString("Enter keyword: ");

        List<Player> results = cabinet.getPlayerManager().searchPlayers(type, keyword);
        // Sort search results by ID
        results.sort((a, b) -> {
            int numA = Integer.parseInt(a.getPlayerId().substring(2));
            int numB = Integer.parseInt(b.getPlayerId().substring(2));
            return Integer.compare(numA, numB);
        });
        System.out.println("\nSearch Results:");
        if (results.isEmpty()) {
            System.out.println("Không tìm thấy kết quả nào phù hợp!");
        } else {
            System.out.format("%-6s %-20s %-4s %-10s %-6s %-16s %-8s\n", "ID", "Name", "Age", "Position", "Shirt", "Type", "Status");
            System.out.println("-----------------------------------------------------------------------");
            for (Player p : results) {
                String typeStr = (p instanceof StarPlayer) ? "Star Player" : "Regular Player";
                System.out.format("%-6s %-20s %-4d %-10s %-6d %-16s %-8s\n",
                        p.getPlayerId(), p.getFullName(), p.getAge(), p.getPosition(), p.getShirtNumber(), typeStr, p.getStatus());
            }
            System.out.println("-----------------------------------------------------------------------");
        }
        System.out.println("Press ENTER to return...");
        scanner.nextLine();
    }

    private static void handleS5ViewPlayerDetails() {
        System.out.println("\n----------- PLAYER DETAILS -----------");
        String id = Menu.readString("Enter Player ID: ", "^PL\\d{4}$", "ID dạng PLXXXX!");
        Player p = cabinet.getPlayerManager().searchPlayerById(id);
        if (p == null) {
            System.out.println(Notification.PLAYER_NOT_FOUND.getMessage(id));
        } else {
            String typeStr = (p instanceof StarPlayer) ? "Star Player" : "Regular Player";
            System.out.println("\nPlayer ID: " + p.getPlayerId());
            System.out.println("Full Name: " + p.getFullName());
            System.out.println("Age: " + p.getAge());
            System.out.println("Nationality: " + p.getNationality());
            System.out.println("Position: " + p.getPosition());
            System.out.println("Shirt Number: " + p.getShirtNumber());
            System.out.format("Base Salary: %,.0f VND\n", p.getBaseSalary());
            System.out.println("Player Type: " + typeStr);
            System.out.println("Status: " + p.getStatus());
        }
        System.out.println("\nPress ENTER to return...");
        scanner.nextLine();
    }

    // ==========================================
    // 2. TRAINING AND MATCH SUBMENU (S6 - S11)
    // ==========================================
    private static void runTrainingMatchMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----------- TRAINING & MATCH MANAGEMENT -----------");
            System.out.println("1. S6 - Create Training Session");
            System.out.println("2. S7 - Record Training Attendance");
            System.out.println("3. S8 - Create Match Record");
            System.out.println("4. S9 - View Training History");
            System.out.println("5. S10 - View Match History");
            System.out.println("6. S11 - Add / Update Player Performance");
            System.out.println("7. Back to Main Menu");
            System.out.println("--------------------------------------------------");
            int choice = Menu.readInt("Choose an option (1-7): ", 1, 7);
            switch (choice) {
                case 1:
                    handleS6CreateTrainingSession();
                    break;
                case 2:
                    handleS7RecordAttendance();
                    break;
                case 3:
                    handleS8CreateMatchRecord();
                    break;
                case 4:
                    handleS9ViewTrainingHistory();
                    break;
                case 5:
                    handleS10ViewMatchHistory();
                    break;
                case 6:
                    handleS11AddUpdatePerformance();
                    break;
                case 7:
                    back = true;
                    break;
            }
        }
    }

    private static void handleS6CreateTrainingSession() {
        System.out.println("\n----------- CREATE TRAINING SESSION -----------");
        String id = cabinet.getTrainingManager().generateNextTrainingId();
        System.out.println("Auto-generated Training ID: " + id);
        String date = Menu.readString("Date (dd/MM/yyyy): ", "^\\d{2}/\\d{2}/\\d{4}$", "Định dạng dd/MM/yyyy!");
        String location = Menu.readNonEmptyString("Location: ");
        String topic = Menu.readNonEmptyString("Training Topic: ");

        System.out.println("[1] Submit [2] Cancel");
        int opt = Menu.readInt("Choose an option: ", 1, 2);
        if (opt == 1) {
            if (cabinet.getTrainingManager().addSession(new TrainingSession(id, date, location, topic))) {
                cabinet.getDataManager().saveData();
                System.out.println("Output:\nTraining session created successfully.");
            }
        } else {
            System.out.println("Output:\nAction cancelled.");
        }
    }

    private static void handleS7RecordAttendance() {
        System.out.println("\n----------- RECORD TRAINING ATTENDANCE -----------");
        String sid = Menu.readString("Training ID: ", "^TR\\d{4}$", "Dạng TRXXXX!");
        TrainingSession session = cabinet.getTrainingManager().searchSessionById(sid);
        if (session == null) {
            System.out.println(Notification.SESSION_NOT_FOUND.getMessage(sid));
            return;
        }

        // Determine valid players list for attendance snapshot (BR18 snapshot or active players BR16)
        List<String> validPlayerIds = new ArrayList<>();
        AttendanceRecord existingAr = cabinet.getTrainingManager().searchAttendanceBySessionId(sid);
        int totalTracked;

        if (existingAr != null) {
            validPlayerIds.addAll(existingAr.getAttendance().keySet());
            totalTracked = validPlayerIds.size();
        } else {
            for (Player p : cabinet.getPlayerManager().getListPlayer()) {
                if (p.getStatus().equalsIgnoreCase("Active")) {
                    validPlayerIds.add(p.getPlayerId());
                }
            }
            totalTracked = validPlayerIds.size();
        }

        System.out.println("Date: " + session.getDate());
        System.out.println("Total Tracked Players: " + totalTracked);
        System.out.println("Enter absent Player IDs, separated by commas.");
        System.out.println("Leave blank if all included active players are present.");

        // Read and validate absent list
        List<String> absentPlayerIds = new ArrayList<>();
        while (true) {
            System.out.print("Absent Player IDs: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                break; // All present
            }
            String[] tokens = input.split(",");
            boolean allValid = true;
            List<String> tempAbsent = new ArrayList<>();
            for (String t : tokens) {
                String id = t.trim().toUpperCase();
                // Ensure id is one of the valid active snapshot players (BR15 & BR18)
                if (!validPlayerIds.contains(id)) {
                    System.out.println("❌ Lỗi: Cầu thủ ID '" + id + "' không hợp lệ hoặc không có trạng thái Active tại buổi tập này!");
                    allValid = false;
                    break;
                }
                // Check duplicate absent IDs (BR15)
                if (tempAbsent.contains(id)) {
                    System.out.println("❌ Lỗi: Trùng lặp Player ID '" + id + "' trong danh sách vắng mặt!");
                    allValid = false;
                    break;
                }
                tempAbsent.add(id);
            }
            if (allValid) {
                absentPlayerIds = tempAbsent;
                break;
            }
        }

        System.out.println("[1] Submit Attendance [2] Cancel");
        int opt = Menu.readInt("Choose an option: ", 1, 2);
        if (opt == 1) {
            // Get active players at time
            List<Player> activePlayers = new ArrayList<>();
            for (Player p : cabinet.getPlayerManager().getListPlayer()) {
                if (p.getStatus().equalsIgnoreCase("Active")) {
                    activePlayers.add(p);
                }
            }

            AttendanceRecord ar = cabinet.getTrainingManager().recordAttendance(sid, absentPlayerIds, activePlayers);
            if (ar != null) {
                int presentCount = 0;
                int absentCount = 0;
                for (boolean present : ar.getAttendance().values()) {
                    if (present) presentCount++;
                    else absentCount++;
                }

                cabinet.getDataManager().saveData();
                System.out.println("Output:\nTraining attendance was recorded successfully.");
                System.out.println("Summary:");
                System.out.println("Present: " + presentCount);
                System.out.println("Absent: " + absentCount);
            }
        } else {
            System.out.println("Output:\nAction cancelled.");
        }
    }

    private static void handleS8CreateMatchRecord() {
        System.out.println("\n----------- CREATE MATCH RECORD -----------");
        String id = cabinet.getMatchManager().generateNextMatchId();
        System.out.println("Auto-generated Match ID: " + id);
        String date = Menu.readString("Date (dd/MM/yyyy): ", "^\\d{2}/\\d{2}/\\d{4}$", "Định dạng dd/MM/yyyy!");
        String opponent = Menu.readNonEmptyString("Opponent Team: ");
        
        System.out.println("Match Type:");
        System.out.println("1. Friendly");
        System.out.println("2. League");
        System.out.println("3. Cup");
        int typeChoice = Menu.readInt("Choose Match Type: ", 1, 3);
        String matchType = "Friendly";
        if (typeChoice == 2) matchType = "League";
        else if (typeChoice == 3) matchType = "Cup";

        System.out.println("[1] Submit [2] Cancel");
        int opt = Menu.readInt("Choose an option: ", 1, 2);
        if (opt == 1) {
            if (cabinet.getMatchManager().addMatch(new MatchRecord(id, date, opponent, matchType))) {
                cabinet.getDataManager().saveData();
                System.out.println("Output:\nMatch record created successfully.");
            }
        } else {
            System.out.println("Output:\nAction cancelled.");
        }
    }

    private static void handleS9ViewTrainingHistory() {
        System.out.println("\n----------- TRAINING HISTORY -----------");
        List<TrainingSession> list = cabinet.getTrainingManager().getSessionsSortedById();
        if (list.isEmpty()) {
            System.out.println(Notification.SESSION_LIST_EMPTY.getMessage());
        } else {
            System.out.format("%-8s %-12s %-25s %-25s\n", "ID", "Date", "Location", "Topic");
            System.out.println("---------------------------------------------------------------------------------");
            for (TrainingSession ts : list) {
                System.out.format("%-8s %-12s %-25s %-25s\n", ts.getTrainingId(), ts.getDate(), ts.getLocation(), ts.getTopic());
            }
            System.out.println("---------------------------------------------------------------------------------");
        }
        System.out.println("Press ENTER to return...");
        scanner.nextLine();
    }

    private static void handleS10ViewMatchHistory() {
        System.out.println("\n----------- MATCH HISTORY -----------");
        List<MatchRecord> list = cabinet.getMatchManager().getMatchesSortedById();
        if (list.isEmpty()) {
            System.out.println(Notification.MATCH_LIST_EMPTY.getMessage());
        } else {
            System.out.format("%-8s %-12s %-20s %-12s\n", "ID", "Date", "Opponent Team", "Match Type");
            System.out.println("-----------------------------------------------------------------");
            for (MatchRecord mr : list) {
                System.out.format("%-8s %-12s %-20s %-12s\n", mr.getMatchId(), mr.getDate(), mr.getOpponentTeam(), mr.getMatchType());
            }
            System.out.println("-----------------------------------------------------------------");
        }
        System.out.println("Press ENTER to return...");
        scanner.nextLine();
    }

    private static void handleS11AddUpdatePerformance() {
        System.out.println("\n----------- ADD / UPDATE PLAYER PERFORMANCE -----------");
        String mid = Menu.readString("Match ID: ", "^MA\\d{4}$", "Dạng MAXXXX!");
        if (cabinet.getMatchManager().searchMatchById(mid) == null) {
            System.out.println(Notification.MATCH_NOT_FOUND.getMessage(mid));
            return;
        }

        String pid = Menu.readString("Player ID: ", "^PL\\d{4}$", "Dạng PLXXXX!");
        Player p = cabinet.getPlayerManager().searchPlayerById(pid);
        if (p == null) {
            System.out.println(Notification.PLAYER_NOT_FOUND.getMessage(pid));
            return;
        }
        
        // BR9: Only Active players can be in new match performance
        if (!p.getStatus().equalsIgnoreCase("Active")) {
            System.out.println("❌ Lỗi: Cầu thủ '" + p.getFullName() + "' không ở trạng thái Active, không thể lưu hiệu suất thi đấu!");
            return;
        }

        System.out.println("Player Name: " + p.getFullName());

        PerformanceRecord existingPerf = cabinet.getMatchManager().searchPerformance(mid, pid);
        boolean processInput = false;

        if (existingPerf == null) {
            // Case 1: No existing performance
            System.out.println("\n(Chưa có thống kê hiệu suất trong trận này. Vui lòng nhập trực tiếp)");
            processInput = true;
        } else {
            // Case 2: Existing performance found
            System.out.println("\nExisting Performance:");
            System.out.format("Goals: %d | Assists: %d | Yellow Cards: %d | Red Cards: %d | Minutes Played: %d\n",
                    existingPerf.getGoals(), existingPerf.getAssists(), existingPerf.getYellowCards(),
                    existingPerf.getRedCards(), existingPerf.getMinutesPlayed());
            System.out.println("Do you want to replace this record?");
            System.out.println("[1] Replace [2] Cancel");
            int choice = Menu.readInt("Choose an option: ", 1, 2);
            if (choice == 1) {
                processInput = true;
            } else {
                System.out.println("Output:\nAction cancelled.");
            }
        }

        if (processInput) {
            System.out.println("\nEnter performance data:");
            int goals = Menu.readInt("Goals: ", 0, 20);
            int assists = Menu.readInt("Assists: ", 0, 20);
            int yellow = Menu.readInt("Yellow Cards: ", 0, 2);
            int red = Menu.readInt("Red Cards: ", 0, 1);
            int mins = Menu.readInt("Minutes Played: ", 0, 120);

            // BR21: If mins == 0, then goals, assists, yellow, red must all be 0
            if (mins == 0 && (goals > 0 || assists > 0 || yellow > 0 || red > 0)) {
                System.out.println("❌ Lỗi nghiệp vụ (BR21): Số phút thi đấu bằng 0 thì số bàn thắng, kiến tạo, thẻ vàng, thẻ đỏ phải đều bằng 0!");
                return;
            }

            System.out.println("[1] Submit [2] Cancel");
            int opt = Menu.readInt("Choose an option: ", 1, 2);
            if (opt == 1) {
                PerformanceRecord newPerf = new PerformanceRecord(mid, pid, goals, assists, yellow, red, mins);
                if (cabinet.getMatchManager().replacePerformance(mid, newPerf)) {
                    cabinet.getDataManager().saveData();
                    System.out.println("Output:\nPlayer performance saved successfully.");
                    System.out.println("Performance Points: " + newPerf.calculatePerformancePoints());
                } else {
                    System.out.println("Output:\nFailed to save performance.");
                }
            } else {
                System.out.println("Output:\nAction cancelled.");
            }
        }
    }

    // ==========================================
    // 3. CONTRACT AND SALARY SUBMENU (S12)
    // ==========================================
    private static void runContractSalaryMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----------- CONTRACT & SALARY MANAGEMENT -----------");
            System.out.println("1. S12 - Calculate Player Salary");
            System.out.println("2. Back to Main Menu");
            System.out.println("----------------------------------------------------");
            int choice = Menu.readInt("Choose an option (1-2): ", 1, 2);
            if (choice == 1) {
                handleS12CalculatePlayerSalary();
            } else {
                back = true;
            }
        }
    }

    private static void handleS12CalculatePlayerSalary() {
        System.out.println("\n----------- CALCULATE PLAYER SALARY -----------");
        int month = Menu.readInt("Enter Month (1-12): ", 1, 12);
        int year = Menu.readInt("Enter Year (2000-2100): ", 2000, 2100);
        String pid = Menu.readString("Enter Player ID: ", "^PL\\d{4}$", "Dạng PLXXXX!");
        
        Player p = cabinet.getPlayerManager().searchPlayerById(pid);
        if (p == null) {
            System.out.println(Notification.PLAYER_NOT_FOUND.getMessage(pid));
            return;
        }

        // Format month and year as MM/yyyy
        String monthYear = String.format("%02d/%04d", month, year);
        int points = cabinet.getSalaryManager().calculateMonthlyPerformancePoints(pid, monthYear, cabinet.getMatchManager());
        double bonus = p.calculateBonus(points);
        double totalSalary = p.calculateMonthlySalary(points);

        String typeStr = (p instanceof StarPlayer) ? "Star Player" : "Regular Player";

        System.out.println("\nPlayer: " + p.getFullName());
        System.out.println("Type: " + typeStr);
        System.out.format("Base Salary: %,.0f VND\n", p.getBaseSalary());
        System.out.println("Monthly Performance Points: " + points);
        System.out.println("\nOutput:");
        System.out.println("Salary Summary:");
        System.out.format("Base Salary: %,.0f VND\n", p.getBaseSalary());
        System.out.format("Performance Bonus: %,.0f VND\n", bonus);
        System.out.format("Total Salary: %,.0f VND\n", totalSalary);
    }

    // ==========================================
    // 4. REPORTS SUBMENU (S13 - S14)
    // ==========================================
    private static void runReportMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----------- REPORTS -----------");
            System.out.println("1. S13 - Salary Summary Report");
            System.out.println("2. S14 - All-time Top Goal Scorers");
            System.out.println("3. Back to Main Menu");
            System.out.println("-------------------------------");
            int choice = Menu.readInt("Choose an option (1-3): ", 1, 3);
            switch (choice) {
                case 1:
                    handleS13SalaryReport();
                    break;
                case 2:
                    handleS14TopGoalScorers();
                    break;
                case 3:
                    back = true;
                    break;
            }
        }
    }

    private static void handleS13SalaryReport() {
        System.out.println("\n----------- SALARY SUMMARY REPORT -----------");
        int month = Menu.readInt("Enter Month (1-12): ", 1, 12);
        int year = Menu.readInt("Enter Year (2000-2100): ", 2000, 2100);
        String monthYear = String.format("%02d/%04d", month, year);

        cabinet.getReportManager().generateSalaryReport(monthYear, cabinet.getPlayerManager(), cabinet.getMatchManager(), cabinet.getSalaryManager());
        System.out.println("\nPress ENTER to return...");
        scanner.nextLine();
    }

    private static void handleS14TopGoalScorers() {
        cabinet.getReportManager().generateTopGoalScorers(cabinet.getMatchManager(), cabinet.getPlayerManager());
        System.out.println("\nPress ENTER to return...");
        scanner.nextLine();
    }

    // ==========================================
    // 5. EXIT SUBMENU (S15)
    // ==========================================
    private static void runExitMenu() {
        System.out.println("\n----------- EXIT SYSTEM -----------");
        System.out.println("1. Save Only (Continue)");
        System.out.println("2. Save and Exit");
        System.out.println("3. Exit without Saving");
        System.out.println("4. Cancel");
        int choice = Menu.readInt("Choose an option: ", 1, 4);
        if (choice == 1) {
            cabinet.getDataManager().saveData();
            System.out.println("\nOutput:\nData saved successfully. Returning to main menu...");
        } else if (choice == 2) {
            cabinet.getDataManager().saveData();
            System.out.println("\nOutput:\nData saved successfully.");
            System.out.println("Thank you for using the Football Player Management System.");
            System.exit(0);
        } else if (choice == 3) {
            System.out.println("\nOutput:\nThank you for using the Football Player Management System.");
            System.exit(0);
        } else {
            System.out.println("Output:\nAction cancelled.");
        }
    }
}
