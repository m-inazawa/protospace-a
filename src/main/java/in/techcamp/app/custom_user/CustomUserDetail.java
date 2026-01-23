package in.techcamp.app.custom_user;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import in.techcamp.app.entity.UserEntity;
import lombok.Data;

@Data
public class CustomUserDetail implements UserDetails {

    private final UserEntity user;

    // 定数：有効期限（90日）と警告開始（期限の7日前）
    private static final int EXPIRED_DAYS = 90;
    private static final int WARNING_DAYS_BEFORE = 7;

    public CustomUserDetail(UserEntity user) {
        this.user = user;
    }

    // パスワードが完全に期限切れ（90日経過）しているか判定
    public boolean isPasswordExpired() {
        if (user.getLastPasswordChange() == null) return true;

        LocalDateTime expireDate = user.getLastPasswordChange().plusDays(EXPIRED_DAYS);
        return LocalDateTime.now().isAfter(expireDate);
    }

    // パスワード期限が迫っている（残り7日以内）か判定
    public boolean isPasswordWarningPeriod() {
        if (user.getLastPasswordChange() == null) return true;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireDate = user.getLastPasswordChange().plusDays(EXPIRED_DAYS);
        LocalDateTime warningStartDate = expireDate.minusDays(WARNING_DAYS_BEFORE);

        // 期限切れではなく、かつ警告開始日を過ぎている場合
        return now.isAfter(warningStartDate) && now.isBefore(expireDate);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // ここを false にすると Spring Security がログインを拒否してしまうため、「ログイン後にポップアップを出す」という要件に合わせて常に true を返す。
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();//権限設定なし
    }

    public Integer getUserId() {
      return user.getId();
    }

    public String getLoginUserName() {
        return user.getUserName();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

}