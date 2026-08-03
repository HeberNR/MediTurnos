<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="enums.Especialidad" %> <!-- IMPORTAMOS EL ENUM -->

<jsp:include page="../components/header.jsp" />

<div class="card" style="max-width: 500px; margin: 2rem auto;">
  <h2>Registro de Usuario</h2>

  <% if (request.getAttribute("error") != null) { %>
  <div class="alert alert-error"><%= request.getAttribute("error") %></div>
  <% } %>

  <form action="<%= request.getContextPath() %>/registro" method="post">

    <div class="input-group">
      <label for="rol">Tipo de Usuario</label>
      <select id="rol" name="rol" onchange="toggleEspecialidad()" required style="padding: 0.8rem; border: 1px solid var(--border-color); border-radius: 6px; background-color: var(--bg-color); color: var(--text-primary);">
        <option value="paciente">Paciente</option>
        <option value="doctor">Doctor</option>
        <option value="admin">Administrador</option>
      </select>
    </div>

    <!-- CAMPO ESPECIALIDAD (Solo visible si es doctor gracias a JS) -->
    <div class="input-group" id="grupoEspecialidad" style="display: none;">
      <label for="especialidad">Especialidad Médica</label>
      <select id="especialidad" name="especialidad" style="padding: 0.8rem; border: 1px solid var(--border-color); border-radius: 6px; background-color: var(--bg-color); color: var(--text-primary);">
        <% for (Especialidad esp : Especialidad.values()) { %>
        <option value="<%= esp.getNombre() %>"><%= esp.getNombre() %></option>
        <% } %>
      </select>
    </div>

    <div class="input-group">
      <label for="nombre">Nombre</label>
      <input type="text" id="nombre" name="nombre" required>
    </div>

    <div class="input-group">
      <label for="apellido">Apellido</label>
      <input type="text" id="apellido" name="apellido" required>
    </div>

    <div class="input-group">
      <label for="dni">DNI</label>
      <input type="text" id="dni" name="dni" required placeholder="Sin puntos">
    </div>

    <div class="input-group">
      <label for="email">Correo Electrónico</label>
      <input type="email" id="email" name="email" required>
    </div>

    <div class="input-group">
      <label for="password">Contraseña</label>
      <input type="password" id="password" name="password" required>
    </div>

    <div class="input-group">
      <label for="telefono">Teléfono (Opcional)</label>
      <input type="text" id="telefono" name="telefono">
    </div>

    <button type="submit" class="btn" style="width: 100%; margin-top: 1rem;">Registrarse</button>
  </form>
</div>

<script>
    function toggleEspecialidad() {
        const rol = document.getElementById("rol").value;
        const grupo = document.getElementById("grupoEspecialidad");
        // Muestra el selector de especialidad solo si el rol es doctor
        if (rol === "doctor") {
            grupo.style.display = "block";
        } else {
            grupo.style.display = "none";
        }
    }
</script>

<jsp:include page="../components/footer.jsp" />