<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Usuario" %>
<%@ page import="enums.Especialidad" %>

<%-- Incluimos el header global --%>
<jsp:include page="../components/header.jsp" />

<%-- Tarjeta de tamaño medio expandida a 600px para albergar el formulario de turnos con comodidad --%>
<div class="card" style="max-width: 600px;">
  <h2>Solicitar Nuevo Turno</h2>
  <p class="card-subtitle" style="text-align: center; border-bottom: none; margin-bottom: 2rem;">
    Completá los datos para agendar tu consulta presencial.
  </p>

  <%-- Alertas del sistema en caso de errores --%>
  <% if (request.getAttribute("error") != null) { %>
  <div class="alert alert-error"><%= request.getAttribute("error") %></div>
  <% } %>

  <form action="<%= request.getContextPath() %>/turnos/solicitar" method="post">

    <!-- Selector de Especialidad -->
    <div class="input-group">
      <label for="especialidad">1. Seleccioná la Especialidad</label>
      <%-- El select hereda automáticamente los estilos globales de formulario --%>
      <select id="especialidad" onchange="filtrarDoctores()">
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
      <select id="doctorId" name="doctorId" required>
        <option value="" disabled selected>Seleccioná un profesional...</option>
        <!-- Las opciones se cargan dinámicamente mediante JavaScript -->
      </select>
    </div>

    <%-- Fila flexible para alinear la fecha y la hora una al lado de la otra --%>
    <div class="flex-row" style="gap: 15px; margin-bottom: 1.2rem;">
      <div class="input-group" style="flex: 1; margin-bottom: 0;">
        <label for="fechaTurno">3. Fecha de la Consulta</label>
        <input type="date" id="fechaTurno" name="fechaTurno" min="<%= request.getAttribute("fechaHoy") %>" required>
      </div>
      <div class="input-group" style="flex: 1; margin-bottom: 0;">
        <label for="horaTurno">4. Hora (Aprox)</label>
        <input type="time" id="horaTurno" name="horaTurno" required>
      </div>
    </div>

    <!-- Motivo de la Consulta -->
    <div class="input-group">
      <label for="motivoConsulta">Motivo de Consulta (Breve)</label>
      <textarea id="motivoConsulta" name="motivoConsulta" rows="3" required style="resize: none;"></textarea>
    </div>

    <%-- Botones de acción inferior alineados simétricamente --%>
    <div class="flex-row mt-1" style="gap: 10px;">
      <a href="<%= request.getContextPath() %>/dashboard" class="btn btn-outline" style="flex: 1; text-align: center;">Cancelar</a>
      <button type="submit" class="btn" style="flex: 1;">Confirmar Turno</button>
    </div>

  </form>
</div>

<%-- Script de lógica para filtrar doctores en tiempo real sin recargar la página --%>
<script>
    // Armamos el arreglo asegurándonos de que si la especialidad es null no rompa JS
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

    // Función para ignorar tildes, mayúsculas y espacios invisibles
    function normalizarTexto(texto) {
        if (!texto) return "";
        return texto.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLowerCase().trim();
    }

    function filtrarDoctores() {
        const espSeleccionada = document.getElementById("especialidad").value;
        const selectDoctor = document.getElementById("doctorId");

        // Limpiamos la lista actual
        selectDoctor.innerHTML = '<option value="" disabled selected>Seleccioná un profesional...</option>';

        // Filtramos usando la normalización
        const filtrados = espSeleccionada
            ? doctores.filter(d => normalizarTexto(d.especialidad) === normalizarTexto(espSeleccionada))
            : doctores;

        // Agregamos los filtrados al menú desplegable
        filtrados.forEach(doc => {
            const option = document.createElement("option");
            option.value = doc.id;
            option.textContent = doc.nombre + " (" + doc.especialidad + ")";
            selectDoctor.appendChild(option);
        });
    }

    // Ejecutamos la función apenas carga la página
    window.onload = filtrarDoctores;
</script>

<%-- Incluimos el footer global --%>
<jsp:include page="../components/footer.jsp" />