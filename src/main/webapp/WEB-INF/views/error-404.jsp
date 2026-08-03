<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<jsp:include page="../components/header.jsp" />

<%--
La tarjeta se centra automáticamente en el medio de la pantalla
gracias a la clase .main-content que aplicamos en el header
--%>
<div class="card" style="text-align: center;">

  <%-- Mantenemos este único estilo en línea porque es un diseño tipográfico exclusivo del error --%>
  <h2 style="color: var(--error-text); font-size: 3rem; margin-bottom: 0;">404</h2>

  <h3>Página no encontrada</h3>

  <%-- Usamos las clases utilitarias del CSS para el color de texto y los márgenes --%>
  <p class="text-secondary mt-1 mb-1">
    La ruta a la que intentás acceder no existe o fue movida.
  </p>

  <%-- El botón ya es inline-block por defecto gracias al CSS global --%>
  <a href="<%= request.getContextPath() %>/" class="btn">Volver al Inicio</a>

</div>

<jsp:include page="../components/footer.jsp" />