<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Turno" %>

<%-- BLOQUE JAVA: Recuperamos filtros y variables de paginación --%>
<%
String estadoFiltro = (String) request.getAttribute("estadoFiltro");
Integer currentPage = (Integer) request.getAttribute("currentPage");
Integer totalPages = (Integer) request.getAttribute("totalPages");

if (currentPage == null) currentPage = 1;
if (totalPages == null) totalPages = 1;
%>

<jsp:include page="../components/header.jsp" />

<%-- Contenedor global --%>
<div class="container-large">

  <%-- TARJETA 1: Formulario para agendar paciente presencial --%>
  <div class="card card-large mb-1">
    <div class="card-header">
      <h2>Agendar Paciente Presencial</h2>
    </div>
    <p class="card-subtitle">
      Ingresá los datos. Si el DNI no existe, el sistema lo registrará automáticamente.
    </p>

    <%-- Formulario: .form-row hace que los campos se pongan uno al lado del otro --%>
    <form action="<%= request.getContextPath() %>/turnos/agenda" method="post" class="form-row">
      <input type="hidden" name="accion" value="agendar_presencial">

      <div class="input-group">
        <label for="dni">DNI</label>
        <input type="text" id="dni" name="dni" required placeholder="Sin puntos">
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
        <label for="fechaTurno">Fecha</label>
        <input type="date" id="fechaTurno" name="fechaTurno" required>
      </div>
      <div class="input-group">
        <label for="horaTurno">Hora</label>
        <input type="time" id="horaTurno" name="horaTurno" required>
      </div>
      <div class="input-group">
        <label for="motivoConsulta">Motivo (Breve)</label>
        <input type="text" id="motivoConsulta" name="motivoConsulta" placeholder="Ej: Control...">
      </div>

      <button type="submit" class="btn">Asignar Turno</button>
    </form>
  </div>

  <br> <%-- Separación visual entre tarjetas --%>

  <%-- TARJETA 2: Tabla de turnos asignados --%>
  <div class="card card-large">
    <div class="card-header">
      <h2>Mi Agenda de Turnos</h2>
    </div>

    <%-- Pestañas de Filtro (Reutilizamos .card-subtitle y .flex-row para alinear) --%>
    <div class="card-subtitle flex-row" style="justify-content: flex-start;">
      <a href="<%= request.getContextPath() %>/turnos/agenda?estado=pendiente" class="btn <%= "pendiente".equals(estadoFiltro) ? "" : "btn-outline" %>">Pendientes</a>
      <a href="<%= request.getContextPath() %>/turnos/agenda?estado=atendido" class="btn <%= "atendido".equals(estadoFiltro) ? "" : "btn-outline" %>">Atendidos</a>
      <a href="<%= request.getContextPath() %>/turnos/agenda?estado=ausente" class="btn <%= "ausente".equals(estadoFiltro) ? "" : "btn-outline" %>">Ausentes</a>
      <a href="<%= request.getContextPath() %>/turnos/agenda?estado=cancelado" class="btn <%= "cancelado".equals(estadoFiltro) ? "" : "btn-outline" %>">Cancelados</a>
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
          <th>Fecha y Hora</th>
          <th>Paciente</th>
          <th>Motivo</th>
          <th>Estado / Clínica</th>
          <th style="text-align: center; width: 250px;">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <%
        List<Turno> turnos = (List<Turno>) request.getAttribute("turnos");
          if (turnos != null && !turnos.isEmpty()) {
          for (Turno t : turnos) {
          // Eliminamos el String badgeColor = ... ¡El CSS se encarga!
          %>
          <tr>
            <td>
              <strong><%= t.getFechaTurno() %></strong><br>
              <span class="text-secondary text-small"><%= t.getHoraTurno() %></span>
            </td>
            <td>
              <%= t.getPacienteNombreCompleto() %><br>
              <small class="text-secondary">DNI: <%= t.getPacienteDni() %></small>
            </td>
            <td class="text-secondary">
              <%= t.getMotivoConsulta() != null ? t.getMotivoConsulta() : "-" %>
            </td>
            <td>
              <%-- Dinamismo de Badges --%>
              <span class="badge badge-<%= t.getEstado().toLowerCase() %>"><%= t.getEstado() %></span>

              <% if (!t.getEstado().equalsIgnoreCase("pendiente")) { %>
              <% if (t.getDiagnostico() != null && !t.getDiagnostico().trim().isEmpty()) { %>
              <br><small class="text-secondary mt-1" style="display: block;">
              <strong>Diag:</strong> <%= t.getDiagnostico() %>
            </small>
              <% } %>

              <% if (t.getObservaciones() != null && !t.getObservaciones().trim().isEmpty()) { %>
              <small class="text-secondary" style="display: block;">
                <strong>Obs:</strong> <%= t.getObservaciones() %>
              </small>
              <% } %>
              <% } %>
            </td>
            <td>
              <% if (t.getEstado().equalsIgnoreCase("pendiente")) { %>

              <div style="display: flex; flex-direction: column; gap: 8px;">
                <!-- Bloque Atender (Formulario) -->
                <form action="<%= request.getContextPath() %>/turnos/agenda" method="post" style="margin: 0; display: flex; flex-direction: column; gap: 5px;">
                  <input type="hidden" name="turnoId" value="<%= t.getId() %>">
                  <input type="hidden" name="accion" value="atender">

                  <%-- Envolvemos en .input-group para que tomen el diseño general de inputs --%>
                  <div class="input-group" style="margin-bottom: 0;">
                    <input type="text" name="diagnostico" placeholder="Escribir diagnóstico..." required>
                    <input type="text" name="observaciones" placeholder="Observaciones (opcional)" style="margin-top: 5px;">
                  </div>

                  <%-- Botón Verde --%>
                  <button type="submit" class="btn btn-success" style="padding: 0.4rem 0.8rem; font-size: 0.85rem;">Guardar Consulta</button>
                </form>

                <!-- Bloque Ausente y Cancelar (dividen el ancho) -->
                <div style="display: flex; gap: 5px;">
                  <form action="<%= request.getContextPath() %>/turnos/agenda" method="post" style="flex: 1; margin: 0;">
                    <input type="hidden" name="turnoId" value="<%= t.getId() %>">
                    <input type="hidden" name="accion" value="ausente">
                    <%-- Botón Rojo con Borde --%>
                    <button type="submit" class="btn btn-danger-outline" style="width: 100%; padding: 0.4rem 0.5rem; font-size: 0.8rem;" onclick="return confirm('¿Confirmar que el paciente no se presentó?')">Ausente</button>
                  </form>

                  <form action="<%= request.getContextPath() %>/turnos/agenda" method="post" style="flex: 1; margin: 0;">
                    <input type="hidden" name="turnoId" value="<%= t.getId() %>">
                    <input type="hidden" name="accion" value="cancelar">
                    <%-- Botón Naranja con Borde --%>
                    <button type="submit" class="btn btn-warning-outline" style="width: 100%; padding: 0.4rem 0.5rem; font-size: 0.8rem;" onclick="return confirm('¿Seguro que querés cancelar este turno?')">Cancelar</button>
                  </form>
                </div>
              </div>

              <% } else { %>
              <span class="text-secondary text-small" style="display: block; text-align: center; padding: 1rem 0;">Cita Finalizada</span>
              <% } %>
            </td>
          </tr>
          <%
          }
          } else {
          %>
          <tr>
            <td colspan="5" style="padding: 2rem; text-align: center;" class="text-secondary">No hay turnos agendados en esta categoría.</td>
          </tr>
          <% } %>
        </tbody>
      </table>
    </div>

    <!-- Paginación (Carrusel) -->
    <% if (totalPages > 1) { %>
    <div class="flex-row mt-1" style="align-items: center; padding-top: 1.5rem; border-top: 1px solid var(--border-color);">

      <% if (currentPage > 1) { %>
      <a href="<%= request.getContextPath() %>/turnos/agenda?estado=<%= estadoFiltro %>&page=<%= currentPage - 1 %>" class="btn btn-outline" style="padding: 0.4rem 1rem;">Anterior</a>
      <% } else { %>
      <span class="btn btn-outline" style="padding: 0.4rem 1rem; opacity: 0.5; cursor: not-allowed;">Anterior</span>
      <% } %>

      <span style="color: var(--text-primary); font-weight: 500;">Página <%= currentPage %> de <%= totalPages %></span>

      <% if (currentPage < totalPages) { %>
      <a href="<%= request.getContextPath() %>/turnos/agenda?estado=<%= estadoFiltro %>&page=<%= currentPage + 1 %>" class="btn btn-outline" style="padding: 0.4rem 1rem;">Siguiente</a>
      <% } else { %>
      <span class="btn btn-outline" style="padding: 0.4rem 1rem; opacity: 0.5; cursor: not-allowed;">Siguiente</span>
      <% } %>

    </div>
    <% } %>

  </div>
</div>

<jsp:include page="../components/footer.jsp" />