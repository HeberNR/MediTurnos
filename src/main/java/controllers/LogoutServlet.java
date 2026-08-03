package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // Pedimos la sesión actual (false significa que si no hay, no cree una nueva)
    HttpSession session = req.getSession(false);

    if (session != null) {
      // Esto destruye la sesión y borra al usuario logueado de la memoria del servidor
      session.invalidate();
    }

    // Redirigimos al usuario a la página de inicio (el index.jsp)
    resp.sendRedirect(req.getContextPath() + "/");
  }
}