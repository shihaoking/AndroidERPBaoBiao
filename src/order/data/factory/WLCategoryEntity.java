package order.data.factory;


public class WLCategoryEntity {
	private String name;
	private int key;
	private int parentKey;
	private int category;
	
	public WLCategoryEntity() {
		// TODO Auto-generated constructor stub
		super();
	}

	public WLCategoryEntity(int key,int parentKey,String name, int category) {
		super();
		this.name = name;
		this.key =key;
		this.parentKey = parentKey;
		this.category = category;
	}
	
	
	public void SetName(String name){
		this.name = name;
	}
	
	public void SetKey(int key) {
		this.key =key;
	}
	
	public void SetParentKey(int parentKey) {
		this.parentKey = parentKey;
	}
	
	public void SetCategory(int category) {
		this.category = category;
	}
	
	public String GetName() {
		return name;
	}
	
	public int GetKey() {
		return key;
	}
	
	public int GetParentKey() {
		return parentKey;
	}
	
	public int GetCategory() {
		return category;
	}
}
