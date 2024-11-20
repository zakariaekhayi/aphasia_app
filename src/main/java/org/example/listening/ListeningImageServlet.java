package org.example.listening;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ListeningImageServlet", value = "/listening-image-servlet")
public class ListeningImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM images ORDER BY RAND() LIMIT 3")) {

            List<String> images = new ArrayList<>();
            String correctAnswer = "";
            String voiceUrl = "";

            // Fetch images, correct answer, and voice URL
            while (rs.next()) {
                String imageUrl = rs.getString("image_url");
                images.add(imageUrl);

                if (correctAnswer.isEmpty()) {
                    correctAnswer = rs.getString("correct_answer");
                    voiceUrl = rs.getString("voice_url"); // Fetch the voice URL from the new column
                }
            }

            // HTML structure with voice and image choices
            out.println("<html><body>");
            out.println("<audio controls ><source src='voices//" + voiceUrl + "' type='audio/mpeg'></audio>");
            out.println("<h2>Select the image that matches the voice:</h2>");
            for (String image : images) {
                out.println("<form method='POST' action='check-answer'>");
                out.println("<input type='hidden' kname='answer' value='" + image + "' />");
                out.println("<input type='hidden' name='correct' value='" + correctAnswer + "' />");
                out.println("<input type='hidden' name='sourcePage' value='listening-image-servlet' />");
                out.println("<button type='submit'><img src='" + image + "' width='200px'/></button>");
                out.println("</form>");
            }
            out.println("</body></html>");

        } catch (SQLException e) {
            e.printStackTrace(out);
        }
    }
}
