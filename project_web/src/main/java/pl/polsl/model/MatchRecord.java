package pl.polsl.model;

/**
 * Record representing a single football match between two teams.
 * 
 * <p>
 * Stores the names of the teams and their scored goals. Provides
 * a constructor that parses goal strings to integers and a method
 * to get the textual summary of the match result.
 * </p>
 */
public record MatchRecord(String teamA, String teamB, int goalsA, int goalsB) {

    /**
     * Constructs a MatchRecord from string representations of goals.
     * 
     * @param teamA     name of the first team
     * @param teamB     name of the second team
     * @param goalsAStr goals scored by team A as a string
     * @param goalsBStr goals scored by team B as a string
     * @throws Model.InvalidScoreException if goals are not valid non-negative integers
     */
    public MatchRecord(String teamA, String teamB, String goalsAStr, String goalsBStr) throws Model.InvalidScoreException {
        this(teamA, teamB, parseGoals(goalsAStr), parseGoals(goalsBStr));
    }

    public enum MatchResult {
        TEAM_A_WIN, 
        TEAM_B_WIN, 
        DRAW        
    }
    /**
 * Returns the result of the match as an enum.
 *
 * @return MatchResult.TEAM_A_WIN / TEAM_B_WIN / DRAW
 */
    public MatchResult getResultEnum() {
        if (goalsA > goalsB) return MatchResult.TEAM_A_WIN;
        if (goalsB > goalsA) return MatchResult.TEAM_B_WIN;
        return MatchResult.DRAW;
    }
    
    /**
     * Parses a string to an integer goal value.
     *
     * @param goalsStr the string representation of goals
     * @return the integer value of goals
     * @throws Model.InvalidScoreException if the string cannot be parsed to a non-negative integer
     */
    private static int parseGoals(String goalsStr) throws Model.InvalidScoreException {
        try {
            int goals = Integer.parseInt(goalsStr);
            if (goals < 0) throw new NumberFormatException();
            return goals;
        } catch (NumberFormatException e) {
            throw new Model.InvalidScoreException("Invalid goal: " + goalsStr);
        }
    }

    /**
     * Returns a textual summary of the match result.
     * The format is:
     * - "TeamA wins" if team A scored more goals
     * - "TeamB wins" if team B scored more goals
     * - "Draw" if both teams scored the same number of goals
     *
     * @return textual match result
     */
    public String getSummary() {
    if (goalsA > goalsB) return teamA + " wins";
    if (goalsB > goalsA) return teamB + " wins";
    return "Draw";
    }
}
