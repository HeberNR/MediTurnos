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
      <h2>Mi Portal de Paciente</h2>
    </div>

    <%-- Subtítulo con borde inferior --%>
    <p class="card-subtitle">
      Hola, <strong><%= usuario.getNombre() %> <%= usuario.getApellido() %></strong>. ¿Qué necesitás hacer hoy?
    </p>

    <%-- Sistema de alertas para mostrar éxito o error al solicitar/cancelar turnos --%>
    <% if (request.getAttribute("mensaje") != null) { %>
    <div class="alert alert-success"><%= request.getAttribute("mensaje") %></div>
    <% } %>
    <% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-error"><%= request.getAttribute("error") %></div>
    <% } %>

    <%-- Grilla responsiva para las opciones --%>
    <div class="dashboard-grid">

      <!-- Opción 1: Solicitar Turno -->
      <div class="dashboard-card">
        <h3> Nuevo Turno</h3>
        <p>Agendá una nueva consulta presencial con nuestros doctores.</p>
        <a href="<%= request.getContextPath() %>/turnos/solicitar" class="btn">Agendar Turno</a>
      </div>

      <!-- Opción 2: Mis Turnos -->
      <div class="dashboard-card">
        <h3> Mis Turnos</h3>
        <p>Revisá tus turnos pendientes o cancelá una cita.</p>
        <a href="<%= request.getContextPath() %>/turnos/mis-turnos" class="btn btn-outline">Ver Historial</a>
      </div>

    </div>
  </div>
</div>

<jsp:include page="../components/footer.jsp" />