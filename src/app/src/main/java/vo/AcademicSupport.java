package vo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by juventino on 26/08/15.
 */
public class AcademicSupport {

    private String comments;
    private boolean situation;
    private Map<Integer, String> foundProblems;
    private String result;
    private String solutionDescription;

    public AcademicSupport() {
        this.comments = new String();
        this.situation = false;
        this.foundProblems = new HashMap<>();
        this.result = new String();
        this.solutionDescription = new String();
    }

    public AcademicSupport(String comments, boolean situation, HashMap foundProblems, Grammar g, String solutionDescription) {
        this.comments = comments;
        this.situation = situation;
        this.foundProblems = foundProblems;
        setResult(g);
        this.solutionDescription = solutionDescription;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean getSituation() {
        return situation;
    }

    public void setSituation(boolean situation) {
        this.situation = situation;
    }

    public Map<Integer, String> getFoundProblems() {
        return foundProblems;
    }

    public void setFoundProblems(Map<Integer, String> foundProblems) {
        this.foundProblems = foundProblems;
    }

    public String getResult() {
        return result;
    }

    public void setResult(Grammar g) {
        this.result = formatResultGrammar(g);
    }

    public String getSolutionDescription() {
        return solutionDescription;
    }

    public void setSolutionDescription(String solutionDescription) {
        this.solutionDescription = solutionDescription;
    }

    public String formatResultGrammar(final Grammar g) {
        StringBuilder txtGrammar = new StringBuilder(g.getInitialSymbol() + " ->");
        for (Rule element : g.getRules()) {
            if (element.getLeftSide().equals(g.getInitialSymbol())) {
                txtGrammar.append(" " + element.getRightSide() + " |");
            }
        }
        txtGrammar.deleteCharAt(txtGrammar.length() - 1);
        txtGrammar.append("\n");
        for (String variable : g.getVariables()) {
            if (!variable.equals(g.getInitialSymbol())) {
                txtGrammar.append(variable + " ->");
                for (Rule element : g.getRules()) {
                    if (variable.equals(element.getLeftSide())) {
                        txtGrammar.append(" " + element.getRightSide() + " |");
                    }
                }
                txtGrammar.deleteCharAt(txtGrammar.length() - 1);
                txtGrammar.append("\n");
            }
        }
        return txtGrammar.toString();
    }
}


