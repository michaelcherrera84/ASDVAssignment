package com.michaelcherrera.asdv_assignment.service;

import com.michaelcherrera.asdv_assignment.entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

/**
 * UserFacadeREST is a stateless RESTful web service that provides CRUD and query operations for
 * {@link com.michaelcherrera.asdv_assignment.entities.User} entities.
 *
 * <p>This class extends {@link AbstractFacade} to inherit generic JPA-based entity management,
 * and exposes the functionality via JAX-RS annotations for web service access.</p>
 *
 * <p>It uses the {@code my_persistence_unit} persistence unit to interact with the database,
 * and supports operations such as creating, editing, deleting, finding, and counting users, as well as custom endpoints
 * to retrieve all usernames or find a user by their username.</p>
 *
 * <p>Base URI path: {@code /com.michaelcherrera.asdv_assignment.entities.user}</p>
 *
 * @author michaelcherrera
 * @see AbstractFacade
 * @see com.michaelcherrera.asdv_assignment.entities.User
 */
@Stateless
@Path("com.michaelcherrera.asdv_assignment.entities.user")
public class UserFacadeREST extends AbstractFacade<User> {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    public UserFacadeREST() {super(User.class);}

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(User entity) {super.create(entity);}

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, User entity) {super.edit(entity);}

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {super.remove(super.find(id));}

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public User find(@PathParam("id") Integer id) {return super.find(id);}

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<User> findAll() {return super.findAll();}

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<User> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {

        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {return String.valueOf(super.count());}

    @Override
    protected EntityManager getEntityManager() {return em;}

    /**
     * Find All usernames.
     *
     * @return a List of all usernames
     */
    @GET
    @Path("usernames")
    @Produces({MediaType.APPLICATION_JSON})
    @Override
    public List<String> findAllNames() {return super.findAllNames();}

    /**
     * Find by username.
     *
     * @return the User
     */
    @GET
    @Path("usernames/{userName}")
    @Produces({MediaType.APPLICATION_JSON})
    @Override
    public User findByUsername(@PathParam("userName") String userName) {return super.findByUsername(userName);}

}
