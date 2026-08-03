<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="models.Usuario" %>

<%-- BLOQUE JAVA: Recuperamos el usuario logueado para mostrar su nombre --%>
<% Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado"); %>

<jsp:include page="../components/header.jsp" />

<%-- Contenedor centrado de tamaño medio (800px máximo) --%>
<div class="container-medium">

  <%-- Tarjeta principal expandida al 100% --%>
  <div class="card card-large">

    <%-- Encabezado de la tarjeta --%>
    <div class="card-header">
      <h2>Portal Médico</h2>
    </div>

    <%-- Subtítulo con borde inferior --%>
    <p class="card-subtitle">
      Bienvenido/a, <strong>Dr/a. <%= usuario.getNombre() %> <%= usuario.getApellido() %></strong>.
    </p>

    <%-- Grilla responsiva: Acomoda las tarjetas automáticamente --%>
    <div class="dashboard-grid">

      <!-- Tarjeta de Opción 1: Agenda Diaria -->
      <div class="dashboard-card">
        <h3>Mi Agenda</h3>
        <p>Revisá los turnos del día, agendá pacientes presenciales y cargá diagnósticos.</p>
        <a href="<%= request.getContextPath() %>/turnos/agenda" class="btn">Ver Pacientes</a>
      </div>

      <!-- Tarjeta de Opción 2: Historial Médico -->
      <div class="dashboard-card">
        <h3>Historial Clínico</h3>
        <p>Consultá el registro de consultas pasadas y diagnósticos de pacientes atendidos.</p>
        <%-- Botón con variante outline (borde transparente) --%>
        <a href="<%= request.getContextPath() %>/turnos/historial" class="btn btn-outline">Ver Historial</a>
      </div>

    </div>
  </div>
</div>

<jsp:include page="../components/footer.jsp" />