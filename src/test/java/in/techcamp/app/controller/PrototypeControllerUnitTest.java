package in.techcamp.app.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import in.techcamp.app.custom_user.CustomUserDetail;
import in.techcamp.app.entity.CommentEntity;
import in.techcamp.app.entity.PrototypeEntity;
import in.techcamp.app.entity.UserEntity;
import in.techcamp.app.repository.PrototypeRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PrototypeControllerUnitTest {
  @Mock
  private PrototypeRepository prototypeRepository;

  @InjectMocks
  private PrototypeController prototypeController;

  @Test
  public void 一覧表示機能にリクエストするとプロトタイプ一覧表示のビューファイルがレスポンスで返ってくる() {
    UserEntity user = new UserEntity(); 
    CustomUserDetail currentUser = new CustomUserDetail(user);
    Model model = new ExtendedModelMap();

    String result = prototypeController.showPrototypes(currentUser, model);

    assertThat(result, is("prototype/index"));

  }

  @Test
  public void 一覧表示機能にリクエストするとレスポンスに投稿済みのプロトタイプがすべて含まれること() {
    PrototypeEntity prototype1 = new PrototypeEntity();
    prototype1.setId(1);
    prototype1.setPrototypeName("プロトタイプ1");
    prototype1.setCatchCopy("キャッチコピー1");

    PrototypeEntity prototype2 = new PrototypeEntity();
    prototype2.setId(2);
    prototype2.setPrototypeName("プロトタイプ2");
    prototype2.setCatchCopy("キャッチコピー2");

    List<PrototypeEntity> expectedPrototypeList = Arrays.asList(prototype1, prototype2);

    when(prototypeRepository.findAll()).thenReturn(expectedPrototypeList);

    Model model = new ExtendedModelMap();
    prototypeController.showPrototypes(null, model);

    assertThat(model.getAttribute("prototypes"), is(expectedPrototypeList));
  }

  @Test
  public void 新規投稿機能にリクエストすると新規投稿画面のビューファイルがレスポンスで返ってくる() {
    Model model = new ExtendedModelMap();

    String result = prototypeController.showPrototypeNew(model);

    assertThat(result, is("prototype/new"));
  }

  @Test
  public void 編集機能にリクエストすると編集画面のビューファイルがレスポンスで返ってくる() {
    Model model = new ExtendedModelMap();
    Integer prototypeId = 1;
    CustomUserDetail mockUser = mock(CustomUserDetail.class);
    when(mockUser.getUserId()).thenReturn(1);

    PrototypeEntity mockPrototype = new PrototypeEntity();
    UserEntity mockUserEntity = new UserEntity();
    mockUserEntity.setId(1);
    mockPrototype.setUser(mockUserEntity);

    when(prototypeRepository.findById(prototypeId)).thenReturn(mockPrototype);

    String result = prototypeController.showPrototypeEdit(prototypeId, mockUser, model);

    assertThat(result, is("prototype/edit"));
  }

  @Test
  public void 詳細表示機能にリクエストすると詳細画面のビューファイルがレスポンスで返ってくる() {
    Model model = new ExtendedModelMap();
    Integer prototypeId = 1;

    PrototypeEntity mockPrototype = new PrototypeEntity();
    mockPrototype.setId(prototypeId);
    mockPrototype.setPrototypeName("プロトタイプ１");

    List<CommentEntity> comments = new ArrayList<>();
    // コメントデータをセット
    CommentEntity comment = new CommentEntity();
    comment.setComment("テストのコメント");
    comment.setPrototypeId(prototypeId);
    comments.add(comment);
    comment.setComment("テストのコメント");
    comment.setPrototypeId(prototypeId);
    comments.add(comment);
    // リストをmockPrototypeに追加
    mockPrototype.setComments(comments);

    when(prototypeRepository.findById(prototypeId)).thenReturn(mockPrototype);

    String result = prototypeController.showPrototypeDetail(prototypeId, model);
    
    assertThat(result, is("prototype/detail"));
    assertThat(model.getAttribute("prototype"), is(mockPrototype));
  }
}
