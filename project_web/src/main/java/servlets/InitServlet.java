package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import pl.polsl.model.Model;
import pl.polsl.model.Model.InvalidScoreException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Servlet responsible for initializing the application data state upon server startup.
 * <p>
 * It is configured with <code>loadOnStartup = 1</code> to ensure it runs immediately.
 * It reads the initial match data from the 'matches.txt' file located in the WEB-INF directory
 * and populates the global {@link Model} object.
 * </p>
 *
 * @author Adam Bronikowski
 * @version 1.0
 */
@WebServlet(name = "InitServlet", urlPatterns = {"/init"}, loadOnStartup = 1)
public class InitServlet extends HttpServlet {

    /**
     * Initializes the servlet resources and loads data.
     * <p>
     * This method performs the following steps:
     * <ol>
     * <li>Creates a new instance of {@link Model}.</li>
     * <li>Reads the <code>/WEB-INF/matches.txt</code> file line by line.</li>
     * <li>Parses each line (splitting by space) to extract team names and goals.</li>
     * <li>Adds valid matches to the model.</li>
     * <li>Stores the populated model in the <code>ServletContext</code> for application-wide access.</li>
     * </ol>
     * </p>
     */
    @Override
    public void init() {
        Model model = new Model();

        try (InputStream is = getServletContext().getResourceAsStream("/WEB-INF/matches.txt")) {
            if (is != null) {
                // Using UTF-8 to support potential special characters
                try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.trim().isEmpty()) continue;

                        // Parsing the line assuming space separator: "TeamA TeamB 1 0"
                        String[] parts = line.split(" ");
                        
                        if (parts.length == 4) {
                            String teamA = parts[0];
                            String teamB = parts[1];
                            String goalsA = parts[2];
                            String goalsB = parts[3];
                            
                            try {
                                model.addMatchFromStrings(teamA, teamB, goalsA, goalsB);
                            } catch (InvalidScoreException e) {
                                System.err.println("Data error in matches.txt (Invalid Score): " + line + " -> " + e.getMessage());
                            } catch (Exception e) {
                                // This catch block handles Database/Persistence exceptions
                                System.err.println("Database error while loading matches.txt: " + line + " -> " + e.getMessage());
                                e.printStackTrace(); 
                            }
                        }
                    }
                }
            } else {
                System.err.println("Warning: matches.txt file not found in WEB-INF.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Storing the model in the application scope
        getServletContext().setAttribute("model", model);
    }
}