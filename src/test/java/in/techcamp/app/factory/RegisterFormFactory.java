package in.techcamp.app.factory;

import com.github.javafaker.Faker;

import in.techcamp.app.form.RegisterForm;

public class RegisterFormFactory {
  private static final Faker faker = new Faker();

  public static RegisterForm createUser() {
    RegisterForm registerForm = new RegisterForm();

    registerForm.setEmail(faker.internet().emailAddress());
    registerForm.setPassword(faker.internet().password(6,12));
    registerForm.setPasswordConfirmation(registerForm.getPassword());

    String generatedUsername =faker.name().username();
    if (generatedUsername.length() > 6) {
        generatedUsername = generatedUsername.substring(0, 6);
    }

    registerForm.setUserName(generatedUsername);
    registerForm.setProfile(createUserProfile());
    registerForm.setAffiliation(faker.company().name());
    registerForm.setPosition(faker.job().position());
    return registerForm;
  }

  public static String createUserProfile() {
    return faker.job().keySkills() + "が得意です。";
  }
}
