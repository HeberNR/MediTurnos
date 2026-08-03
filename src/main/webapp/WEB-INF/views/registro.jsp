<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="enums.Especialidad" %>

<%-- Incluimos el header global[cite: 1] --%>
<jsp:include page="../components/header.jsp" />

<%-- Tarjeta de registro con ancho máximo extendido a 500px para acomodar mejor los campos --%>
<div class="card" style="max-width: 500px;">
  <h2>Registro de Usuario</h2>

  <%-- Alerta de error en caso de que falle el registro[cite: 1] --%>
  <% if (request.getAttribute("error") != null) { %>
  <div class="alert alert-error"><%= request.getAttribute("error") %></div>
  <% } %>

  <%-- Formulario de registro que envía los datos mediante POST hacia la ruta /registro[cite: 1] --%>
  <form action="<%= request.getContextPath() %>/registro" method="post">

    <%-- Selector de Tipo de Usuario[cite: 1] --%>
    <div class="input-group">
      <label for="rol">Tipo de Usuario</label>
      <%-- El select hereda automáticamente los estilos globales de .input-group select --%>
      <select id="rol" name="rol" onchange="toggleEspecialidad()" required>
        <option value="paciente">Paciente</option>
        <option value="doctor">Doctor</option>
        <option value="admin">Administrador</option>
      </select>
    </div>

    <%--
    CAMPO DE ESPECIALIDAD MÉDICA[cite: 1]
    Por defecto está oculto (display: none). Se muestra dinámicamente mediante JavaScript
    si el usuario selecciona el rol de "Doctor".
    --%>
    <div class="input-group" id="grupoEspecialidad" style="display: none;">
      <label for="especialidad">Especialidad Médica</label>
      <select id="especialidad" name="especialidad">
        <%-- Iteramos sobre los valores del Enum Especialidad[cite: 1] --%>
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

    <%-- Botón de envío con ancho completo y utilidad de margen superior[cite: 1] --%>
    <button type="submit" class="btn mt-1" style="width: 100%;">Registrarse</button>
  </form>
</div>

<%-- Script de JavaScript para controlar la interactividad del formulario[cite: 1] --%>
<script>
    function toggleEspecialidad() {
        const rol = document.getElementById("rol").value;
        const grupo = document.getElementById("grupoEspecialidad");

        // Muestra el selector de especialidad solo si el rol seleccionado es doctor
        if (rol === "doctor") {
            grupo.style.display = "block";
        } else {
            grupo.style.display = "none";
        }
    }
</script>

<%-- Incluimos el footer global[cite: 1] --%>
<jsp:include page="../components/footer.jsp" />