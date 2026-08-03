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
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/turnos/historial")
public class HistorialMedicoServlet extends HttpServlet {

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

    Usuario doctor = (Usuario) session.getAttribute("usuarioLogueado");

    // Seguridad: Solo los doctores pueden ver su historial de atención
    if (!"doctor".equalsIgnoreCase(doctor.getRol())) {
      resp.sendRedirect(req.getContextPath() + "/dashboard");
      return;
    }

    // Usamos Streams para filtrar ÚNICAMENTE los turnos marcados como "atendido"
    List<Turno> historial = turnoService.obtenerTurnosPorDoctor(doctor.getId())
        .stream()
        .filter(t -> "atendido".equalsIgnoreCase(t.getEstado()))
        .collect(Collectors.toList());

    req.setAttribute("historial", historial);
    req.getRequestDispatcher("/WEB-INF/views/historial-medico.jsp").forward(req, resp);
  }
}