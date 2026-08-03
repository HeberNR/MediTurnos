<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Turno" %>
<%@ page import="models.Usuario" %>
<%@ page import="enums.Especialidad" %>

<%-- Incluimos el header global --%>
<jsp:include page="../components/header.jsp" />

<%-- Contenedor de ancho grande (1100px) --%>
<div class="container-large">

  <%-- TARJETA 1: Formulario para agendar turno como Administrador --%>
  <div class="card card-large mb-1" style="margin-bottom: 2rem;">
    <div class="card-header">
      <h2>Agendar Nuevo Turno (Administración)</h2>
    </div>
    <p class="card-subtitle">
      Seleccioná la especialidad, el profesional y completá los datos del paciente. Si el DNI no existe, se registrará automáticamente.
    </p>

    <%-- Alertas del sistema --%>
    <% if (request.getAttribute("errorAdmin") != null) { %>
    <div class="alert alert-error"><%= request.getAttribute("errorAdmin") %></div>
    <% } %>
    <% if (request.getAttribute("exitoAdmin") != null) { %>
    <div class="alert alert-success"><%= request.getAttribute("exitoAdmin") %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/admin/turnos" method="post" class="form-row">
      <input type="hidden" name="accion" value="crear_turno">

      <!-- 1. Selector de Especialidad -->
      <div class="input-group" style="flex: 1; min-width: 200px;">
        <label for="especialidadFiltro">Especialidad Médica</label>
        <select id="especialidadFiltro" onchange="filtrarDoctoresAdmin()">
          <option value="" selected>Todas las especialidades</option>
          <% for (Especialidad esp : Especialidad.values()) { %>
          <option value="<%= esp.getNombre() %>"><%= esp.getNombre() %></option>
          <% } %>
        </select>
      </div>

      <!-- 2. Selector de Doctor (Filtrado por JS) -->
      <div class="input-group" style="flex: 1; min-width: 200px;">
        <label for="doctorId">Profesional Asignado</label>
        <select id="doctorId" name="doctorId" required>
          <option value="" disabled selected>Seleccioná un profesional...</option>
        </select>
      </div>

      <!-- 3. Datos del Paciente -->
      <div class="input-group" style="flex: 1; min-width: 130px;">
        <label for="dni">DNI del Paciente</label>
        <input type="text" id="dni" name="dni" required placeholder="Sin puntos">
      </div>
      <div class="input-group" style="flex: 1; min-width: 140px;">
        <label for="nombre">Nombre</label>
        <input type="text" id="nombre" name="nombre" required>
      </div>
      <div class="input-group" style="flex: 1; min-width: 140px;">
        <label for="apellido">Apellido</label>
        <input type="text" id="apellido" name="apellido" required>
      </div>

      <!-- 4. Fecha y Hora -->
      <div class="input-group" style="flex: 1; min-width: 130px;">
        <label for="fechaTurno">Fecha</label>
        <input type="date" id="fechaTurno" name="fechaTurno" min="<%= request.getAttribute("fechaHoy") %>" required>
      </div>
      <div class="input-group" style="flex: 1; min-width: 100px;">
        <label for="horaTurno">Hora</label>
        <input type="time" id="horaTurno" name="horaTurno" required>
      </div>

      <!-- 5. Motivo -->
      <div class="input-group" style="flex: 2; min-width: 220px;">
        <label for="motivoConsulta">Motivo (Breve)</label>
        <input type="text" id="motivoConsulta" name="motivoConsulta" placeholder="Ej: Control general...">
      </div>

      <button type="submit" class="btn" style="height: 100%;">Asignar Turno</button>
    </form>
  </div>

  <%-- TARJETA 2: Auditoría General de Turnos (La tabla que ya tenías) --%>
  <div class="card card-large">
    <div class="card-header">
      <h2>Auditoría General de Turnos</h2>
      <a href="<%= request.getContextPath() %>/dashboard" class="btn btn-outline">Volver al Panel</a>
    </div>

    <p class="card-subtitle">
      Listado completo de citas registradas en el sistema de la clínica.
    </p>

    <div class="table-container">
      <table class="table">
        <thead>
        <tr>
          <th>Fecha y Hora</th>
          <th>Paciente</th>
          <th>Profesional Asignado</th>
          <th>Motivo</th>
          <th>Estado del Turno</th>
        </tr>
        </thead>
        <tbody>
        <%
        List<Turno> turnos = (List<Turno>) request.getAttribute("turnos");
          if (turnos != null && !turnos.isEmpty()) {
          for (Turno t : turnos) {
          %>
          <tr>
            <td>
              <strong><%= t.getFechaTurno() %></strong><br>
              <span class="text-secondary text-small"><%= t.getHoraTurno() %></span>
            </td>
            <td>
              <strong><%= t.getPacienteNombreCompleto() %></strong><br>
              <span class="text-secondary text-small">DNI: <%= t.getPacienteDni() %></span>
            </td>
            <td>
              Dr/a. <%= t.getDoctorNombreCompleto() %><br>
              <span class="text-secondary text-small"><%= t.getEspecialidad() %></span>
            </td>
            <td class="text-secondary">
              <%= t.getMotivoConsulta() != null ? t.getMotivoConsulta() : "-" %>
            </td>
            <td>
              <span class="badge badge-<%= t.getEstado().toLowerCase() %>">
                <%= t.getEstado() %>
              </span>
            </td>
          </tr>
          <%
          }
          } else {
          %>
          <tr>
            <td colspan="5" style="text-align: center; padding: 2rem;" class="text-secondary">
              No hay turnos registrados en la clínica.
            </td>
          </tr>
          <% } %>
        </tbody>
      </table>
    </div>
  </div>
</div>

<%-- SCRIPT DE FILTRADO DE DOCTORES PARA EL ADMIN --%>
<script>
    const doctores = [
        <%
        List<Usuario> doctores = (List<Usuario>) request.getAttribute("doctores");
    if (doctores != null) {
        for (Usuario doc : doctores) {
        %>
            {
                id: <%= doc.getId() %>,
                nombre: "Dr/a. <%= doc.getNombre() %> <%= doc.getApellido() %>",
                especialidad: "<%= doc.getEspecialidadNombre() != null ? doc.getEspecialidadNombre() : "" %>"
            },
            <%
        }
    }
    %>
    ];

    function normalizarTexto(texto) {
        if (!texto) return "";
        return texto.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLowerCase().trim();
    }

    function filtrarDoctoresAdmin() {
        const espSeleccionada = document.getElementById("especialidadFiltro").value;
        const selectDoctor = document.getElementById("doctorId");

        selectDoctor.innerHTML = '<option value="" disabled selected>Seleccioná un profesional...</option>';

        const filtrados = espSeleccionada
            ? doctores.filter(d => normalizarTexto(d.especialidad) === normalizarTexto(espSeleccionada))
            : doctores;

        filtrados.forEach(doc => {
            const option = document.createElement("option");
            option.value = doc.id;
            option.textContent = doc.nombre + " (" + doc.especialidad + ")";
            selectDoctor.appendChild(option);
        });
    }

    window.onload = function() {
        filtrarDoctoresAdmin();
    };
</script>

<%-- Incluimos el pie de página global --%>
<jsp:include page="../components/footer.jsp" />