<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Turno" %>

<jsp:include page="../components/header.jsp" />

<div style="max-width: 1100px; margin: 2rem auto; padding: 0 1rem; width: 100%;">
  <div class="card" style="max-width: 100%;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
      <h2 style="margin: 0; text-align: left;">Historial Clínico de Pacientes</h2>
      <a href="<%= request.getContextPath() %>/dashboard" class="btn btn-outline">Volver al Panel</a>
    </div>

    <p style="color: var(--text-secondary); margin-bottom: 2rem; border-bottom: 1px solid var(--border-color); padding-bottom: 1rem;">
      Acá podés revisar todas las consultas finalizadas, los diagnósticos y las observaciones de tus pacientes.
    </p>

    <div style="overflow-x: auto;">
      <table style="width: 100%; border-collapse: collapse; text-align: left;">
        <thead>
        <tr style="border-bottom: 2px solid var(--border-color); color: var(--text-secondary);">
          <th style="padding: 1rem;">Fecha</th>
          <th style="padding: 1rem;">Paciente</th>
          <th style="padding: 1rem;">Motivo Inicial</th>
          <th style="padding: 1rem;">Diagnóstico Médico</th>
          <th style="padding: 1rem;">Observaciones</th>
        </tr>
        </thead>
        <tbody>
        <%
        List<Turno> historial = (List<Turno>) request.getAttribute("historial");
          if (historial != null && !historial.isEmpty()) {
          for (Turno t : historial) {
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
            <td style="padding: 1rem; color: var(--text-secondary); max-width: 150px;">
              <%= t.getMotivoConsulta() != null ? t.getMotivoConsulta() : "-" %>
            </td>
            <td style="padding: 1rem; color: var(--success-text); font-weight: 500; max-width: 200px;">
              <%= t.getDiagnostico() != null ? t.getDiagnostico() : "Sin diagnóstico" %>
            </td>
            <td style="padding: 1rem; font-size: 0.9rem; max-width: 250px;">
              <%= t.getObservaciones() != null && !t.getObservaciones().trim().isEmpty() ? t.getObservaciones() : "-" %>
            </td>
          </tr>
          <%
          }
          } else {
          %>
          <tr>
            <td colspan="5" style="padding: 2rem; text-align: center; color: var(--text-secondary);">Aún no tenés consultas médicas finalizadas en tu historial.</td>
          </tr>
          <% } %>
        </tbody>
      </table>
    </div>
  </div>
</div>

<jsp:include page="../components/footer.jsp" />