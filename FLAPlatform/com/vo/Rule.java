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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((leftSide == null) ? 0 : leftSide.hashCode());
		result = prime * result
				+ ((rightSide == null) ? 0 : rightSide.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;
		if (leftSide == null) {
			if (other.leftSide != null)
				return false;
		} else if (!leftSide.equals(other.leftSide))
			return false;
		if (rightSide == null) {
			if (other.rightSide != null)
				return false;
		} else if (!rightSide.equals(other.rightSide))
			return false;
		return true;
	}
	
	
}
