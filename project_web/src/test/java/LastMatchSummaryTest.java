import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;
import pl.polsl.model.Model;

/**
 * Unit tests for the getLastMatchSummary method of the Model class.
 */
public class LastMatchSummaryTest {

    /**
     * Parameterized test verifying that the summary of the last match
     * reflects the actual match result.
     */
    @ParameterizedTest
    @CsvSource({
        "Team A, Team B, 3, 2, Team A wins",
        "Team C, Team D, 1, 4, Team D wins",
        "Team E, Team F, 2, 2, Draw"
    })
    @DisplayName("Last match summary should reflect match result")
    void testLastMatchSummary_withMatches(String teamA, String teamB,
                                          String goalsA, String goalsB,
                                          String expectedSummary) throws Exception {
        Model model = new Model();

        model.addMatchFromStrings(teamA, teamB, goalsA, goalsB);
        assertEquals(expectedSummary, model.getLastMatchSummary());
    }

    /**
     * Test verifying behavior when no matches are stored in the model.
     * This is a boundary case.
     */
    @Test
    @DisplayName("No matches should return default summary")
    void testLastMatchSummary_noMatches() {
        Model model = new Model();

        assertEquals("No matches recorded.", model.getLastMatchSummary());
    }
}

