package app.codingGround.api.account.entitiy;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User")
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_NUM")
    private Long userNum;

    @Column(name = "USER_ID", length = 20, nullable = false)
    private String userId;

    @Column(name = "USER_PASSWORD", length = 100, nullable = false)
    private String userPassword;

    @Column(name = "USER_NAME", length = 20, nullable = false)
    private String userName;

    @Column(name = "USER_EMAIL", length = 100, nullable = false)
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
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userName;
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
    }
}