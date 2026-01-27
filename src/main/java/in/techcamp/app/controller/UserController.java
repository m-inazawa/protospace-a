package in.techcamp.app.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import in.techcamp.app.custom_user.CustomUserDetail;
import in.techcamp.app.entity.PrototypeEntity;
import in.techcamp.app.entity.UserEntity;
import in.techcamp.app.form.FollowForm;
import in.techcamp.app.form.RegisterForm;
import in.techcamp.app.repository.FollowRepository;
import in.techcamp.app.repository.PrototypeRepository;
import in.techcamp.app.repository.UserRepository;
import in.techcamp.app.service.UserService;
import in.techcamp.app.validation.ValidationOrder;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;



@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
  private final UserService userService;
  private final UserRepository userRepository;
  private final PrototypeRepository prototypeRepository;
  private final FollowRepository followRepository;

  @GetMapping("/login")
  public String showLogin(@RequestParam(value = "error", required = false) String error, Model model) {
    if (error != null) {
      model.addAttribute("loginError", "メールアドレスもしくはパスワードが間違っています。");
    }
    return ("users/login");
  }

  @GetMapping("/register")
  public String showUserForm(Model model) {
    model.addAttribute("registerForm", new RegisterForm());
    return ("users/register");
  }

  @PostMapping("/register")
  public String registerUser(@ModelAttribute("registerForm") @Validated({ValidationOrder.class}) RegisterForm registerForm, BindingResult result, Model model) {
    try {
      userService.createUserWithEncryptedPassword(registerForm, result);
      if (result.hasErrors()) {
        model.addAttribute("registerError", result);
        return "users/register";
      }
    } catch (Exception e) {
      model.addAttribute("registerError", "システムエラーにより操作を完了できませんでした。");
      return "users/register";
    }
      
    return "redirect:/users/login";
  }

  @GetMapping("/{userId}")
  public String showUserDetail(
    @PathVariable("userId") Integer userId,
    Model model,
    @AuthenticationPrincipal CustomUserDetail currentUser
  ) {
    UserEntity userEntity = userRepository.findByUserId(userId);
    List<PrototypeEntity> prototypeEntity = prototypeRepository.findByUserId(userId);
    FollowForm followForm = new FollowForm();
    followForm.setFollowCounts(followRepository.getFollowCount(userId));
    followForm.setFollowerCounts(followRepository.getFollowerCount(userId));
    if (currentUser != null) {
      boolean exists = followRepository.isExistedByPrimaryKey(currentUser.getUserId(), userId);
      followForm.setIsFollow(exists ? "follow" : "unFollow");
    }
    model.addAttribute("follows", followForm);
    model.addAttribute("user", userEntity);
    model.addAttribute("prototypes", prototypeEntity);
    return "users/detail";
  }

  @PostMapping("/follow/{followerId}/{followFlg}")
  public ResponseEntity<Void> setFollow(
    @PathVariable("followerId") Integer followerId,
    @PathVariable("followFlg") String followFlg,
    @AuthenticationPrincipal CustomUserDetail currentUser
  ) {
    // フォローかフォロー解除か判定
    if (followFlg.equals("unFollow")) {
      // インサートのリポジトリ実行
      followRepository.insert(currentUser.getUserId(), followerId);
    } else if (followFlg.equals("follow")) {
      // デリートのリポジトリ実行
      followRepository.delete(currentUser.getUserId(), followerId);
    }

    return ResponseEntity.ok().build();
  }

  @GetMapping("/edit")
  public String showEditForm(@AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    // ログイン中のユーザー情報をDBから取得（最新のversionを取得するため）
    UserEntity userEntity = userRepository.findByUserId(currentUser.getUserId());
    
    // Entityの値をFormにコピー（一括でセットしてくれる）
    RegisterForm registerForm = new RegisterForm();
    BeanUtils.copyProperties(userEntity, registerForm);
    // versionもセット（RegisterFormにversionフィールドを追加済みであること）
    registerForm.setVersion(userEntity.getVersion());

    model.addAttribute("registerForm", registerForm);
    return "users/edit"; // 編集用のHTML
  }

  @PostMapping("/update")
  public String updateUser(
      @ModelAttribute("registerForm") @Validated({ValidationOrder.class}) RegisterForm registerForm, 
      BindingResult result, 
      @AuthenticationPrincipal CustomUserDetail currentUser,
      HttpSession session,
      Model model) {

    // パスワード確認チェック
    registerForm.validatePasswordConfirmation(result);

    if (result.hasErrors()) {
      return "users/edit";
    }

    try {
      // ユーザー情報の更新（引数にログインユーザーIDを渡す）
      userService.updateUser(registerForm, currentUser.getUserId());
      
      // 更新に成功したら、パスワード期限切れポップアップ用のセッションを消去
      session.removeAttribute("PWD_STATUS");

    } catch (OptimisticLockingFailureException e) {
      // 楽観ロックエラーのキャッチ
      model.addAttribute("registerError", "他の端末で更新されたため、保存できませんでした。一度画面を読み直してください。");
      return "users/edit";
    } catch (Exception e) {
      model.addAttribute("registerError", "システムエラーにより更新に失敗しました。");
      return "users/edit";
    }

    return "redirect:/users/" + currentUser.getUserId();
  }

  @PostMapping("/clear-password-status")
  @ResponseBody // 画面遷移させないために必要
  public void clearPasswordStatus(HttpSession session) {
    // 警告(WARNING)の時だけセッションから消す
    // ※EXPIRED（期限切れ）は強制なので、消さずに毎回出しても良いかもしれません
    String status = (String) session.getAttribute("PWD_STATUS");
    if ("WARNING".equals(status)) {
        session.removeAttribute("PWD_STATUS");
    }
  }
}
