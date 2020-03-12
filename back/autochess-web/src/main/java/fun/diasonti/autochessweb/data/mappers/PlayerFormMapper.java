package fun.diasonti.autochessweb.data.mappers;

import fun.diasonti.autochessweb.data.entity.UserAccount;
import fun.diasonti.autochessweb.data.form.PlayerForm;
import fun.diasonti.autochessweb.data.mappers.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper
public interface PlayerFormMapper extends BaseMapper<UserAccount, PlayerForm> {
}
