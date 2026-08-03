package controllers;

import models.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);

    if (session == null || session.getAttribute("usuarioLogueado") == null) {
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }

    // Recuperamos al usuario para saber quién es y qué rol tiene
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    String rol = usuario.getRol().toLowerCase();

    // Ruteo inteligente según el rol (RBAC)
    switch (rol) {
      case "paciente":
        req.getRequestDispatcher("/WEB-INF/views/dashboard-paciente.jsp").forward(req, resp);
        break;
      case "doctor":
        req.getRequestDispatcher("/WEB-INF/views/dashboard-doctor.jsp").forward(req, resp);
        break;
      case "admin":
        req.getRequestDispatcher("/WEB-INF/views/dashboard-admin.jsp").forward(req, resp);
        break;
      default:
        // Si por alguna razón tiene un rol inválido, lo pateamos
        session.invalidate();
        resp.sendRedirect(req.getContextPath() + "/login");
        break;
    }
  }
}