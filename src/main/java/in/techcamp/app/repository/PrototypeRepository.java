package in.techcamp.app.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.techcamp.app.entity.PrototypeEntity;

@Mapper
public interface PrototypeRepository {

  @Select("SELECT p.*, u.id AS user_id, u.name AS user_name FROM prototypes p JOIN users u ON p.user_id = u.id ORDER BY p.created_at DESC")
  @Results(value = {
    @Result(property = "user.id", column = "user_id"),
    @Result(property = "user.name", column = "user_name")
  })
  List<PrototypeEntity> findAll();
}
