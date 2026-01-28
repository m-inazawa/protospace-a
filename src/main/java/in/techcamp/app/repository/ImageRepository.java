package in.techcamp.app.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.techcamp.app.entity.ImageEntity;

@Mapper
public interface ImageRepository {
  @Select("SELECT * FROM images WHERE id = #{imageId}")
  ImageEntity findByImageId(Integer imageId);

  @Insert("INSERT INTO images (image, prototype_id) VALUES (#{image}, #{prototypeId})")
  @Options(useGeneratedKeys=true, keyProperty="id")
  void insert(ImageEntity imageEntity);

  @Update("UPDATE images SET image = #{image} WHERE prototype_id = #{prototypeId}")
  void update(ImageEntity imageEntity);

  @Select("SELECT pg_database_size(current_database())")
  long getCurrentDatabaseSize();

  @Select("SELECT * FROM images WHERE prototype_id = #{prototypeId}")
  ImageEntity findByPrototypeId(Integer prototypeId);

}
