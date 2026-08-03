<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Turno" %>

<%-- BLOQUE JAVA: Recuperamos la vista actual (si es pendientes o historial) y la paginación --%>
<%
String vistaActual = (String) request.getAttribute("vistaActual");
Integer currentPage = (Integer) request.getAttribute("currentPage");
Integer totalPages = (Integer) request.getAttribute("totalPages");

if (currentPage == null) currentPage = 1;
if (totalPages == null) totalPages = 1;
%>

<jsp:include page="../components/header.jsp" />

<%-- Contenedor de ancho grande (1000px/1100px) --%>
<div class="container-large">
  <div class="card card-large">

    <%-- Cabecera con título y botón de acción principal --%>
    <div class="card-header">
      <h2>Mi Portal de Turnos</h2>
      <a href="<%= request.getContextPath() %>/turnos/solicitar" class="btn">+ Nuevo Turno</a>
    </div>

    <%-- Pestañas de Filtro para Pacientes (Reutilizamos .card-subtitle y .flex-row) --%>
    <div class="card-subtitle flex-row" style="justify-content: flex-start;">
      <a href="<%= request.getContextPath() %>/turnos/mis-turnos?vista=pendientes" class="btn <%= "pendientes".equals(vistaActual) ? "" : "btn-outline" %>">Próximos Turnos</a>
      <a href="<%= request.getContextPath() %>/turnos/mis-turnos?vista=historial" class="btn <%= "historial".equals(vistaActual) ? "" : "btn-outline" %>">Mi Historial Clínico</a>
    </div>

    <%-- Alertas del sistema --%>
    <% if (request.getAttribute("mensajeExito") != null) { %>
    <div class="alert alert-success"><%= request.getAttribute("mensajeExito") %></div>
    <% } %>
    <% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-error"><%= request.getAttribute("error") %></div>
    <% } %>

    <div class="table-container">
      <table class="table">
        <thead>
        <tr>
          <th>Fecha</th>
          <th>Profesional</th>
          <th>Estado</th>
          <% if ("historial".equals(vistaActual)) { %>
          <th>Diagnóstico y Observaciones</th>
          <% } else { %>
          <th style="text-align: center;">Acción</th>
          <% } %>
        </tr>
        </thead>
        <tbody>
        <%
        List<Turno> turnos = (List<Turno>) request.getAttribute("turnos");
          if (turnos != null && !turnos.isEmpty()) {
          for (Turno t : turnos) {
          %>
          <tr>
            <td style="white-space: nowrap;">
              <strong><%= t.getFechaTurno() %></strong><br>
              <span class="text-secondary text-small"><%= t.getHoraTurno() %></span>
            </td>
            <td>
              <strong>Dr/a. <%= t.getDoctorNombreCompleto() %></strong><br>
              <span class="text-secondary text-small"><%= t.getEspecialidad() != null ? t.getEspecialidad() : "General" %></span>
            </td>
            <td>
              <%-- Dinamismo de badges de estado --%>
              <span class="badge badge-<%= t.getEstado().toLowerCase() %>"><%= t.getEstado() %></span>
            </td>

            <% if ("historial".equals(vistaActual)) { %>
            <!-- Vista del Historial (Diagnóstico) -->
            <td class="text-small">
              <% if (t.getEstado().equalsIgnoreCase("atendido")) { %>
              <strong>Diag:</strong> <span style="color: var(--success-text);"><%= t.getDiagnostico() != null ? t.getDiagnostico() : "Sin diagnóstico" %></span>
              <% if (t.getObservaciones() != null && !t.getObservaciones().trim().isEmpty()) { %>
              <br><span class="text-secondary"><strong>Obs:</strong> <%= t.getObservaciones() %></span>
              <% } %>
              <% } else { %>
              <span class="text-secondary">-</span>
              <% } %>
            </td>
            <% } else { %>
            <!-- Vista de Pendientes (Botón Cancelar) -->
            <td style="text-align: center;">
              <form action="<%= request.getContextPath() %>/turnos/mis-turnos" method="post" style="margin: 0;">
                <input type="hidden" name="turnoId" value="<%= t.getId() %>">
                <%-- Usamos nuestra clase .btn-danger-outline definida en el CSS global --%>
                <button type="submit" class="btn btn-danger-outline" style="padding: 0.4rem 0.8rem; font-size: 0.85rem;" onclick="return confirm('¿Seguro que querés cancelar este turno?')">Cancelar Turno</button>
              </form>
            </td>
            <% } %>

          </tr>
          <%
          }
          } else {
          %>
          <tr>
            <td colspan="4" style="padding: 2rem; text-align: center;" class="text-secondary">No hay turnos para mostrar en esta sección.</td>
          </tr>
          <% } %>
        </tbody>
      </table>
    </div>

    <!-- Paginación (Carrusel) -->
    <% if (totalPages > 1) { %>
    <div class="flex-row mt-1" style="align-items: center; padding-top: 1.5rem; border-top: 1px solid var(--border-color);">

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