package org.example.jakartaeehelloworld2;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "customHelloServlet", value = "/custom-hello-servlet")
public class CustomHelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Welcome to the Custom Hello Servlet!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Custom HTML
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("<p>This is a custom servlet response.</p>");
        out.println("<ul>");
        out.println("<li>Item 1</li>");
        out.println("<li>Item 2</li>");
        out.println("<li>Item 3</li>");
        out.println("</ul>");
        out.println("<footer>Servlet Example Footer</footer>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}
