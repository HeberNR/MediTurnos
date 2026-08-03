<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="models.Usuario" %>
<%--
BLOQUE JAVA (Scriptlet):
Acá pedimos la sesión actual. El parámetro 'false' significa que si el usuario no tiene sesión,
no queremos que el servidor cree una nueva vacía, simplemente devuelve null.
Luego, buscamos si existe el atributo "usuarioLogueado" en esa sesión.
--%>
<%
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

    <!-- Acá vinculamos nuestro archivo global de estilos CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/styles.css">
  </head>
  <body>
    <header class="header">
      <!-- Logo de la clínica que siempre redirige a la raíz del sitio (index) -->
      <a href="<%= request.getContextPath() %>/" class="logo">⚕️ MediTurnos</a>

      <nav class="nav-links">
        <%-- Evaluamos con un IF de Java si el usuario está logueado para mostrar un menú u otro --%>
        <% if (usuarioLogueado != null) { %>

        <!-- Menú para usuarios LOGUEADOS -->
        <%-- Usamos la clase .user-greeting de nuestro CSS en lugar de estilos en línea --%>
        <span class="user-greeting">Hola, <%= usuarioLogueado.getEmail() %></span>
        <a href="<%= request.getContextPath() %>/dashboard">Panel</a>
        <a href="<%= request.getContextPath() %>/logout" class="btn btn-outline">Salir</a>

        <% } else { %>

        <!-- Menú para visitantes (NO LOGUEADOS) -->
        <a href="<%= request.getContextPath() %>/login">Ingresar</a>
        <a href="<%= request.getContextPath() %>/registro" class="btn">Crear Cuenta</a>

        <% } %>
      </nav>
    </header>

    <!-- Abrimos el contenedor principal. Todo el contenido de las otras vistas va a ir acá adentro.
         Esta etiqueta <main> recién se cierra en el archivo footer.jsp -->
    <main class="main-content">