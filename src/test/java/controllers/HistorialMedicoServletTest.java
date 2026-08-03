package controllers;

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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HistorialMedicoServletTest {

  @Mock private HttpServletRequest request;
  @Mock private HttpServletResponse response;
  @Mock private HttpSession session;
  @Mock private RequestDispatcher dispatcher;
  @Mock private TurnoService turnoService;

  @InjectMocks private HistorialMedicoServlet servlet;

  @Test
  public void doGet_DebeMostrarHistorial() throws Exception {
    Usuario mockUser = new Usuario();
    mockUser.setId(1);
    mockUser.setRol("doctor");

    lenient().when(request.getSession(false)).thenReturn(session);
    lenient().when(session.getAttribute("usuarioLogueado")).thenReturn(mockUser);
    lenient().when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

    servlet.doGet(request, response);

    verify(dispatcher).forward(request, response);
  }

  @Test
  public void doGet_SinSesion_DebeRedirigirLogin() throws Exception {
    when(request.getSession(false)).thenReturn(null); // Usuario no logueado
    servlet.doGet(request, response);
    verify(response).sendRedirect(anyString()); // Verifica que lo eche al login
  }
}