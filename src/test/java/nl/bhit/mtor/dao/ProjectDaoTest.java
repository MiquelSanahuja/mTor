package nl.bhit.mtor.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.User;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

public class ProjectDaoTest extends BaseDaoTestCase {
	
    @Autowired
    private ProjectDao projectDao;

    @Test
    public void testGet() {
    	LOG.debug("Testing get...");
    	
    	Project project = projectDao.get(-1L);

        assertNotNull(project);
    }
    
    @Test
    public void testGetAll() {
    	LOG.debug("Testing get all...");
    	
    	List<Project> projects = projectDao.getAll();
    	assertNotNull(projects);
    	assertEquals(3, projects.size());
    }
    
    @Test
    public void testGetAllDistinct() {
    	LOG.debug("Testing get all distinct...");
    	
    	List<Project> projects = projectDao.getAllDistinct();
    	assertNotNull(projects);
    	assertEquals(3, projects.size());
    }
    
    @Test
    public void testProjectExists() {
    	LOG.debug("Testing project exists...");
    	
    	assertTrue(projectDao.exists(-1L));
    }
    
    @Test
    public void testProjectNotExists() {
    	LOG.debug("Testing project not exists...");
    	
    	assertFalse(projectDao.exists(-1000L));
    }
    
    @Test
    public void testAddProject() {
    	LOG.debug("Testing add project...");
    	
    	Project project = new Project("Mock Project");
    	Project savedProject = projectDao.save(project);
    	assertNotNull(savedProject);
    	assertNotNull(savedProject.getId());
    }
    
    @Test(expected=DataIntegrityViolationException.class)
    public void testAddProjectWithNullName() {
    	LOG.debug("Testing add project with same name...");
    	
    	Project project = new Project();
    	
    	// Should throw DataIntegrityViolationException
    	projectDao.save(project);
    }
    
    @Test(expected=DataIntegrityViolationException.class)
    public void testAddProjectWithSameName() {
    	LOG.debug("Testing add project with same name...");
    	
    	Project existingProject = projectDao.get(-1L);
    	Project project = new Project(existingProject.getName());
    	
    	// Should throw DataIntegrityViolationException
    	projectDao.save(project);
    }
    
    @Test
    public void testUpdateProject() {
    	LOG.debug("Testing update project...");
    	
    	Project updatableProject = projectDao.get(-1L);
    	boolean previousMonitoring = updatableProject.isMonitoring();
    	updatableProject.setMonitoring(!previousMonitoring);
    	Project updatedProject = projectDao.save(updatableProject);
    	assertEquals(!previousMonitoring, updatedProject.isMonitoring());
    }
    
    @Test
    public void testRemoveProject() {
    	LOG.debug("Testing remove project...");
    	
    	Project project = new Project("Remove Project");
    	Project removeProject = projectDao.save(project);
    	
    	List<Project> projectsBeforeRemove = projectDao.getAll();
    	int projectsSizeBeforeRemove = projectsBeforeRemove.size();
    	
    	projectDao.remove(removeProject);
    	
    	List<Project> projectsAfterRemove = projectDao.getAll();
    	assertEquals(projectsSizeBeforeRemove - 1, projectsAfterRemove.size());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testRemoveNotExistingProject() {
    	LOG.debug("Testing remove a not existing project...");
    	
    	// Should throw IllegalArgumentException: attempt to create delete event with null entity
    	projectDao.remove(-1000L);
    }
    
    @Test
    public void testGetProjectWithNonResolvedMessages() {
    	LOG.debug("Testing get project with non resolved messages...");
    	
    	List<Project> projects = projectDao.getWithNonResolvedMessages();
    	assertEquals(3, projects.size());
    }
    
    @Test
    public void testGetProjectWithNonResolvedMessagesByUser() {
    	LOG.debug("Testing get project with non resolved messages by user...");
    	
    	User user = new User();
    	user.setId(-1L);
    	List<Project> projects = projectDao.getWithNonResolvedMessages(user);
    	assertEquals(1, projects.size());
    }
    
    @Test
    public void testGetSortedAndPaginatedList() {
    	LOG.debug("Testing get sorted and paginated project list...");
    	
    	String sortField = "name";
    	boolean ascOrder = true;
    	Integer pageNumber = 0;
    	Integer pageSize = 1;
    	List<Project> projects = projectDao.getSortedAndPaginatedList(sortField, ascOrder, pageNumber, pageSize);
    	assertEquals(1, projects.size());
    }
    
}