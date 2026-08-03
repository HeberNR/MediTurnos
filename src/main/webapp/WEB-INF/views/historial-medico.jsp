<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Turno" %>

<%-- Incluimos el header global --%>
<jsp:include page="../components/header.jsp" />

<%-- Contenedor de ancho grande (1100px) --%>
<div class="container-large">

  <%-- Tarjeta grande que ocupa todo el ancho del contenedor --%>
  <div class="card card-large">

    <%-- Cabecera con título alineado a la izquierda y botón de retorno a la derecha --%>
    <div class="card-header">
      <h2>Historial Clínico de Pacientes</h2>
      <a href="<%= request.getContextPath() %>/dashboard" class="btn btn-outline">Volver al Panel</a>
    </div>

    <%-- Subtítulo descriptivo con línea divisoria --%>
    <p class="card-subtitle">
      Acá podés revisar todas las consultas finalizadas, los diagnósticos y las observaciones de tus pacientes.
    </p>

    <%-- Contenedor con scroll horizontal para tablas responsivas --%>
    <div class="table-container">
      <table class="table">
        <thead>
        <tr>
          <th>Fecha</th>
          <th>Paciente</th>
          <th>Motivo Inicial</th>
          <th>Diagnóstico Médico</th>
          <th>Observaciones</th>
        </tr>
        </thead>
        <tbody>
        <%
        List<Turno> historial = (List<Turno>) request.getAttribute("historial");
          if (historial != null && !historial.isEmpty()) {
          for (Turno t : historial) {
          %>
          <tr>
            <td style="white-space: nowrap;">
              <strong><%= t.getFechaTurno() %></strong><br>
              <span class="text-secondary text-small"><%= t.getHoraTurno() %></span>
            </td>
            <td>
              <strong><%= t.getPacienteNombreCompleto() %></strong><br>
              <span class="text-secondary text-small">DNI: <%= t.getPacienteDni() %></span>
            </td>
            <td class="text-secondary" style="max-width: 150px;">
              <%= t.getMotivoConsulta() != null ? t.getMotivoConsulta() : "-" %>
            </td>
            <%-- Usamos variables de texto de éxito para los diagnósticos médicos --%>
            <td style="color: var(--success-text); font-weight: 500; max-width: 200px;">
              <%= t.getDiagnostico() != null ? t.getDiagnostico() : "Sin diagnóstico" %>
            </td>
            <td class="text-small" style="max-width: 250px;">
              <%= t.getObservaciones() != null && !t.getObservaciones().trim().isEmpty() ? t.getObservaciones() : "-" %>
            </td>
          </tr>
          <%
          }
          } else {
          %>
          <tr>
            <td colspan="5" style="padding: 2rem; text-align: center;" class="text-secondary">
              Aún no tenés consultas médicas finalizadas en tu historial.
            </td>
          </tr>
          <% } %>
        </tbody>
      </table>
    </div>
  </div>
</div>

<%-- Incluimos el footer global --%>
<jsp:include page="../components/footer.jsp" />