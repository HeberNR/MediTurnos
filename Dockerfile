# Usamos Tomcat 10.1 con Java 21 compatible con Jakarta EE 10
FROM tomcat:10.1-jdk21

# Limpiamos las apps por defecto de Tomcat para evitar conflictos
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiamos el .war que genera Maven a la carpeta de webapps de Tomcat
# Lo renombramos a ROOT.war para que la app abra directamente en localhost:8080/
COPY target/gestion-turnos-medicos.war /usr/local/tomcat/webapps/ROOT.war

# Exponemos el puerto
EXPOSE 8080

# Iniciamos Tomcat
CMD ["catalina.sh", "run"]