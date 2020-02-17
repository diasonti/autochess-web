package fun.diasonti.autochessweb.data.mappers;

import fun.diasonti.autochessweb.data.entity.UserAccount;
import fun.diasonti.autochessweb.data.form.UserAccountForm;
import fun.diasonti.autochessweb.data.mappers.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper
public interface UserAccountMapper extends BaseMapper<UserAccount, UserAccountForm> {
}
