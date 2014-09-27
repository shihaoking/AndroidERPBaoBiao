package order.data.factory;


public class BMEntity {
	private String no;
	private String name;
	private int key;
	private int parentKey;
	
	public BMEntity() {
		// TODO Auto-generated constructor stub
		super();
	}

	public BMEntity(String no,String name, int key,int parentKey) {
		super();
		this.no = no;
		this.name = name;
		this.key =key;
		this.parentKey = parentKey;
	}
	
	public void SetNo(String no){
		this.no = no;
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
	
	public String GetNo() {
		return no;
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
}
