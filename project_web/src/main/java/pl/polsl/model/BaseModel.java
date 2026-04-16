package pl.polsl.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * BaseModel class that serves as a base for storing football matches.
 */
@NoArgsConstructor
public class BaseModel {

    /** List of all matches stored in the model. */
    @Getter
    protected final List<MatchRecord> matches = new ArrayList<>();

    /** Adds a match object to the list of matches. */
    public void addMatch(MatchRecord match) {
        matches.add(match);
    }

    /** Adds a match by parsing team names and goal strings. */
    public void addMatchFromStrings(String teamA, String teamB, String goalsAStr, String goalsBStr)
            throws Model.InvalidScoreException {
        MatchRecord m = new MatchRecord(teamA, teamB, goalsAStr, goalsBStr);
        addMatch(m);
    }

    /** Checks whether the model has any matches. */
    public boolean isEmpty() {
        return matches.isEmpty();
    }
}
