
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import pl.polsl.model.Model;

/**
 * Unit tests for adding matches to the model and verifying
 * the summary of the last added match.
 */
public class AddMatchTest {

    /**
     * Parameterized test verifying correct summaries for wins and draws.
     * This test covers valid input scenarios.
     */
    @ParameterizedTest
    @CsvSource({
        "Team A, Team B, 3, 2, Team A wins",
        "Team C, Team D, 1, 4, Team D wins",
        "Team E, Team F, 2, 2, Draw"
    })
    @DisplayName("Correct summary after adding a match")
    void testAddMatchSummary(String teamA, String teamB,
                             String goalsA, String goalsB,
                             String expectedSummary) throws Exception {
        Model model = new Model();

        model.addMatchFromStrings(teamA, teamB, goalsA, goalsB);
        assertEquals(expectedSummary, model.getLastMatchSummary());
    }

    /**
     * Test verifying that invalid goal values cause an exception.
     * This test covers incorrect input scenarios.
     */
    @ParameterizedTest
    @CsvSource({
        "Team A, Team B, -1, 2",
        "Team A, Team B, a, 2",
        "Team A, Team B, 1, -5",
        "Team A, Team B, 1, x"
    })
    @DisplayName("Invalid score should throw InvalidScoreException")
    void testAddMatchInvalidScore(String teamA, String teamB,
                                  String goalsA, String goalsB) {
        Model model = new Model();

        assertThrows(Model.InvalidScoreException.class, () ->
            model.addMatchFromStrings(teamA, teamB, goalsA, goalsB)
        );
    }
}

