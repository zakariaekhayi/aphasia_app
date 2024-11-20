package org.example.spelling;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.jakartaeehelloworld1.DatabaseHelper;

import java.io.*;
import java.sql.*;

@WebServlet(name = "NamingImageServlet", value = "/naming-image-servlet")
public class NamingImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM images ORDER BY RAND() LIMIT 1")) {

            String correctAnswer = "";
            String imageUrl = "";

            // Fetch the image URL and correct answer
            if (rs.next()) {
                correctAnswer = rs.getString("correct_answer").toLowerCase(); // The correct word
                imageUrl = rs.getString("image_url"); // The image URL
            }

            // HTML structure with image and input field
            out.println("<html><body>");
            out.println("<h2>Type the name of the object in the image:</h2>");
            out.println("<img src='" + imageUrl + "' width='200px' />");
            out.println("<form method='POST' action='check-answer'>");
            out.println("<input type='text' name='answer' placeholder='Enter your answer' />");
            out.println("<input type='hidden' name='correct' value='" + correctAnswer + "' />");
            out.println("<input type='hidden' name='sourcePage' value='naming-image-servlet' />");
            out.println("<button type='submit'>Submit</button>");
            out.println("</form>");
            out.println("</body></html>");

        } catch (SQLException e) {
            e.printStackTrace(out);
        }
    }
}
