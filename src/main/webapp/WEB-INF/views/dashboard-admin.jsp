<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="models.Usuario" %>

<%-- BLOQUE JAVA: Recuperamos el usuario logueado para mostrar su nombre --%>
<% Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado"); %>

<jsp:include page="../components/header.jsp" />

<%-- Contenedor centrado de tamaño medio (800px máximo) --%>
<div class="container-medium">

  <%-- Tarjeta principal expandida al 100% del contenedor --%>
  <div class="card card-large">

    <%-- Encabezado de la tarjeta --%>
    <div class="card-header">
      <h2>Panel de Administración</h2>
    </div>

    <%-- Subtítulo con borde inferior y espaciado automático --%>
    <p class="card-subtitle">
      Administrador/a: <strong><%= usuario.getNombre() %> <%= usuario.getApellido() %></strong>.
      <br>
      <%-- Mantenemos el color naranja (código #f39c12) únicamente para resaltar esta advertencia de seguridad --%>
      <small style="color: #f39c12;">🔒 Acceso restringido: Operaciones logísticas (Sin acceso a historiales clínicos).</small>
    </p>

    <%-- Grilla responsiva: Acomoda las tarjetas automáticamente según el tamaño de la pantalla --%>
    <div class="dashboard-grid">

      <%-- Tarjeta de Opción 1 --%>
      <div class="dashboard-card">
        <h3>Supervisión de Turnos</h3>
        <p>Monitoreá todas las citas de la clínica, estados generales y profesionales asignados.</p>
        <a href="<%= request.getContextPath() %>/admin/turnos" class="btn">Ver Agenda General</a>
      </div>

    </div>
  </div>
</div>

<jsp:include page="../components/footer.jsp" />