CREATE TABLE visits
(
    id          UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    date        TIMESTAMP,
    description VARCHAR(255),
    pet_id      VARCHAR(255) NOT NULL
);
