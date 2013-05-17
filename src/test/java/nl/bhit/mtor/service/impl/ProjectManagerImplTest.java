package nl.bhit.mtor.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.bhit.mtor.dao.ProjectDao;
import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.User;

import org.jmock.Expectations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProjectManagerImplTest extends BaseManagerMockTestCase {
	
    // ~ Instance fields ========================================================
	private ProjectManagerImpl projectManager;
	private ProjectDao projectDao;
	
    // ~ Methods ================================================================
    @Before
    public void setUp() {
    	projectDao = context.mock(ProjectDao.class);
    	projectManager = new ProjectManagerImpl(projectDao);
    }
    
    @After
    public void tearDown() {
    	projectManager = null;
    }
    
    @Test
    public void testGet() {
    	LOG.debug("Testing get...");
    	
    	final Long id = -1L;
        final Project project = new Project("Mock Project");

        // set expected behavior on dao
        context.checking(new Expectations() {
            {
            	oneOf(projectDao).get(with(equal(id)));
                will(returnValue(project));
            }
        });

        Project result = projectManager.get(id);
        assertSame(project, result);
    }
    
    @Test
    public void testGetAll() {
    	LOG.debug("Testing get all...");
    	
    	final List<Project> projects = new ArrayList<Project>();
    	
        // set expected behavior on dao
        context.checking(new Expectations() {
            {
                oneOf(projectDao).getAll();
                will(returnValue(projects));
            }
        });

        List<Project> result = projectManager.getAll();
        assertSame(projects, result);
    }
    
    @Test
    public void testGetWithNonResolvedMessages() {
    	LOG.debug("Testing get with non resolved messages...");
    	
    	final List<Project> projects = new ArrayList<Project>();
    	
        // set expected behavior on dao
        context.checking(new Expectations() {
            {
                oneOf(projectDao).getWithNonResolvedMessages();
                will(returnValue(projects));
            }
        });

        List<Project> result = projectManager.getWithNonResolvedMessages();
        assertSame(projects, result);
    }
    
    @Test
    public void testGetWithNonResolvedMessagesByUser() {
    	LOG.debug("Testing get with non resolved messages by user...");
    	
    	final User user = new User("Mock User");
    	final List<Project> projects = new ArrayList<Project>();
    	
        // set expected behavior on dao
        context.checking(new Expectations() {
            {
                oneOf(projectDao).getWithNonResolvedMessages(user);
                will(returnValue(projects));
            }
        });

        List<Project> result = projectManager.getWithNonResolvedMessages(user);
        assertSame(projects, result);
    }
    
    @Test
    public void testGetJSONProjectsPage() {
    	LOG.debug("Testing get JSON projects page...");
    	
    	final List<Project> projects = new ArrayList<Project>();
    	Project project = new Project("Mock Project");
    	project.setId(-1L);
    	project.setMonitoring(true);
    	projects.add(project);
    	
    	final String sortField = "name";
    	final boolean ascOrder = true;
    	final Integer pageNumber = 0;
    	final Integer pageSize = 1;
    	final List<Map<String, String>> infoProjects = new ArrayList<Map<String,String>>();
    	Map<String, String> mapProject = new HashMap<String, String>();
    	mapProject.put("id", String.valueOf(project.getId()));
    	mapProject.put("name", project.getName());
		mapProject.put("status", "ERROR");
		mapProject.put("usernames", "[]");
		mapProject.put("monitoring", String.valueOf(project.isMonitoring()));
		infoProjects.add(mapProject);
		
		// set expected behavior on dao
        context.checking(new Expectations() {
            {
                oneOf(projectDao).getSortedAndPaginatedList(sortField, ascOrder, pageNumber, pageSize);
                will(returnValue(projects));
            }
        });
        
        List<Map<String, String>> result = projectManager.getJSONProjectsPage(sortField, ascOrder, pageNumber, pageSize);
        assertNotNull(result);
        assertEquals(infoProjects.size(), result.size());
        assertTrue(result.get(0) != null);
        assertEquals(infoProjects.get(0).size(), result.get(0).size());
        assertEquals(infoProjects.get(0).get("id"), result.get(0).get("id"));
        assertEquals(infoProjects.get(0).get("name"), result.get(0).get("name"));
        assertEquals(infoProjects.get(0).get("status"), result.get(0).get("status"));
        assertEquals(infoProjects.get(0).get("usernames"), result.get(0).get("usernames"));
        assertEquals(infoProjects.get(0).get("monitoring"), result.get(0).get("monitoring"));
    }
    
    @Test
    public void testSaveProject() {
    	LOG.debug("Testing save...");
    	
    	final Project project = new Project("Mock Project");
    	
        // set expected behavior on dao
        context.checking(new Expectations() {
            {
                oneOf(projectDao).save(with(same(project)));
                will(returnValue(project));
            }
        });

        Project result = projectManager.save(project);
        assertSame(project, result);
    }
    
    @Test
    public void testRemoveProject() {
        LOG.debug("Testing remove...");

        final Long id = -1L;
        
        // set expected behavior on dao
        context.checking(new Expectations() {
            {
                oneOf(projectDao).remove(with(equal(id)));
            }
        });

        projectManager.remove(id);
    }
    
}
