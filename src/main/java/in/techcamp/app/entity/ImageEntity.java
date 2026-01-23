package in.techcamp.app.entity;

import lombok.Data;
import lombok.ToString;

@Data
public class ImageEntity {
  private Integer id;
  @ToString.Exclude
  private byte[] image;
  private Integer prototypeId;
}
