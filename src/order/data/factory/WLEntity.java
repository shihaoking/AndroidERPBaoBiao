package order.data.factory;


public class WLEntity {
	private String no;
	private String name;
	private int category;
	private String level;
	private String unit;
	
	public WLEntity() {
		// TODO Auto-generated constructor stub
		super();
	}

	public WLEntity(String no,String name,int category,String level,String unit) {
		super();
		this.no = no;
		this.name = name;
		this.category = category;
		this.level = level;
		this.unit = unit;
	}
	
	public void SetNo(String no){
		this.no = no;
	}
	
	public void SetName(String name){
		this.name = name;
	}
	
	public void SetCategory(int category) {
		this.category =category;
	}
	
	public void SetLevel(String level) {
		this.level = level;
	}
	
	public void SetUnit(String unit) {
		this.unit = unit;
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
	
	public String GetLevel() {
		return level;
	}
	
	public String GetUnit() {
		return unit;
	}
}
