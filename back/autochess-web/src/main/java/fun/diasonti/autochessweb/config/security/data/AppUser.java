package fun.diasonti.autochessweb.config.security.data;

import com.google.common.collect.ImmutableSet;
import fun.diasonti.autochessweb.data.form.UserAccountForm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AppUser extends User {

    private final UserAccountForm userAccount;

    public AppUser(UserAccountForm userAccount) {
        super(userAccount.getUsername(), userAccount.getPassword(), ImmutableSet.of(new SimpleGrantedAuthority("USER")));
        this.userAccount = userAccount;
    }

    public UserAccountForm getUserAccount() {
        return userAccount;
    }
}
