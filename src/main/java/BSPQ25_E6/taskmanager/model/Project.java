package BSPQ25_E6.taskmanager.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Project 
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToMany(mappedBy = "projects")
    private Set<User> users = new HashSet<>();

    @ManyToOne
    private User owner;

    public Project() {
    }

    public Project(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public Long getId() 
    {
        return id;
    }

    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }
    //in order to get the users of a project
    public Set<User> getUsers() 
    {
        return users;
    }

    public void setUsers(Set<User> users) 
    {
        this.users = users;
    }

    public User getOwner() 
    {
    return owner;
    }

    public void setOwner(User owner) 
    {
        this.owner = owner;
    }
}
