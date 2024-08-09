package com.mmdevelopement.crm.infrastructure.keycloak;

import com.mmdevelopement.crm.config.database.properties.KeycloakClientProperties;
import com.mmdevelopement.crm.config.security.context.RequestContextHolder;
import com.mmdevelopement.crm.domain.user.entity.dto.UserDto;
import com.mmdevelopement.crm.domain.user.entity.enums.UserRoles;
import com.mmdevelopement.crm.infrastructure.exceptions.BadRequestException;
import com.mmdevelopement.crm.infrastructure.exceptions.ResourceNotFoundException;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KeycloakAdminService {

    private final Keycloak keycloak;
    private final KeycloakClientProperties keycloakClientProperties;

    public KeycloakAdminService(@Autowired KeycloakClientProperties keycloakClientProperties) {
        this.keycloakClientProperties = keycloakClientProperties;

        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakClientProperties.getServerUrl())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .realm(keycloakClientProperties.getRealm())
                .clientId(keycloakClientProperties.getClientId())
                .clientSecret(keycloakClientProperties.getSecret())
                .build();

        keycloak.tokenManager().getAccessToken();
    }

    public Response createUser(UserDto userDTO) {
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(userDTO.getPassword());

        UserRepresentation user = new UserRepresentation();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        List<String> roles = userDTO.getRoles().stream()
                .map(UserRoles::getRoleString)
                .toList();
        user.setRealmRoles(roles);

        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("tenant_id", Collections.singletonList(RequestContextHolder.getCurrentTenantId()));
        user.setAttributes(attributes);

        UsersResource instance = getUserResourceInstance();
        return instance.create(user);
    }

    public UserRepresentation getUserByEmail(String email) {
        UsersResource usersResource = getUserResourceInstance();
        return usersResource.search(email).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found in Keycloak"));
    }

    public void updateUser(String userId, UserDto userDTO) {
        UserRepresentation user = getUserResourceInstance().get(userId).toRepresentation();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());

        updateUserRoles(userId, userDTO.getRoles().stream()
                .map(UserRoles::getRoleString)
                .collect(Collectors.toSet()));

        getUserResourceInstance().get(userId).update(user);
    }

    public void updateUserPassword(String userId, String password) {
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(password);

        UserRepresentation user = getUserResourceInstance().get(userId).toRepresentation();
        user.setCredentials(Collections.singletonList(credential));

        getUserResourceInstance().get(userId).update(user);
    }

    public void deleteUser(String userId) {
        UsersResource userResource = getUserResourceInstance();
        userResource.get(userId).remove();
    }

    private void updateUserRoles(String userId, Set<String> newRoles) {
        UserRepresentation user = getUserResourceInstance().get(userId).toRepresentation();

        Set<String> oldRoles = new HashSet<>(user.getRealmRoles());
        Set<String> roles = new HashSet<>(user.getRealmRoles().retainAll(newRoles) ? oldRoles : Set.of());
        roles.addAll(newRoles.removeAll(oldRoles) ? newRoles : Set.of());

        user.setRealmRoles(roles.stream().toList());
    }

    public UsersResource getUserResourceInstance() {
        return keycloak.realm(keycloakClientProperties.getRealm())
                .users();
    }

    public void validateUserPassword(String userGuid, String oldPassword) {
        UserRepresentation user = getUserResourceInstance().get(userGuid).toRepresentation();
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(oldPassword);

        if (!user.getCredentials().contains(credential)) {
            throw new BadRequestException("Invalid password provided");
        }
    }
}
