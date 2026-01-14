package in.techcamp.app.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import in.techcamp.app.entity.CommentEntity;

@Mapper
public interface CommentRepository {
  @Insert("INSERT INTO comments (prototype_id, user_id, comment) VALUES (#{prototypeId}, #{userId}, #{comment})")
  @Options(useGeneratedKeys=true, keyProperty="id")
  void insert(CommentEntity commentEntity);
}
