package pl.polsl.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Singleton zarządzający połączeniem z bazą danych.
 */
public class DbManager implements AutoCloseable {

    private static DbManager instance;
    private final EntityManagerFactory emf;
    private final String defaultTournamentName;

    // Prywatny konstruktor (Singleton)
    private DbManager() {
        // Wymaganie: Brak zaszytych stringów konfiguracyjnych w kodzie
        ResourceBundle bundle = ResourceBundle.getBundle("db"); // szuka pliku db.properties
        String puName = bundle.getString("db.pu.name");
        this.defaultTournamentName = bundle.getString("db.default.tournament");
        
        // Utworzenie fabryki (tylko raz w życiu aplikacji)
        this.emf = Persistence.createEntityManagerFactory(puName);
    }

    // Metoda dostępowa
    public static synchronized DbManager getInstance() {
        if (instance == null) {
            instance = new DbManager();
        }
        return instance;
    }

    public void addMatch(MatchEntity matchEntity) throws Exception {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Logika: Znajdź istniejący turniej lub utwórz nowy (Relacja)
            TournamentEntity tournament = em.createQuery("SELECT t FROM TournamentEntity t WHERE t.name = :name", TournamentEntity.class)
                    .setParameter("name", defaultTournamentName)
                    .getResultStream().findFirst()
                    .orElse(new TournamentEntity(defaultTournamentName));

            // Jeśli turniej jest nowy (brak ID), trzeba go utrwalić
            if (tournament.getId() == null) {
                em.persist(tournament);
            }

            // Ustawienie relacji dwustronnej
            tournament.addMatch(matchEntity);
            
            // Zapis meczu
            em.persist(matchEntity);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new Exception("Błąd zapisu do bazy: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public List<MatchEntity> getAllMatches() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT m FROM MatchEntity m", MatchEntity.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}