package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import pl.polsl.model.Model;
import pl.polsl.model.MatchRecord;
import pl.polsl.model.Model.InvalidScoreException;

/**
 * Servlet responsible for managing football matches.
 * <p>
 * It allows the user to:
 * <ul>
 * <li>Add a new match using an HTML form</li>
 * <li>Store the last entered Team A name in a cookie</li>
 * <li>Display the history of all added matches (from Database)</li>
 * </ul>
 * </p>
 *
 * The servlet is available under the URL pattern: <b>/match</b>.
 *
 * @author Adam Bronikowski
 */
@WebServlet("/match")
public class MatchServlet extends HttpServlet {

    /**
     * Retrieves the application model stored in the servlet context.
     *
     * @return the {@link Model} object containing match data
     */
    private Model getModel() {
        return (Model) getServletContext().getAttribute("model");
    }

    /**
     * Processes both HTTP GET and POST requests.
     * <p>
     * Handles form submission, validation of input data, adding a new match,
     * cookie management, and generation of the HTML response.
     * </p>
     *
     * @param request  the {@link HttpServletRequest} object containing client request data
     * @param response the {@link HttpServletResponse} object used to send the response
     * @throws ServletException if a servlet-related error occurs
     * @throws IOException if an input/output error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String errorMsg = null;
        String infoMsg = null;

        String teamA = request.getParameter("teamA");
        String teamB = request.getParameter("teamB");
        String goalsA = request.getParameter("goalsA");
        String goalsB = request.getParameter("goalsB");

        if (teamA != null) {
            if (teamA.isBlank() || teamB.isBlank() || goalsA.isBlank() || goalsB.isBlank()) {
                errorMsg = "All fields must be filled!";
            } else {
                try {
                    // This method now saves to the DB and might throw a generic Exception
                    getModel().addMatchFromStrings(teamA, teamB, goalsA, goalsB);
                    infoMsg = "Match added successfully!";

                    // Save cookie with last Team A name
                    String cookieValue = URLEncoder.encode(teamA, StandardCharsets.UTF_8);
                    Cookie teamCookie = new Cookie("lastTeamA", cookieValue);
                    teamCookie.setMaxAge(60 * 60 * 24);
                    response.addCookie(teamCookie);

                } catch (InvalidScoreException e) {
                    errorMsg = "Invalid data: " + e.getMessage();
                } catch (Exception e) {
                    // NEW: Catching database/persistence errors
                    errorMsg = "Database error: " + e.getMessage();
                    e.printStackTrace();
                }
            }
        }

        String lastTeamName = "None";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("lastTeamA".equals(c.getName())) {
                    lastTeamName = URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8);
                    break;
                }
            }
        }

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Match List</title>");
        out.println("<style>table, th, td { border: 1px solid black; border-collapse: collapse; padding: 5px; }</style>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h1>Match Management</h1>");

        if (errorMsg != null) {
            out.println("<p style='color:red; font-weight:bold;'>Error: " + errorMsg + "</p>");
        }
        if (infoMsg != null) {
            out.println("<p style='color:green; font-weight:bold;'>" + infoMsg + "</p>");
        }

        // Requirement: Distinguish between Cookie data and DB data
        out.println("<div style='border: 1px solid blue; padding: 10px; margin-bottom: 20px;'>");
        out.println("<h3>Cookie Info</h3>");
        out.println("<p>Last added Team A (read from cookie): <strong>" + lastTeamName + "</strong></p>");
        out.println("</div>");

        out.println("<hr>");

        out.println("<h3>Add new match</h3>");
        out.println("<form action='match' method='POST'>");
        out.println("Team A: <input type='text' name='teamA'><br>");
        out.println("Team B: <input type='text' name='teamB'><br>");
        out.println("Goals A: <input type='number' name='goalsA'><br>");
        out.println("Goals B: <input type='number' name='goalsB'><br>");
        out.println("<input type='submit' value='Add match'>");
        out.println("</form>");

        out.println("<hr>");

        out.println("<div style='border: 1px solid green; padding: 10px;'>");
        out.println("<h3>Match history (From Database)</h3>");
        out.println("<table>");
        out.println("<tr><th>Team A</th><th>Score</th><th>Team B</th><th>Description</th></tr>");

        // getMatches() fetches data directly from the Database via DbManager
        for (MatchRecord m : getModel().getMatches()) {
            if (m != null) {
                out.println("<tr>");
                out.println("<td>" + m.teamA() + "</td>");
                out.println("<td>" + m.goalsA() + " : " + m.goalsB() + "</td>");
                out.println("<td>" + m.teamB() + "</td>");
                out.println("<td>" + m.getSummary() + "</td>");
                out.println("</tr>");
            }
        }

        out.println("</table>");
        out.println("</div>");

        out.println("<br><a href='stats'>Statistics</a>");

        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Handles HTTP GET requests.
     *
     * @param request  the {@link HttpServletRequest} object
     * @param response the {@link HttpServletResponse} object
     * @throws ServletException if a servlet-related error occurs
     * @throws IOException if an input/output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles HTTP POST requests.
     *
     * @param request  the {@link HttpServletRequest} object
     * @param response the {@link HttpServletResponse} object
     * @throws ServletException if a servlet-related error occurs
     * @throws IOException if an input/output error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}