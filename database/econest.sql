CREATE DATABASE IF NOT EXISTS econest_db;
USE econest_db;

CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    price DECIMAL(10, 2) NOT NULL,
    eco_rating INT CHECK (eco_rating BETWEEN 1 AND 5),
    stock_qty INT DEFAULT 0,
    reorder_level INT DEFAULT 5
);

CREATE TABLE clients (
    client_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    client_type VARCHAR(20)
);

CREATE TABLE delivery_agents (
    agent_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    vehicle_details VARCHAR(100)
);

CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT,
    agent_id INT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2),
    status VARCHAR(30) DEFAULT 'Pending',
    FOREIGN KEY (client_id) REFERENCES clients(client_id),
    FOREIGN KEY (agent_id) REFERENCES delivery_agents(agent_id)
);