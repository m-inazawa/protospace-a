package in.techcamp.app.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import in.techcamp.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import in.techcamp.app.form.RegisterForm;
import in.techcamp.app.entity.UserEntity;

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
    System.out.println("★この先のエラーはUserService以降の処理内容に関連したエラーです。");

    String encodedPassword = passwordEncoder.encode(registerForm.getPassword());
    System.out.println("★encodedパスワード: " + encodedPassword);
    userEntity.setPassword(encodedPassword);

    userEntity.setUserName(registerForm.getUserName());
    userEntity.setProfile(registerForm.getProfile());
    userEntity.setAffiliation(registerForm.getAffiliation());
    userEntity.setPosition(registerForm.getPosition());
    System.out.println("★Formから取得した内容をSet後のuserEntityの内容(この時点で、idとcreatedAtがnullであることは問題ないです。):" + userEntity);
    userRepository.insertUser(userEntity);
  }
}