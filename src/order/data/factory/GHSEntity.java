package order.data.factory;

public class GHSEntity {
	private String no;
	private String name;
	private int category;
	
	public GHSEntity(){
		super();
	}
	
	public GHSEntity(String no,String name,int category) {
		this.no =no;
		this.name = name;
		this.category = category;
	}
	
	
	public void SetNo(String no) {
		this.no = no;
	}
	
	public void SetName(String name) {
		this.name = name;
	}
	 
	public void SetCategory(int category) {
		this.category = category;
	}
	
	public String GetNo() {
		return no;
	}
	
	public String GetName() {
		return name;
	}
	
	public int GetCategory() {
		return category;
	}
}
