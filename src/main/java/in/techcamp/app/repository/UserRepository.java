package in.techcamp.app.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.techcamp.app.entity.UserEntity;

@Mapper
public interface  UserRepository {
  @Insert("INSERT INTO users (email, user_name, password, profile, affiliation, position) VALUES (#{email}, #{userName}, #{password}, #{profile}, #{affiliation}, #{position})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertUser(UserEntity userEntity);

  @Select("SELECT EXISTS(SELECT 1 FROM users WHERE email = #{email})")
  boolean findByEmail(String email);

  @Select("SELECT * FROM users WHERE email = #{email}")
  UserEntity findByEmailForEntities(String email);

  @Select("SELECT id, email, user_name, profile, affiliation, position, created_at FROM users WHERE id = #{userId}")
  @Results({
    @Result(property = "id", column = "id", id = true),
    @Result(property = "userName", column = "user_name"),
    @Result(property = "prototypes", column = "id",
      many = @Many(select = "in.techcamp.app.repository.PrototypeRepository.findByUserId"))
  })
  UserEntity findByUserId(Integer userId);
}