package fun.diasonti.autochessweb.data.entity;

import fun.diasonti.autochessweb.data.entity.base.BaseEntity;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_account")
public class UserAccount extends BaseEntity {

    @Column(name = "username", unique = true, nullable = false, updatable = false)
    private String username;

    @Column(name = "email", unique = true, nullable = false, updatable = false)
    private String email;

    @Column(name = "email_confirmed", nullable = false)
    private boolean emailConfirmed;

    @Column(name = "email_confirmation_token", nullable = false)
    private String emailConfirmationToken;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "whitePlayer")
    @OrderBy("finishedAt DESC")
    private List<MatchHistory> whiteMatchHistory;

    @OneToMany(mappedBy = "blackPlayer")
    @OrderBy("finishedAt DESC")
    private List<MatchHistory> blackMatchHistory;

    @Column(name = "rank")
    private int rank;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public String getEmailConfirmationToken() {
        return emailConfirmationToken;
    }

    public void setEmailConfirmationToken(String emailConfirmationToken) {
        this.emailConfirmationToken = emailConfirmationToken;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Transient
    public MatchHistory getLastFinishedMatch() {
        MatchHistory whiteMatch = null;
        MatchHistory blackMatch = null;

        if (!getWhiteMatchHistory().isEmpty())
            whiteMatch = getWhiteMatchHistory().get(0);
        if (!getBlackMatchHistory().isEmpty())
            blackMatch = getBlackMatchHistory().get(0);

        if (whiteMatch == null || blackMatch == null)
            return ObjectUtils.firstNonNull(whiteMatch, blackMatch);

        return whiteMatch.getFinishedAt().isAfter(blackMatch.getFinishedAt()) ? whiteMatch : blackMatch;
    }
}
