CREATE TABLE coordinates
(
    id SERIAL PRIMARY KEY,
    x REAL CHECK (x <= 39),
    y INT NOT NULL CHECK (y <= 905)
);

CREATE TABLE person
(
    id SERIAL PRIMARY KEY,
    person_name TEXT NOT NULL CHECK (person_name <> ''),
    birthday DATE,
    height DOUBLE PRECISION NOT NULL CHECK (height > 0),
    eye_color TEXT NOT NULL,
    nationality TEXT NOT NULL
);

CREATE TABLE product
(
    id SERIAL PRIMARY KEY,
    product_name TEXT NOT NULL CHECK (product_name <> ''),
    coordinates_id INT NOT NULL REFERENCES coordinates (id) ON DELETE CASCADE ON UPDATE CASCADE,
    creation_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    price INT NOT NULL CHECK (price > 0),
    unit_of_measure TEXT,
    owner_id INT REFERENCES person (id) ON DELETE CASCADE ON UPDATE CASCADE
);
