package in.techcamp.app.form;

import in.techcamp.app.validation.ValidationPriority1;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentForm {
  @NotBlank(message="コメントを入力してください", groups=ValidationPriority1.class)
  private String comment;
}
