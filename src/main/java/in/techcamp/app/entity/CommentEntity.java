package in.techcamp.app.entity;

import lombok.Data;

@Data
public class CommentEntity {
  private Integer id;
  private Integer prototypeId;
  private Integer userId;
  private String comment;
  private UserEntity user;
}
