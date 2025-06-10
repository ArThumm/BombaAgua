CREATE TABLE lineasDibujo (
    linea_id INT AUTO_INCREMENT PRIMARY KEY,
    start_x DOUBLE NOT NULL,
    start_y DOUBLE NOT NULL,
    end_x DOUBLE NOT NULL,
    end_y DOUBLE NOT NULL,
    color_id INT NOT NULL,
    linea_ancho DOUBLE NOT NULL,
    save_id INT NOT NULL
);

CREATE TABLE coloresLinea (
    color_id INT PRIMARY KEY,
    color_red DOUBLE NOT NULL,
    color_green DOUBLE NOT NULL,
    color_blue DOUBLE NOT NULL
);

CREATE TABLE bombauser (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(255) NOT NULL,
    user_email VARCHAR(255) NOT NULL,
    user_password VARCHAR(255) NOT NULL
);

INSERT INTO bombaUser (user_name, user_email, user_password)
VALUES ('root', 'root@gmail.com', '123456');

INSERT INTO coloresLinea (color_id, color_red, color_green, color_blue) VALUES (1, 1, 0, 0);
INSERT INTO coloresLinea (color_id, color_red, color_green, color_blue) VALUES (2, 0, 1, 0);
INSERT INTO coloresLinea (color_id, color_red, color_green, color_blue) VALUES (3, 0, 0, 1);