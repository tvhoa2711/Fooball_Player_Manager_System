package entity;

/**
 * Concrete subclass representing a Regular Player.
 */
public class RegularPlayer extends Player {

    public RegularPlayer(String playerId, String fullName, int age, String nationality, 
                         String position, int shirtNumber, double baseSalary, String status) {
        super(playerId, fullName, age, nationality, position, shirtNumber, baseSalary, status);
    }

    @Override
    public double calculateBonus(int monthlyPerformancePoints) {
        if (monthlyPerformancePoints < 0) return 0;
        return monthlyPerformancePoints * 200000.0;
    }

    @Override
    public double calculateMonthlySalary(int monthlyPerformancePoints) {
        return getBaseSalary() + calculateBonus(monthlyPerformancePoints);
    }

    @Override
    public String toString() {
        return super.toString() + " | Type: Regular";
    }
}
