package fun.diasonti.autochessweb.config.security;

import fun.diasonti.autochessweb.config.security.data.AppUser;
import fun.diasonti.autochessweb.data.entity.UserAccount;
import fun.diasonti.autochessweb.data.form.UserAccountForm;
import fun.diasonti.autochessweb.data.mappers.UserAccountMapper;
import fun.diasonti.autochessweb.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;
    private final UserAccountMapper userAccountMapper;

    @Autowired
    public CustomUserDetailsService(UserAccountRepository userAccountRepository, UserAccountMapper userAccountMapper) {
        this.userAccountRepository = userAccountRepository;
        this.userAccountMapper = userAccountMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        final UserAccount userAccount = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        final UserAccountForm userAccountForm = userAccountMapper.entityToForm(userAccount);
        return new AppUser(userAccountForm);
    }
}
