package Lesson_7.source.server.auth;

import java.util.Objects;

public class AuthEntry {
    private final String log;
    private final String nickname;
    private final String password;

    public String getLog() {
        return log;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public AuthEntry(String log, String nickname, String password) {
        this.log = log;
        this.nickname = nickname;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthEntry authEntry = (AuthEntry) o;
        return Objects.equals(log, authEntry.log) && Objects.equals(nickname, authEntry.nickname) &&
                Objects.equals(password, authEntry.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(log, nickname, password);
    }
}
