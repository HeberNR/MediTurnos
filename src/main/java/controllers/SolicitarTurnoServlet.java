package controllers;

import daos.TurnoDAO;
import daos.UsuarioDAO;
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
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/turnos/solicitar")
public class SolicitarTurnoServlet extends HttpServlet {

  private TurnoService turnoService;
  private UsuarioDAO usuarioDAO;

  @Override
  public void init() throws ServletException {
    TurnoDAO turnoDAO = new TurnoDAO();
    this.turnoService = new TurnoService(turnoDAO);
    this.usuarioDAO = new UsuarioDAO();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    if (session == null || session.getAttribute("usuarioLogueado") == null) {
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }

    // Solo mandamos la lista de doctores a la vista (el enum se encarga de las especialidades)
    List<Usuario> doctores = usuarioDAO.listarDoctoresConEspecialidad();
    req.setAttribute("doctores", doctores);

    // Mandamos la fecha de hoy para bloquear el calendario HTML
    req.setAttribute("fechaHoy", LocalDate.now().toString());

    req.getRequestDispatcher("/WEB-INF/views/solicitar-turno.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    Usuario paciente = (Usuario) session.getAttribute("usuarioLogueado");

    try {
      int doctorId = Integer.parseInt(req.getParameter("doctorId"));

      Date fecha = Date.valueOf(req.getParameter("fechaTurno"));

      // Validación BACKEND: Prevenir fechas en el pasado
      if (fecha.toLocalDate().isBefore(LocalDate.now())) {
        throw new IllegalArgumentException("No podés solicitar un turno en una fecha que ya pasó.");
      }

      String horaStr = req.getParameter("horaTurno");
      if (horaStr.length() == 5) horaStr += ":00";
      Time hora = Time.valueOf(horaStr);

      String motivo = req.getParameter("motivoConsulta");

      Turno turno = new Turno();
      turno.setPacienteId(paciente.getId());
      turno.setDoctorId(doctorId);
      turno.setFechaTurno(fecha);
      turno.setHoraTurno(hora);
      turno.setMotivoConsulta(motivo);

      turnoService.solicitarTurno(turno);
      req.getSession().setAttribute("mensajeExito", "¡Turno agendado correctamente!");
      resp.sendRedirect(req.getContextPath() + "/turnos/mis-turnos");

    } catch (Exception e) {
      req.setAttribute("error", "Error al solicitar el turno: " + e.getMessage());
      doGet(req, resp); // Recargamos el form con el mensaje de error
    }
  }
}