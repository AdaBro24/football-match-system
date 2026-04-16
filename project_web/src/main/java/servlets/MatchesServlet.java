package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import pl.polsl.model.Model;


@WebServlet(name = "MatchesServlet", urlPatterns = {"/matches"})
public class MatchesServlet extends HttpServlet {

    private Model getModel() {
        return (Model) getServletContext().getAttribute("model");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html;charset=UTF-8");
        var out = response.getWriter();

        out.println("<html><body>");
        out.println("<h1>Mecze</h1>");
        out.println("<table border='1'>");

        for (var m : getModel().getMatches()) {
            out.println("<tr>");
            out.println("<td>" + m.teamA() + "</td>");
            out.println("<td>" + m.goalsA() + "</td>");
            out.println("<td>" + m.goalsB() + "</td>");
            out.println("<td>" + m.teamB() + "</td>");
            out.println("<td>" + m.getSummary() + "</td>");
            out.println("</tr>");
        }

        out.println("</table>");
        out.println("<a href='match'>Powrót</a>");
        out.println("</body></html>");
    }
}
