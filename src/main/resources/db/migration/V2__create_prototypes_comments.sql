-- prototypes テーブル作成
CREATE TABLE prototypes (
  id              SERIAL PRIMARY KEY,
  user_id         INTEGER NOT NULL,
  prototype_name  VARCHAR(512) NOT NULL,
  concept         VARCHAR(512) NOT NULL,
  catch_copy      VARCHAR(512) NOT NULL,
  image           VARCHAR(512) NOT NULL,
  created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- comments テーブル作成
CREATE TABLE comments (
  id            SERIAL PRIMARY KEY,
  prototype_id  INTEGER NOT NULL,
  user_id       INTEGER NOT NULL,
  comment       VARCHAR(512) NOT NULL,
  created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (prototype_id) REFERENCES prototypes(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(id) 
);
