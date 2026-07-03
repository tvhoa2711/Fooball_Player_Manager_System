package controller;

import entity.MatchRecord;
import entity.PerformanceRecord;
import entity.Player;

/**
 * Controller class that handles player contract salary and monthly bonus calculations.
 */
public class SalaryManager {

    /**
     * Calculates the total performance points accumulated by a player in a specific month (MM/yyyy).
     */
    public int calculateMonthlyPerformancePoints(String playerId, String monthYear, MatchManager matchManager) {
        int totalPoints = 0;
        if (playerId == null || monthYear == null || matchManager == null) return 0;

        for (MatchRecord match : matchManager.getListMatch()) {
            // Extract MM/yyyy from dd/MM/yyyy (e.g., "15/06/2026" -> "06/2026")
            if (match.getDate().length() == 10) {
                String matchMonthYear = match.getDate().substring(3);
                if (matchMonthYear.equals(monthYear.trim())) {
                    for (PerformanceRecord perf : match.getPerformances()) {
                        if (perf.getPlayerId().equalsIgnoreCase(playerId.trim())) {
                            totalPoints += perf.calculatePerformancePoints();
                        }
                    }
                }
            }
        }
        return totalPoints;
    }

    /**
     * Calculates the total salary for a player in a specific month.
     */
    public double calculateMonthlySalary(Player player, String monthYear, MatchManager matchManager) {
        if (player == null) return 0;
        int points = calculateMonthlyPerformancePoints(player.getPlayerId(), monthYear, matchManager);
        return player.calculateMonthlySalary(points);
    }
}
