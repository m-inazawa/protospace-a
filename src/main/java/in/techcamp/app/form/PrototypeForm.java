package in.techcamp.app.form;

import in.techcamp.app.validation.ValidationPriority1;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrototypeForm {
  
  @NotBlank(message = "プロトタイプ名を入力してください。",groups = ValidationPriority1.class)
  private String prototypeName;
  
  @NotBlank(message = "コンセプトを入力してください。", groups = ValidationPriority1.class)
  private String concept;

  @NotBlank(message = "キャッチコピーを入力してください。", groups = ValidationPriority1.class)
  private String catchCopy;

  @NotBlank(message = "画像を入力してください。", groups = ValidationPriority1.class)
  private String image;
}