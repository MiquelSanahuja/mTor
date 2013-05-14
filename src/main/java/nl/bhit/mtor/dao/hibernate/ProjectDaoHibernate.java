package nl.bhit.mtor.dao.hibernate;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;

import nl.bhit.mtor.dao.ProjectDao;
import nl.bhit.mtor.model.MTorMessage;
import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.User;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository("projectDao")
public class ProjectDaoHibernate extends GenericDaoHibernate<Project, Long> implements ProjectDao {

    public ProjectDaoHibernate() {
        super(Project.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Project> getWithNonResolvedMessages(User user) {
        Query query = getSession().createQuery(
                "select p as project from Project as p left join p.users as u where u = :user");
        query.setLong("user", user.getId());
        List<Project> result = query.list();
        addNonResolved(result);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Project> getWithNonResolvedMessages() {
        String queryString = "from Project";
        LOG.debug("getting all projects: " + queryString);
        Query query = getSession().createQuery(queryString);
        List<Project> result = query.list();
        addNonResolved(result);
        return result;
    }

    @SuppressWarnings("unchecked")
    private void addNonResolved(List<Project> projects) {
        String queryString = "select m as message from MTorMessage as m where m.project.id  = :projectId AND  m.resolved = :resolved";
        LOG.trace("retrieving messages with query: " + queryString);
        if (projects == null) {
            return;
        }
        for (Project project : projects) {
            LOG.trace("retrieving messages for project: " + project);
            Query query = getSession().createQuery(queryString);
            query.setLong("projectId", project.getId());
            query.setBoolean("resolved", false);
            HashSet<MTorMessage> messges = new HashSet<MTorMessage>(query.list());
            project.setMessages(messges);
        }
    }

	@Override
	public List<Project> getSortedAndPaginatedList(String sortField, boolean ascOrder, Integer pageNumber, Integer pageSize) {
		if (sortField == null) {
			return null;
		}
		Field pField = FieldUtils.getDeclaredField(Project.class, sortField, true);
		if (pField == null) {
			return null;
		}
		
		StringBuilder sbQuery = new StringBuilder("from Project as p order by p.");
		sbQuery.append(sortField).append(" ").append(ascOrder ? "asc" : "desc");
		Query query = getSession().createQuery(sbQuery.toString());
		if (pageNumber != null && pageNumber >= 0 && pageSize != null && pageSize > 0) {
			query.setMaxResults(pageSize);
			query.setFirstResult(pageSize * pageNumber);
		}
		@SuppressWarnings("unchecked")
		List<Project> page = query.list();
		
		return page;
	}
}
