package org.opensingular.dbuserprovider.model;

public class UserConfiguration {
    private final String emailVerified;
    private final String enabled;

    public UserConfiguration(String emailVerified, String enabled) {
        this.emailVerified = emailVerified;
        this.enabled = enabled;
    }

    public String getEmailVerified() {
        return this.emailVerified;
    }

    public String getEnabled() {
        return this.enabled;
    }
}
