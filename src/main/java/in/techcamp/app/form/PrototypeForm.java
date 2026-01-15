package in.techcamp.app.form;

import in.techcamp.app.validation.ValidationPriority1;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrototypeForm {
  
  @NotBlank(message = "空欄は無理です。",groups = ValidationPriority1.class)
    private String prototypeName;
    private String concept;
    private String catchCopy;
    private String image;
}