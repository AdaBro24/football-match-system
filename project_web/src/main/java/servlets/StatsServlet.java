package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import pl.polsl.model.Model;

/**
 * Servlet responsible for calculating and displaying team statistics.
 * <p>
 * It retrieves the Model from the application context and uses its business logic
 * to generate a summary of wins and matches played for each team.
 * The output is rendered directly as a simple HTML page.
 * </p>
 *
 * @author Adam Bronikowski
 * @version 1.0
 */
@WebServlet("/stats")
public class StatsServlet extends HttpServlet {

    /**
     * Retrieves the shared {@link Model} instance from the ServletContext.
     *
     * @return the singleton Model object containing application data
     */
    private Model getModel() {
        return (Model) getServletContext().getAttribute("model");
    }

    /**
     * Handles the HTTP <code>GET</code> method to display statistics.
     * <p>
     * Generates a dynamic HTML page containing the pre-calculated team statistics.
     * </p>
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Team Statistics</title>");
        out.println("<style>body{font-family:sans-serif; padding:20px;}</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Current Team Statistics</h1>");
        
        Model model = getModel();
        
        if (model != null) {
            String stats = model.getTeamStatistics();
            
            out.println("<pre style='background:#f4f4f4; padding:15px; border:1px solid #ddd; font-size: 14px;'>" + stats + "</pre>"); 
        } else {
            out.println("<p style='color:red;'>Błąd: Model nie został załadowany (sprawdź InitServlet)!</p>");
        }
        
        out.println("<hr>");
        out.println("<a href='match'>Back to Matches</a> | <a href='index.html'>Main Menu</a>");
        out.println("</body>");
        out.println("</html>");
    }
}