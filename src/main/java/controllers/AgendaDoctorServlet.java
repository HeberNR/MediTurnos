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
import java.sql.Date;
import java.sql.Time;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/turnos/agenda")
public class AgendaDoctorServlet extends HttpServlet {

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

    if (!"doctor".equalsIgnoreCase(doctor.getRol())) {
      resp.sendRedirect(req.getContextPath() + "/dashboard");
      return;
    }

    String estadoFiltro = req.getParameter("estado");
    if (estadoFiltro == null || estadoFiltro.isEmpty()) {
      estadoFiltro = "pendiente";
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

    List<Turno> todosLosTurnos = turnoService.obtenerTurnosPorDoctor(doctor.getId());

    final String filtroActual = estadoFiltro;
    List<Turno> turnosFiltrados = todosLosTurnos.stream()
        .filter(t -> t.getEstado().equalsIgnoreCase(filtroActual))
        .sorted(Comparator.comparing(Turno::getFechaTurno).thenComparing(Turno::getHoraTurno))
        .collect(Collectors.toList());

    int pageSize = 5;
    int totalRegistros = turnosFiltrados.size();
    int totalPaginas = (int) Math.ceil((double) totalRegistros / pageSize);

    if (page > totalPaginas && totalPaginas > 0) page = totalPaginas;
    if (page < 1) page = 1;

    int start = (page - 1) * pageSize;
    int end = Math.min(start + pageSize, totalRegistros);

    List<Turno> turnosPaginados = turnosFiltrados.subList(start, end);

    req.setAttribute("turnos", turnosPaginados);
    req.setAttribute("estadoFiltro", estadoFiltro);
    req.setAttribute("currentPage", page);
    req.setAttribute("totalPages", totalPaginas);

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

    req.getRequestDispatcher("/WEB-INF/views/agenda-doctor.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    Usuario doctor = (Usuario) session.getAttribute("usuarioLogueado");

    try {
      String accion = req.getParameter("accion");

      if ("agendar_presencial".equals(accion)) {
        String dni = req.getParameter("dni");
        String nombre = req.getParameter("nombre");
        String apellido = req.getParameter("apellido");
        Date fecha = Date.valueOf(req.getParameter("fechaTurno"));
        String horaStr = req.getParameter("horaTurno");
        if (horaStr.length() == 5) horaStr += ":00";
        Time hora = Time.valueOf(horaStr);
        String motivo = req.getParameter("motivoConsulta");

        turnoService.crearTurnoPresencial(dni, nombre, apellido, doctor.getId(), fecha, hora, motivo);
        session.setAttribute("mensajeExito", "Turno presencial agendado correctamente.");

      } else if ("atender".equals(accion)) {
        int turnoId = Integer.parseInt(req.getParameter("turnoId"));
        String diagnostico = req.getParameter("diagnostico");
        String observaciones = req.getParameter("observaciones");

        turnoService.registrarAtencionMedica(turnoId, "atendido", diagnostico, observaciones);
        session.setAttribute("mensajeExito", "Consulta finalizada y guardada en el historial.");

      } else if ("ausente".equals(accion)) {
        int turnoId = Integer.parseInt(req.getParameter("turnoId"));
        turnoService.cambiarEstadoTurno(turnoId, "ausente");
        session.setAttribute("mensajeExito", "Paciente marcado como ausente.");

      } else if ("cancelar".equals(accion)) { // <--- NUEVA LÓGICA AGREGADA
        int turnoId = Integer.parseInt(req.getParameter("turnoId"));
        turnoService.cambiarEstadoTurno(turnoId, "cancelado");
        session.setAttribute("mensajeExito", "Turno cancelado correctamente.");
      }

    } catch (Exception e) {
      session.setAttribute("error", "Error al procesar la solicitud: " + e.getMessage());
    }

    resp.sendRedirect(req.getContextPath() + "/turnos/agenda");
  }
}