CREATE TABLE IF NOT EXISTS csv_file (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(1024) NOT NULL,
    import_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_records INT,
    status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED', 'FAILED') DEFAULT 'PENDING' ,
    error_message TEXT,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS game_sales
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    row_id       INT,
    game_no      INT CHECK (game_no BETWEEN 1 AND 100),
    game_name    VARCHAR(20),
    game_code    VARCHAR(5),
    type         INT CHECK (type IN (1, 2)),
    cost_price   DECIMAL(5, 2) CHECK (cost_price <= 100),
    tax          DECIMAL(3, 2) DEFAULT 0.09,
    sale_price   DECIMAL(5, 2),
    date_of_sale TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    csv_file_id BIGINT,
    CONSTRAINT fk_csv_file FOREIGN KEY (csv_file_id) REFERENCES csv_file(id)
);
