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
public class AgendaDoctorServletTest {

  @Mock private HttpServletRequest request;
  @Mock private HttpServletResponse response;
  @Mock private HttpSession session;
  @Mock private RequestDispatcher dispatcher;
  @Mock private TurnoService turnoService;

  @InjectMocks private AgendaDoctorServlet servlet;

  private void simularSesionDoctor() {
    Usuario mockDoctor = new Usuario();
    mockDoctor.setId(1);
    mockDoctor.setRol("doctor");
    lenient().when(request.getSession(false)).thenReturn(session);
    lenient().when(session.getAttribute("usuarioLogueado")).thenReturn(mockDoctor);
  }

  @Test
  public void doGet_DebeMostrarAgenda() throws Exception {
    simularSesionDoctor();
    lenient().when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

    servlet.doGet(request, response);

    verify(dispatcher).forward(request, response);
  }

  @Test
  public void doPost_DebeProcesarAcciones() throws Exception {
    simularSesionDoctor();
    lenient().when(request.getParameter("accion")).thenReturn("atender");
    lenient().when(request.getParameter("turnoId")).thenReturn("1");
    lenient().when(request.getParameter("diagnostico")).thenReturn("Todo ok");
    lenient().when(request.getParameter("observaciones")).thenReturn("Sin observaciones");

    servlet.doPost(request, response);

    verify(response).sendRedirect(anyString());
  }

  @Test
  public void doGet_SinSesion_DebeRedirigirLogin() throws Exception {
    when(request.getSession(false)).thenReturn(null);
    servlet.doGet(request, response);
    verify(response).sendRedirect(anyString());
  }

  @Test
  public void doPost_AccionNula_DebeIgnorarOVolver() throws Exception {
    simularSesionDoctor();
    lenient().when(request.getParameter("accion")).thenReturn(null); // Sin acción
    servlet.doPost(request, response);
    verify(response).sendRedirect(anyString());
  }
}