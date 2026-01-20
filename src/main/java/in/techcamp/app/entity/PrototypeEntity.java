package in.techcamp.app.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class PrototypeEntity {
  private Integer id;
  private String prototypeName;
  private String concept;
  private String catchCopy;
  private String image;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private UserEntity user;
  private List<CommentEntity> comments;
}