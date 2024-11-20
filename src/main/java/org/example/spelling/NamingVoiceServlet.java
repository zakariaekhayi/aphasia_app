package org.example.spelling;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.jakartaeehelloworld1.DatabaseHelper;

import java.io.*;
import java.sql.*;

@WebServlet(name = "NamingVoiceServlet", value = "/naming-voice-servlet")
public class NamingVoiceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM images ORDER BY RAND() LIMIT 1")) {

            String correctAnswer = "";
            String voiceUrl = "";

            // Fetch the voice URL and correct answer
            if (rs.next()) {
                correctAnswer = rs.getString("correct_answer").toLowerCase(); // The correct word
                voiceUrl = rs.getString("voice_url"); // The voice URL
            }

            // HTML structure with voice and input field
            out.println("<html><body>");
            out.println("<audio controls autoplay><source src='voices/" + voiceUrl + "' type='audio/mpeg'></audio>");
            out.println("<h2>Type the word you heard:</h2>");
            out.println("<form method='POST' action='check-answer'>");
            out.println("<input type='text' name='answer' placeholder='Enter your answer' />");
            out.println("<input type='hidden' name='correct' value='" + correctAnswer + "' />");
            out.println("<input type='hidden' name='sourcePage' value='naming-voice-servlet' />");
            out.println("<button type='submit'>Submit</button>");
            out.println("</form>");
            out.println("</body></html>");

        } catch (SQLException e) {
            e.printStackTrace(out);
        }
    }
}
