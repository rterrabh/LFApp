package com.ufla.lfapp.vo;

public class Rule {
	
	//attributes 
	private String leftSide;
	private String rightSide;
	
	//builder
	public Rule() {
		this("", "");
	}
	
	
	public Rule(String left, String right) {
		super();
		this.leftSide = left;
		this.rightSide = right;
	}

	//methods
	
	//accessors

	public Rule(Rule r) {
		this(r.getLeftSide(), r.getRightSide());
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Rule other = (Rule) obj;
		if (leftSide == null) {
			if (other.leftSide != null) {
				return false;
			}
		} else if (!leftSide.equals(other.leftSide)) {
			return false;
		}
		if (rightSide == null) {
			if (other.rightSide != null) {
				return false;
			}
		} else if (!rightSide.equals(other.rightSide)) {
			return false;
		}
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


	public boolean isFNC(String initialSymbol) {
		if(leftSide.equals(initialSymbol)) {
			if(rightSide.equals(Grammar.LAMBDA)) {
				return true;
			}
		}
		if(rightSide.contains(initialSymbol)) {
			return false;
		}
		if(rightSide.length() == 1) {
			return Character.isLowerCase(rightSide.charAt(0));
		}

		int indice = 0;
		if(!Character.isUpperCase(rightSide.charAt(indice++))) {
				return false;
		}
		while(Character.isDigit(rightSide.charAt(indice))) {
			indice++;
			if(indice == rightSide.length()) {
				return false;
			}
		}
		if(!Character.isUpperCase(rightSide.charAt(indice++))) {
			return false;
		}
		if(indice == rightSide.length()) {
			return true;
		}
		while(indice != rightSide.length() &&
				Character.isDigit(rightSide.charAt(indice))) {
			indice++;
		}
		return indice == rightSide.length();
	}

	public boolean isFNG(boolean initialSymbol) {
		if(initialSymbol) {
			if(rightSide.equals(Grammar.LAMBDA)) {
				return true;
			}
		}
		if(rightSide.length() == 1) {
			return Character.isLowerCase(rightSide.charAt(0));
		}
		if(Character.isLowerCase(rightSide.charAt(0))) {
			for(int i = 1; i < rightSide.length(); i++) {
				if(!(Character.isDigit(rightSide.charAt(i)) ||
						Character.isUpperCase(rightSide.charAt(i)))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public boolean existsLeftRecursion() {
		if(rightSide.length() > leftSide.length()) {
			return rightSide.startsWith(leftSide) &&
					!Character.isDigit(rightSide.charAt(leftSide.length()));
		}
		if(rightSide.length() == leftSide.length()) {
			return rightSide.startsWith(leftSide);
		}
		return false;
	}

	public String getFirstVariableOfRightSide() {
		if(!Character.isUpperCase(rightSide.charAt(0))) {
			return null;
		}
		int indice = 0;
		while(indice+1 != rightSide.length() &&
				Character.isDigit(rightSide.charAt(indice+1))) {
			indice++;
		}
		return rightSide.substring(0, indice+1);
	}


	
}
