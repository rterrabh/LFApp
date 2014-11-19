package vo;

public class Rule {
	
	//attributes 
	private String leftSide;
	private String rightSide;
	
	//builder
	public Rule() {}

	//methods
	
	//accessors

	public Rule(Rule r) {
		// TODO Auto-generated constructor stub
		leftSide = r.getleftSide();
		rightSide = r.getrightSide();
	}

	public String getleftSide() {
		return leftSide;
	}

	public void setleftSide(String leftSide) {
		this.leftSide = leftSide ;
	}

	public String getrightSide() {
		return rightSide;
	}

	public void setrightSide(String rightSide) {
		this.rightSide = rightSide;
	}
}
