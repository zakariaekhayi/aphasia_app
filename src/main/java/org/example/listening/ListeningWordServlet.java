package org.example.listening;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ListeningWordServlet", value = "/listening-word-servlet")
public class ListeningWordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM images ORDER BY RAND() LIMIT 3")) {

            List<String> words = new ArrayList<>();
            String correctAnswer = "";
            String voiceUrl = "";

            // Fetch words, correct answer, and voice URL
            while (rs.next()) {
                String word = rs.getString("correct_answer").toLowerCase(); // Convert word to lowercase
                words.add(word);

                if (correctAnswer.isEmpty()) {
                    correctAnswer = word;
                    voiceUrl = rs.getString("voice_url"); // Fetch the voice URL from the new column
                }
            }

            // HTML structure with voice and word choices
            out.println("<html><body>");
            out.println("<audio controls autoplay><source src='voices/" + voiceUrl + "' type='audio/mpeg'></audio>");
            out.println("<h2>Select the word that matches the voice:</h2>");
            for (String word : words) {
                out.println("<form method='POST' action='check-answer'>");
                out.println("<input type='hidden' name='answer' value='" + word + "' />");
                out.println("<input type='hidden' name='correct' value='" + correctAnswer + "' />");
                out.println("<input type='hidden' name='sourcePage' value='listening-word-servlet' />");
                out.println("<button type='submit'>" + word + "</button>");
                out.println("</form>");
            }
            out.println("</body></html>");

        } catch (SQLException e) {
            e.printStackTrace(out);
        }
    }
}
