package pl.polsl.model;

import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Model class serving as a facade between the View/Controller and the Database.
 */
@NoArgsConstructor
public class Model {

    /**
     * Adds a new match to the Database via Singleton Manager.
     */
    public void addMatchFromStrings(String teamA, String teamB, String goalsAStr, String goalsBStr)
            throws InvalidScoreException, Exception {
        
        // 1. Walidacja (Business Logic)
        int gA = parseGoals(goalsAStr);
        int gB = parseGoals(goalsBStr);

        // 2. Utworzenie Encji
        MatchEntity entity = new MatchEntity(teamA, teamB, gA, gB);

        // 3. Zapis do bazy danych (Persistence Logic)
        DbManager.getInstance().addMatch(entity);
    }
    
    /**
     * Retrieves matches from Database and converts them to Records for display.
     */
    public List<MatchRecord> getMatches() {
        // Pobierz encje z bazy
        List<MatchEntity> entities = DbManager.getInstance().getAllMatches();

        // Zamień Encje (Baza) na Rekordy (Widok)
        return entities.stream()
            .map(e -> {
                try {
                    return new MatchRecord(
                        e.getTeamA(), 
                        e.getTeamB(), 
                        String.valueOf(e.getGoalsA()), 
                        String.valueOf(e.getGoalsB())
                    );
                } catch (Exception ex) {
                    return null;
                }
            })
            .collect(Collectors.toList());
    }

    /**
     * Returns summary of the last match (fetched from DB).
     */
    public String getLastMatchSummary() {
        List<MatchRecord> all = getMatches();
        if (all.isEmpty()) return "No matches recorded in Database.";
        return all.get(all.size() - 1).getSummary();
    }

    /**
     * Computes statistics based on matches stored in Database.
     */
    public String getTeamStatistics() {
        // POBIERAMY DANE Z BAZY (TO JEST KLUCZOWA ZMIANA)
        List<MatchRecord> matches = getMatches();
        
        if (matches.isEmpty()) return "No matches recorded.";

        // Stream-based counting of wins
        Map<String, Long> wins = matches.stream()
            .flatMap(m -> {
                // Musimy obsłużyć nulla, jeśli konwersja się nie udała (rzadki przypadek)
                if (m == null) return Stream.empty();
                return switch (m.getResultEnum()) {
                    case TEAM_A_WIN -> Stream.of(m.teamA());
                    case TEAM_B_WIN -> Stream.of(m.teamB());
                    case DRAW -> Stream.empty();
                };
            })
            .collect(Collectors.groupingBy(team -> team, Collectors.counting()));

        // Stream-based counting of total games per team
        Map<String, Long> games = matches.stream()
            .filter(m -> m != null)
            .flatMap(m -> Stream.of(m.teamA(), m.teamB()))
            .collect(Collectors.groupingBy(team -> team, Collectors.counting()));

        StringBuilder sb = new StringBuilder("Team statistics (From DB):\n");
        games.forEach((team, gameCount) -> {
            sb.append(team).append(": ")
              .append(wins.getOrDefault(team, 0L))
              .append(" wins, ")
              .append(gameCount)
              .append(" matches total.\n");
        });

        return sb.toString();
    }

    /**
     * Converts matches to table list format.
     */
    public List<List<Object>> getMatchesForTableAsList() {
        List<MatchRecord> matches = getMatches();
        List<List<Object>> tableData = new ArrayList<>();
        
        for (MatchRecord m : matches) {
            if (m == null) continue;
            List<Object> row = new ArrayList<>();
            row.add(m.teamA());
            row.add(m.goalsA());
            row.add(m.goalsB());
            row.add(m.teamB());
            row.add(m.getSummary());
            tableData.add(row);
        }
        return tableData;
    }

    // Helper method
    private int parseGoals(String goalsStr) throws InvalidScoreException {
        try {
            int goals = Integer.parseInt(goalsStr);
            if (goals < 0) throw new NumberFormatException();
            return goals;
        } catch (NumberFormatException e) {
            throw new InvalidScoreException("Invalid goal value: " + goalsStr);
        }
    }
    
    // Exception class
    public static class InvalidScoreException extends Exception {
        public InvalidScoreException(String message) { super(message); }
    }
}