<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- Llamamos al Header -->
<jsp:include page="WEB-INF/components/header.jsp" />

<div style="display: flex; flex-direction: column; align-items: center; text-align: center; max-width: 600px; margin: 0 auto; padding-top: 4rem;">
  <h1 style="color: var(--accent-color); margin-bottom: 1rem; font-size: 2.5rem;">Bienvenido a MediTurnos</h1>
  <p style="color: var(--text-secondary); font-size: 1.1rem; line-height: 1.6; margin-bottom: 2rem;">
    El sistema de gestión médica más moderno. Administrá tus consultas, revisá tus historiales y solicitá turnos de manera rápida y segura.
  </p>

  <div style="display: flex; gap: 15px; justify-content: center;">
    <a href="<%= request.getContextPath() %>/login" class="btn" style="padding: 0.8rem 2rem; font-size: 1.1rem;">Ingresar al sistema</a>
    <a href="<%= request.getContextPath() %>/registro" class="btn btn-outline" style="padding: 0.8rem 2rem; font-size: 1.1rem;">Soy nuevo</a>
  </div>
</div>

<!-- Llamamos al Footer -->
<jsp:include page="WEB-INF/components/footer.jsp" />