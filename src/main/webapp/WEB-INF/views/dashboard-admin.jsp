<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Usuario" %>
<% Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado"); %>

<jsp:include page="../components/header.jsp" />

<div style="max-width: 800px; margin: 2rem auto; padding: 0 1rem; width: 100%;">
  <div class="card" style="max-width: 100%;">
    <h2 style="text-align: left; margin-bottom: 0.5rem;">Panel de Administración</h2>
    <p style="color: var(--text-secondary); margin-bottom: 2rem; border-bottom: 1px solid var(--border-color); padding-bottom: 1rem;">
      Administrador/a: <strong><%= usuario.getNombre() %> <%= usuario.getApellido() %></strong>.
      <br><small style="color: #f39c12;">🔒 Acceso restringido: Operaciones logísticas (Sin acceso a historiales clínicos).</small>
    </p>

    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 1.5rem;">
      <div style="border: 1px solid var(--border-color); border-radius: 8px; padding: 1.5rem; text-align: center; background-color: var(--bg-color);">
        <h3 style="margin-bottom: 1rem; font-size: 1.2rem;">Supervisión de Turnos</h3>
        <p style="color: var(--text-secondary); font-size: 0.9rem; margin-bottom: 1rem;">Monitoreá todas las citas de la clínica, estados generales y profesionales asignados.</p>
        <a href="<%= request.getContextPath() %>/admin/turnos" class="btn" style="display: inline-block;">Ver Agenda General</a>
      </div>
    </div>
  </div>
</div>

<jsp:include page="../components/footer.jsp" />