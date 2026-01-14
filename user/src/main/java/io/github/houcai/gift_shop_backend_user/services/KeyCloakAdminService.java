package io.github.houcai.gift_shop_backend_user.services;

import io.github.houcai.gift_shop_backend_user.dtos.UserRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class KeyCloakAdminService {

    @Value("${keycloak.admin.username}")
    private String adminUserName;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    @Value("${keycloak.admin.server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.admin.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    @Value("${keycloak.admin.client-uid}")
    private String clientUid;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAdminAccessToken(){
        // define headers for http entity.
        HttpHeaders headers = new HttpHeaders();
        // APPLICATION_FORM_URLENCODED: Request body is encoded like an HTML form submit.
        // For the /protocol/openid-connect/token endpoint,
        // OAuth2 expects parameters to be sent as application/x-www-form-urlencoded in the POST body.
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // application/x-www-form-urlencoded maps to a MultiValueMap<String, String>.
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("username", adminUserName);
        params.add("password", adminPassword);
        params.add("grant_type", "password");
        // create http entity
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        // ex. http://localhost:8443/realms/realm-name/protocol/openid-connect/token
        String url = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        return (String) (response.getBody() != null ? response.getBody().get("access_token") : "");
    }

    /**
     * Create a user in keycloak.
     * @param token the Jwt token.
     * @param userRequest a user creationg request including the info of the user.
     * @return The keycloak Id of the user.
     */
    public String createUser(String token, UserRequest userRequest){
        HttpHeaders headers = new HttpHeaders();
        // MediaType.APPLICATION_JSON maps naturally to a normal Map<String, Object>
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Map<String, Object> userPayload = new HashMap<>();

        userPayload.put("username", userRequest.getUsername());
        userPayload.put("email", userRequest.getEmail());
        userPayload.put("firstName", userRequest.getFirstName());
        userPayload.put("lastName", userRequest.getLastName());

        userPayload.put("enabled", true); // ??

        Map<String, Object> credential = new HashMap<>();
        credential.put("type", "password");
        credential.put("value", userRequest.getPassword());
        credential.put("temporary", false);

        userPayload.put("credentials", List.of(credential));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(userPayload, headers);

        String url = keycloakServerUrl + "/admin/realms/" + realm + "/users";

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        if (!HttpStatus.CREATED.equals(response.getStatusCode())) {
            throw new RuntimeException("Failed to create user in keycloak " + response.getBody());
        }

        // Extract Keycloak user id
        URI location = response.getHeaders().getLocation();
        if (location == null) {
            throw new RuntimeException("Keycloak did not return location header " + response.getBody());
        }

        String path = location.getPath();
        return path.substring(path.lastIndexOf("/") + 1);
    }

    /**
     * Use GET request to get the role representation using the keycloak api.
     * @param token the access token of the user which must have the role of view clients.
     * @param roleName
     * @return a map of (role id, role name) containing the requested client role.
     */
    private Map<String, Object> getClientRoleRepresentation(String token, String roleName) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String url = keycloakServerUrl + "/admin/realms/" + realm + "/clients/" + clientUid + "/roles/" + roleName;
        // the access token of the user must have roles like view clients.
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        return response.getBody();
    }

    /**
     * Assign an existing client role to a user.
     * @param username
     * @param roleName
     * @param userId
     */
    public void assignClientRoleToUser(String username, String roleName, String userId) {
        String token = getAdminAccessToken();
        Map<String, Object> roleRep = getClientRoleRepresentation(token, roleName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(List.of(roleRep), headers);

        // user id is the id of the newly created user, not the admin id!
        String url = keycloakServerUrl + "/admin/realms/" + realm +
                "/users/" + userId + "/role-mappings/clients/" + clientUid;
        ResponseEntity<Void> response = restTemplate.postForEntity(url, entity, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()){
            throw new RuntimeException("Failed to assign role" + roleName +
                    " to user " + username + ": HTTP " + response.getStatusCode());
        }

    }


}
