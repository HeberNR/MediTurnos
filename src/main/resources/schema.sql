-- Borramos la base y la creamos totalmente limpia
DROP DATABASE IF EXISTS gestion_turnos;
CREATE DATABASE gestion_turnos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gestion_turnos;

-- 1. Tabla Unificada de Usuarios (Ahora incluye la especialidad como texto)
CREATE TABLE usuarios (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          apellido VARCHAR(100) NOT NULL,
                          dni VARCHAR(20) NOT NULL UNIQUE,
                          email VARCHAR(150) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          rol VARCHAR(50) NOT NULL, -- 'paciente', 'doctor', 'admin'
                          activo BOOLEAN DEFAULT TRUE,
                          telefono VARCHAR(30) NULL,
                          fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          especialidad VARCHAR(100) NULL -- Almacena el valor del Enum directamente
);

-- 2. Tabla de Turnos (Con diagnóstico y observaciones incorporados)
CREATE TABLE turnos (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        paciente_id INT NOT NULL,
                        doctor_id INT NOT NULL,
                        fecha_turno DATE NOT NULL,
                        hora_turno TIME NOT NULL,
                        estado VARCHAR(50) NOT NULL DEFAULT 'pendiente',
                        motivo_consulta TEXT,
                        diagnostico TEXT NULL,
                        observaciones TEXT NULL,
                        fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT fk_turno_paciente FOREIGN KEY (paciente_id) REFERENCES usuarios(id),
                        CONSTRAINT fk_turno_doctor FOREIGN KEY (doctor_id) REFERENCES usuarios(id)
);

-- 3. Auditoría de Turnos (Para trazabilidad del consultorio)
CREATE TABLE auditoria_turnos (
                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                  turno_id INT,
                                  estado_anterior VARCHAR(50),
                                  estado_nuevo VARCHAR(50),
                                  fecha_cambio TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. TRIGGER: Registra el cambio de estado automáticamente
DELIMITER //
CREATE TRIGGER trg_auditoria_estado_turno
    AFTER UPDATE ON turnos
    FOR EACH ROW
BEGIN
    IF OLD.estado != NEW.estado THEN
        INSERT INTO auditoria_turnos (turno_id, estado_anterior, estado_nuevo)
        VALUES (OLD.id, OLD.estado, NEW.estado);
END IF;
END; //
DELIMITER ;

-- 5. Vista Definitiva para listar los turnos
CREATE VIEW vista_turnos_detallados AS
SELECT
    t.id AS turno_id,
    t.fecha_turno,
    t.hora_turno,
    t.estado,
    t.motivo_consulta,
    t.diagnostico,
    t.observaciones,
    p.id AS paciente_id,
    p.nombre AS paciente_nombre,
    p.apellido AS paciente_apellido,
    p.dni,
    d.id AS doctor_id,
    d.nombre AS doctor_nombre,
    d.apellido AS doctor_apellido,
    d.especialidad AS especialidad
FROM turnos t
         INNER JOIN usuarios p ON t.paciente_id = p.id
         INNER JOIN usuarios d ON t.doctor_id = d.id;

-- 6. ÍNDICES DE RENDIMIENTO (Agregados para agilizar los DAOs)
CREATE INDEX idx_usuarios_rol ON usuarios(rol);
CREATE INDEX idx_usuarios_activo ON usuarios(activo);
CREATE INDEX idx_turnos_estado ON turnos(estado);