package controllers;

import daos.TurnoDAO;
import models.Turno;
import models.Usuario;
import services.TurnoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/admin/turnos")
public class AdminTurnosServlet extends HttpServlet {

  private TurnoService turnoService;

  @Override
  public void init() throws ServletException {
    TurnoDAO turnoDAO = new TurnoDAO();
    this.turnoService = new TurnoService(turnoDAO);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    if (session == null || session.getAttribute("usuarioLogueado") == null) {
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }

    Usuario admin = (Usuario) session.getAttribute("usuarioLogueado");

    // Seguridad estricta: Si no es admin, afuera
    if (!"admin".equalsIgnoreCase(admin.getRol())) {
      resp.sendRedirect(req.getContextPath() + "/dashboard");
      return;
    }

    // El admin ve todos los turnos ordenados por fecha, pero NUNCA ve diagnósticos
    List<Turno> todosLosTurnos = turnoService.listarTodosLosTurnos().stream()
        .sorted(Comparator.comparing(Turno::getFechaTurno).reversed().thenComparing(Turno::getHoraTurno))
        .collect(Collectors.toList());

    req.setAttribute("turnos", todosLosTurnos);
    req.getRequestDispatcher("/WEB-INF/views/admin-turnos.jsp").forward(req, resp);
  }
}