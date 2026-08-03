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
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/turnos/mis-turnos")
public class MisTurnosServlet extends HttpServlet {

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

    Usuario paciente = (Usuario) session.getAttribute("usuarioLogueado");

    // 1. Obtener parámetros de vista
    String vista = req.getParameter("vista");
    if (vista == null || vista.isEmpty()) {
      vista = "pendientes"; // Por defecto arranca en los próximos turnos
    }

    int page = 1;
    String pageParam = req.getParameter("page");
    if (pageParam != null && !pageParam.isEmpty()) {
      try {
        page = Integer.parseInt(pageParam);
      } catch (NumberFormatException e) {
        page = 1;
      }
    }

    // 2. Traer todos los turnos de este paciente
    List<Turno> misTurnos = turnoService.obtenerTurnosPorPaciente(paciente.getId());

    // 3. Filtrar y Ordenar según la pestaña elegida
    List<Turno> turnosFiltrados;
    if ("historial".equals(vista)) {
      // En historial mostramos atendidos, cancelados y ausentes (ordenados del más reciente al más viejo)
      turnosFiltrados = misTurnos.stream()
          .filter(t -> !"pendiente".equalsIgnoreCase(t.getEstado()))
          .sorted(Comparator.comparing(Turno::getFechaTurno).reversed().thenComparing(Turno::getHoraTurno))
          .collect(Collectors.toList());
    } else {
      // En pendientes mostramos los próximos (ordenados del más cercano al más lejano)
      turnosFiltrados = misTurnos.stream()
          .filter(t -> "pendiente".equalsIgnoreCase(t.getEstado()))
          .sorted(Comparator.comparing(Turno::getFechaTurno).thenComparing(Turno::getHoraTurno))
          .collect(Collectors.toList());
    }

    // 4. Paginación (Máximo 5 por página)
    int pageSize = 5;
    int totalRegistros = turnosFiltrados.size();
    int totalPages = (int) Math.ceil((double) totalRegistros / pageSize);

    if (page > totalPages && totalPages > 0) page = totalPages;
    if (page < 1) page = 1;

    int start = (page - 1) * pageSize;
    int end = Math.min(start + pageSize, totalRegistros);

    List<Turno> turnosPaginados = turnosFiltrados.subList(start, end);

    req.setAttribute("turnos", turnosPaginados);
    req.setAttribute("vistaActual", vista);
    req.setAttribute("currentPage", page);
    req.setAttribute("totalPages", totalPages);

    String mensajeExito = (String) session.getAttribute("mensajeExito");
    if (mensajeExito != null) {
      req.setAttribute("mensajeExito", mensajeExito);
      session.removeAttribute("mensajeExito");
    }
    String error = (String) session.getAttribute("error");
    if (error != null) {
      req.setAttribute("error", error);
      session.removeAttribute("error");
    }

    req.getRequestDispatcher("/WEB-INF/views/mis-turnos.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    Usuario paciente = (Usuario) session.getAttribute("usuarioLogueado");

    try {
      int turnoId = Integer.parseInt(req.getParameter("turnoId"));

      // RBAC: Verificamos que el turno sea de ESTE paciente
      Optional<Turno> turnoOpt = turnoService.buscarTurno(turnoId);
      if (!turnoOpt.isPresent()) {
        throw new IllegalArgumentException("El turno no existe.");
      }
      if (!turnoOpt.get().getPacienteId().equals(paciente.getId())) {
        throw new SecurityException("No tenés permiso para cancelar este turno.");
      }

      boolean actualizado = turnoService.cambiarEstadoTurno(turnoId, "cancelado");
      if (actualizado) {
        req.getSession().setAttribute("mensajeExito", "El turno ha sido cancelado exitosamente.");
      } else {
        req.getSession().setAttribute("error", "No se pudo actualizar el estado del turno.");
      }
    } catch (Exception e) {
      req.getSession().setAttribute("error", "Error: " + e.getMessage());
    }

    resp.sendRedirect(req.getContextPath() + "/turnos/mis-turnos");
  }
}