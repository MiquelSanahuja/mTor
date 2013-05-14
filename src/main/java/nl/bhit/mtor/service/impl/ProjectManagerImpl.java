package nl.bhit.mtor.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.bhit.mtor.dao.ProjectDao;
import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.User;
import nl.bhit.mtor.service.ProjectManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("projectManager")
public class ProjectManagerImpl extends GenericManagerImpl<Project, Long> implements ProjectManager {
    ProjectDao projectDao;

    @Autowired
    public ProjectManagerImpl(ProjectDao projectDao) {
        super(projectDao);
        this.projectDao = projectDao;
    }

    @Override
    public List<Project> getWithNonResolvedMessages(User user) {
        return projectDao.getWithNonResolvedMessages(user);
    }

    public List<Project> getWithNonResolvedMessages() {
        return projectDao.getWithNonResolvedMessages();
    }

	@Override
	public List<Map<String, String>> getJSONProjectsPage(String sortField, boolean ascOrder, Integer pageNumber, Integer pageSize) {
		List<Map<String, String>> jsonProjectsInfo = new ArrayList<Map<String,String>>();
		
		List<Project> projects = projectDao.getSortedAndPaginatedList(sortField, ascOrder, pageNumber, pageSize);
		if (projects == null) {
			return jsonProjectsInfo;
		}
		
		String userNames;
		Map<String, String> mapAux;
		for (final Project p : projects) {
			mapAux = new HashMap<String, String>();
			mapAux.put("id", String.valueOf(p.getId()));
			mapAux.put("name", p.getName());
			mapAux.put("status", p.statusOfProject());
    		userNames = "[";
    		if (p.userNames() != null) {
    			int i = 0;
    			int n = p.userNames().size() - 1;
    			Iterator<String> itName = p.userNames().iterator();
    			while (itName.hasNext()) {
    				userNames += itName.next();
    				if (i++ < n) {
    					userNames += ", ";
    				}
    			}
    		}
    		userNames += "]";
    		mapAux.put("usernames", userNames);
    		mapAux.put("monitoring", String.valueOf(p.isMonitoring()));
			jsonProjectsInfo.add(mapAux);
		}
		
		return jsonProjectsInfo;
	}
}