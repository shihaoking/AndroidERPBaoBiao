package order.data.factory;

public class CGDEntity {

	private String no;
	private String category;
	private String checker;
	private String maker;
	private String createTime;
	private String checkerLc;
	private String ghsNo; 
	private String ddKindNo; 
	private String ywyNo;
	private String bumenNo; 
	private String xmNo;
	private String fkfsNo; 
	private String ysfsNo;
	private String fktjNo; 
	private String moneyKind;
	private String dhAddress;
	private String bz;
	private double hl;
	private double yf;
	private double dj;

	public CGDEntity() {
		// TODO Auto-generated constructor stub
		super();
	}

	public CGDEntity(String no, String category,String ywyNo,String checker,String createTime) {
		super();
		this.no = no;
		this.category = category;
		this.ywyNo = ywyNo;
		this.checker = checker;
		this.createTime = createTime;
	}
	
	public void SetMaker(String maker)
	{
		this.maker = maker;
	}
	public void SetCheckerLc(String checkerLc) {
		this.checkerLc = checkerLc;
	}

	public void SetNo(String no) {
		this.no = no;
	}

	public void SetCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public void SetChecker(String checker) {
		this.checker = checker;
	}

	public void SetCategory(String category) {
		this.category = category;
	}
	
	
	public void SetGhsNo(String ghs_no) {
		this.ghsNo = ghs_no;
	}
	
	public void SetDDKindNo(String ddkind_no) {
		this.ddKindNo = ddkind_no;
	}

	public void SetYwyNo(String ywy_no) {
		this.ywyNo= ywy_no;
	}
	
	public void SetBumenNo(String bumen_no) {
		this.bumenNo = bumen_no;
	}
	
	public void SetXMNo(String xm_no) {
		this.xmNo = xm_no;
	}
	
	public void SetFkfsNo(String fkfs_no) {
		this.fkfsNo = fkfs_no;
	}
	
	public void SetYsfsNo(String ysfs_no) {
		this.ysfsNo = ysfs_no;
	}
	
	public void SetFktjNo(String fktj_no) {
		this.fktjNo = fktj_no;
	}
	
	public  void SetMoneyKind(String money_kind) {
		this.moneyKind = money_kind;
	}
	
	public void SetDhAdress(String dhAdress) {
		this.dhAddress =dhAdress;
	}
	
	public void SetBz(String bz) {
		this.bz = bz;
	}
	
	public void setHl(double hl){
		this.hl = hl;
	}
	public void SetYf(double yf) {
		this.yf = yf;
	}
	
	public void SetDj(double dj) {
		this.dj = dj;
	}
	
	public String GetMaker() {
		return maker;
	}
	
	public String GetCheckerLc() {
		return checkerLc;
	}
	
	public String GetNo() {
		return no;
	}

	public String GetCategory() {
		return category;
	}
	
	public String GetCreateTime() {
		return createTime;
	}
	
	public String GetChecker() {
		return checker;	
	}
	
	
	public String GetGhsNo() {
		return ghsNo;
	}
	
	public String GetDdKindNo() {
		return ddKindNo;
	}

	public String GetYwyNo() {
		return ywyNo;
	}
	
	public String GetBumenNo() {
		return bumenNo;
	}
	
	public String GetXmNo() {
		return xmNo;
	}
	
	public String GetFkfsNo() {
		return fkfsNo;
	}
	
	public String GetYsfsNo() {	
		return ysfsNo;
	}
	
	public String GetFktjNo() {
		return fktjNo;
	}
	
	public String GetMoneyKind() {
		return moneyKind;
	}
	
	public String GetDhAdress() {
		return dhAddress;
	}
	
	public String GetBz() {
		return bz;
	}
	
	public double GetHl() {
		return hl;
	}
	
	public double GetYf() {
		return yf;
	}
	
	public double GetDj() {
		return dj;
	}
}
