package fun.diasonti.autochessweb.data.entity;

import fun.diasonti.autochessweb.data.entity.base.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_account")
public class UserAccount extends BaseEntity {

    @Column(name = "username", unique = true, nullable = false, updatable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "whitePlayer")
    @OrderBy("finishedAt DESC")
    private List<MatchHistory> whiteMatchHistory;

    @OneToMany(mappedBy = "blackPlayer")
    @OrderBy("finishedAt DESC")
    private List<MatchHistory> blackMatchHistory;

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

    public List<MatchHistory> getWhiteMatchHistory() {
        if (whiteMatchHistory == null)
            whiteMatchHistory = new ArrayList<>();
        return whiteMatchHistory;
    }

    public List<MatchHistory> getBlackMatchHistory() {
        if (blackMatchHistory == null)
            blackMatchHistory = new ArrayList<>();
        return blackMatchHistory;
    }
}
