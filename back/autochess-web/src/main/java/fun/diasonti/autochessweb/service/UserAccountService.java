package fun.diasonti.autochessweb.service;

import fun.diasonti.autochessweb.data.entity.UserAccount;
import fun.diasonti.autochessweb.data.form.UserAccountForm;
import fun.diasonti.autochessweb.data.mappers.UserAccountMapper;
import fun.diasonti.autochessweb.data.mappers.base.BaseMapper;
import fun.diasonti.autochessweb.data.pojo.ValidationErrors;
import fun.diasonti.autochessweb.gateway.EmailGateway;
import fun.diasonti.autochessweb.repository.UserAccountRepository;
import fun.diasonti.autochessweb.repository.base.BaseRepository;
import fun.diasonti.autochessweb.service.base.BaseFormService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserAccountService extends BaseFormService<UserAccount, UserAccountForm> {

    private final static Pattern emailPattern = Pattern.compile("^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])$");

    private final UserAccountRepository repository;
    private final UserAccountMapper mapper;
    private final EmailGateway emailGateway;
    private final PasswordEncoder passwordEncoder;

    @Value("${autochess.front.hostname}")
    private String frontHostname;

    @Autowired
    public UserAccountService(UserAccountRepository repository, UserAccountMapper mapper, EmailGateway emailGateway, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.emailGateway = emailGateway;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected BaseRepository<UserAccount> getRepository() {
        return repository;
    }

    @Override
    protected BaseMapper<UserAccount, UserAccountForm> getMapper() {
        return mapper;
    }

    @Transactional(readOnly = true)
    public UserAccountForm getByUsername(String username) {
        return repository.findByUsername(username).map(this::entityToForm).orElse(null);
    }

    public void cleanForm(UserAccountForm form) {
        form.setEmail(StringUtils.lowerCase(form.getEmail()));
    }

    @Transactional(readOnly = true)
    public ValidationErrors validateForm(UserAccountForm form) {
        final ValidationErrors errors = new ValidationErrors();

        // Username
        if (StringUtils.isEmpty(form.getUsername())) {
            errors.addError("username", "invalid");
        } else {
            final boolean usernameTaken = repository.findFirstByUsername(form.getUsername())
                    .filter(userAccount -> !userAccount.getId().equals(form.getId()))
                    .isPresent();
            if (usernameTaken) {
                errors.addError("username", "taken");
            }
        }

        // Email
        if (StringUtils.isEmpty(form.getEmail()) || !emailPattern.matcher(form.getEmail()).matches()) {
            errors.addError("email", "invalid");
        } else {
            final boolean emailTaken = repository.findFirstByEmail(form.getEmail())
                    .filter(userAccount -> !userAccount.getId().equals(form.getId()))
                    .isPresent();
            if (emailTaken) {
                errors.addError("email", "taken");
            }
        }

        // Password
        if (StringUtils.isEmpty(form.getPassword()) || !emailPattern.matcher(form.getEmail()).matches()) {
            errors.addError("password", "invalid");
        } else if (form.getPassword().length() < 6) {
            errors.addError("password", "weak");
        }
        return errors;
    }

    @Transactional
    public void createNewUser(UserAccountForm form) {
        form.setId(null);
        final UserAccount userAccount = getMapper().formToEntity(form);
        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        userAccount.setEmailConfirmationToken(UUID.randomUUID().toString());
        repository.save(userAccount);
        sendEmailConfirmation(userAccount.getEmail(), userAccount.getEmailConfirmationToken());
    }

    @Transactional
    public boolean attemptEmailConfirmation(String email, String token) {
        final UserAccount userAccount = repository.findByEmail(email).orElse(null);
        if (userAccount != null && userAccount.getEmailConfirmationToken().equals(token)) {
            userAccount.setEmailConfirmed(true);
            return true;
        }
        return false;
    }

    private void sendEmailConfirmation(String email, String token) {
        final String link = frontHostname + "/registration?confirmationEmail="+email+"&token=" + token;
        final String text = "<h4>Your confirmation link: <a href='"+link+"'>"+link+"</a></h4>";
        emailGateway.send(email, "Email confirmation", text);
    }
}
