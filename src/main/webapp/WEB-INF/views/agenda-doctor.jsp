<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Turno" %>

<%
String estadoFiltro = (String) request.getAttribute("estadoFiltro");
Integer currentPage = (Integer) request.getAttribute("currentPage");
Integer totalPages = (Integer) request.getAttribute("totalPages");

if (currentPage == null) currentPage = 1;
if (totalPages == null) totalPages = 1;
%>

<jsp:include page="../components/header.jsp" />

<div style="max-width: 1100px; margin: 2rem auto; padding: 0 1rem; width: 100%;">

  <div class="card" style="max-width: 100%; margin-bottom: 2rem;">
    <h3 style="margin-top: 0; color: var(--accent-color);">Agendar Paciente Presencial</h3>
    <p style="color: var(--text-secondary); font-size: 0.9rem; margin-bottom: 1rem;">
      Ingresá los datos. Si el DNI no existe, el sistema lo registrará automáticamente.
    </p>

    <form action="<%= request.getContextPath() %>/turnos/agenda" method="post" style="display: flex; flex-wrap: wrap; gap: 15px; align-items: flex-end;">
      <input type="hidden" name="accion" value="agendar_presencial">

      <div class="input-group" style="flex: 1; min-width: 110px; margin-bottom: 0;">
        <label for="dni">DNI</label>
        <input type="text" id="dni" name="dni" required placeholder="Sin puntos">
      </div>
      <div class="input-group" style="flex: 1; min-width: 120px; margin-bottom: 0;">
        <label for="nombre">Nombre</label>
        <input type="text" id="nombre" name="nombre" required>
      </div>
      <div class="input-group" style="flex: 1; min-width: 120px; margin-bottom: 0;">
        <label for="apellido">Apellido</label>
        <input type="text" id="apellido" name="apellido" required>
      </div>
      <div class="input-group" style="flex: 1; min-width: 130px; margin-bottom: 0;">
        <label for="fechaTurno">Fecha</label>
        <input type="date" id="fechaTurno" name="fechaTurno" required>
      </div>
      <div class="input-group" style="flex: 1; min-width: 100px; margin-bottom: 0;">
        <label for="horaTurno">Hora</label>
        <input type="time" id="horaTurno" name="horaTurno" required>
      </div>
      <div class="input-group" style="flex: 2; min-width: 180px; margin-bottom: 0;">
        <label for="motivoConsulta">Motivo (Breve)</label>
        <input type="text" id="motivoConsulta" name="motivoConsulta" placeholder="Ej: Control...">
      </div>

      <button type="submit" class="btn" style="padding: 0.8rem 1.5rem; height: 100%;">Asignar Turno</button>
    </form>
  </div>

  <div class="card" style="max-width: 100%;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
      <h2 style="margin: 0; text-align: left;">Mi Agenda de Turnos</h2>
    </div>

    <!-- Pestañas de Filtro -->
    <div style="display: flex; gap: 10px; margin-bottom: 1.5rem; border-bottom: 1px solid var(--border-color); padding-bottom: 1rem;">
      <a href="<%= request.getContextPath() %>/turnos/agenda?estado=pendiente" class="btn <%= "pendiente".equals(estadoFiltro) ? "" : "btn-outline" %>" style="padding: 0.5rem 1rem;">Pendientes</a>
      <a href="<%= request.getContextPath() %>/turnos/agenda?estado=atendido" class="btn <%= "atendido".equals(estadoFiltro) ? "" : "btn-outline" %>" style="padding: 0.5rem 1rem;">Atendidos</a>
      <a href="<%= request.getContextPath() %>/turnos/agenda?estado=ausente" class="btn <%= "ausente".equals(estadoFiltro) ? "" : "btn-outline" %>" style="padding: 0.5rem 1rem;">Ausentes</a>
      <a href="<%= request.getContextPath() %>/turnos/agenda?estado=cancelado" class="btn <%= "cancelado".equals(estadoFiltro) ? "" : "btn-outline" %>" style="padding: 0.5rem 1rem;">Cancelados</a>
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
          <th style="padding: 1rem;">Fecha y Hora</th>
          <th style="padding: 1rem;">Paciente</th>
          <th style="padding: 1rem;">Motivo</th>
          <th style="padding: 1rem;">Estado / Clínica</th>
          <th style="padding: 1rem; text-align: center; width: 250px;">Acciones</th>
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
            <td style="padding: 1rem;">
              <strong><%= t.getFechaTurno() %></strong><br>
              <span style="color: var(--text-secondary);"><%= t.getHoraTurno() %></span>
            </td>
            <td style="padding: 1rem;">
              <%= t.getPacienteNombreCompleto() %><br>
              <small style="color: var(--text-secondary);">DNI: <%= t.getPacienteDni() %></small>
            </td>
            <td style="padding: 1rem; max-width: 150px;">
              <%= t.getMotivoConsulta() != null ? t.getMotivoConsulta() : "-" %>
            </td>
            <td style="padding: 1rem;">
              <span style="font-weight: bold; color: <%= badgeColor %>; text-transform: capitalize;"><%= t.getEstado() %></span>

              <% if (!t.getEstado().equalsIgnoreCase("pendiente")) { %>
              <% if (t.getDiagnostico() != null && !t.getDiagnostico().trim().isEmpty()) { %>
              <br><small style="color: var(--text-secondary); margin-top: 5px; display: block;">
              <strong>Diag:</strong> <%= t.getDiagnostico() %>
            </small>
              <% } %>
              <% if (t.getObservaciones() != null && !t.getObservaciones().trim().isEmpty()) { %>
              <small style="color: var(--text-secondary); margin-top: 2px; display: block;">
                <strong>Obs:</strong> <%= t.getObservaciones() %>
              </small>
              <% } %>
              <% } %>
            </td>
            <td style="padding: 1rem;">
              <% if (t.getEstado().equalsIgnoreCase("pendiente")) { %>
              <div style="display: flex; flex-direction: column; gap: 8px;">

                <!-- Bloque Atender -->
                <form action="<%= request.getContextPath() %>/turnos/agenda" method="post" style="margin: 0; display: flex; flex-direction: column; gap: 5px;">
                  <input type="hidden" name="turnoId" value="<%= t.getId() %>">
                  <input type="hidden" name="accion" value="atender">
                  <input type="text" name="diagnostico" placeholder="Escribir diagnóstico..." required style="padding: 0.4rem; font-size: 0.85rem; border: 1px solid var(--border-color); border-radius: 4px; background: var(--bg-color); color: var(--text-primary);">
                  <input type="text" name="observaciones" placeholder="Observaciones (opcional)" style="padding: 0.4rem; font-size: 0.85rem; border: 1px solid var(--border-color); border-radius: 4px; background: var(--bg-color); color: var(--text-primary);">
                  <button type="submit" class="btn" style="padding: 0.4rem 0.8rem; font-size: 0.85rem; background-color: #27ae60;">Guardar Consulta</button>
                </form>

                <!-- Bloque Ausente y Cancelar (dividen el ancho) -->
                <div style="display: flex; gap: 5px;">
                  <form action="<%= request.getContextPath() %>/turnos/agenda" method="post" style="flex: 1; margin: 0;">
                    <input type="hidden" name="turnoId" value="<%= t.getId() %>">
                    <input type="hidden" name="accion" value="ausente">
                    <button type="submit" class="btn btn-outline" style="width: 100%; padding: 0.4rem 0.5rem; font-size: 0.8rem; color: var(--error-text); border-color: var(--error-text);" onclick="return confirm('¿Confirmar que el paciente no se presentó?')">Ausente</button>
                  </form>

                  <form action="<%= request.getContextPath() %>/turnos/agenda" method="post" style="flex: 1; margin: 0;">
                    <input type="hidden" name="turnoId" value="<%= t.getId() %>">
                    <input type="hidden" name="accion" value="cancelar">
                    <button type="submit" class="btn btn-outline" style="width: 100%; padding: 0.4rem 0.5rem; font-size: 0.8rem; color: #f39c12; border-color: #f39c12;" onclick="return confirm('¿Seguro que querés cancelar este turno?')">Cancelar</button>
                  </form>
                </div>

              </div>
              <% } else { %>
              <span style="color: var(--text-secondary); font-size: 0.85rem; display: block; text-align: center; padding: 1rem 0;">Cita Finalizada</span>
              <% } %>
            </td>
          </tr>
          <%
          }
          } else {
          %>
          <tr>
            <td colspan="5" style="padding: 2rem; text-align: center; color: var(--text-secondary);">No hay turnos agendados en esta categoría.</td>
          </tr>
          <% } %>
        </tbody>
      </table>
    </div>

    <!-- Paginación (Carrusel) -->
    <% if (totalPages > 1) { %>
    <div style="display: flex; justify-content: center; align-items: center; gap: 15px; margin-top: 1.5rem; padding-top: 1.5rem; border-top: 1px solid var(--border-color);">

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