import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;
import pl.polsl.model.Model;
import java.util.stream.Stream;

/**
 * Unit tests for the getTeamStatistics method of the Model class.
 */
public class getTeamStatisticsTest {

    /**
     * Provides valid match data and expected statistics fragments.
     */
    static Stream<Object[]> statisticsDataProvider() {
        return Stream.of(
            new Object[]{"teamA", "teamB", "1", "2", "teamB: 1 wins, 1 matches total"},
            new Object[]{"teamC", "teamD", "2", "2", "teamC: 0 wins, 1 matches total"},
            new Object[]{"teamE", "teamF", "1", "3", "teamF: 1 wins, 1 matches total"}
        );
    }

    /**
     * Boundary test: no matches in the model.
     */
    @Test
    @DisplayName("No matches should return appropriate message")
    void testStatistics_noMatches() {
        Model model = new Model();
        assertEquals("No matches recorded.", model.getTeamStatistics(),
                "Should indicate that no matches have been added");
    }

    /**
     * Parameterized test verifying statistics content for valid matches.
     */
    @ParameterizedTest
    @MethodSource("statisticsDataProvider")
    @DisplayName("Statistics should correctly reflect wins and matches")
    void testStatistics_withMatches(String teamA, String teamB,
                                    String goalsA, String goalsB,
                                    String expectedFragment) throws Exception {
        Model model = new Model();

        model.addMatchFromStrings(teamA, teamB, goalsA, goalsB);
        String stats = model.getTeamStatistics();

        assertTrue(stats.contains(expectedFragment),
                "Statistics output should contain correct data for teams");
    }

    /**
     * Negative test: invalid score values should throw an exception.
     */
    @Test
    @DisplayName("Invalid scores should throw InvalidScoreException")
    void testStatistics_invalidInput() {
        Model model = new Model();

        assertThrows(Model.InvalidScoreException.class, () ->
            model.addMatchFromStrings("teamA", "teamB", "-1", "2"),
            "Negative goals should cause InvalidScoreException"
        );
    }
}

