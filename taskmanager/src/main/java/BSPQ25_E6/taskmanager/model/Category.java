package BSPQ25_E6.taskmanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Category {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generate the ID
    private Long id;
	private String name;
	
	public Category() {
		super();
	}
	public Category(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public Category(String name) {
		// TODO Auto-generated constructor stub
		this.name=name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
