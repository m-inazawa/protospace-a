CREATE TABLE images (
  id SERIAL NOT NULL PRIMARY KEY,
  image BYTEA NOT NULL,
  prototype_id INTEGER NOT NULL REFERENCES prototypes(id)
);

ALTER TABLE prototypes DROP COLUMN image;