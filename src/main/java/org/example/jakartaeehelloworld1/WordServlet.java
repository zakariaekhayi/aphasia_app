package org.example.jakartaeehelloworld1;

import java.util.logging.Logger;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "WordServlet", value = "/word-servlet")
public class WordServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(WordServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM images ORDER BY RAND() LIMIT 3")) {

            List<String> images = new ArrayList<>();
            String correctAnswerUrl = "";
            String word = "";

            // Process the ResultSet to get images and the correct answer URL
            while (rs.next()) {
                String imageUrl = rs.getString("image_url");
                images.add(imageUrl);

                if (correctAnswerUrl.isEmpty()) {
                    correctAnswerUrl = imageUrl; // Set the first image as the correct answer
                    word = rs.getString("correct_answer"); // The word for the game is the correct answer for the image
                }
            }

            // Shuffle the list of images
            Collections.shuffle(images);

            // Generate HTML to display the word and images
            out.println("<html><body>");
            out.println("<h2>Select the image that matches the word: <b>" + word + "</b></h2>");

            // Display the images as clickable links
            for (String image : images) {
                if (image.startsWith("/")) {
                    image = image.substring(1); // Remove the leading '/'
                }
                out.println("<form method='POST' action='check-answer' style='display:inline;'>");
                out.println("<input type='hidden' name='answer' value='" + image + "' />");
                out.println("<input type='hidden' name='correct' value='" + correctAnswerUrl + "' />");
                out.println("<input type='hidden' name='sourcePage' value='word-servlet' />");
                out.println("<button type='submit' style='border:none; background:none; padding:0;'>");
                out.println("<img src='" + image + "' width='200px' style='margin: 10px; border: 2px solid black;' />");
                out.println("</button>");
                out.println("</form>");
            }

            out.println("</body></html>");

        } catch (SQLException e) {
            LOGGER.severe("Database error: " + e.getMessage());
            e.printStackTrace(out); // Show the error on the webpage for debugging
        }
    }
}
