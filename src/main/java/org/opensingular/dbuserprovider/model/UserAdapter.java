package org.opensingular.dbuserprovider.model;

import lombok.extern.jbosslog.JBossLog;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JBossLog
public class UserAdapter extends AbstractUserAdapterFederatedStorage {
    public static final String GROUPS_ATTRIBUTE = "groups";
    public static final String GROUPS_DELIMITER = ",";
    private final UserConfiguration userConfiguration;
    private final String keycloakId;
    private final Map<String, String> data;
    private String username;

    public UserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, Map<String, String> data,
            boolean allowDatabaseToOverwriteKeycloak, UserConfiguration userConfiguration) {
        super(session, realm, model);
        this.keycloakId = StorageId.keycloakId(model, data.get("id"));
        this.userConfiguration = userConfiguration;
        this.username = data.get("username");
        this.data = data;
        try {
            Map<String, List<String>> attributes = this.getAttributes();
            for (Entry<String, String> e : data.entrySet()) {
                Set<String> newValues = new HashSet<>();
                if (!allowDatabaseToOverwriteKeycloak) {
                    List<String> attribute = attributes.get(e.getKey());
                    if (attribute != null) {
                        newValues.addAll(attribute);
                    }
                }
                newValues.add(StringUtils.trimToNull(e.getValue()));
                this.setAttribute(e.getKey(), newValues.stream().filter(Objects::nonNull).collect(Collectors.toList()));
            }
        } catch (Exception e) {
            log.errorv(e, "UserAdapter constructor, username={0}", this.username);
        }
    }

    @Override
    public String getId() {
        return keycloakId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    private static boolean isTrueish(String s) {
        return s != null && (s.equals("1") || s.toLowerCase().startsWith("t"));
    }
    @Override
    public boolean isEnabled() {
        String enabled = this.data.get(this.userConfiguration.getEnabled());
        log.infov("isEnabled: {0}", enabled);
        return isTrueish(enabled);
    }

    @Override
    public boolean isEmailVerified() {
        String emailVerified = this.data.get(this.userConfiguration.getEmailVerified());
        log.infov("emailVerified: {0}", emailVerified);
        return isTrueish(emailVerified);
    }

    private String[] getLocalGroupNames() {
        return Objects.requireNonNullElse(this.data.get(GROUPS_ATTRIBUTE), "")
                .split(GROUPS_DELIMITER);
    }

    @Override
    protected Set<GroupModel> getGroupsInternal() {
        final Set<GroupModel> groups = super.getGroupsInternal();
        final String[] localGroupNames = getLocalGroupNames();

        if (localGroupNames.length > 0 ) {
            final Set<GroupModel> allGroups = Arrays.stream(localGroupNames).map(groupName -> KeycloakModelUtils.findGroupByPath(realm, groupName)).filter(Objects::nonNull).collect(Collectors.toSet());
            allGroups.addAll(groups);
            return allGroups;
        } else {
            return groups;
        }
}

}
