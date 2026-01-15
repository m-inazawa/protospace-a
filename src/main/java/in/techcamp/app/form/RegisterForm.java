package in.techcamp.app.form;

import org.hibernate.validator.constraints.Length;
import org.springframework.validation.BindingResult;

import in.techcamp.app.validation.ValidationPriority1;
import in.techcamp.app.validation.ValidationPriority2;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterForm {
  @NotBlank(message = "メールアドレスを入力してください", groups = ValidationPriority1.class)
  @Email(message = "有効なメールアドレスを入力してください。", groups = ValidationPriority2.class)
  private String email;

  // @Length(min = 6, max = 128, message = "Password should be between 6 and 128 characters", groups = ValidationPriority2.class)
  @NotBlank(message = "パスワードを入力してください。", groups = ValidationPriority1.class)
  @Length(min = 6, message = "6文字以上のパスワードを入力してください。", groups = ValidationPriority2.class)
  private String password;

  @NotBlank(message = "パスワードを再入力してください。", groups = ValidationPriority1.class)
  private String passwordConfirmation;

  public void validatePasswordConfirmation(BindingResult result) {
    if (!password.equals(passwordConfirmation)) {
      result.rejectValue("passwordConfirmation", null, "パスワードが一致しません。");
    }
  }

  // @Length(max = 6, message = "Name is too long (maximum is 6 characters)", groups = ValidationPriority2.class)
  @NotBlank(message = "名前欄を入力してください。", groups = ValidationPriority1.class)
  private String userName;

  @NotBlank(message = "プロフィール欄を入力してください。", groups = ValidationPriority1.class)
  private String profile;

  @NotBlank(message = "所属欄を入力してください。", groups = ValidationPriority1.class)
  private String affiliation;

  @NotBlank(message = "役職欄を入力してください。", groups = ValidationPriority1.class)
  private String position;
}