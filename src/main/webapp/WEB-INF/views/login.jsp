<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- 1. Llamamos al Header dinámico -->
<jsp:include page="../components/header.jsp" />

<div class="card">
  <h2>Iniciar Sesión</h2>

  <!-- Manejo de Alertas -->
  <% if (request.getAttribute("error") != null) { %>
  <div class="alert alert-error"><%= request.getAttribute("error") %></div>
  <% } %>
  <% if (request.getAttribute("mensajeExito") != null) { %>
  <div class="alert alert-success"><%= request.getAttribute("mensajeExito") %></div>
  <% } %>

  <form action="<%= request.getContextPath() %>/login" method="post">
    <div class="input-group">
      <label for="email">Correo Electrónico</label>
      <input type="email" id="email" name="email" required placeholder="tu@email.com">
    </div>
    <div class="input-group">
      <label for="password">Contraseña</label>
      <input type="password" id="password" name="password" required placeholder="••••••••">
    </div>
    <button type="submit" class="btn" style="width: 100%; margin-top: 10px;">Ingresar al sistema</button>
  </form>
</div>

<!-- 2. Llamamos al Footer -->
<jsp:include page="../components/footer.jsp" />