-- Crear esquema si no existe
CREATE SCHEMA IF NOT EXISTS rq;

-- Crear tabla state
CREATE TABLE rq.state (
                          id BIGINT PRIMARY KEY,
                          code VARCHAR(50),
                          name VARCHAR(100),
                          description VARCHAR(255)
);

-- Insertar algunos estados de ejemplo
INSERT INTO rq.state (id, code, name, description) VALUES
                                                       (1,'ACTIVE','Activo','Estado cuando la solicitud está activa'),
                                                       (2,'INACTIVE','Inactivo','Estado cuando la solicitud está inactiva'),
                                                       (3,'PENDING','Pendiente de revisión',	'Estado cuando la solicitud está en proceso'),
                                                       (4,'CLOSED','Cerrado','Estado cuando la solicitud ya fue cerrada'),
                                                       (5,'APPROVED','Aprobado','Estado cuando la solicitud ha sido aprobada'),
                                                       (6,'REJECTED','Rechazada','Estado cuando la soliciutd es rechazada'),
                                                       (7,'REVIEW','Revisión manual','Estado cuando la soliciud es por revisión manual');
-- Crear tabla loan_type
CREATE TABLE rq.loan_type (
                              id BIGINT PRIMARY KEY,
                              name VARCHAR(100),
                              code VARCHAR(50),
                              minimum_amount DOUBLE PRECISION,
                              maximum_amount DOUBLE PRECISION,
                              interest_rate DOUBLE PRECISION,
                              automatic_validation BOOLEAN
);

-- Insertar algunos tipos de préstamo
INSERT INTO rq.loan_type (id, name, code, minimum_amount, maximum_amount, interest_rate, automatic_validation) VALUES
                                                                                                                   (1, 'Personal', 'PERS', 1000, 5000, 5.0, true),
                                                                                                                   (2, 'Hipotecario', 'HOME', 5000, 50000, 3.5, false);

-- Crear tabla requests
CREATE TABLE rq.requests (
                             request_id BIGINT PRIMARY KEY,
                             amount DOUBLE PRECISION,
                             period INT,
                             email VARCHAR(100) NOT NULL,
                             id_state BIGINT REFERENCES rq.state(id),
                             id_loan_type BIGINT REFERENCES rq.loan_type(id)
);

-- Insertar una solicitud de prueba
INSERT INTO rq.requests (request_id, amount, period, email, id_state, id_loan_type) VALUES
    (1, 2000.0, 12, 'john.acevedo@example.com', 5, 1),
    (2, 2000.0, 10, 'john.acevedo@example.com', 3, 2),
    (3, 2000.0, 10, 'john.acevedo@example.com', 5, 1);

