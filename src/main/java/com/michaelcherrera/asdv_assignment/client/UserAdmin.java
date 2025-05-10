package com.michaelcherrera.asdv_assignment.client;

import com.michaelcherrera.asdv_assignment.entities.User;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.text.MessageFormat;
import java.util.List;

/**
 * Jersey REST client generated for REST resource:UserFacadeREST [com.michaelcherrera.asdv_assignment.entities.user]<br>
 * USAGE:
 * <pre>
 *        UserAdmin client = new UserAdmin();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Michael C. Herrera
 */
public class UserAdmin {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/ASDVAssignment/resources";

    public UserAdmin() {

        client = ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("com.michaelcherrera.asdv_assignment.entities.user");
    }

    public String countREST() throws ClientErrorException {
        try {
            WebTarget resource = webTarget;
            resource = resource.path("count");
            return resource.request(MediaType.TEXT_PLAIN).get(String.class);
        } catch (Throwable t) {
            return "";
        }
    }

    public void edit(Object requestEntity, String id) throws ClientErrorException {

        Response response = webTarget.path(MessageFormat.format("{0}", id)).request(MediaType.APPLICATION_JSON).put(
                Entity.entity(requestEntity, MediaType.APPLICATION_JSON));
        if (response.getStatus() < 200 || response.getStatus() >= 300)
            throw new RuntimeException();
    }

    public <T> T find(Class<T> responseType, String id) throws ClientErrorException {

        WebTarget resource = webTarget;
        resource = resource.path(MessageFormat.format("{0}", id));
        return resource.request(MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T findRange(Class<T> responseType, String from, String to) throws ClientErrorException {

        WebTarget resource = webTarget;
        resource = resource.path(MessageFormat.format("{0}/{1}", from, to));
        return resource.request(MediaType.APPLICATION_JSON).get(responseType);
    }

    public void create(Object requestEntity) throws ClientErrorException {

        Response response = webTarget.request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(requestEntity, MediaType.APPLICATION_JSON));
        if (response.getStatus() < 200 || response.getStatus() >= 300)
            throw new RuntimeException();
    }

    public <T> T findAll(Class<T> responseType) throws ClientErrorException {

        WebTarget resource = webTarget;
        return resource.request(MediaType.APPLICATION_JSON).get(responseType);
    }

    public void remove(String id) throws ClientErrorException {

        Response response = webTarget.path(MessageFormat.format("{0}", id)).request().delete();
        if (response.getStatus() < 200 || response.getStatus() >= 300)
            throw new RuntimeException();
    }

    public void close() {client.close();}

    /**
     * Find all usernames.
     *
     * @return A list with all usernames
     * @throws ClientErrorException if there is a client request error (HTTP 4xx status codes)
     */
    public List<String> findAllNames() throws ClientErrorException {

        WebTarget resource = webTarget;
        resource = resource.path("usernames");
        return resource.request(MediaType.APPLICATION_JSON).get(List.class);
    }

    /**
     * Find a user by username.
     *
     * @param username the Username
     * @return the User
     * @throws ClientErrorException if there is a client request error (HTTP 4xx status codes)
     */
    public User findByUsername(String username) throws ClientErrorException {

        WebTarget resource = webTarget;
        resource = resource.path("usernames/{0}");
        return resource.resolveTemplate("0", username).request(MediaType.APPLICATION_JSON).get(User.class);
    }
}
