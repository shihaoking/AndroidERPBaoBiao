package order.data.factory;

import android.R.string;

public class CGDWLEntity extends WLEntity {

	private String cgdNo;
	private double price;
	private double count;
	private String DhDate;
	private String XqDate;
	private String masterId;
	
	public CGDWLEntity() {

	}
	public CGDWLEntity(String no,String masterId) {
		SetNo(no);
		this.masterId = masterId;
	}
	
	public void SetCgdNo(String cgdNo) {
		this.cgdNo = cgdNo;
	}
	
	public void SetPrice(double d) {
		this.price = d;
	}
	
	public void SetCount(double count2) {
		this.count = count2;
	}
	
	public void SetDhDate(String dhDate) {
		this.DhDate = dhDate;
	}
	
	public void SetXqDate(String xqDate) {
		this.XqDate = xqDate;
	}
	
	public void SetMasterId(String masterId) {
		this.masterId = masterId;
	}
	
	@Override
	public void SetNo(String no) {
		// TODO Auto-generated method stub
		super.SetNo(no);
	}

	@Override
	public void SetName(String name) {
		// TODO Auto-generated method stub
		super.SetName(name);
	}

	@Override
	public void SetCategory(int category) {
		// TODO Auto-generated method stub
		super.SetCategory(category);
	}

	@Override
	public void SetLevel(String level) {
		// TODO Auto-generated method stub
		super.SetLevel(level);
	}

	@Override
	public void SetUnit(String unit) {
		// TODO Auto-generated method stub
		super.SetUnit(unit);
	}
	
	public String GetCgdNo()
	{
		return cgdNo;
	}

	public double GetPrice() {
		return price;
	}
	
	public double GetCount() {
		return count;
	}
	
	public String GetDhDate() {
		return DhDate;
	}
	
	public String GetXqDate() {
		return XqDate;
	}
	
	public String GetMasterId() {
		return masterId;
	}
	
	@Override
	public String GetNo() {
		// TODO Auto-generated method stub
		return super.GetNo();
	}

	@Override
	public String GetName() {
		// TODO Auto-generated method stub
		return super.GetName();
	}

	@Override
	public int GetCategory() {
		// TODO Auto-generated method stub
		return super.GetCategory();
	}

	@Override
	public String GetLevel() {
		// TODO Auto-generated method stub
		return super.GetLevel();
	}

	@Override
	public String GetUnit() {
		// TODO Auto-generated method stub
		return super.GetUnit();
	}

	
}
