package in.techcamp.app.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class UserEntity {
  private Integer id;
  private String email;
  private String userName;
  private String password;
  private String profile;
  private String affiliation;
  private String position;
  private LocalDateTime createdAt;
  private List<PrototypeEntity> prototypes;
  private List<CommentEntity> comments;
}