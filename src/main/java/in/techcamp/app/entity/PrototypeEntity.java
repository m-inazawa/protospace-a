package in.techcamp.app.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class PrototypeEntity {
  private Integer id;
  private String prototypeName;
  private String concept;
  private String catchCopy;
  private String image;
  private Timestamp updatedAt;
  private UserEntity user;
}