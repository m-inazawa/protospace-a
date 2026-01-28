-- 1. 現在の制約を削除
ALTER TABLE images 
DROP CONSTRAINT images_prototype_id_fkey;


-- 2. CASCADE付きで制約を再作成
ALTER TABLE images 
ADD CONSTRAINT images_prototype_id_fkey 
  FOREIGN KEY (prototype_id) 
  REFERENCES prototypes(id) 
  ON DELETE CASCADE;