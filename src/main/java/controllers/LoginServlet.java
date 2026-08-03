package controllers;

import daos.UsuarioDAO;
import models.Usuario;
import services.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

// Mapeamos la URL directamente. Cuando el usuario entre a /login, cae acá.
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

  private UsuarioService usuarioService;

  // Inicializamos el servicio cuando arranca el Servlet
  @Override
  public void init() throws ServletException {
    // Instanciamos el DAO y se lo inyectamos al Servicio
    UsuarioDAO usuarioDAO = new UsuarioDAO();
    this.usuarioService = new UsuarioService(usuarioDAO);
  }

  // El método GET solo se encarga de mostrar la vista (el formulario)
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // Si el usuario ya tiene sesión, lo pateamos directo a su panel para que no se vuelva a loguear
    HttpSession session = req.getSession(false);
    if (session != null && session.getAttribute("usuarioLogueado") != null) {
      resp.sendRedirect(req.getContextPath() + "/dashboard");
      return;
    }

    // Redirigimos a la página JSP del login
    req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
  }

  // El método POST procesa los datos que el usuario escribió en el formulario
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String email = req.getParameter("email");
    String password = req.getParameter("password");

    // LÍNEAS DE DIAGNÓSTICO
    System.out.println("--- INTENTO DE LOGIN ---");
    System.out.println("Email capturado del JSP: " + email);
    System.out.println("Password capturado del JSP: " + (password != null ? "¡Dato recibido!" : "NULL - ¡Error en el name del HTML!"));

    // Llamamos al servicio UNA sola vez
    Optional<Usuario> usuarioOpt = usuarioService.login(email, password);

    System.out.println("Resultado de la búsqueda en BD: " + (usuarioOpt.isPresent() ? "Usuario Encontrado y Hash correcto" : "Fallo en BD o Contraseña incorrecta"));

    if (usuarioOpt.isPresent()) {
      // ¡Login Exitoso!
      Usuario usuario = usuarioOpt.get();

      // Creamos una sesión y guardamos el objeto del usuario entero adentro
      HttpSession session = req.getSession();
      session.setAttribute("usuarioLogueado", usuario);
      session.setAttribute("rolUsuario", usuario.getRol());

      // Lo redirigimos a la pantalla principal
      resp.sendRedirect(req.getContextPath() + "/dashboard");

    } else {
      // ¡Login Fallido!
      // Guardamos un mensaje de error en el request para mostrarlo en rojo en el HTML
      req.setAttribute("error", "Credenciales inválidas o cuenta inactiva. Intente nuevamente.");

      // Volvemos a mostrar el formulario de login, pero ahora con el error inyectado
      req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }
  }
}