<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<jsp:include page="WEB-INF/components/header.jsp" />

<div class="hero-section">
  <h1 class="hero-title">Bienvenido a MediTurnos</h1>
  <p class="hero-text">
    El sistema de gestión médica más moderno. Administrá tus consultas, revisá
    tus historiales y solicitá turnos de manera rápida y segura.
  </p>
  <div class="flex-row">
    <a href="<%= request.getContextPath() %>/login" class="btn">Ingresar al sistema</a>
    <a href="<%= request.getContextPath() %>/registro" class="btn btn-outline">Soy nuevo</a>
  </div>
</div>

<jsp:include page="WEB-INF/components/footer.jsp" />