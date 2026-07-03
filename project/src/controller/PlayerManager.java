package controller;

import entity.Player;
import util.Notification;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class that manages the list of football players (ArrayList of Player).
 */
public class PlayerManager {
    private List<Player> listPlayer;

    public PlayerManager() {
        this.listPlayer = new ArrayList<>();
    }

    public List<Player> getListPlayer() {
        return listPlayer;
    }

    public boolean addPlayer(Player player) {
        if (player == null) {
            System.out.println(Notification.PLAYER_NULL.getMessage());
            return false;
        }

        // 1. Check duplicate ID
        if (searchPlayerById(player.getPlayerId()) != null) {
            System.out.println(Notification.DUPLICATE_PLAYER_ID.getMessage(player.getPlayerId()));
            return false;
        }

        // 2. Check duplicate shirt number for Active players
        if (player.getStatus().equalsIgnoreCase("Active")) {
            for (Player p : listPlayer) {
                if (p.getStatus().equalsIgnoreCase("Active") && p.getShirtNumber() == player.getShirtNumber()) {
                    System.out.println(Notification.DUPLICATE_SHIRT_NUMBER.getMessage(player.getShirtNumber()));
                    return false;
                }
            }
        }

        listPlayer.add(player);
        return true;
    }

    public Player searchPlayerById(String id) {
        if (id == null) return null;
        for (Player p : listPlayer) {
            if (p.getPlayerId().equalsIgnoreCase(id.trim())) {
                return p;
            }
        }
        return null;
    }

    /**
     * Search players based on criteria (Task S4)
     * searchType: 1 - Name, 2 - Position, 3 - Nationality, 4 - Status
     */
    public List<Player> searchPlayers(int searchType, String keyword) {
        List<Player> result = new ArrayList<>();
        if (keyword == null) return result;
        String kw = keyword.trim().toLowerCase();
        for (Player p : listPlayer) {
            boolean match = false;
            switch (searchType) {
                case 1: // Name
                    match = p.getFullName().toLowerCase().contains(kw);
                    break;
                case 2: // Position
                    match = p.getPosition().toLowerCase().contains(kw);
                    break;
                case 3: // Nationality
                    match = p.getNationality().toLowerCase().contains(kw);
                    break;
                case 4: // Status
                    match = p.getStatus().toLowerCase().contains(kw);
                    break;
            }
            if (match) {
                result.add(p);
            }
        }
        return result;
    }

    public void displayAllPlayers() {
        if (listPlayer.isEmpty()) {
            System.out.println(Notification.PLAYER_LIST_EMPTY.getMessage());
            return;
        }
        System.out.println("======================================== DANH SÁCH CẦU THỦ ========================================");
        for (Player p : listPlayer) {
            System.out.println(p);
        }
        System.out.println("===================================================================================================");
    }

    public boolean updatePlayer(String id, String newName, int newAge, String newNationality, 
                                String newPosition, int newShirtNumber, double newBaseSalary, String newStatus) {
        Player player = searchPlayerById(id);
        if (player == null) {
            System.out.println(Notification.PLAYER_NOT_FOUND.getMessage(id));
            return false;
        }

        String oldName = player.getFullName();
        int oldAge = player.getAge();
        String oldNationality = player.getNationality();
        String oldPosition = player.getPosition();
        int oldShirtNumber = player.getShirtNumber();
        double oldBaseSalary = player.getBaseSalary();
        String oldStatus = player.getStatus();

        try {
            boolean isTargetActive = newStatus.equalsIgnoreCase("Active");
            if (isTargetActive) {
                for (Player p : listPlayer) {
                    if (p != player && p.getStatus().equalsIgnoreCase("Active") && p.getShirtNumber() == newShirtNumber) {
                        System.out.println(Notification.DUPLICATE_SHIRT_NUMBER.getMessage(newShirtNumber));
                        return false;
                    }
                }
            }

            player.setFullName(newName);
            player.setAge(newAge);
            player.setNationality(newNationality);
            player.setPosition(newPosition);
            player.setShirtNumber(newShirtNumber);
            player.setBaseSalary(newBaseSalary);
            player.setStatus(newStatus);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi Cập nhật: " + e.getMessage());
            System.out.println("Đang hoàn tác các thay đổi...");
            player.setFullName(oldName);
            player.setAge(oldAge);
            player.setNationality(oldNationality);
            player.setPosition(oldPosition);
            player.setShirtNumber(oldShirtNumber);
            player.setBaseSalary(oldBaseSalary);
            player.setStatus(oldStatus);
            return false;
        }
    }

    public boolean deactivatePlayer(String id) {
        Player player = searchPlayerById(id);
        if (player == null) {
            System.out.println(Notification.PLAYER_NOT_FOUND.getMessage(id));
            return false;
        }

        if (player.getStatus().equalsIgnoreCase("Inactive")) {
            System.out.println(Notification.PLAYER_INACTIVE_ALREADY.getMessage(player.getFullName()));
            return false;
        }

        player.setStatus("Inactive");
        System.out.println(Notification.SUCCESS_DEACTIVATE_PLAYER.getMessage(player.getFullName()));
        return true;
    }

    /**
     * Auto-generates the next Player ID by finding the current max numeric suffix and incrementing.
     */
    public String generateNextPlayerId() {
        int maxNum = 0;
        for (Player p : listPlayer) {
            try {
                int num = Integer.parseInt(p.getPlayerId().substring(2));
                if (num > maxNum) maxNum = num;
            } catch (NumberFormatException e) {
                // Skip malformed IDs
            }
        }
        return String.format("PL%04d", maxNum + 1);
    }

    /**
     * Returns a copy of the player list sorted by the numeric part of the Player ID.
     */
    public List<Player> getPlayersSortedById() {
        List<Player> sorted = new ArrayList<>(listPlayer);
        sorted.sort((a, b) -> {
            int numA = Integer.parseInt(a.getPlayerId().substring(2));
            int numB = Integer.parseInt(b.getPlayerId().substring(2));
            return Integer.compare(numA, numB);
        });
        return sorted;
    }
}
