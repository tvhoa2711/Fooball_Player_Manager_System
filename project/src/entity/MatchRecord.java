package entity;

import util.Notification;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a match played by the club.
 * Fully encapsulated with static validation helpers and final setters.
 */
public class MatchRecord {
    private String matchId;
    private String date; // dd/MM/yyyy
    private String opponentTeam;
    private String matchType; // "Friendly", "League", "Cup"
    private List<PerformanceRecord> performances;

    public MatchRecord(String matchId, String date, String opponentTeam, String matchType) {
        setMatchId(matchId);
        setDate(date);
        setOpponentTeam(opponentTeam);
        setMatchType(matchType);
        this.performances = new ArrayList<>();
    }

    // ==========================================
    // STATIC VALIDATION HELPERS
    // ==========================================

    public static boolean isValidMatchId(String id) {
        return id != null && id.trim().matches("^MA\\d{4}$");
    }

    public static boolean isValidDate(String date) {
        return date != null && date.trim().matches("^\\d{2}/\\d{2}/\\d{4}$");
    }

    public static boolean isValidOpponentTeam(String opponent) {
        return opponent != null && !opponent.trim().isEmpty();
    }

    public static boolean isValidMatchType(String type) {
        if (type == null) return false;
        String t = type.trim().toUpperCase();
        return t.equals("FRIENDLY") || t.equals("LEAGUE") || t.equals("CUP");
    }

    // ==========================================
    // GETTERS & FINAL SETTERS
    // ==========================================

    public String getMatchId() { return matchId; }
    
    public final void setMatchId(String matchId) {
        if (!isValidMatchId(matchId)) {
            throw new IllegalArgumentException(Notification.VAL_MATCH_ID.getMessage());
        }
        this.matchId = matchId.trim();
    }

    public String getDate() { return date; }
    
    public final void setDate(String date) {
        if (!isValidDate(date)) {
            throw new IllegalArgumentException(Notification.VAL_MATCH_DATE.getMessage());
        }
        this.date = date.trim();
    }

    public String getOpponentTeam() { return opponentTeam; }
    
    public final void setOpponentTeam(String opponentTeam) {
        if (!isValidOpponentTeam(opponentTeam)) {
            throw new IllegalArgumentException(Notification.VAL_MATCH_OPPONENT.getMessage());
        }
        this.opponentTeam = opponentTeam.trim();
    }

    public String getMatchType() { return matchType; }
    
    public final void setMatchType(String matchType) {
        if (!isValidMatchType(matchType)) {
            throw new IllegalArgumentException(Notification.VAL_MATCH_TYPE.getMessage());
        }
        String type = matchType.trim();
        type = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
        this.matchType = type;
    }

    public List<PerformanceRecord> getPerformances() { return performances; }
    
    public final void setPerformances(List<PerformanceRecord> performances) {
        this.performances = performances;
    }

    public void addPerformance(PerformanceRecord perf) {
        if (perf == null) {
            throw new IllegalArgumentException("Lỗi: Bản ghi hiệu suất không được null!");
        }
        this.performances.add(perf);
    }

    @Override
    public String toString() {
        return String.format("Match ID: %-8s | Date: %-10s | Opponent: %-15s | Type: %-10s | Players: %d",
                matchId, date, opponentTeam, matchType, performances.size());
    }
}
