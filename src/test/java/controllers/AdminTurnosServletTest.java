package controllers;

import daos.UsuarioDAO;
import models.Usuario;
import services.TurnoService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminTurnosServletTest {

  @Mock private HttpServletRequest request;
  @Mock private HttpServletResponse response;
  @Mock private HttpSession session;
  @Mock private RequestDispatcher dispatcher;
  @Mock private TurnoService turnoService;
  @Mock private UsuarioDAO usuarioDAO;

  @InjectMocks private AdminTurnosServlet servlet;

  @Test
  public void init_DebeInicializarCorrectamente() throws Exception {
    servlet.init();
    // Verifica que el método init se ejecute sin arrojar excepciones
  }

  @Test
  public void doGet_DebeListarTodosLosTurnos() throws Exception {
    Usuario mockAdmin = new Usuario();
    mockAdmin.setRol("admin");

    lenient().when(request.getSession(false)).thenReturn(session);
    lenient().when(session.getAttribute("usuarioLogueado")).thenReturn(mockAdmin);
    lenient().when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    lenient().when(usuarioDAO.listarDoctoresConEspecialidad()).thenReturn(Collections.emptyList());

    servlet.doGet(request, response);

    verify(turnoService, atLeastOnce()).listarTodosLosTurnos();
    verify(dispatcher).forward(request, response);
  }

  @Test
  public void doGet_SinSesion_DebeRedirigirLogin() throws Exception {
    when(request.getSession(false)).thenReturn(null);
    servlet.doGet(request, response);
    verify(response).sendRedirect(anyString());
  }

  @Test
  public void doGet_NoAdmin_DebeRedirigirDashboard() throws Exception {
    Usuario mockPaciente = new Usuario();
    mockPaciente.setRol("paciente");

    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("usuarioLogueado")).thenReturn(mockPaciente);

    servlet.doGet(request, response);
    verify(response).sendRedirect(anyString());
  }

  @Test
  public void doPost_SinSesion_DebeRedirigirLogin() throws Exception {
    when(request.getSession(false)).thenReturn(null);
    servlet.doPost(request, response);
    verify(response).sendRedirect(anyString());
  }

  @Test
  public void doPost_NoAdmin_DebeRedirigirDashboard() throws Exception {
    Usuario mockPaciente = new Usuario();
    mockPaciente.setRol("paciente");

    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("usuarioLogueado")).thenReturn(mockPaciente);

    servlet.doPost(request, response);
    verify(response).sendRedirect(anyString());
  }

  @Test
  public void doPost_CrearTurno_Exito() throws Exception {
    Usuario mockAdmin = new Usuario();
    mockAdmin.setRol("admin");

    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("usuarioLogueado")).thenReturn(mockAdmin);
    when(request.getParameter("accion")).thenReturn("crear_turno");
    when(request.getParameter("doctorId")).thenReturn("1");
    when(request.getParameter("dni")).thenReturn("12345678");
    when(request.getParameter("nombre")).thenReturn("Juan");
    when(request.getParameter("apellido")).thenReturn("Perez");
    when(request.getParameter("fechaTurno")).thenReturn("2026-08-10");
    when(request.getParameter("horaTurno")).thenReturn("10:00");
    when(request.getParameter("motivoConsulta")).thenReturn("Control");
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    when(usuarioDAO.listarDoctoresConEspecialidad()).thenReturn(Collections.emptyList());

    servlet.doPost(request, response);

    verify(turnoService).agendarTurnoAdministrativo(eq(1), eq("12345678"), eq("Juan"), eq("Perez"), eq("2026-08-10"), eq("10:00"), eq("Control"));
    verify(dispatcher).forward(request, response);
  }

  @Test
  public void doPost_CrearTurno_FaltanDatos_DebeMostrarError() throws Exception {
    Usuario mockAdmin = new Usuario();
    mockAdmin.setRol("admin");

    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("usuarioLogueado")).thenReturn(mockAdmin);
    when(request.getParameter("accion")).thenReturn("crear_turno");
    when(request.getParameter("doctorId")).thenReturn(null); // Datos incompletos
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    when(usuarioDAO.listarDoctoresConEspecialidad()).thenReturn(Collections.emptyList());

    servlet.doPost(request, response);

    verify(request).setAttribute(eq("errorAdmin"), anyString());
    verify(dispatcher).forward(request, response);
  }
}