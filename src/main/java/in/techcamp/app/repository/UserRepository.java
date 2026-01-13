package in.techcamp.app.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import in.techcamp.app.entity.UserEntity;

@Mapper
public interface  UserRepository {
 
  @Select("SELECT * FROM users WHERE email = #{email}")
  UserEntity findByEmail(String email);
}