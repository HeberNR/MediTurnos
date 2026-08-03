<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Turno" %>

<%
String vistaActual = (String) request.getAttribute("vistaActual");
Integer currentPage = (Integer) request.getAttribute("currentPage");
Integer totalPages = (Integer) request.getAttribute("totalPages");

if (currentPage == null) currentPage = 1;
if (totalPages == null) totalPages = 1;
%>

<jsp:include page="../components/header.jsp" />

<div style="max-width: 1000px; margin: 2rem auto; padding: 0 1rem; width: 100%;">
  <div class="card" style="max-width: 100%;">

    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
      <h2 style="margin: 0; text-align: left;">Mi Portal de Turnos</h2>
      <a href="<%= request.getContextPath() %>/turnos/solicitar" class="btn">+ Nuevo Turno</a>
    </div>

    <!-- Pestañas de Filtro para Pacientes -->
    <div style="display: flex; gap: 10px; margin-bottom: 1.5rem; border-bottom: 1px solid var(--border-color); padding-bottom: 1rem;">
      <a href="<%= request.getContextPath() %>/turnos/mis-turnos?vista=pendientes" class="btn <%= "pendientes".equals(vistaActual) ? "" : "btn-outline" %>" style="padding: 0.5rem 1rem;">Próximos Turnos</a>
      <a href="<%= request.getContextPath() %>/turnos/mis-turnos?vista=historial" class="btn <%= "historial".equals(vistaActual) ? "" : "btn-outline" %>" style="padding: 0.5rem 1rem;">Mi Historial Clínico</a>
    </div>

    <% if (request.getAttribute("mensajeExito") != null) { %>
    <div class="alert alert-success"><%= request.getAttribute("mensajeExito") %></div>
    <% } %>
    <% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-error"><%= request.getAttribute("error") %></div>
    <% } %>

    <div style="overflow-x: auto;">
      <table style="width: 100%; border-collapse: collapse; text-align: left;">
        <thead>
        <tr style="border-bottom: 2px solid var(--border-color); color: var(--text-secondary);">
          <th style="padding: 1rem;">Fecha</th>
          <th style="padding: 1rem;">Profesional</th>
          <th style="padding: 1rem;">Estado</th>
          <% if ("historial".equals(vistaActual)) { %>
          <th style="padding: 1rem;">Diagnóstico y Observaciones</th>
          <% } else { %>
          <th style="padding: 1rem; text-align: center;">Acción</th>
          <% } %>
        </tr>
        </thead>
        <tbody>
        <%
        List<Turno> turnos = (List<Turno>) request.getAttribute("turnos");
          if (turnos != null && !turnos.isEmpty()) {
          for (Turno t : turnos) {
          String badgeColor = t.getEstado().equalsIgnoreCase("pendiente") ? "#f39c12" :
          t.getEstado().equalsIgnoreCase("cancelado") ? "var(--error-text)" :
          t.getEstado().equalsIgnoreCase("ausente") ? "var(--error-text)" : "var(--success-text)";
          %>
          <tr style="border-bottom: 1px solid var(--border-color); vertical-align: top;">
            <td style="padding: 1rem; white-space: nowrap;">
              <strong><%= t.getFechaTurno() %></strong><br>
              <span style="color: var(--text-secondary); font-size: 0.85rem;"><%= t.getHoraTurno() %></span>
            </td>
            <td style="padding: 1rem;">
              <strong>Dr/a. <%= t.getDoctorNombreCompleto() %></strong><br>
              <span style="color: var(--text-secondary); font-size: 0.85rem;"><%= t.getEspecialidad() != null ? t.getEspecialidad() : "General" %></span>
            </td>
            <td style="padding: 1rem;">
              <span style="font-weight: bold; color: <%= badgeColor %>; text-transform: capitalize;"><%= t.getEstado() %></span>
            </td>

            <% if ("historial".equals(vistaActual)) { %>
            <!-- Vista del Historial (Diagnóstico) -->
            <td style="padding: 1rem; font-size: 0.9rem;">
              <% if (t.getEstado().equalsIgnoreCase("atendido")) { %>
              <strong>Diag:</strong> <span style="color: var(--success-text);"><%= t.getDiagnostico() != null ? t.getDiagnostico() : "Sin diagnóstico" %></span>
              <% if (t.getObservaciones() != null && !t.getObservaciones().trim().isEmpty()) { %>
              <br><span style="color: var(--text-secondary);"><strong>Obs:</strong> <%= t.getObservaciones() %></span>
              <% } %>
              <% } else { %>
              <span style="color: var(--text-secondary);">-</span>
              <% } %>
            </td>
            <% } else { %>
            <!-- Vista de Pendientes (Botón Cancelar) -->
            <td style="padding: 1rem; text-align: center;">
              <form action="<%= request.getContextPath() %>/turnos/mis-turnos" method="post" style="margin: 0;">
                <input type="hidden" name="turnoId" value="<%= t.getId() %>">
                <button type="submit" class="btn btn-outline" style="padding: 0.4rem 0.8rem; font-size: 0.85rem; color: var(--error-text); border-color: var(--error-text);" onclick="return confirm('¿Seguro que querés cancelar este turno?')">Cancelar Turno</button>
              </form>
            </td>
            <% } %>

          </tr>
          <%
          }
          } else {
          %>
          <tr>
            <td colspan="4" style="padding: 2rem; text-align: center; color: var(--text-secondary);">No hay turnos para mostrar en esta sección.</td>
          </tr>
          <% } %>
        </tbody>
      </table>
    </div>

    <!-- Paginación (Carrusel) -->
    <% if (totalPages > 1) { %>
    <div style="display: flex; justify-content: center; align-items: center; gap: 15px; margin-top: 1.5rem; padding-top: 1.5rem; border-top: 1px solid var(--border-color);">

      <% if (currentPage > 1) { %>
      <a href="<%= request.getContextPath() %>/turnos/mis-turnos?vista=<%= vistaActual %>&page=<%= currentPage - 1 %>" class="btn btn-outline" style="padding: 0.4rem 1rem;">Anterior</a>
      <% } else { %>
      <span class="btn btn-outline" style="padding: 0.4rem 1rem; opacity: 0.5; cursor: not-allowed;">Anterior</span>
      <% } %>

      <span style="color: var(--text-primary); font-weight: 500;">Página <%= currentPage %> de <%= totalPages %></span>

      <% if (currentPage < totalPages) { %>
      <a href="<%= request.getContextPath() %>/turnos/mis-turnos?vista=<%= vistaActual %>&page=<%= currentPage + 1 %>" class="btn btn-outline" style="padding: 0.4rem 1rem;">Siguiente</a>
      <% } else { %>
      <span class="btn btn-outline" style="padding: 0.4rem 1rem; opacity: 0.5; cursor: not-allowed;">Siguiente</span>
      <% } %>

    </div>
    <% } %>

  </div>
</div>

<jsp:include page="../components/footer.jsp" />