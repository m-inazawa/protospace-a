package in.techcamp.app.validation;

import jakarta.validation.GroupSequence;

@GroupSequence({ ValidationPriority1.class, ValidationPriority2.class})
public interface ValidationOrder {
  // 実際に処理を行うものではないため、中身は空で問題なし
}
