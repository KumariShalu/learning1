package controller;

import service.facade.ChildFacade;
import domain.Child;
import static java.util.Collections.singletonMap;
import java.util.List;
import javax.ejb.EJB;
import static javax.ws.rs.client.Entity.json;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.jboss.arquillian.container.test.api.Deployment;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.valid4j.matchers.http.HttpResponseMatchers.hasContentType;
import static org.valid4j.matchers.http.HttpResponseMatchers.hasStatus;

/**
 * Test class for the ChildController REST controller.
 *
 */
public class ChildControllerTest extends ApplicationTest {

    private static final String DEFAULT_NAME = "A";
    private static final String UPDATED_NAME = "B";
    private static final String RESOURCE_PATH = "api/child";

    @Deployment
    public static WebArchive createDeployment() {
        return buildApplication().addClass(Child.class).addClass(ChildFacade.class).addClass(ChildController.class);
    }

    private static Child child;

    @EJB
    private ChildFacade childFacade;

    @Test
    @InSequence(1)
    public void createChild() throws Exception {

        int databaseSizeBeforeCreate = childFacade.findAll().size();

        // Create the Child
        child = new Child();
        child.setName(DEFAULT_NAME);
        Response response = target(RESOURCE_PATH).post(json(child));
        assertThat(response, hasStatus(Status.CREATED));
        child = response.readEntity(Child.class);

        // Validate the Child in the database
        List<Child> children = childFacade.findAll();
        assertThat(children.size(), is(databaseSizeBeforeCreate + 1));
        Child testChild = children.get(children.size() - 1);
        assertThat(testChild.getName(), is(DEFAULT_NAME));
    }

    @Test
    @InSequence(2)
    public void getAllChildren() throws Exception {

        int databaseSize = childFacade.findAll().size();
        // Get all the children
        Response response = target(RESOURCE_PATH).get();
        assertThat(response, hasStatus(Status.OK));
        assertThat(response, hasContentType(MediaType.APPLICATION_JSON_TYPE));

        List<Child> children = response.readEntity(List.class);
        assertThat(children.size(), is(databaseSize));
    }

    @Test
    @InSequence(3)
    public void getChild() throws Exception {

        // Get the child
        Response response = target(RESOURCE_PATH + "/{id}", singletonMap("id", child.getId())).get();
        Child testChild = response.readEntity(Child.class);
        assertThat(response, hasStatus(Status.OK));
        assertThat(response, hasContentType(MediaType.APPLICATION_JSON_TYPE));
        assertThat(testChild.getId(), is(child.getId()));
        assertThat(testChild.getName(), is(DEFAULT_NAME));
    }

    @Test
    @InSequence(4)
    public void getNonExistingChild() throws Exception {

        // Get the child
        Response response = target(RESOURCE_PATH + "/{id}", singletonMap("id", Long.MAX_VALUE)).get();
        assertThat(response, hasStatus(Status.NOT_FOUND));
    }

    @Test
    @InSequence(5)
    public void updateChild() throws Exception {

        int databaseSizeBeforeUpdate = childFacade.findAll().size();

        // Update the child
        Child updatedChild = new Child();
        updatedChild.setId(child.getId());
        updatedChild.setName(UPDATED_NAME);

        Response response = target(RESOURCE_PATH).put(json(updatedChild));
        assertThat(response, hasStatus(Status.OK));

        // Validate the Child in the database
        List<Child> children = childFacade.findAll();
        assertThat(children.size(), is(databaseSizeBeforeUpdate));
        Child testChild = children.get(children.size() - 1);
        assertThat(testChild.getName(), is(UPDATED_NAME));
    }

    @Test
    @InSequence(6)
    public void removeChild() throws Exception {

        int databaseSizeBeforeDelete = childFacade.findAll().size();

        // Delete the child
        Response response = target(RESOURCE_PATH + "/{id}", singletonMap("id", child.getId())).delete();
        assertThat(response, hasStatus(Status.OK));

        // Validate the database is empty
        List<Child> children = childFacade.findAll();
        assertThat(children.size(), is(databaseSizeBeforeDelete - 1));
    }

}
