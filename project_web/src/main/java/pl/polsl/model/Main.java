package pl.polsl.model;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        
        System.out.println("--- START APLIKACJI (TEST BAZY) ---");

        DbManager db = DbManager.getInstance();
        
        try {
            System.out.println("-> Zapisuje przykładowy mecz...");
            MatchEntity testMatch = new MatchEntity("Polska", "Francja", 1, 1);
            db.addMatch(testMatch);
        } catch (Exception e) {
            System.err.println("Błąd zapisu: " + e.getMessage());
        }
        
        System.out.println("-> Pobieram dane z bazy...");
        List<MatchEntity> matches = db.getAllMatches();
        
        System.out.println("-> Znaleziono meczów: " + matches.size());
        for (MatchEntity m : matches) {
            String turniej = (m.getTournament() != null) ? m.getTournament().getName() : "Brak";
            System.out.println("   [MECZ] " + m.getTeamA() + " vs " + m.getTeamB() + " | Wynik: " + m.getGoalsA() + ":" + m.getGoalsB() + " (Turniej: " + turniej + ")");
        }
        
        db.close();
    }
}