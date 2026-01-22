package in.techcamp.app.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.MatcherAssert;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DirectFieldBindingResult;

import in.techcamp.app.entity.PrototypeEntity;
import in.techcamp.app.entity.UserEntity;
import in.techcamp.app.form.RegisterForm;
import in.techcamp.app.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserControllerUnitTest {

  @BeforeEach
  public void setup() {
  }

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserController userController;

  @Nested
  class ユーザー管理機能の正常系{
    @Test
    public void ログイン画面のGETリクエストの戻り値がログイン画面Viewファイルであること() {
      Model model = new ExtendedModelMap();
      String errorMessage = "";
      String result = userController.showLogin(errorMessage, model);
      MatcherAssert.assertThat(result, Matchers.is("users/login"));
    }

    @Test
    public void ユーザー新規登録画面のGETリクエストの戻り値がユーザー新規登録画面Viewファイルであること() {
      Model model = new ExtendedModelMap();
      String result = userController.showUserForm(model);
      MatcherAssert.assertThat(result, Matchers.is("users/register"));
    }

    @Test
    public void ユーザー新規登録に成功した場合の戻り値がログイン画面Viewファイルであること() {
      Model model = new ExtendedModelMap();
      RegisterForm form = new RegisterForm();
      BindingResult bindingResult = new DirectFieldBindingResult(form, "registerForm");
      String result = userController.registerUser(form, bindingResult, model);
      MatcherAssert.assertThat(result, Matchers.is("redirect:/users/login"));
    }
  }

  @Nested
  class ユーザー管理機能の異常系 {
    @Test
    public void ユーザー新規登録に失敗した場合の戻り値がユーザー新規登録画面Viewファイルであること() {
      Model model = new ExtendedModelMap();
      RegisterForm form = new RegisterForm();
      BindingResult bindingResult = new DirectFieldBindingResult(form, "registerForm");
      bindingResult.reject(null, "ご入力いただいたメールアドレスは既に使用されています。");
      String viewName = userController.registerUser(form, bindingResult, model);
      MatcherAssert.assertThat(viewName, Matchers.is("/users/register"));
    }
  }


  @Test
  public void ユーザ詳細取得機能の戻り値がビューファイルであること() {
    Model model = new ExtendedModelMap();
    Integer testUserId = 1;
    String result = userController.showUserDetail(testUserId, model);
    assertEquals(result, "users/detail");
  }

  @Test
  public void ユーザ情報とプロトタイプのリストを1件取得() {
    Integer testUserId = 1;

    UserEntity userEntity = new UserEntity();
    userEntity.setId(1);
    userEntity.setUserName("テストユーザー");
    userEntity.setEmail("test@example.com");
    userEntity.setProfile("テストプロフィール");
    userEntity.setAffiliation("テスト所属");
    userEntity.setPosition("テスト役職");
    userEntity.setCreatedAt(LocalDateTime.now());

    List<PrototypeEntity> prototypes = new ArrayList<>();
    PrototypeEntity prototypeEntity1 = new PrototypeEntity();
    prototypeEntity1.setCatchCopy("テストカテゴリー");
    prototypeEntity1.setConcept("テストコンセプト");
    prototypeEntity1.setPrototypeName("テストプロトタイプ名");
    prototypeEntity1.setId(1);
    prototypeEntity1.setCreatedAt(LocalDateTime.now());
    prototypeEntity1.setUpdatedAt(LocalDateTime.now());
    prototypes.add(prototypeEntity1);

    userEntity.setPrototypes(prototypes);

    when(userRepository.findByUserId(testUserId)).thenReturn(userEntity);

    Model model = new ExtendedModelMap();
    userController.showUserDetail(testUserId, model);

    assertThat(model.getAttribute("user"), is(userEntity));

  }
}
