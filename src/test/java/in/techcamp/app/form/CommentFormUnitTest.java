package in.techcamp.app.form;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import in.techcamp.app.validation.ValidationPriority1;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CommentFormUnitTest {

  @Nested
  class 正常系 {
    @Test
    public void commentに値がありバリデーションエラーが発生しない() {
      CommentForm commentForm = new CommentForm();
      commentForm.setComment("test");
      // バリデーションの実行
      ValidatorFactory factory =  Validation.buildDefaultValidatorFactory();
      Validator validator = factory.getValidator();
      Set<ConstraintViolation<CommentForm>> violations = validator.validate(commentForm, ValidationPriority1.class);
      assertEquals(0, violations.size());
    }
  }
  
  @Nested
  class 異常系 {
    @Test
    public void commentが空の場合バリデーションエラーが発生する() {
      CommentForm commentForm = new CommentForm();
      commentForm.setComment("");
      // バリデーションの実行
      ValidatorFactory factory =  Validation.buildDefaultValidatorFactory();
      Validator validator = factory.getValidator();
      Set<ConstraintViolation<CommentForm>> violations = validator.validate(commentForm, ValidationPriority1.class);
      assertEquals(1, violations.size());
      assertEquals("コメントを入力してください", violations.iterator().next().getMessage());
    }
  }
}
