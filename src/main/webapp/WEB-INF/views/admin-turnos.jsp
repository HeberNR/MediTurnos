<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Turno" %>

<jsp:include page="../components/header.jsp" />

<div style="max-width: 1100px; margin: 2rem auto; padding: 0 1rem; width: 100%;">
  <div class="card" style="max-width: 100%;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
      <h2 style="margin: 0; text-align: left;">Auditoría General de Turnos</h2>
      <a href="<%= request.getContextPath() %>/dashboard" class="btn btn-outline">Volver al Panel</a>
    </div>

    <p style="color: var(--text-secondary); margin-bottom: 2rem; border-bottom: 1px solid var(--border-color); padding-bottom: 1rem;">
      Listado completo de citas registradas en el sistema de la clínica.
    </p>

    <div style="overflow-x: auto;">
      <table style="width: 100%; border-collapse: collapse; text-align: left;">
        <thead>
        <tr style="border-bottom: 2px solid var(--border-color); color: var(--text-secondary);">
          <th style="padding: 1rem;">Fecha y Hora</th>
          <th style="padding: 1rem;">Paciente</th>
          <th style="padding: 1rem;">Profesional Asignado</th>
          <th style="padding: 1rem;">Motivo</th>
          <th style="padding: 1rem;">Estado del Turno</th>
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
              <strong><%= t.getPacienteNombreCompleto() %></strong><br>
              <span style="color: var(--text-secondary); font-size: 0.85rem;">DNI: <%= t.getPacienteDni() %></span>
            </td>
            <td style="padding: 1rem;">
              Dr/a. <%= t.getDoctorNombreCompleto() %><br>
              <span style="color: var(--text-secondary); font-size: 0.85rem;"><%= t.getEspecialidad() %></span>
            </td>
            <td style="padding: 1rem; color: var(--text-secondary); max-width: 150px;">
              <%= t.getMotivoConsulta() != null ? t.getMotivoConsulta() : "-" %>
            </td>
            <td style="padding: 1rem;">
              <span style="font-weight: bold; color: <%= badgeColor %>; text-transform: capitalize;"><%= t.getEstado() %></span>
            </td>
          </tr>
          <%
          }
          } else {
          %>
          <tr>
            <td colspan="5" style="padding: 2rem; text-align: center; color: var(--text-secondary);">No hay turnos registrados en la clínica.</td>
          </tr>
          <% } %>
        </tbody>
      </table>
    </div>
  </div>
</div>

<jsp:include page="../components/footer.jsp" />