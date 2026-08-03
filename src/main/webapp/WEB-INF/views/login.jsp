<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<%-- 1. Llamamos al Header dinámico que renderiza la cabecera y abre el contenedor principal <main>[cite: 1] --%>
  <jsp:include page="../components/header.jsp" />

  <%-- Contenedor en forma de tarjeta para centrar visualmente el formulario de inicio de sesión[cite: 1] --%>
  <div class="card">
    <h2>Iniciar Sesión</h2>

    <%--
    BLOQUE DE ALERTAS:
    Acá evaluamos si el Servlet nos devolvió un mensaje de error o de éxito mediante los atributos del request.
    Si existen, se renderizan utilizando las clases de alerta de nuestro CSS global[cite: 1].
    --%>
    <% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-error"><%= request.getAttribute("error") %></div>
    <% } %>
    <% if (request.getAttribute("mensajeExito") != null) { %>
    <div class="alert alert-success"><%= request.getAttribute("mensajeExito") %></div>
    <% } %>

    <%-- Formulario de acceso que envía las credenciales mediante el método POST hacia la ruta /login[cite: 1] --%>
    <form action="<%= request.getContextPath() %>/login" method="post">

      <%-- Grupo de entrada de datos para el correo electrónico[cite: 1] --%>
      <div class="input-group">
        <label for="email">Correo Electrónico</label>
        <input type="email" id="email" name="email" required placeholder="tu@email.com">
      </div>

      <%-- Grupo de entrada de datos para la contraseña[cite: 1] --%>
      <div class="input-group">
        <label for="password">Contraseña</label>
        <input type="password" id="password" name="password" required placeholder="••••••••">
      </div>

      <%-- Botón principal para disparar el envío del formulario[cite: 1] --%>
      <button type="submit" class="btn" style="width: 100%; margin-top: 10px;">Ingresar al sistema</button>
    </form>
  </div>

  <%-- 2. Llamamos al Footer global que cierra las etiquetas HTML y de la estructura principal[cite: 1] --%>
  <jsp:include page="../components/footer.jsp" />