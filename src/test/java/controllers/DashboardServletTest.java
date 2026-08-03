package controllers;

import models.Usuario;
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
public class DashboardServletTest {

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private HttpSession session;

  @Mock
  private RequestDispatcher dispatcher;

  @InjectMocks
  private DashboardServlet dashboardServlet;

  @Test
  public void doGet_DebeRedirigirAlDashboardSegunRol() throws Exception {
    // Simulamos un usuario logueado con rol de paciente
    Usuario mockUsuario = new Usuario();
    mockUsuario.setRol("paciente");

    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("usuarioLogueado")).thenReturn(mockUsuario);

    // Usamos anyString() para atajar cualquier ruta a la que quiera ir
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

    dashboardServlet.doGet(request, response);

    // Verificamos que se haya ejecutado el forward hacia la vista
    verify(dispatcher).forward(request, response);
  }

  @Test
  public void doGet_DebeRedirigirAlDashboardDoctor() throws Exception {
    Usuario mockDoctor = new Usuario();
    mockDoctor.setRol("doctor");
    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("usuarioLogueado")).thenReturn(mockDoctor);
    lenient().when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

    dashboardServlet.doGet(request, response);
    verify(dispatcher).forward(request, response);
  }

  @Test
  public void doGet_DebeRedirigirAlDashboardAdmin() throws Exception {
    Usuario mockAdmin = new Usuario();
    mockAdmin.setRol("admin");
    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("usuarioLogueado")).thenReturn(mockAdmin);
    lenient().when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

    dashboardServlet.doGet(request, response);
    verify(dispatcher).forward(request, response);
  }

  @Test
  public void doGet_SinSesion_DebeRedirigirLogin() throws Exception {
    when(request.getSession(false)).thenReturn(null); // Simulamos que no hay sesión
    dashboardServlet.doGet(request, response);
    verify(response).sendRedirect(anyString());
  }
}