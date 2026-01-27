package in.techcamp.app.form;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;

import in.techcamp.app.factory.RegisterFormFactory;
import in.techcamp.app.validation.ValidationPriority1;
import in.techcamp.app.validation.ValidationPriority2;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;


@ActiveProfiles("Test")
public class RegisterFormUnitTest {
  private RegisterForm registerForm;
  private Validator validator;
  private BindingResult bindingResult;

  @BeforeEach
  public void setup() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
    registerForm = RegisterFormFactory.createUser();
    bindingResult = Mockito.mock(BindingResult.class);
  }

  @Nested
  class 正常系 {
    @Test
    public void 全ての項目が正しく存在すれば登録できる() {
      Set<ConstraintViolation<RegisterForm>> violations = validator.validate(registerForm, ValidationPriority1.class);
      assertEquals(0, violations.size());
    }
  }

  @Nested
  class 異常系 {
    @Test
    public void emailが空の場合バリデーションエラーが発生する() {
      registerForm.setEmail("");
      Set<ConstraintViolation<RegisterForm>> violations = validator.validate(registerForm, ValidationPriority1.class);
      assertEquals(1, violations.size());
      assertEquals("メールアドレスを入力してください", violations.iterator().next().getMessage());
    }

    @Test
    public void emailが有効でないの場合バリデーションエラーが発生する() {
      registerForm.setEmail("notAvailable");
      Set<ConstraintViolation<RegisterForm>> violations = validator.validate(registerForm, ValidationPriority2.class);
      assertEquals(1, violations.size());
      assertEquals("有効なメールアドレスを入力してください。", violations.iterator().next().getMessage());
    }

    @Test
    public void passwordが6文字未満の場合バリデーションエラーが発生する() {
      registerForm.setPassword("aaa");
      Set<ConstraintViolation<RegisterForm>> violations = validator.validate(registerForm, ValidationPriority1.class);
      assertEquals(1, violations.size());
      assertEquals("6文字以上のパスワードを入力してください。", violations.iterator().next().getMessage());
    }

    @Test
    public void passwordConfirmationが空の場合バリデーションエラーが発生する() {
      registerForm.setPasswordConfirmation("");
      Set<ConstraintViolation<RegisterForm>> violations = validator.validate(registerForm, ValidationPriority1.class);
      assertEquals(1, violations.size());
      assertEquals("パスワードを再入力してください。", violations.iterator().next().getMessage());
    }

    @Test
    public void passwordとPasswordConfirmationが不一致の場合バリデーションエラーが発生する() {
      registerForm.setPasswordConfirmation("differentPassword");
      registerForm.validatePasswordConfirmation(bindingResult);
      verify(bindingResult).rejectValue("passwordConfirmation", null, "パスワードが一致しません。");
    }

    @Test
    public void userNameが空の場合バリデーションエラーが発生する() {
      registerForm.setUserName("");
      Set<ConstraintViolation<RegisterForm>> violations = validator.validate(registerForm, ValidationPriority1.class);
      assertEquals(1, violations.size());
      assertEquals("ユーザー名を入力してください。", violations.iterator().next().getMessage());
    }
    
    @Test
    public void profileが空の場合バリデーションエラーが発生する() {
      registerForm.setProfile("");
      Set<ConstraintViolation<RegisterForm>> violations = validator.validate(registerForm, ValidationPriority1.class);
      assertEquals(1, violations.size());
      assertEquals("プロフィールを入力してください。", violations.iterator().next().getMessage());
    }
    
    @Test
    public void affiliationが空の場合バリデーションエラーが発生する() {
      registerForm.setAffiliation("");
      Set<ConstraintViolation<RegisterForm>> violations = validator.validate(registerForm, ValidationPriority1.class);
      assertEquals(1, violations.size());
      assertEquals("所属を入力してください。", violations.iterator().next().getMessage());
    }
    
    @Test
    public void positionが空の場合バリデーションエラーが発生する() {
      registerForm.setPosition("");
      Set<ConstraintViolation<RegisterForm>> violations = validator.validate(registerForm, ValidationPriority1.class);
      assertEquals(1, violations.size());
      assertEquals("役職を入力してください。", violations.iterator().next().getMessage());
    }
  }
}
