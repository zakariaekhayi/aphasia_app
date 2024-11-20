package org.example.jakartaeehelloworld1;

import java.util.logging.Logger;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "ImageServlet", value = "/image-servlet")
public class ImageServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ImageServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM images ORDER BY RAND() LIMIT 3")) {

            List<String> images = new ArrayList<>();
            List<String> words = new ArrayList<>();
            String correctImage = "";
            String correctWord = "";

            // Process the ResultSet to get images and words
            while (rs.next()) {
                String imageUrl = rs.getString("image_url");
                images.add(imageUrl);

                if (correctImage.isEmpty()) {
                    correctImage = imageUrl; // Set the first image as the correct image
                    correctWord = rs.getString("correct_answer"); // The correct word for the image
                } else {
                    words.add(rs.getString("correct_answer")); // Add the rest of the words
                }
            }

            // Ensure the correct word is included in the choices
            if (words.size() < 2) {
                throw new IllegalStateException("Not enough words in the database.");
            }
            words.add(correctWord); // Include the correct answer in the choices
            Collections.shuffle(words); // Shuffle the words to randomize the order

            // Generate HTML to display the words and image
            out.println("<html><body>");
            out.println("<h2>Select the word that matches the image:</h2>");

            // Display the words as clickable links
            for (String word : words) {
                out.println("<form method='POST' action='check-answer' style='display:inline;'>");
                out.println("<input type='hidden' name='answer' value='" + word + "' />");
                out.println("<input type='hidden' name='correct' value='" + correctWord + "' />");
                out.println("<input type='hidden' name='sourcePage' value='image-servlet' />");
                out.println("<button type='submit' style='border:none; background:none; padding:0; margin: 10px;'>");
                out.println("<b>" + word + "</b>");
                out.println("</button>");
                out.println("</form>");
            }

            // Display the image
            if (correctImage.startsWith("/")) {
                correctImage = correctImage.substring(1); // Remove the leading '/'
            }
            out.println("<br/><img src='" + correctImage + "' width='200px' style='margin: 10px; border: 2px solid black;' />");

            out.println("</body></html>");

        } catch (SQLException e) {
            LOGGER.severe("Database error: " + e.getMessage());
            e.printStackTrace(out); // Show the error on the webpage for debugging
        } catch (IllegalStateException e) {
            LOGGER.severe("State error: " + e.getMessage());
            out.println("<html><body>");
            out.println("<h2>Error: Not enough words in the database.</h2>");
            out.println("</body></html>");
        }
    }
}
