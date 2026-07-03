package entity;

import util.Notification;

/**
 * Entity representing a player's individual performance metrics in a specific match.
 * Fully encapsulated with static validation helpers and final setters.
 */
public class PerformanceRecord {
    private String matchId;
    private String playerId;
    private int goals;
    private int assists;
    private int yellowCards;
    private int redCards;
    private int minutesPlayed;

    public PerformanceRecord(String matchId, String playerId, int goals, int assists, 
                             int yellowCards, int redCards, int minutesPlayed) {
        setMatchId(matchId);
        setPlayerId(playerId);
        setGoals(goals);
        setAssists(assists);
        setYellowCards(yellowCards);
        setRedCards(redCards);
        setMinutesPlayed(minutesPlayed);
    }

    // ==========================================
    // STATIC VALIDATION HELPERS
    // ==========================================

    public static boolean isValidGoals(int goals) {
        return goals >= 0; // BR19
    }

    public static boolean isValidAssists(int assists) {
        return assists >= 0; // BR19
    }

    public static boolean isValidYellowCards(int yellow) {
        return yellow >= 0 && yellow <= 2; // BR19 & BR20
    }

    public static boolean isValidRedCards(int red) {
        return red >= 0 && red <= 1; // BR19 & BR20
    }

    public static boolean isValidMinutesPlayed(int minutes) {
        return minutes >= 0 && minutes <= 120; // BR20
    }

    // ==========================================
    // GETTERS & FINAL SETTERS
    // ==========================================

    public String getMatchId() { return matchId; }
    
    public final void setMatchId(String matchId) {
        if (!MatchRecord.isValidMatchId(matchId)) {
            throw new IllegalArgumentException(Notification.VAL_MATCH_ID.getMessage());
        }
        this.matchId = matchId.trim();
    }

    public String getPlayerId() { return playerId; }
    
    public final void setPlayerId(String playerId) {
        if (!Player.isValidPlayerId(playerId)) {
            throw new IllegalArgumentException(Notification.VAL_PLAYER_ID.getMessage());
        }
        this.playerId = playerId.trim();
    }

    public int getGoals() { return goals; }
    
    public final void setGoals(int goals) {
        if (!isValidGoals(goals)) {
            throw new IllegalArgumentException(Notification.VAL_PERF_GOALS.getMessage());
        }
        this.goals = goals;
    }

    public int getAssists() { return assists; }
    
    public final void setAssists(int assists) {
        if (!isValidAssists(assists)) {
            throw new IllegalArgumentException(Notification.VAL_PERF_ASSISTS.getMessage());
        }
        this.assists = assists;
    }

    public int getYellowCards() { return yellowCards; }
    
    public final void setYellowCards(int yellowCards) {
        if (!isValidYellowCards(yellowCards)) {
            throw new IllegalArgumentException(Notification.VAL_PERF_YELLOW.getMessage());
        }
        this.yellowCards = yellowCards;
    }

    public int getRedCards() { return redCards; }
    
    public final void setRedCards(int redCards) {
        if (!isValidRedCards(redCards)) {
            throw new IllegalArgumentException(Notification.VAL_PERF_RED.getMessage());
        }
        this.redCards = redCards;
    }

    public int getMinutesPlayed() { return minutesPlayed; }
    
    public final void setMinutesPlayed(int minutesPlayed) {
        if (!isValidMinutesPlayed(minutesPlayed)) {
            throw new IllegalArgumentException(Notification.VAL_PERF_MINUTES.getMessage());
        }
        this.minutesPlayed = minutesPlayed;
    }

    /**
     * Algorithmic calculation of performance points based on BR24.
     * Performance Points = goals * 5 + assists * 3 - yellowCards * 1 - redCards * 3 (min 0)
     */
    public int calculatePerformancePoints() {
        int finalPoints = (goals * 5) + (assists * 3) - (yellowCards * 1) - (redCards * 3);
        return Math.max(0, finalPoints);
    }

    @Override
    public String toString() {
        return String.format("Player: %s | Goals: %d | Assists: %d | Y/R Cards: %d/%d | Mins: %d | Points: %d",
                playerId, goals, assists, yellowCards, redCards, minutesPlayed, calculatePerformancePoints());
    }
}
