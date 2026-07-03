package entity;

import util.Notification;

/**
 * Abstract class representing a Player.
 * Fully encapsulated with static validation helpers and final setters.
 */
public abstract class Player {
    private String playerId;
    private String fullName;
    private int age;
    private String nationality;
    private String position;
    private int shirtNumber;
    private double baseSalary;
    private String status;

    public Player(String playerId, String fullName, int age, String nationality, 
                  String position, int shirtNumber, double baseSalary, String status) {
        setPlayerId(playerId);
        setFullName(fullName);
        setAge(age);
        setNationality(nationality);
        setPosition(position);
        setShirtNumber(shirtNumber);
        setBaseSalary(baseSalary);
        setStatus(status);
    }

    // ==========================================
    // STATIC VALIDATION HELPERS (Task Predicates)
    // ==========================================

    public static boolean isValidPlayerId(String id) {
        return id != null && id.trim().matches("^PL\\d{4}$");
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.trim().matches("^[\\p{L}\\s]+$");
    }

    public static boolean isValidAge(int age) {
        return age >= 16 && age <= 45; // BR4: 16 to 45
    }

    public static boolean isValidNationality(String nationality) {
        return nationality != null && !nationality.trim().isEmpty();
    }

    public static boolean isValidPosition(String position) {
        if (position == null) return false;
        String pos = position.trim().toUpperCase();
        return pos.equals("GK") || pos.equals("DF") || pos.equals("MF") || pos.equals("FW");
    }

    public static boolean isValidShirtNumber(int shirtNumber) {
        return shirtNumber >= 1 && shirtNumber <= 99; // BR5: 1 to 99
    }

    public static boolean isValidBaseSalary(double salary) {
        return salary > 0; // BR11: salary > 0
    }

    public static boolean isValidStatus(String status) {
        if (status == null) return false;
        String stat = status.trim().toUpperCase();
        return stat.equals("ACTIVE") || stat.equals("INACTIVE");
    }

    // ==========================================
    // GETTERS & FINAL SETTERS
    // ==========================================

    public String getPlayerId() { return playerId; }
    
    public final void setPlayerId(String playerId) {
        if (!isValidPlayerId(playerId)) {
            throw new IllegalArgumentException(Notification.VAL_PLAYER_ID.getMessage());
        }
        this.playerId = playerId.trim();
    }

    public String getFullName() { return fullName; }
    
    public final void setFullName(String fullName) {
        if (!isValidName(fullName)) {
            throw new IllegalArgumentException(Notification.VAL_PLAYER_NAME.getMessage());
        }
        this.fullName = fullName.trim();
    }

    public int getAge() { return age; }
    
    public final void setAge(int age) {
        if (!isValidAge(age)) {
            throw new IllegalArgumentException(Notification.VAL_PLAYER_AGE.getMessage());
        }
        this.age = age;
    }

    public String getNationality() { return nationality; }
    
    public final void setNationality(String nationality) {
        if (!isValidNationality(nationality)) {
            throw new IllegalArgumentException(Notification.VAL_PLAYER_NATIONALITY.getMessage());
        }
        this.nationality = nationality.trim();
    }

    public String getPosition() { return position; }
    
    public final void setPosition(String position) {
        if (!isValidPosition(position)) {
            throw new IllegalArgumentException(Notification.VAL_PLAYER_POSITION.getMessage());
        }
        this.position = position.trim().toUpperCase();
    }

    public int getShirtNumber() { return shirtNumber; }
    
    public final void setShirtNumber(int shirtNumber) {
        if (!isValidShirtNumber(shirtNumber)) {
            throw new IllegalArgumentException(Notification.VAL_PLAYER_SHIRT.getMessage());
        }
        this.shirtNumber = shirtNumber;
    }

    public double getBaseSalary() { return baseSalary; }
    
    public final void setBaseSalary(double baseSalary) {
        if (!isValidBaseSalary(baseSalary)) {
            throw new IllegalArgumentException(Notification.VAL_PLAYER_SALARY.getMessage());
        }
        this.baseSalary = baseSalary;
    }

    public String getStatus() { return status; }
    
    public final void setStatus(String status) {
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException(Notification.VAL_PLAYER_STATUS_INVALID.getMessage());
        }
        String stat = status.trim();
        stat = stat.substring(0, 1).toUpperCase() + stat.substring(1).toLowerCase();
        this.status = stat;
    }

    public abstract double calculateBonus(int monthlyPerformancePoints);
    public abstract double calculateMonthlySalary(int monthlyPerformancePoints);

    @Override
    public String toString() {
        return String.format("ID: %-6s | Name: %-20s | Age: %-2d | Nat: %-12s | Pos: %-3s | Shirt: #%-2d | Base Salary: $%,12.0f | Status: %-8s",
                playerId, fullName, age, nationality, position, shirtNumber, baseSalary, status);
    }
}
