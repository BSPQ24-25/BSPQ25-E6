package BSPQ25_E6.taskmanager.integration;

import static org.junit.jupiter.api.Assertions.*;

import BSPQ25_E6.taskmanager.model.Project;
import BSPQ25_E6.taskmanager.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProjectIntegrationTest 
{

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateProjectAndAssignUser() 
    {
        String randomUUID = UUID.randomUUID().toString();

        //UNIQUE USEr
        User user = new User("Carlos-" + randomUUID, "carlos-" + randomUUID + "@email.com", "password123");
        ResponseEntity<User> userResponse = restTemplate.postForEntity("/users/register", user, User.class);
        assertEquals(HttpStatus.CREATED, userResponse.getStatusCode());
        User createdUser = userResponse.getBody();
        assertNotNull(createdUser);
        Long userId = createdUser.getId();

        //NOW NEEED DTO 
        Map<String, Object> projectRequest = new HashMap<>();
        projectRequest.put("name", "Project Integration " + randomUUID);
        projectRequest.put("description", "Integration");
        projectRequest.put("ownerId", userId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(projectRequest, headers);

        ResponseEntity<Project> projectResponse = restTemplate.postForEntity("/projects/create", request, Project.class);
        assertEquals(HttpStatus.OK, projectResponse.getStatusCode());

        Project createdProject = projectResponse.getBody();
        assertNotNull(createdProject);
        Long projectId = createdProject.getId();

        //aSSIGN USER TO PROJECT    
        ResponseEntity<String> assignResponse = restTemplate.postForEntity("/projects/" + projectId + "/addUser/" + userId, null, String.class);
        assertEquals(HttpStatus.OK, assignResponse.getStatusCode());

        //VERIFY USER ASSIGNMENT
        ResponseEntity<Project> projectGetResponse = restTemplate.getForEntity("/projects/" + projectId, Project.class);
        assertEquals(HttpStatus.OK, projectGetResponse.getStatusCode());
        Project fetchedProject = projectGetResponse.getBody();
        assertNotNull(fetchedProject);
        assertFalse(fetchedProject.getUsers().isEmpty());

        boolean userFound = fetchedProject.getUsers().stream()
                .anyMatch(u -> u.getId().equals(userId));
        assertTrue(userFound, "Assigned user should be part of the project users list");
    }
}
