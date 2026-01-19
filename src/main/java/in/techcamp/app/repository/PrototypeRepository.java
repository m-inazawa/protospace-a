package in.techcamp.app.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.techcamp.app.entity.PrototypeEntity;

@Mapper
public interface PrototypeRepository {

  @Select("SELECT p.*, u.id AS user_id, u.user_name AS user_name FROM prototypes p JOIN users u ON p.user_id = u.id ORDER BY p.created_at DESC")
  @Results(value = {
    @Result(property = "user.id", column = "user_id"),
    @Result(property = "user.userName", column = "user_name")
  })
  List<PrototypeEntity> findAll();

  @Insert("INSERT INTO prototypes (prototype_name, concept, catch_copy, image, user_id) VALUES (#{prototypeName}, #{concept}, #{catchCopy}, #{image}, #{user.id})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(PrototypeEntity prototype);

  @Select("SELECT * FROM prototypes WHERE user_id = #{userId}")
  List<PrototypeEntity> findByUserId(Integer userId);

  @Update("UPDATE prototypes SET prototypeName = #{prototypeName}, concept = #{concept}, catchCopy = #{catchCopy}, image = #{image}, WHERE id = #{id}")
  void update(PrototypeEntity prototype);

  @Select("SELECT * FROM prototypes WHERE id = #{id}")
  PrototypeEntity findById(Integer id);
}
