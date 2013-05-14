package nl.bhit.mtor.service;

import java.util.List;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.User;

@Path("/projects")
public interface ProjectManager extends GenericManager<Project, Long> {

    /**
     * Get project with non resolved messages
     * 
     * @param user
     *            to filter on
     * @return all projects with messages for this user.
     */
    List<Project> getWithNonResolvedMessages(User user);

    /**
     * Get project with non resolved messages
     * 
     * @return all projects with non resolved messages.
     */
    List<Project> getWithNonResolvedMessages();
    
    /**
     * Get projects info sorted and paginated.
     * 
     * @param sortField
     * 				Project field name by which it's going to sort.
     * @param ascOrder
     * 				Flag indicates if it's an ascendent order. True for ascendent order. False for descendant order.
     * @param pageNumber
     * 				Page number.
     * @param pageSize
     * 				Number of rows per page.
     * @return
     * 			Projects info in JSON format. Example:
     * 				[
     * 					{"id":"-3","monitoring":"false","usernames":"[Matt Raible]","status":"ERROR","name":"testProject3"},
     * 					{"id":"-2","monitoring":"false","usernames":"[Matt Raible]","status":"ERROR","name":"testProject2"}
     * 				]
     */
    @GET
    @Path("/table")
    @Produces(MediaType.APPLICATION_JSON)
    List<Map<String, String>> getJSONProjectsPage(@DefaultValue("id") @QueryParam("sortField") String sortField, 
    											  @DefaultValue("true") @QueryParam("ascOrder") boolean ascOrder, 
    											  @QueryParam("pageNumber") Integer pageNumber, 
    											  @QueryParam("pageSize") Integer pageSize);

}