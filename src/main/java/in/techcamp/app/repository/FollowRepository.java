package in.techcamp.app.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FollowRepository {
  @Insert("INSERT INTO follows (follow_id, follower_id) VALUES (#{followId}, #{followerId})")
  void insert(@Param("followId")Integer followId, @Param("followerId")Integer followerId);

  @Delete("DELETE FROM follows WHERE follow_id = #{followId} AND follower_id = #{followerId}")
  void delete(@Param("followId")Integer followId, @Param("followerId")Integer followerId);

  @Select("SELECT COUNT(*) FROM follows WHERE follow_id = #{followId}")
  Long getFollowCount(Integer followId);

  @Select("SELECT COUNT(*) FROM follows WHERE follower_id = #{followerId}")
  Long getFollowerCount(Integer followerId);

  @Select("SELECT EXISTS(SELECT 1 FROM follows WHERE follow_id = #{followId} AND follower_id = #{followerId})")
  boolean isExistedByPrimaryKey(@Param("followId")Integer followId, @Param("followerId")Integer followerId);
}
