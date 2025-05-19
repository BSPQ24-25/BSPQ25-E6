package BSPQ25_E6.taskmanager.dto;

/**
 * DTO para crear un proyecto con nombre, descripción y ownerId.
 */
public class ProjectRequestDTO 
{

    private String name;
    private String description;
    private Long ownerId;

    public ProjectRequestDTO() 
    {
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

    public Long getOwnerId() 
    {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) 
    {
        this.ownerId = ownerId;
    }
}
