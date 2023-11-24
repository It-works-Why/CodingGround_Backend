package app.codingGround.api.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER")
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_NUM")
    private Long userNum;

    @Column(unique = true, name = "USER_ID", length = 20, nullable = false)
    private String userId;

    @Column(name = "USER_PASSWORD", length = 100, nullable = false)
    private String userPassword;

    @Column(unique = true, name = "USER_NICKNAME", length = 20, nullable = false)
    private String userNickname;

    @Column(unique = true, name = "USER_EMAIL", length = 100, nullable = false)
    private String userEmail;

    @Column(name = "USER_AFFILIATION", length = 30, nullable = false)
    private String userAffiliation;

    @Column(name = "USER_AFFILIATION_DETAIL", length = 30, nullable = false)
    private String userAffiliationDetail;

    @Column(name = "USER_PROFILE_IMG", length = 100, nullable = false)
    private String userProfileImg;

    @Column(name = "USER_REGDATE", nullable = false)
    private Timestamp userRegDate;

    @Column(name = "USER_STATUS", length = 10, nullable = false)
    private String userStatus;

    @Column(name = "USER_ROLE", length = 10, nullable = false)
    private String userRole;


    @Override
    public String getPassword() {
        return userPassword;
    }

    // 우리는 userName을 쓰지않음 사용되지 않는 메서드
    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @PrePersist
    protected void onCreate() {
        userRegDate = new Timestamp(new Date().getTime());
        userStatus = "ACTIVE";
        userRole = "USER";
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userRole));
    }
}