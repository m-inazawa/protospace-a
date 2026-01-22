package in.techcamp.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.techcamp.app.custom_user.CustomUserDetail;
import in.techcamp.app.entity.ImageEntity;
import in.techcamp.app.entity.PrototypeEntity;
import in.techcamp.app.form.PrototypeForm;
import in.techcamp.app.repository.ImageRepository;
import in.techcamp.app.repository.PrototypeRepository;
import in.techcamp.app.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PrototypeService {
  private final UserRepository userRepository;
  private final PrototypeRepository prototypeRepository;
  private final ImageRepository imageRepository;

  @Transactional
  public void createPrototype(PrototypeForm prototypeForm, CustomUserDetail currentUser) throws Exception {
    // エンティティを作成
    PrototypeEntity prototype = createPrototypeEntity(prototypeForm, currentUser);

    // プロトタイプをインサート
    prototypeRepository.insert(prototype);
    // イメージをエンティティにセット
    ImageEntity image = new ImageEntity();
    // フォーム内の画像のバイト配列を取得
    byte[] byteImage = prototypeForm.getImage().getBytes();
    image.setImage(byteImage);
    image.setPrototypeId(prototype.getId());
    
    // イメージをインサート
    imageRepository.insert(image);
  }

  @Transactional
  public void updatePrototype(PrototypeForm prototypeForm, CustomUserDetail currentUser, Integer prototypeId) throws Exception {
    // エンティティを作成
    PrototypeEntity prototype = createPrototypeEntity(prototypeForm, currentUser);
    prototype.setId(prototypeId);
    // プロトタイプをアップデート、戻り値（更新件数）を受け取る
    int count = prototypeRepository.update(prototype);

    // ★追記：更新件数が0なら、誰かが先に更新した（＝バージョンが不一致）とみなして例外を投げる
    if (count == 0) {
        throw new org.springframework.dao.OptimisticLockingFailureException("競合エラー：他のユーザーが更新しました。");
    }
    // イメージをエンティティにセット
    ImageEntity image = imageRepository.findByImageId(prototypeId);
    byte[] byteImage = prototypeForm.getImage().getBytes();
    image.setImage(byteImage);

    // イメージをアップデート
    imageRepository.update(image);

  }

  // プロトタイプエンティティ作成のプライベートメソッド
  private PrototypeEntity createPrototypeEntity(PrototypeForm prototypeForm, CustomUserDetail currentUser) {
    PrototypeEntity prototype = new PrototypeEntity();
    prototype.setUser(userRepository.findByUserId(currentUser.getUserId()));
    prototype.setPrototypeName(prototypeForm.getPrototypeName());
    prototype.setConcept(prototypeForm.getConcept());
    prototype.setCatchCopy(prototypeForm.getCatchCopy());

    return prototype;
  }
}
