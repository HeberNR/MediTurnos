<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="../components/header.jsp" />
<div class="card" style="max-width: 500px; margin: 4rem auto; text-align: center;">
  <h2 style="color: var(--error-text); font-size: 3rem; margin-bottom: 0;">404</h2>
  <h3>Página no encontrada</h3>
  <p style="color: var(--text-secondary); margin: 1.5rem 0;">La ruta a la que intentás acceder no existe o fue movida.</p>
  <a href="<%= request.getContextPath() %>/" class="btn" style="display: inline-block;">Volver al Inicio</a>
</div>
<jsp:include page="../components/footer.jsp" />