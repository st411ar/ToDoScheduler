package by.intervale.task.bean;

public class Item {
	private String id;
	private String date;
	private String name;
	private String status;
	private String description;

	public Item(
			String id, 
			String date, 
			String name, 
			String status, 
			String description
	) {
		this.id = id;
		this.date = date;
		this.name = name;
		this.status = status;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}