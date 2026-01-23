package in.techcamp.app.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import in.techcamp.app.custom_user.CustomUserDetail;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {

        // 1. ログインしたユーザー情報を取得
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        HttpSession session = request.getSession(); //ここでセットした値をJavaScriptで読み取ってポップアップの種類を切り替える。

        // 2. 判定ロジックを呼び出し、セッションに状態を保存
        if (userDetail.isPasswordExpired()) {
            session.setAttribute("PWD_STATUS", "EXPIRED");
        } else if (userDetail.isPasswordWarningPeriod()) {
            // 【期限間近】フラグを立てる（遷移先は通常通り）
            session.setAttribute("PWD_STATUS", "WARNING");
        }

        // 3. 通常の遷移先（デフォルトのトップ画面など）へ移動
        super.onAuthenticationSuccess(request, response, authentication);
    } 
  }
