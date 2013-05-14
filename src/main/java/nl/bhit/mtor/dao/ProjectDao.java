package nl.bhit.mtor.dao;

import java.util.List;

import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.User;

/**
 * An interface that provides a data management interface to the Project table.
 */
public interface ProjectDao extends GenericDao<Project, Long> {

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
     * @return all projects with messages
     */
    List<Project> getWithNonResolvedMessages();
    
    /**
     * Get projects list sorted and paginated by pageNumber and pageSize parameters. If pageNumber and pageSize aren't valid,
     * that is null or pageNumber lower than zero or pageSize lower than one, all projects will be returned.
     * 
     * @param sortField
     * 				Project field name by which it's going to sort.
     * @param ascOrder
     * 				Flag indicates if it's an ascendent order. True for ascendent order. False for descendant order.
     * @param pageNumber
     * 				Page number. Note that the first page will be index 0.
     * @param pageSize
     * 				Number of rows per page.
     * @return
     * 			Project list sorted and paginated. Null if sortField is not a valid Project entity field.
     */
    List<Project> getSortedAndPaginatedList(String sortField, boolean ascOrder, Integer pageNumber, Integer pageSize);
    
}