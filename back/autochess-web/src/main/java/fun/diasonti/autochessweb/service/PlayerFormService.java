package fun.diasonti.autochessweb.service;

import fun.diasonti.autochessweb.data.entity.UserAccount;
import fun.diasonti.autochessweb.data.form.PlayerForm;
import fun.diasonti.autochessweb.data.mappers.PlayerFormMapper;
import fun.diasonti.autochessweb.data.mappers.base.BaseMapper;
import fun.diasonti.autochessweb.repository.UserAccountRepository;
import fun.diasonti.autochessweb.repository.base.BaseRepository;
import fun.diasonti.autochessweb.service.base.BaseFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerFormService extends BaseFormService<UserAccount, PlayerForm> {

    private final UserAccountRepository repository;
    private final PlayerFormMapper mapper;

    @Autowired
    public PlayerFormService(UserAccountRepository repository, PlayerFormMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    protected BaseRepository<UserAccount> getRepository() {
        return repository;
    }

    @Override
    protected BaseMapper<UserAccount, PlayerForm> getMapper() {
        return mapper;
    }

    @Transactional(readOnly = true)
    public PlayerForm getByUsername(String username) {
        return repository.findByUsername(username).map(this::entityToForm).orElse(null);
    }
}
