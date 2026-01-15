package in.techcamp.app.form;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
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
    }

    @Test  //正常系
        public void プロトタイプ名とコンセプトとキャッチコピーと画像が存在していれば投稿できる () {
          Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(prototypeForm, ValidationPriority1.class);
          assertEquals(0, violations.size());
    }

    @Test  //異常系
        public void プロトタイプ名が空ではバリデーションエラーが発生する() {
          prototypeForm.setPrototypeName("");

          Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(prototypeForm, ValidationPriority1.class);          
          assertEquals(1, violations.size());
          assertEquals("空欄は無理です。", violations.iterator().next().getMessage());
    }
}