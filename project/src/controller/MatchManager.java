package controller;

import entity.MatchRecord;
import entity.PerformanceRecord;
import util.Notification;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class that manages matches and individual player match performances.
 */
public class MatchManager {
    private List<MatchRecord> listMatch;

    public MatchManager() {
        this.listMatch = new ArrayList<>();
    }

    public List<MatchRecord> getListMatch() {
        return listMatch;
    }

    public boolean addMatch(MatchRecord match) {
        if (match == null) {
            System.out.println(Notification.MATCH_NULL.getMessage());
            return false;
        }
        if (searchMatchById(match.getMatchId()) != null) {
            System.out.println(Notification.DUPLICATE_MATCH_ID.getMessage(match.getMatchId()));
            return false;
        }
        listMatch.add(match);
        return true;
    }

    public MatchRecord searchMatchById(String id) {
        if (id == null) return null;
        for (MatchRecord mr : listMatch) {
            if (mr.getMatchId().equalsIgnoreCase(id.trim())) {
                return mr;
            }
        }
        return null;
    }

    public void displayAllMatches() {
        if (listMatch.isEmpty()) {
            System.out.println(Notification.MATCH_LIST_EMPTY.getMessage());
            return;
        }
        System.out.println("==================================== DANH SÁCH TRẬN ĐẤU ====================================");
        for (MatchRecord mr : listMatch) {
            System.out.println(mr);
        }
        System.out.println("==========================================================================================");
    }

    /**
     * Search performance of a player in a specific match (Task S11).
     */
    public PerformanceRecord searchPerformance(String matchId, String playerId) {
        MatchRecord mr = searchMatchById(matchId);
        if (mr == null) return null;
        for (PerformanceRecord p : mr.getPerformances()) {
            if (p.getPlayerId().equalsIgnoreCase(playerId.trim())) {
                return p;
            }
        }
        return null;
    }

    /**
     * Replaces or overwrites player performance record in a match (Task S11).
     */
    public boolean replacePerformance(String matchId, PerformanceRecord newPerf) {
        MatchRecord mr = searchMatchById(matchId);
        if (mr == null) {
            System.out.println(Notification.MATCH_NOT_FOUND.getMessage(matchId));
            return false;
        }
        
        // Find and remove old performance if it exists
        PerformanceRecord oldPerf = null;
        for (PerformanceRecord p : mr.getPerformances()) {
            if (p.getPlayerId().equalsIgnoreCase(newPerf.getPlayerId().trim())) {
                oldPerf = p;
                break;
            }
        }
        if (oldPerf != null) {
            mr.getPerformances().remove(oldPerf);
        }
        
        mr.addPerformance(newPerf);
        return true;
    }

    public boolean addPerformanceToMatch(String matchId, PerformanceRecord perf) {
        MatchRecord mr = searchMatchById(matchId);
        if (mr == null) {
            System.out.println(Notification.MATCH_NOT_FOUND.getMessage(matchId));
            return false;
        }
        // Check if player performance already exists in this match
        for (PerformanceRecord p : mr.getPerformances()) {
            if (p.getPlayerId().equalsIgnoreCase(perf.getPlayerId())) {
                System.out.println(Notification.DUPLICATE_PERFORMANCE.getMessage(perf.getPlayerId(), matchId));
                return false;
            }
        }
        mr.addPerformance(perf);
        return true;
    }

    public void displayMatchStatistics(String matchId) {
        MatchRecord mr = searchMatchById(matchId);
        if (mr == null) {
            System.out.println(Notification.MATCH_NOT_FOUND.getMessage(matchId));
            return;
        }
        System.out.println("=================== THỐNG KÊ CHI TIẾT TRẬN ĐẤU: " + matchId + " ===================");
        System.out.println(mr);
        System.out.println("Thống kê cầu thủ thi đấu:");
        if (mr.getPerformances().isEmpty()) {
            System.out.println("  (Chưa có cầu thủ nào được nhập thống kê hiệu suất)");
        } else {
            for (PerformanceRecord pr : mr.getPerformances()) {
                System.out.println("  - " + pr);
            }
        }
        System.out.println("===================================================================================");
    }

    /**
     * Auto-generates the next Match ID by finding the current max numeric suffix and incrementing.
     */
    public String generateNextMatchId() {
        int maxNum = 0;
        for (MatchRecord mr : listMatch) {
            try {
                int num = Integer.parseInt(mr.getMatchId().substring(2));
                if (num > maxNum) maxNum = num;
            } catch (NumberFormatException e) {
                // Skip malformed IDs
            }
        }
        return String.format("MA%04d", maxNum + 1);
    }

    /**
     * Returns a copy of the match list sorted by the numeric part of the Match ID.
     */
    public List<MatchRecord> getMatchesSortedById() {
        List<MatchRecord> sorted = new ArrayList<>(listMatch);
        sorted.sort((a, b) -> {
            int numA = Integer.parseInt(a.getMatchId().substring(2));
            int numB = Integer.parseInt(b.getMatchId().substring(2));
            return Integer.compare(numA, numB);
        });
        return sorted;
    }
}
