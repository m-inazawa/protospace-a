package in.techcamp.app.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import in.techcamp.app.entity.UserEntity;
import in.techcamp.app.form.RegisterForm;
import in.techcamp.app.repository.UserRepository;
import lombok.AllArgsConstructor;

@Transactional
@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  public void createUserWithEncryptedPassword(RegisterForm registerForm, BindingResult result) {
    registerForm.validatePasswordConfirmation(result);

    if (userRepository.findByEmail(registerForm.getEmail())) {
      result.rejectValue("email", null,"ご入力いただいたメールアドレスは既に使用されています。");
    }

    if (result.hasErrors()) {
      return; 
    }

    UserEntity userEntity = new UserEntity();
    userEntity.setEmail(registerForm.getEmail());

    String encodedPassword = passwordEncoder.encode(registerForm.getPassword());
    userEntity.setPassword(encodedPassword);

    userEntity.setUserName(registerForm.getUserName());
    userEntity.setProfile(registerForm.getProfile());
    userEntity.setAffiliation(registerForm.getAffiliation());
    userEntity.setPosition(registerForm.getPosition());
    userRepository.insertUser(userEntity);
  }

  public void updateUser(RegisterForm registerForm, Integer userId) {
    // 1. 現在のユーザー情報をDBから取得
    UserEntity userEntity = userRepository.findByUserId(userId);
      if (userEntity == null) {
      throw new RuntimeException("ユーザーが見つかりませんでした。");
    }

    // 2. 基本情報の詰め替え
    userEntity.setUserName(registerForm.getUserName());
    userEntity.setEmail(registerForm.getEmail());
    userEntity.setProfile(registerForm.getProfile());
    userEntity.setAffiliation(registerForm.getAffiliation());
    userEntity.setPosition(registerForm.getPosition());

    // 3. パスワードの判定と更新
    String inputPassword = registerForm.getPassword();
    String currentEncodedPassword = userEntity.getPassword(); // DBにある現在のパスワード

    if (inputPassword != null && !inputPassword.isEmpty()) {
        
        // 入力されたパスワードが現在のパスワードと「異なる」かチェック
        if (!passwordEncoder.matches(inputPassword, currentEncodedPassword)) {
            // 新しいパスワードを暗号化してセット
            userEntity.setPassword(passwordEncoder.encode(inputPassword));
            
            // ★パスワードが変更されたので、更新日時を「今」に設定
            userEntity.setLastPasswordChange(LocalDateTime.now());
        } else {
            // 同じパスワードの場合は、パスワードフィールドも更新日時もそのまま
            // (userEntity.setPassword は実行しないことで、無駄な上書きを防ぐ)
            System.out.println("パスワードに変更がないため、期限日は更新しません。");
        }
    }

    // 4. 画面から送られてきたバージョンをセット（楽観ロック用）
    userEntity.setVersion(registerForm.getVersion());

    // 更新実行
    int updatedRows = userRepository.updateUser(userEntity);

    // 更新行数が0なら、楽観ロックエラーを投げる
    if (updatedRows == 0) {
      throw new org.springframework.dao.OptimisticLockingFailureException("競合エラー：他のユーザーが更新しました。");
    }
  }
}