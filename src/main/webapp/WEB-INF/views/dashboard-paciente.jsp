<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Usuario" %>
<%
Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
%>

<jsp:include page="../components/header.jsp" />

<div style="max-width: 800px; margin: 2rem auto; padding: 0 1rem; width: 100%;">
  <div class="card" style="max-width: 100%;">
    <h2 style="text-align: left; margin-bottom: 0.5rem;">Mi Portal de Paciente</h2>

    <p style="color: var(--text-secondary); margin-bottom: 2rem; border-bottom: 1px solid var(--border-color); padding-bottom: 1rem;">
      Hola, <strong><%= usuario.getNombre() %> <%= usuario.getApellido() %></strong>. ¿Qué necesitás hacer hoy?
    </p>

    <!-- Mensajes de éxito al solicitar o cancelar un turno -->
    <% if (request.getAttribute("mensaje") != null) { %>
    <div class="alert alert-success"><%= request.getAttribute("mensaje") %></div>
    <% } %>
    <% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-error"><%= request.getAttribute("error") %></div>
    <% } %>

    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 1.5rem;">

      <!-- Opción 1: Solicitar Turno -->
      <div style="border: 1px solid var(--border-color); border-radius: 8px; padding: 1.5rem; text-align: center; background-color: var(--bg-color);">
        <h3 style="margin-bottom: 1rem; font-size: 1.2rem;">➕ Nuevo Turno</h3>
        <p style="color: var(--text-secondary); font-size: 0.9rem; margin-bottom: 1rem;">Agenda una nueva consulta presencial con nuestros doctores.</p>
        <a href="<%= request.getContextPath() %>/turnos/solicitar" class="btn" style="display: inline-block;">Agendar Turno</a>
      </div>

      <!-- Opción 2: Mis Turnos -->
      <div style="border: 1px solid var(--border-color); border-radius: 8px; padding: 1.5rem; text-align: center; background-color: var(--bg-color);">
        <h3 style="margin-bottom: 1rem; font-size: 1.2rem;">📅 Mis Turnos</h3>
        <p style="color: var(--text-secondary); font-size: 0.9rem; margin-bottom: 1rem;">Revisá tus turnos pendientes o cancelá una cita.</p>
        <a href="<%= request.getContextPath() %>/turnos/mis-turnos" class="btn btn-outline" style="display: inline-block;">Ver Historial</a>
      </div>

    </div>
  </div>
</div>

<jsp:include page="../components/footer.jsp" />