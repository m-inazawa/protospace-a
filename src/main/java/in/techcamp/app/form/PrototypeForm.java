package in.techcamp.app.form;

import org.springframework.web.multipart.MultipartFile;

import in.techcamp.app.validation.ValidationPriority1;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrototypeForm {
  
  @NotBlank(message = "プロトタイプ名を入力してください。",groups = ValidationPriority1.class)
  private String prototypeName;
  
  @NotBlank(message = "コンセプトを入力してください。", groups = ValidationPriority1.class)
  private String concept;

  @NotBlank(message = "キャッチコピーを入力してください。", groups = ValidationPriority1.class)
  private String catchCopy;

  @NotNull(message = "画像を入力してください。", groups = ValidationPriority1.class)
  private MultipartFile image;
}