package fun.diasonti.autochessweb.data.form;

import fun.diasonti.autochessweb.data.form.base.BaseForm;

import java.util.Objects;

public class UserAccountForm extends BaseForm {

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccountForm)) return false;
        if (!super.equals(o)) return false;
        UserAccountForm that = (UserAccountForm) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username);
    }

    @Override
    public String toString() {
        return "UserAccountForm{" +
                "username='" + username + '\'' +
                ", password=[PROTECTED]" +
                '}';
    }

}
