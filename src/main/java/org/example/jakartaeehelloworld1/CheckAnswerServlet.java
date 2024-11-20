package org.example.jakartaeehelloworld1;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;

@WebServlet(name = "CheckAnswerServlet", value = "/check-answer")
public class CheckAnswerServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get user's answer and correct answer
        String userAnswer = request.getParameter("answer").toLowerCase(); // Convert user's answer to lowercase
        String correctAnswer = request.getParameter("correct").toLowerCase(); // Convert correct answer to lowercase
        String sourcePage = request.getParameter("sourcePage"); // Get the source servlet

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");

        // Check if the user's answer matches the correct answer
        if (userAnswer.equals(correctAnswer)) {
            out.println("<h2 style='color:green'>Correct!</h2>");
        } else {
            out.println("<h2 style='color:red'>Incorrect!</h2>");

            // Display the correct answer depending on the source page
            switch (sourcePage) {
                case "word-servlet":
                    out.println("<p>The correct answer is:</p>");
                    out.println("<img src='images/" + correctAnswer + ".png' width='200px' />"); // Display correct image
                    break;
                case "image-servlet":
                    out.println("<p>The correct answer is: " + correctAnswer + "</p>"); // Display correct word
                    break;
                case "listening-word-servlet":
                    out.println("<p>The correct word is: <b>" + correctAnswer + "</b></p>"); // Display correct word for listening
                    break;
                case "listening-image-servlet":
                    out.println("<p>The correct image was:</p>");
                    out.println("<img src='images/" + correctAnswer + ".png' width='200px' style='margin: 10px; border: 2px solid black;' />"); // Display correct image for listening
                    break;
                default:
                    out.println("<p>The correct answer is: " + correctAnswer + "</p>");
                    break;
            }
        }

        // Add a "Try Again" button to allow the user to retry the exercise
        switch (sourcePage) {
            case "naming-image-servlet":
                out.println("<a href='naming-image-servlet'>Try Again</a>");
                break;
            case "naming-voice-servlet":
                out.println("<a href='naming-voice-servlet'>Try Again</a>");
                break;
            case "listening-word-servlet":
                out.println("<a href='listening-word-servlet'>Try Again</a>");
                break;
            case "listening-image-servlet":
                out.println("<a href='listening-image-servlet'>Try Again</a>");
                break;
            case "word-servlet":
                out.println("<a href='word-servlet'>Try Again</a>");
                break;
            case "image-servlet":
                out.println("<a href='image-servlet'>Try Again</a>");
                break;
            default:
                out.println("<a href='/'>Go to Home</a>");
                break;
        }

        out.println("</body></html>");
    }
}
