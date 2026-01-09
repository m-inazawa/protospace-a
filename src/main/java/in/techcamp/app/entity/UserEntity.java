package in.techcamp.app.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserEntity {
  private Integer id;
  private String email;
  private String user_name;
  private String password;
  private String profile;
  private String affiliation;
  private String position;
  private LocalDateTime created_at;
}
