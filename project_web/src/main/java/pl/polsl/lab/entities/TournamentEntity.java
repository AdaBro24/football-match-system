package pl.polsl.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TournamentEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Relacja 1:N (Jeden turniej ma wiele meczów)
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<MatchEntity> matches = new ArrayList<>();

    public TournamentEntity() {}

    public TournamentEntity(String name) {
        this.name = name;
    }

    // Metoda pomocnicza do zarządzania relacją
    public void addMatch(MatchEntity match) {
        matches.add(match);
        match.setTournament(this);
    }

    // Gettery i Settery
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<MatchEntity> getMatches() { return matches; }
    public void setMatches(List<MatchEntity> matches) { this.matches = matches; }
}