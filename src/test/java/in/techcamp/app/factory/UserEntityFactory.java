package in.techcamp.app.factory;

import com.github.javafaker.Faker;

import in.techcamp.app.entity.UserEntity;

public class UserEntityFactory {
  private static final Faker faker = new Faker();

  public static UserEntity createUser() {
    UserEntity userEntity = new UserEntity();

    userEntity.setEmail(faker.internet().emailAddress());
    userEntity.setPassword(faker.internet().password(6,12));

    String generatedUsername =faker.name().username();
    if (generatedUsername.length() > 6) {
        generatedUsername = generatedUsername.substring(0, 6);
    }

    userEntity.setUserName(generatedUsername);
    userEntity.setProfile(createUserProfile());
    userEntity.setAffiliation(faker.company().name());
    userEntity.setPosition(faker.job().position());
    return userEntity;
  }

  public static String createUserProfile() {
    return faker.job().keySkills() + "が得意です。";
  }

}
