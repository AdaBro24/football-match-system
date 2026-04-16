package pl.polsl.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class MatchEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY jest bezpieczniejsze dla prostych baz
    private Long id;

    private String teamA;
    private String teamB;
    private int goalsA;
    private int goalsB;

    @ManyToOne
    @JoinColumn(name = "tournament_id") // Klucz obcy w bazie
    private TournamentEntity tournament;

    public MatchEntity() {}

    public MatchEntity(String teamA, String teamB, int goalsA, int goalsB) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.goalsA = goalsA;
        this.goalsB = goalsB;
    }

    // --- Gettery i Settery ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTeamA() { return teamA; }
    public void setTeamA(String teamA) { this.teamA = teamA; }

    public String getTeamB() { return teamB; }
    public void setTeamB(String teamB) { this.teamB = teamB; }

    public int getGoalsA() { return goalsA; }
    public void setGoalsA(int goalsA) { this.goalsA = goalsA; }

    public int getGoalsB() { return goalsB; }
    public void setGoalsB(int goalsB) { this.goalsB = goalsB; }

    public TournamentEntity getTournament() { return tournament; }
    public void setTournament(TournamentEntity tournament) { this.tournament = tournament; }
}