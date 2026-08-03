<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<jsp:include page="../components/header.jsp" />

<div class="card" style="text-align: center;">

  <%-- Mantenemos el estilo tipográfico específico para el número de error --%>
  <h2 style="color: var(--error-text); font-size: 3rem; margin-bottom: 0;">500</h2>

  <h3>Error interno del servidor</h3>

  <%-- Usamos las clases utilitarias del CSS global --%>
  <p class="text-secondary mt-1 mb-1">
    Ocurrió un imprevisto procesando tu solicitud. Por favor, intententalo de nuevo más tarde.
  </p>

  <%-- El botón ya tiene su comportamiento estructurado en el CSS --%>
  <a href="<%= request.getContextPath() %>/" class="btn">Volver al Inicio</a>

</div>

<jsp:include page="../components/footer.jsp" />