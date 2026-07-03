package controller;

import entity.MatchRecord;
import entity.PerformanceRecord;
import entity.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class that aggregates data and prints system reports.
 */
public class ReportManager {

    /**
     * Report 1: Generate Salary Summary Report for a specific month.
     */
    public void generateSalaryReport(String monthYear, PlayerManager playerManager, 
                                     MatchManager matchManager, SalaryManager salaryManager) {
        System.out.println("\n==========================================================================================================");
        System.out.println("                                BÁO CÁO TỔNG HỢP LƯƠNG THÁNG: " + monthYear);
        System.out.println("==========================================================================================================");
        System.out.format("%-6s | %-20s | %-12s | %-12s | %-10s | %-15s | %-15s\n",
                "ID", "Họ và Tên", "Loại", "Lương Cứng", "Điểm Tháng", "Tiền Thưởng", "Tổng Lương");
        System.out.println("----------------------------------------------------------------------------------------------------------");

        double totalClubPayroll = 0;
        for (Player p : playerManager.getPlayersSortedById()) {
            if (p.getStatus().equalsIgnoreCase("Active")) {
                int points = salaryManager.calculateMonthlyPerformancePoints(p.getPlayerId(), monthYear, matchManager);
                double bonus = p.calculateBonus(points);
                double totalSalary = p.calculateMonthlySalary(points);
                totalClubPayroll += totalSalary;

                String type = p.getClass().getSimpleName().replace("Player", "");
                System.out.format("%-6s | %-20s | %-12s | %-12.0f | %-10d | %-15.0f | %-15.0f\n",
                        p.getPlayerId(), p.getFullName(), type, p.getBaseSalary(), points, bonus, totalSalary);
            }
        }
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.format("TỔNG CHI PHÍ LƯƠNG CỦA CÂU LẠC BỘ TRONG THÁNG: %,.0f VND\n", totalClubPayroll);
        System.out.println("==========================================================================================================");
    }

    /**
     * Report 2: Generate All-time Top Goal Scorers Report.
     */
    public void generateTopGoalScorers(MatchManager matchManager, PlayerManager playerManager) {
        // Map to store Player ID -> Goals
        Map<String, Integer> goalMap = new HashMap<>();

        // Aggregate goals from all matches
        for (MatchRecord match : matchManager.getListMatch()) {
            for (PerformanceRecord perf : match.getPerformances()) {
                String pid = perf.getPlayerId();
                int goals = perf.getGoals();
                goalMap.put(pid, goalMap.getOrDefault(pid, 0) + goals);
            }
        }

        // Convert Map to a list of Entries and sort by Value descending
        List<Map.Entry<String, Integer>> list = new ArrayList<>(goalMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue()); // Descending
            }
        });

        System.out.println("\n==================================================");
        System.out.println("          BẢNG XẾP HẠNG VUA PHÁ LƯỚI");
        System.out.println("==================================================");
        System.out.format("%-4s | %-6s | %-20s | %-10s\n", "Hạng", "ID", "Họ và Tên", "Bàn Thắng");
        System.out.println("--------------------------------------------------");

        int rank = 1;
        for (Map.Entry<String, Integer> entry : list) {
            String pid = entry.getKey();
            int goals = entry.getValue();
            if (goals > 0) {
                Player p = playerManager.searchPlayerById(pid);
                String name = (p != null) ? p.getFullName() : "Cầu thủ đã xóa";
                System.out.format("%-4d | %-6s | %-20s | %-10d\n", rank++, pid, name, goals);
            }
        }
        if (rank == 1) {
            System.out.println("Chưa có bàn thắng nào được ghi nhận!");
        }
        System.out.println("==================================================");
    }
}
