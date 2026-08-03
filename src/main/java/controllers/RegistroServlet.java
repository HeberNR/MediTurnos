package controllers;

import daos.UsuarioDAO;
import services.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

  private UsuarioService usuarioService;

  @Override
  public void init() throws ServletException {
    UsuarioDAO usuarioDAO = new UsuarioDAO();
    this.usuarioService = new UsuarioService(usuarioDAO);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    String email = req.getParameter("email");
    String password = req.getParameter("password");
    String nombre = req.getParameter("nombre");
    String apellido = req.getParameter("apellido");
    String dni = req.getParameter("dni");
    String telefono = req.getParameter("telefono");
    String rol = req.getParameter("rol");

    // NUEVO: Capturamos la especialidad del formulario
    String especialidad = req.getParameter("especialidad");

    if (rol == null || rol.trim().isEmpty()) {
      rol = "paciente";
    }

    try {
      // Llamamos al servicio pasando también la especialidad
      usuarioService.registrarUsuario(email, password, rol, nombre, apellido, dni, telefono, especialidad);

      req.setAttribute("mensajeExito", "Usuario registrado. ¡Ahora podés iniciar sesión!");
      req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    } catch (IllegalArgumentException e) {
      req.setAttribute("error", e.getMessage());
      req.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(req, resp);
    }
  }
}