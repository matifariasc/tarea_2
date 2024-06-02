CREATE SCHEMA `toycars` ;

CREATE TABLE Clientes (
    cliente_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    rut VARCHAR(255) NOT NULL
);

CREATE TABLE Vehiculos (
    vehiculo_id INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    marca VARCHAR(255) NOT NULL,
    cantidad_ruedas INT NOT NULL,
    control_remoto BOOLEAN NOT NULL,
    anio_fabricacion YEAR NOT NULL
);

CREATE TABLE Accesorios (
    accesorio_id INT AUTO_INCREMENT PRIMARY KEY,
    vehiculo_id INT NOT NULL,
    tipo_accesorio VARCHAR(255) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (vehiculo_id) REFERENCES Vehiculos(vehiculo_id)
);

CREATE TABLE Precios (
    precio_id INT AUTO_INCREMENT PRIMARY KEY,
    vehiculo_id INT NOT NULL,
    marca VARCHAR(255) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (vehiculo_id) REFERENCES Vehiculos(vehiculo_id)
);

CREATE TABLE Compras (
    compra_id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    fecha DATE NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    descuento DECIMAL(10, 2) NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES Clientes(cliente_id)
);

CREATE TABLE Detalle_Compras (
    detalle_id INT AUTO_INCREMENT PRIMARY KEY,
    compra_id INT NOT NULL,
    vehiculo_id INT NOT NULL,
    accesorio_id INT,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (compra_id) REFERENCES Compras(compra_id),
    FOREIGN KEY (vehiculo_id) REFERENCES Vehiculos(vehiculo_id),
    FOREIGN KEY (accesorio_id) REFERENCES Accesorios(accesorio_id)
);

ALTER TABLE Accesorios
MODIFY COLUMN precio INT;

ALTER TABLE Precios
MODIFY COLUMN precio INT;

ALTER TABLE Compras
MODIFY COLUMN subtotal INT,
MODIFY COLUMN descuento INT,
MODIFY COLUMN total INT;

ALTER TABLE Detalle_Compras
MODIFY COLUMN precio_unitario INT;
