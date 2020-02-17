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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final BaseForm other = (BaseForm) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            } else {
                return super.equals(obj);
            }
        } else {
            return id.equals(other.id);
        }
    }

    @Override
    public int hashCode() {
        if (id == null)
            return super.hashCode();
        return Objects.hash(id);
    }
}
