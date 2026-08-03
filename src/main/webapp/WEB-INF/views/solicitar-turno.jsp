<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Usuario" %>
<%@ page import="enums.Especialidad" %>

<jsp:include page="../components/header.jsp" />

<div class="card" style="max-width: 600px; margin: 2rem auto;">
  <h2>Solicitar Nuevo Turno</h2>
  <p style="text-align: center; color: var(--text-secondary); margin-bottom: 2rem;">Completá los datos para agendar tu consulta presencial.</p>

  <% if (request.getAttribute("error") != null) { %>
  <div class="alert alert-error"><%= request.getAttribute("error") %></div>
  <% } %>

  <form action="<%= request.getContextPath() %>/turnos/solicitar" method="post">

    <!-- Selector de Especialidad -->
    <div class="input-group">
      <label for="especialidad">1. Seleccioná la Especialidad</label>
      <select id="especialidad" onchange="filtrarDoctores()" style="padding: 0.8rem; border: 1px solid var(--border-color); border-radius: 6px; background-color: var(--bg-color); color: var(--text-primary);">
        <option value="" selected>Todas las especialidades</option>
        <%
        for (Especialidad esp : Especialidad.values()) {
        %>
        <option value="<%= esp.getNombre() %>"><%= esp.getNombre() %></option>
        <% } %>
      </select>
    </div>

    <!-- Selector de Doctor -->
    <div class="input-group">
      <label for="doctorId">2. Seleccioná el Profesional</label>
      <select id="doctorId" name="doctorId" required style="padding: 0.8rem; border: 1px solid var(--border-color); border-radius: 6px; background-color: var(--bg-color); color: var(--text-primary);">
        <option value="" disabled selected>Seleccioná un profesional...</option>
        <!-- Las opciones se van a cargar mediante JavaScript -->
      </select>
    </div>

    <div style="display: flex; gap: 15px;">
      <div class="input-group" style="flex: 1;">
        <label for="fechaTurno">3. Fecha de la Consulta</label>
        <!-- NUEVO: Atributo MIN para bloquear fechas pasadas -->
        <input type="date" id="fechaTurno" name="fechaTurno" min="<%= request.getAttribute("fechaHoy") %>" required>
      </div>
      <div class="input-group" style="flex: 1;">
        <label for="horaTurno">4. Hora (Aprox)</label>
        <input type="time" id="horaTurno" name="horaTurno" required>
      </div>
    </div>

    <div class="input-group">
      <label for="motivoConsulta">Motivo de Consulta (Breve)</label>
      <textarea id="motivoConsulta" name="motivoConsulta" rows="3" required style="padding: 0.8rem; border: 1px solid var(--border-color); border-radius: 6px; background-color: var(--bg-color); color: var(--text-primary); resize: none;"></textarea>
    </div>

    <div style="display: flex; gap: 10px; margin-top: 1rem;">
      <a href="<%= request.getContextPath() %>/dashboard" class="btn btn-outline" style="flex: 1; text-align: center;">Cancelar</a>
      <button type="submit" class="btn" style="flex: 1;">Confirmar Turno</button>
    </div>
  </form>
</div>

<!-- SCRIPT MAGICO: Filtra doctores sin recargar la página -->
<script>
    // Armamos un arreglo en JS con los datos que nos mandó Java
    const doctores = [
        <%
    List<Usuario> doctores = (List<Usuario>) request.getAttribute("doctores");
    if (doctores != null) {
        for (Usuario doc : doctores) {
        %>
            { id: <%= doc.getId() %>, nombre: "Dr/a. <%= doc.getNombre() %> <%= doc.getApellido() %>", especialidad: "<%= doc.getEspecialidadNombre() %>" },
            <%
        }
    }
    %>
    ];

    function filtrarDoctores() {
        const espSeleccionada = document.getElementById("especialidad").value;
        const selectDoctor = document.getElementById("doctorId");

        // Limpiamos la lista actual
        selectDoctor.innerHTML = '<option value="" disabled selected>Seleccioná un profesional...</option>';

        // Filtramos los doctores según lo que eligió (o mostramos todos si no eligió nada)
        const filtrados = espSeleccionada ? doctores.filter(d => d.especialidad === espSeleccionada) : doctores;

        // Agregamos los filtrados al menú
        filtrados.forEach(doc => {
            const option = document.createElement("option");
            option.value = doc.id;
            option.textContent = doc.nombre + " (" + doc.especialidad + ")";
            selectDoctor.appendChild(option);
        });
    }

    // Ejecutamos la función apenas carga la página para que se llene la lista inicial
    window.onload = filtrarDoctores;
</script>

<jsp:include page="../components/footer.jsp" />