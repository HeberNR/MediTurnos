package controllers;

import models.Usuario;
import daos.UsuarioDAO;
import services.TurnoService;
import services.UsuarioService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SolicitarTurnoServletTest {

  @Mock private HttpServletRequest request;
  @Mock private HttpServletResponse response;
  @Mock private HttpSession session;
  @Mock private RequestDispatcher dispatcher;
  @Mock private TurnoService turnoService;
  @Mock private UsuarioService usuarioService;

  // Este mock soluciona el NullPointerException
  @Mock private UsuarioDAO usuarioDAO;

  @InjectMocks private SolicitarTurnoServlet servlet;

  private void simularSesionPaciente() {
    Usuario mockUser = new Usuario();
    mockUser.setId(1);
    mockUser.setRol("paciente");
    lenient().when(request.getSession(false)).thenReturn(session);
    lenient().when(request.getSession()).thenReturn(session);
    lenient().when(session.getAttribute("usuarioLogueado")).thenReturn(mockUser);
  }

  @Test
  public void doGet_DebeMostrarFormulario() throws Exception {
    simularSesionPaciente();
    lenient().when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

    servlet.doGet(request, response);

    verify(dispatcher).forward(request, response);
  }

  @Test
  public void doPost_DebeSolicitarTurno() throws Exception {
    simularSesionPaciente();

    lenient().when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

    lenient().when(request.getParameter("doctorId")).thenReturn("2");
    lenient().when(request.getParameter("fechaTurno")).thenReturn("2026-12-12");
    lenient().when(request.getParameter("horaTurno")).thenReturn("10:00");
    lenient().when(request.getParameter("motivoConsulta")).thenReturn("Control");

    servlet.doPost(request, response);

    verify(response).sendRedirect(anyString());
  }

  @Test
  public void doGet_SinSesion_DebeRedirigirLogin() throws Exception {
    when(request.getSession(false)).thenReturn(null); // Usuario no logueado
    servlet.doGet(request, response);
    verify(response).sendRedirect(anyString()); // Verifica que lo eche al login
  }
}