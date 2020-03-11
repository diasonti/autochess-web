package fun.diasonti.autochessweb.data.form.base;

import java.io.Serializable;
import java.util.Objects;

public abstract class BaseForm implements Serializable {

    private static final long serialVersionUID = 7490760140489959L;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseForm baseForm = (BaseForm) o;
        return Objects.equals(id, baseForm.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
