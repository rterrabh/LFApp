package voOld;

import java.util.HashSet;
import java.util.Set;

public class Rule {
	
	//attributes 
	private String leftSide;
	private String rightSide;
	
	//builder
	public Rule() {}
	
	
	public Rule(String left, String right) {
		this.leftSide = left;
		this.rightSide = right;
	}

	//methods
	
	//accessors

	public Rule(Rule r) {
		// TODO Auto-generated constructor stub
		this.leftSide = r.getLeftSide();
		this.rightSide = r.getRightSide();
	}

	public String getLeftSide() {
		return leftSide;
	}

	public void setLeftSide(String leftSide) {
		this.leftSide = leftSide ;
	}

	public String getRightSide() {
		return rightSide;
	}

	public void setRightSide(String rightSide) {
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
	
	@Override
	protected Object clone()   {
		Rule rc = new Rule();
		
		rc.setLeftSide(this.leftSide);
		rc.setRightSide(this.rightSide);
		
		return rc;
	}
	
	@Override
	public String toString() {
		return this.leftSide + " -> " + this.rightSide;
	}
	
	
}
