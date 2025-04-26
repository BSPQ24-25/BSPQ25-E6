package BSPQ25_E6.taskmanager.integration;

import static org.junit.jupiter.api.Assertions.*;

import BSPQ25_E6.taskmanager.model.Project;
import BSPQ25_E6.taskmanager.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProjectIntegrationTest 
{

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateProjectAndAssignUser() 
    {
        //create a new user
        User user = new User("Carlos", "carlos@email.com", "password123");
        ResponseEntity<User> userResponse = restTemplate.postForEntity("/api/users/register", user, User.class);
        assertEquals(HttpStatus.CREATED, userResponse.getStatusCode());
        User createdUser = userResponse.getBody();
        assertNotNull(createdUser);
        Long userId = createdUser.getId();

        //create a new project
        Project project = new Project("Proyect Integration", "Integration");
        ResponseEntity<Project> projectResponse = restTemplate.postForEntity("/projects/create", project, Project.class);
        assertEquals(HttpStatus.OK, projectResponse.getStatusCode());
        Project createdProject = projectResponse.getBody();
        assertNotNull(createdProject);
        Long projectId = createdProject.getId();

        //assgin user to project
        ResponseEntity<String> assignResponse = restTemplate.postForEntity("/projects/" + projectId + "/addUser/" + userId, null, String.class);
        assertEquals(HttpStatus.OK, assignResponse.getStatusCode());

        
        
    }
}
