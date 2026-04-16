import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;
import pl.polsl.model.Model;
import java.util.List;
import java.util.stream.Stream;

/**
 * Unit tests for the getMatchesForTableAsList method of the Model class.
 */
public class MatchesTableTest {

    /**
     * Provides valid match data and expected table row values.
     */
    static Stream<Object[]> matchDataProvider() {
        return Stream.of(
            new Object[]{"teamA", "teamB", "1", "2", "teamB wins"},
            new Object[]{"teamC", "teamD", "2", "2", "Draw"},
            new Object[]{"teamE", "teamF", "2", "3", "teamF wins"}
        );
    }

    /**
     * Parameterized test verifying that the table contains correct rows
     * after adding valid matches.
     */
    @ParameterizedTest
    @MethodSource("matchDataProvider")
    @DisplayName("Table rows should correctly represent match data")
    void testGetMatchesForTableAsList(String teamA, String teamB,
                                      String goalsA, String goalsB,
                                      String expectedSummary) throws Exception {
        Model model = new Model();

        model.addMatchFromStrings(teamA, teamB, goalsA, goalsB);
        List<List<Object>> table = model.getMatchesForTableAsList();

        assertEquals(1, table.size(), "Table should contain exactly one row");

        List<Object> row = table.get(0);
        assertEquals(teamA, row.get(0));
        assertEquals(Integer.valueOf(goalsA), row.get(1));
        assertEquals(Integer.valueOf(goalsB), row.get(2));
        assertEquals(teamB, row.get(3));
        assertEquals(expectedSummary, row.get(4));
    }

    /**
     * Boundary test verifying behavior when no matches are present.
     */
    @Test
    @DisplayName("Empty model should return empty table")
    void testGetMatchesForTableAsList_empty() {
        Model model = new Model();
        List<List<Object>> table = model.getMatchesForTableAsList();

        assertTrue(table.isEmpty(), "Table should be empty when no matches exist");
    }
}

