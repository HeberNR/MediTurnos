USE gestion_turnos;

-- ==========================================
-- SCRIPT DE INSERCIÓN DE DATOS DE PRUEBA
-- ==========================================

-- 1. ADMINISTRADORES (2)
INSERT INTO usuarios (nombre, apellido, dni, email, password, rol, activo) VALUES
                                                                               ('Admin', 'Principal', '10000000', 'admin@mediturnos.com', '$2a$10$xT/Ejto4kG3Xt3TLlIc0Oe8z2jX2NNbvGjzFq8u.CkCanM0Pj/muC', 'admin', true),
                                                                               ('Admin', 'Secundario', '10000001', 'admin2@mediturnos.com', '$2a$10$xT/Ejto4kG3Xt3TLlIc0Oe8z2jX2NNbvGjzFq8u.CkCanM0Pj/muC', 'admin', true);

-- 2. DOCTORES (Sin tildes en las especialidades)
INSERT INTO usuarios (nombre, apellido, dni, email, password, rol, activo, especialidad) VALUES
                                                                                             ('Gregory', 'House', '20000001', 'house@mediturnos.com', '$2a$10$xT/Ejto4kG3Xt3TLlIc0Oe8z2jX2NNbvGjzFq8u.CkCanM0Pj/muC', 'doctor', true, 'Cardiologia'),
                                                                                             ('Rene', 'Favaloro', '20000002', 'favaloro@mediturnos.com', '$2a$10$xT/Ejto4kG3Xt3TLlIc0Oe8z2jX2NNbvGjzFq8u.CkCanM0Pj/muC', 'doctor', true, 'Cardiologia'),
                                                                                             ('Meredith', 'Grey', '20000003', 'grey@mediturnos.com', '$2a$10$xT/Ejto4kG3Xt3TLlIc0Oe8z2jX2NNbvGjzFq8u.CkCanM0Pj/muC', 'doctor', true, 'Pediatria'),
                                                                                             ('Carlos', 'Pediatra', '20000004', 'carlos@mediturnos.com', '$2a$10$xT/Ejto4kG3Xt3TLlIc0Oe8z2jX2NNbvGjzFq8u.CkCanM0Pj/muC', 'doctor', true, 'Pediatria'),
                                                                                             ('Stephen', 'Strange', '20000005', 'strange@mediturnos.com', '$2a$10$xT/Ejto4kG3Xt3TLlIc0Oe8z2jX2NNbvGjzFq8u.CkCanM0Pj/muC', 'doctor', true, 'Traumatologia'),
                                                                                             ('Laura', 'Huesos', '20000006', 'laura@mediturnos.com', '$2a$10$xT/Ejto4kG3Xt3TLlIc0Oe8z2jX2NNbvGjzFq8u.CkCanM0Pj/muC', 'doctor', true, 'Traumatologia'),
                                                                                             ('Derek', 'Shepherd', '20000007', 'shepherd@mediturnos.com', '$2a$10$xT/Ejto4kG3Xt3TLlIc0Oe8z2jX2NNbvGjzFq8u.CkCanM0Pj/muC', 'doctor', true, 'Neurologia'),
                                                                                             ('Alicia', 'Cerebro', '20000008', 'alicia@mediturnos.com', '$2a$10$xT/Ejto4kG3Xt3TLlIc0Oe8z2jX2NNbvGjzFq8u.CkCanM0Pj/muC', 'doctor', true, 'Neurologia');

-- 3. PACIENTES (Sin tildes en los nombres/apellidos)
INSERT INTO usuarios (nombre, apellido, dni, email, password, rol, activo, telefono) VALUES
                                                                                         ('Juan', 'Perez', '30000001', 'juan@paciente.com', '$2a$10$xT/Ejto4kG3Xt3TLlIc0Oe8z2jX2NNbvGjzFq8u.CkCanM0Pj/muC', 'paciente', true, '1122334455'),
                                                                                         ('Maria', 'Gomez', '30000002', 'maria@paciente.com', '$2a$10$xT/Ejto4kG3Xt3TLlIc0Oe8z2jX2NNbvGjzFq8u.CkCanM0Pj/muC', 'paciente', true, '1122334456'),
                                                                                         ('Carlos', 'Lopez', '30000003', 'carlos.l@paciente.com', '$2a$10$xT/Ejto4kG3Xt3TLlIc0Oe8z2jX2NNbvGjzFq8u.CkCanM0Pj/muC', 'paciente', true, '1122334457'),
                                                                                         ('Ana', 'Martinez', '30000004', 'ana@paciente.com', '$2a$10$xT/Ejto4kG3Xt3TLlIc0Oe8z2jX2NNbvGjzFq8u.CkCanM0Pj/muC', 'paciente', true, '1122334458'),
                                                                                         ('Luis', 'Fernandez', '30000005', 'luis@paciente.com', '$2a$10$xT/Ejto4kG3Xt3TLlIc0Oe8z2jX2NNbvGjzFq8u.CkCanM0Pj/muC', 'paciente', true, '1122334459'),
                                                                                         ('Lucia', 'Garcia', '30000006', 'lucia@paciente.com', '$2a$10$xT/Ejto4kG3Xt3TLlIc0Oe8z2jX2NNbvGjzFq8u.CkCanM0Pj/muC', 'paciente', true, '1122334460'),
                                                                                         ('Diego', 'Rodriguez', '30000007', 'diego@paciente.com', '$2a$10$xT/Ejto4kG3Xt3TLlIc0Oe8z2jX2NNbvGjzFq8u.CkCanM0Pj/muC', 'paciente', true, '1122334461'),
                                                                                         ('Sofia', 'Sanchez', '30000008', 'sofia@paciente.com', '$2a$10$xT/Ejto4kG3Xt3TLlIc0Oe8z2jX2NNbvGjzFq8u.CkCanM0Pj/muC', 'paciente', true, '1122334462'),
                                                                                         ('Javier', 'Ramirez', '30000009', 'javier@paciente.com', '$2a$10$xT/Ejto4kG3Xt3TLlIc0Oe8z2jX2NNbvGjzFq8u.CkCanM0Pj/muC', 'paciente', true, '1122334463'),
                                                                                         ('Valentina', 'Torres', '30000010', 'valentina@paciente.com', '$2a$10$xT/Ejto4kG3Xt3TLlIc0Oe8z2jX2NNbvGjzFq8u.CkCanM0Pj/muC', 'paciente', true, '1122334464');