package controller;

import domain.Child;
import service.facade.ChildFacade;
import controller.util.HeaderUtil;
import app.security.Secured;
import org.slf4j.Logger;
import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * REST controller for managing Child.
 */
@Path("/api/child")
@Secured
public class ChildController {

    @Inject
    private Logger log;

    @Inject
    private ChildFacade childFacade;

    /**
     * POST : Create a new child.
     *
     * @param child the child to create
     * @return the Response with status 201 (Created) and with body the new
     * child, or with status 400 (Bad Request) if the child has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @POST
    public Response createChild(Child child) throws URISyntaxException {
        log.debug("REST request to save Child : {}", child);
        childFacade.create(child);
        return HeaderUtil.createEntityCreationAlert(Response.created(new URI("/resources/api/child/" + child.getId())),
                "child", child.getId().toString())
                .entity(child).build();
    }

    /**
     * PUT : Updates an existing child.
     *
     * @param child the child to update
     * @return the Response with status 200 (OK) and with body the updated
     * child, or with status 400 (Bad Request) if the child is not valid, or
     * with status 500 (Internal Server Error) if the child couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PUT
    public Response updateChild(Child child) throws URISyntaxException {
        log.debug("REST request to update Child : {}", child);
        childFacade.edit(child);
        return HeaderUtil.createEntityUpdateAlert(Response.ok(), "child", child.getId().toString())
                .entity(child).build();
    }

    /**
     * GET : get all the children.
     *
     * @return the Response with status 200 (OK) and the list of children in
     * body
     *
     */
    @GET
    public List<Child> getAllChildren() {
        log.debug("REST request to get all Children");
        List<Child> children = childFacade.findAll();
        return children;
    }

    /**
     * GET /:id : get the "id" child.
     *
     * @param id the id of the child to retrieve
     * @return the Response with status 200 (OK) and with body the child, or
     * with status 404 (Not Found)
     */
    @Path("/{id}")
    @GET
    public Response getChild(@PathParam("id") Long id) {
        log.debug("REST request to get Child : {}", id);
        Child child = childFacade.find(id);
        return Optional.ofNullable(child)
                .map(result -> Response.status(Response.Status.OK).entity(child).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * DELETE /:id : remove the "id" child.
     *
     * @param id the id of the child to delete
     * @return the Response with status 200 (OK)
     */
    @Path("/{id}")
    @DELETE
    public Response removeChild(@PathParam("id") Long id) {
        log.debug("REST request to delete Child : {}", id);
        childFacade.remove(childFacade.find(id));
        return HeaderUtil.createEntityDeletionAlert(Response.ok(), "child", id.toString()).build();
    }

}
