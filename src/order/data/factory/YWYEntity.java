package order.data.factory;


public class YWYEntity {
	private String no;
	private String name;
	private int level;
	
	public YWYEntity() {
		// TODO Auto-generated constructor stub
		super();
	}

	public YWYEntity(String no,String name, int level) {
		super();
		this.no = no;
		this.name = name;
		this.level = level;
	}
	
	public void SetNo(String no){
		this.no = no;
	}
	
	public void SetName(String name){
		this.name = name;
	}
	
	public void SetLevel(int level) {
		this.level = level;
	}
	
	
	public String GetNo() {
		return no;
	}
	
	public String GetName() {
		return name;
	}
	
	public int GetLevel() {
		return level;
	}
	
}
