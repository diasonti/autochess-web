package fun.diasonti.autochessweb.data.mappers.base;

import fun.diasonti.autochessweb.data.entity.base.BaseEntity;
import fun.diasonti.autochessweb.data.form.base.BaseForm;

public interface BaseMapper<E extends BaseEntity, F extends BaseForm> {
    E formToEntity(F form);
    F entityToForm(E form);
}
