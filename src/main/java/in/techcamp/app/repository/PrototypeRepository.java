package in.techcamp.app.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.techcamp.app.entity.CommentEntity;
import in.techcamp.app.entity.PrototypeEntity;

@Mapper
public interface PrototypeRepository {

  @Select("SELECT p.*, u.id AS user_id, u.user_name AS user_name, i.id as image_id FROM prototypes p JOIN users u ON p.user_id = u.id JOIN images i ON p.id = i.prototype_id  ORDER BY p.created_at DESC")
  @Results(value = {
    @Result(property = "user.id", column = "user_id"),
    @Result(property = "user.userName", column = "user_name"),
    @Result(property = "imageId", column = "image_id")
  })
  List<PrototypeEntity> findAll();

  @Insert("INSERT INTO prototypes (prototype_name, concept, catch_copy, user_id) VALUES (#{prototypeName}, #{concept}, #{catchCopy}, #{user.id})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(PrototypeEntity prototype);

  @Select("SELECT p.*, u.id AS user_id, u.user_name AS user_name, i.id as image_id FROM prototypes p JOIN users u ON p.user_id = u.id JOIN images i ON p.id = i.prototype_id WHERE user_id = #{userId} ORDER BY p.created_at DESC")
  @Results(value = {
    @Result(property = "user.id", column = "user_id"),
    @Result(property = "user.userName", column = "user_name")
  })
  List<PrototypeEntity> findByUserId(Integer userId);

  @Update("UPDATE prototypes SET \"prototype_name\" = #{prototypeName}, \"concept\" = #{concept}, \"catch_copy\" = #{catchCopy} WHERE id = #{id}")
  void update(PrototypeEntity prototype);

  //@Select("SELECT * FROM prototypes WHERE id = #{id}")
  //PrototypeEntity findById(Integer id);

  // ユーザー名を一緒に取得するためにJOINを追加
  @Select("SELECT p.*, u.id AS user_id, u.user_name AS user_name, i.id AS image_id " +
          "FROM prototypes p " +
          "JOIN users u ON p.user_id = u.id " +
          "JOIN images i ON p.id = i.prototype_id " +
          "WHERE p.id = #{id}")
  @Results(value = {
      // prototypesテーブルのidをprototype.idにセット
      @Result(property = "id", column = "id"),
      // usersテーブルのid(user_id)をprototype.user.id にセット
      @Result(property = "user.id", column = "user_id"),
      // usersテーブルの名前(user_name)をprototype.user.userName にセット
      @Result(property = "user.userName", column = "user_name"),
      // プロトタイプのidを引数にしてコメントのメソッドを呼び出す
      @Result(property = "comments", column = "id", 
              many = @Many(select = "findCommentsByPrototypeId"))
  })
  PrototypeEntity findById(Integer id);

  //投稿についたコメントを取得(ユーザー名も合わせて取得)
  @Select("SELECT c.*, u.user_name AS user_name, u.id AS user_id " +
        "FROM comments c " +
        "JOIN users u ON c.user_id = u.id " +
        "WHERE c.prototype_id = #{prototypeId}")
  @Results(value = {
    @Result(property = "user.userName", column = "user_name"),
    @Result(property = "user.id", column = "user_id")
  })
  List<CommentEntity> findCommentsByPrototypeId(Integer prototypeId);

  @Delete("DELETE FROM prototypes WHERE id = #{id}")
  void deleteById(Integer id);
}
