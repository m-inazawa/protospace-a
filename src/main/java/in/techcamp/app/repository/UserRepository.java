package in.techcamp.app.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

  @Select("SELECT id, email, password, user_name, profile, affiliation, position, created_at, version, last_password_change FROM users WHERE id = #{userId}")
  @Results({
    @Result(property = "id", column = "id", id = true),
    @Result(property = "password", column = "password"),
    @Result(property = "userName", column = "user_name"),
    @Result(property = "lastPasswordChange", column = "last_password_change"), 
    @Result(property = "version", column = "version"), 
    @Result(property = "prototypes", column = "id",
      many = @Many(select = "in.techcamp.app.repository.PrototypeRepository.findByUserId"))
  })
  UserEntity findByUserId(Integer userId);

  @Update("UPDATE users SET " +
          "email = #{email}, user_name = #{userName}, password = #{password}, " +
          "profile = #{profile}, affiliation = #{affiliation}, position = #{position}, " +
          "last_password_change = #{lastPasswordChange}, " +
          "version = version + 1 " + // 保存時にバージョンを1つ上げる
          "WHERE id = #{id} AND version = #{version}") // バージョンが一致する場合のみ更新
  int updateUser(UserEntity userEntity);
}