<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Usuario" %>
<% Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado"); %>

<jsp:include page="../components/header.jsp" />

<div style="max-width: 800px; margin: 2rem auto; padding: 0 1rem; width: 100%;">
  <div class="card" style="max-width: 100%;">
    <h2 style="text-align: left; margin-bottom: 0.5rem;">Portal Médico</h2>
    <p style="color: var(--text-secondary); margin-bottom: 2rem; border-bottom: 1px solid var(--border-color); padding-bottom: 1rem;">
      Bienvenido/a, <strong>Dr/a. <%= usuario.getNombre() %> <%= usuario.getApellido() %></strong>.
    </p>

    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 1.5rem;">

      <!-- Tarjeta de Agenda Diaria -->
      <div style="border: 1px solid var(--border-color); border-radius: 8px; padding: 1.5rem; text-align: center; background-color: var(--bg-color);">
        <h3 style="margin-bottom: 1rem; font-size: 1.2rem;">Mi Agenda</h3>
        <p style="color: var(--text-secondary); font-size: 0.9rem; margin-bottom: 1rem;">Revisá los turnos del día, agendá pacientes presenciales y cargá diagnósticos.</p>
        <a href="<%= request.getContextPath() %>/turnos/agenda" class="btn" style="display: inline-block;">Ver Pacientes</a>
      </div>

      <!-- Tarjeta NUEVA de Historial Médico -->
      <div style="border: 1px solid var(--border-color); border-radius: 8px; padding: 1.5rem; text-align: center; background-color: var(--bg-color);">
        <h3 style="margin-bottom: 1rem; font-size: 1.2rem;">Historial Clínico</h3>
        <p style="color: var(--text-secondary); font-size: 0.9rem; margin-bottom: 1rem;">Consultá el registro de consultas pasadas y diagnósticos de pacientes atendidos.</p>
        <a href="<%= request.getContextPath() %>/turnos/historial" class="btn btn-outline" style="display: inline-block;">Ver Historial</a>
      </div>

    </div>
  </div>
</div>

<jsp:include page="../components/footer.jsp" />