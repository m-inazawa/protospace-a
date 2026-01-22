package in.techcamp.app.entity;

import lombok.Data;

@Data
public class ImageEntity {
  private Integer id;
  private byte[] image;
  private Integer prototypeId;
}
