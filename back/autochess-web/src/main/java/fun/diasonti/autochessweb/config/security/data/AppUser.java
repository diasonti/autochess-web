package fun.diasonti.autochessweb.config.security.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.ImmutableList;
import fun.diasonti.autochessweb.data.form.UserAccountForm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.stream.Collectors;

@JsonIgnoreProperties({"password", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled"})
public class AppUser extends User {

    public AppUser(UserAccountForm userAccount) {
        this(userAccount.getUsername(), userAccount.getPassword(), ImmutableList.of("PLAYER"));
    }

    public AppUser(String username, Collection<String> authorities) {
        this(username, "[PROTECTED]", authorities);
    }

    public AppUser(String username, String password, Collection<String> authorities) {
        super(username, password, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
    }
}
