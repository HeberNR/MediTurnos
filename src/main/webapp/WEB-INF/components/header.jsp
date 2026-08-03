<%@ page import="models.Usuario" %>
<%
// Recuperamos la sesión actual (si existe). El 'false' evita que se cree una nueva si no hay.
HttpSession currentSession = request.getSession(false);
Usuario usuarioLogueado = null;

if (currentSession != null) {
usuarioLogueado = (Usuario) currentSession.getAttribute("usuarioLogueado");
}
%>
<!DOCTYPE html>
<html lang="es">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Turnos</title>
    <!-- Importamos nuestro CSS custom -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/styles.css">
  </head>
  <body>
    <header class="header">
      <a href="<%= request.getContextPath() %>/" class="logo">⚕️ MediTurnos</a>

      <nav class="nav-links">
        <% if (usuarioLogueado != null) { %>
        <!-- Menú para usuarios LOGUEADOS -->
        <span style="color: var(--text-secondary);">Hola, <%= usuarioLogueado.getEmail() %></span>
        <a href="<%= request.getContextPath() %>/dashboard">Panel</a>
        <a href="<%= request.getContextPath() %>/logout" class="btn btn-outline">Salir</a>
        <% } else { %>
        <!-- Menú para visitantes (NO LOGUEADOS) -->
        <a href="<%= request.getContextPath() %>/login">Ingresar</a>
        <a href="<%= request.getContextPath() %>/registro" class="btn">Crear Cuenta</a>
        <% } %>
      </nav>
    </header>

    <!-- Abrimos la etiqueta main, que se va a cerrar en el footer -->
    <main class="main-content">