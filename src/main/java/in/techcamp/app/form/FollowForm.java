package in.techcamp.app.form;

import lombok.Data;

@Data
public class FollowForm {
  private Long followCounts;
  private Long followerCounts;
  private String isFollow;
}
