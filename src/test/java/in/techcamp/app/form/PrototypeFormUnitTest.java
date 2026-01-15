package in.techcamp.app.form;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import in.techcamp.app.validation.ValidationPriority1;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ActiveProfiles("test")
public class PrototypeFormUnitTest {
  private Validator validator;
  private PrototypeForm prototypeForm;

  @BeforeEach
    public void setUp() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        prototypeForm = new PrototypeForm();
        prototypeForm.setPrototypeName("プロトタイプ名");
        prototypeForm.setConcept("コンセプト");
        prototypeForm.setCatchCopy("キャッチコピー");
        prototypeForm.setImage("画像");
    }

    @Test  //正常系
        public void プロトタイプ名とコンセプトとキャッチコピーと画像が存在していれば投稿できる () {
          Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(prototypeForm, ValidationPriority1.class);
          assertEquals(0, violations.size());
    }

    @Nested  //異常系
      public class プロトタイプ投稿ができない場合 {
    
      @Test
        public void プロトタイプ名が空ではバリデーションエラーが発生する() {
          prototypeForm.setPrototypeName("");

          Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(prototypeForm, ValidationPriority1.class);          
          assertEquals(1, violations.size());
          assertEquals("プロトタイプ名を入力してください。", violations.iterator().next().getMessage());
      }

      @Test
        public void コンセプトが空ではバリデーションエラーが発生する() {
          prototypeForm.setConcept("");

          Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(prototypeForm, ValidationPriority1.class);          
          assertEquals(1, violations.size());
          assertEquals("コンセプトを入力してください。", violations.iterator().next().getMessage());
      }

      @Test
        public void キャッチコピーが空ではバリデーションエラーが発生する() {
          prototypeForm.setCatchCopy("");

          Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(prototypeForm, ValidationPriority1.class);          
          assertEquals(1, violations.size());
          assertEquals("キャッチコピーを入力してください。", violations.iterator().next().getMessage());
      }

      @Test
        public void 画像が空ではバリデーションエラーが発生する() {
          prototypeForm.setImage("");

          Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(prototypeForm, ValidationPriority1.class);          
          assertEquals(1, violations.size());
          assertEquals("画像を入力してください。", violations.iterator().next().getMessage());
      }
  }
}