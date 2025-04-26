package BSPQ25_E6.taskmanager.model;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
@Entity
@Table(name = "app_user")
public class User 
    {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generate the ID
    private Long id;
    private String username;
    private String email;
    private String password;

    @ManyToMany
    //we create a table for the many-to-many relationship between users and projects
    @JoinTable
    (
        name = "user_project",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects = new HashSet<>();
    
    public User() 
    {
    }

    public User(String username, String email, String password) 
    {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() 
    {
        return username;
    }

    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getEmail() 
    {
        return email;
    }

    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getPassword() 
    {
        return password;
    }

   
    public void setPassword(String password) 
    {
        this.password = password;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    public Set<Project> getProjects() 
    {
        return projects;
    }
    
    public void setProjects(Set<Project> projects) 
    {
        this.projects = projects;
    }
    
}