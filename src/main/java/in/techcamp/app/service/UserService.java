package in.techcamp.app.service;

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
}