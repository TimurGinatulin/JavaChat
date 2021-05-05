package Lesson_7.source.server.auth;

import java.util.Set;

public class AuthenticationService {
    private static final Set<AuthEntry> entries = Set.of(
            new AuthEntry("l1", "nick_1", "p1"),
            new AuthEntry("l2", "nick_2", "p2"),
            new AuthEntry("l3", "nick_3", "p3")
    );

    public AuthEntry findUserByCredentials(String login, String password) {
        for (AuthEntry entry : entries) {
            if (entry.getLog().equals(login) && entry.getPassword().equals(password)) {
                return entry;
            }
        }
        return null;
    }
}
