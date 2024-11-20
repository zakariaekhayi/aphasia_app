package org.example.naming;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.listening.DatabaseHelper;

import java.io.*;
import java.sql.*;

@WebServlet(name = "ImageWithVoiceServlet", value = "/image-with-voice-servlet")
public class ImageWithVoiceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM images ORDER BY RAND() LIMIT 1")) {

            String imageUrl = "";
            String voiceUrl = "";
            String correctAnswer = "";  // Assuming this is the name of the photo

            // Fetch image, voice URL, and correct answer
            if (rs.next()) {
                imageUrl = rs.getString("image_url");
                voiceUrl = rs.getString("voice_url");
                correctAnswer = rs.getString("correct_answer").toLowerCase(); // Assuming this is the name of the photo
            }

            // HTML structure to display the image, play the voice, display the name of the photo, and provide the "Next" button
            out.println("<html><body>");

            // Display the image
            out.println("<img src='" + imageUrl + "' width='200px' style='margin: 10px; border: 2px solid black;' />");

            // Display the name of the image (correct answer) below the image
            out.println("<p><b>Image name:</b> " + correctAnswer + "</p>");

            // Play the voice
            out.println("<audio controls autoplay><source src='voices/" + voiceUrl + "' type='audio/mpeg'></audio>");

            // Submit form for the current question
            out.println("<form method='POST' action='check-answer'>");
            out.println("<input type='hidden' name='answer' value='" + correctAnswer + "' />");
            out.println("<input type='hidden' name='correct' value='" + correctAnswer + "' />");
            out.println("<input type='hidden' name='sourcePage' value='image-with-voice-servlet' />");

            out.println("</form>");

            // "Next" button to load the next random question
            out.println("<form method='GET' action='image-with-voice-servlet'>");
            out.println("<button type='submit'>Next</button>");
            out.println("</form>");

            out.println("</body></html>");

        } catch (SQLException e) {
            e.printStackTrace(out);
        }
    }
}
