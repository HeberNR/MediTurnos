package controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogoutServletTest {

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private HttpSession session;

  @InjectMocks
  private LogoutServlet logoutServlet;

  @Test
  public void doGet_DebeInvalidarSesionYRedirigirLogin() throws Exception {
    // Simulamos que hay una sesión activa
    when(request.getSession(false)).thenReturn(session);

    logoutServlet.doGet(request, response);

    // Verificamos que se invalide la sesión y redirija
    verify(session).invalidate();
    verify(response).sendRedirect(request.getContextPath() + "/");
  }
}