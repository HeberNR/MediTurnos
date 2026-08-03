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
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/admin/turnos")
public class AdminTurnosServlet extends HttpServlet {

  private TurnoService turnoService;
  private UsuarioDAO usuarioDAO;

  // Constructor por defecto requerido por Tomcat
  public AdminTurnosServlet() {
    this.turnoService = new TurnoService(new TurnoDAO());
    this.usuarioDAO = new UsuarioDAO();
  }

  // Constructor para inyección en pruebas unitarias (Mockito)
  public AdminTurnosServlet(TurnoService turnoService, UsuarioDAO usuarioDAO) {
    this.turnoService = turnoService;
    this.usuarioDAO = usuarioDAO;
  }

  @Override
  public void init() throws ServletException {
    super.init();
    if (this.turnoService == null) {
      this.turnoService = new TurnoService(new TurnoDAO());
    }
    if (this.usuarioDAO == null) {
      this.usuarioDAO = new UsuarioDAO();
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    if (session == null || session.getAttribute("usuarioLogueado") == null) {
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }

    Usuario admin = (Usuario) session.getAttribute("usuarioLogueado");

    if (!"admin".equalsIgnoreCase(admin.getRol())) {
      resp.sendRedirect(req.getContextPath() + "/dashboard");
      return;
    }

    req.setAttribute("fechaHoy", LocalDate.now().toString());

    List<Usuario> doctores = usuarioDAO.listarDoctoresConEspecialidad();
    req.setAttribute("doctores", doctores);

    List<Turno> todosLosTurnos = turnoService.listarTodosLosTurnos().stream()
        .sorted(Comparator.comparing(Turno::getFechaTurno).reversed().thenComparing(Turno::getHoraTurno))
        .collect(Collectors.toList());

    req.setAttribute("turnos", todosLosTurnos);
    req.getRequestDispatcher("/WEB-INF/views/admin-turnos.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    if (session == null || session.getAttribute("usuarioLogueado") == null) {
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }

    Usuario admin = (Usuario) session.getAttribute("usuarioLogueado");
    if (!"admin".equalsIgnoreCase(admin.getRol())) {
      resp.sendRedirect(req.getContextPath() + "/dashboard");
      return;
    }

    try {
      String accion = req.getParameter("accion");

      if ("crear_turno".equals(accion)) {
        String doctorIdStr = req.getParameter("doctorId");
        String dni = req.getParameter("dni");
        String nombre = req.getParameter("nombre");
        String apellido = req.getParameter("apellido");
        String fechaTurno = req.getParameter("fechaTurno");
        String horaTurno = req.getParameter("horaTurno");
        String motivoConsulta = req.getParameter("motivoConsulta");

        if (doctorIdStr == null || dni == null || fechaTurno == null || horaTurno == null) {
          req.setAttribute("errorAdmin", "Faltan datos obligatorios para agendar el turno.");
        } else {
          int doctorId = Integer.parseInt(doctorIdStr);
          turnoService.agendarTurnoAdministrativo(doctorId, dni, nombre, apellido, fechaTurno, horaTurno, motivoConsulta);
          req.setAttribute("exitoAdmin", "Turno agendado y asignado correctamente al profesional.");
        }
      }
    } catch (Exception e) {
      req.setAttribute("errorAdmin", "Error al procesar el turno: " + e.getMessage());
    }

    doGet(req, resp);
  }
}