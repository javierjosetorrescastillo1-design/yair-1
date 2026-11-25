-- Script de referencia (el programa Java ya hace esto automáticamente)
CREATE DATABASE IF NOT EXISTS pat_db CHARACTER SET utf8mb4;
USE pat_db;

CREATE TABLE IF NOT EXISTS usuario (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    estado_acceso VARCHAR(20) NOT NULL
);

INSERT INTO usuario (nombre, rol, estado_acceso) VALUES
('Carlos', 'Administrador', 'Autorizado'),
('María', 'Usuario', 'Denegado'),
('Andrés', 'Administrador', 'Autorizado'),
('Laura', 'Gestor', 'Denegado'),
('Sofía', 'Usuario', 'Autorizado');

-- Cambiar el acceso de un usuario (ejemplo: María, id 2)
UPDATE usuario SET estado_acceso = 'Autorizado' WHERE id_usuario = 2;
