package eu._5gzorro.legalproserepository.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Objects;

@Component
@RequestScope
public class AuthData {
    private String userId = "AuthenticatedUserId"; // TODO: Extract from auth header/token in auth filter and set

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthData authData = (AuthData) o;
        return userId.equals(authData.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "AuthData{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
