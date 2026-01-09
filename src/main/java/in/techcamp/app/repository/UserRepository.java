package in.techcamp.app.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import in.techcamp.app.entity.UserEntity;

@Mapper
public interface UserRepository {
  @Select("SELECT * FROM users")
  List<UserEntity> findAll();
}
