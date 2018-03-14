package com.ufla.lfapp.activities.grammar.algorithms;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.grammar.HeaderGrammarActivity;
import com.ufla.lfapp.utils.UtilActivities;
import com.ufla.lfapp.core.grammar.AcademicSupport;
import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.grammar.Rule;

/**
 * Created by root on 21/07/16.
 */
public class EmptyProductionActivity extends HeaderGrammarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_empty_productions);
        super.onCreate(savedInstanceState);
        setTitle();
        removingEmptyProductions(getGrammar());
    }

    private void setTitle() {
        switch(algorithm) {
            case CHOMSKY_NORMAL_FORM:
                setTitle(getResources().getString(R.string.lfapp_cnf_title)
                        + " - 2/6");
                break;
            case GREIBACH_NORMAL_FORM:
                setTitle(getResources().getString(R.string.lfapp_gnf_title)
                        + " - 1/7");
                break;
            case REMOVE_LEFT_RECURSION:
                setTitle(getResources().getString(R.string.lfapp_left_recursion_title)
                        + " - 2/7");
                break;
        }
    }

    @Override
    protected  Grammar getGrammar() {
        switch(algorithm) {
            case CHOMSKY_NORMAL_FORM:
            case REMOVE_LEFT_RECURSION:
            case GREIBACH_NORMAL_FORM:
                return new Grammar(grammar)
                        .getGrammarWithInitialSymbolNotRecursive(new Grammar
                                (grammar), new AcademicSupport());
            default: return super.getGrammar();
        }
    }

    public void back(View view) {
        changeActivity(this, RemoveInitialSymbolRecursiveActivity.class);
    }

    public void next(View view) {
        changeActivity(this, ChainRulesActivity.class);
    }


    /**
     * Método que junta duas gramáticas em um String
     * @param grammar1
     * @param grammar2
     * @return
     */
    public String joinGrammars(final Grammar grammar1, final Grammar grammar2) {
        StringBuilder newG = new StringBuilder();

        for (Rule element : grammar2.getRules()) {
            if (element.getLeftSide().equals(grammar1.getInitialSymbol()) && !grammar1.getRules().contains(element)) {
                newG.append(element);
                newG.append("\n");
            }
        }

        for (Rule element : grammar1.getRules(grammar1.getInitialSymbol())) {
            newG.append(element).append("\n");
        }


        for (Rule element : grammar1.getRules()) {
            if (!element.getLeftSide().equals(grammar1.getInitialSymbol())) {
                newG.append(element);
                newG.append("\n");
            }
        }
        for (Rule element : grammar2.getRules()) {
            if (!element.getLeftSide().equals(grammar1.getInitialSymbol()) && !grammar1.getRules().contains(element)) {
                newG.append(element);
                newG.append("\n");
            }
        }
        return newG.toString();
    }


    /**
     * Método que retorna o pseudocódigo do algoritmo nullable
     * @return
     */
    public String getNullableAlgorith() {
        return getString(R.string.nullable_algol);
    }

    /**
     * Método que conta o número de dígitos a partir do índice fornecido
     * @param index
     * @param element
     * @return
     */
    private int getNumberOfDigits(int index, String element) {
        int i = index;
        for (; i < element.length() && Character.isDigit(element.charAt(i)); i++);
        //i = (i == element.length() ? (i - 1) : (i));
        return i;
    }


    /**
     * Método que realiza a remoção de produções vazias e acrescenta as informações acadêmicas.
     * @param g : Gramática
     */
    public void removingEmptyProductions(final Grammar g) {
        AcademicSupport academicSupport = new AcademicSupport();
        StringBuilder academicInfoComments = new StringBuilder();

        //Configura comentários sobre o processo
        academicInfoComments.append(Html.fromHtml(getString(R.string.removal_empty_prod_comments)));
        academicSupport.setComments(academicInfoComments.toString());

        //Realiza processo
        Grammar gc = g.getGrammarEssentiallyNoncontracting(g, academicSupport);
        academicSupport.setResult(gc);

        //configura a gramática de resultado
        TextView grammarResult = (TextView) findViewById(R.id.ResultGrammarEmptyProductions);
        assert grammarResult != null;
        grammarResult.setText(Html.fromHtml(academicSupport.getResult()));

        if (academicSupport.getSituation()) {
            //Configura os comentários sobre a solução
            TextView result = (TextView) findViewById(R.id.AnswerOfEmptyProductions);
            result.setText(academicSupport.getComments());

            //Configuração do passo 1 (Tabela dos conjuntos)
            TextView explanation1 = (TextView) findViewById(R.id.ExplanationEmptyRules1);
            explanation1.setText(Html.fromHtml(getString(R.string.removal_empty_prod_step_1)));
            TextView pseudo =(TextView) findViewById(R.id.PseudoNullableAlgorith);
            assert pseudo != null;
            pseudo.setText(Html.fromHtml(getNullableAlgorith()));
            TableLayout tableOfSets = (TableLayout) findViewById(R.id.TableOfSets);
            tableOfSets.setShrinkAllColumns(true);
            tableOfSets.setBackgroundColor(getResources().getColor(R.color.Gainsboro));
            UtilActivities.printTableOfSets(tableOfSets, academicSupport,
                    "NULL", "PREV", this);

            //Configuração do passo 2 (Gramática com as regras criadas no processo)
            if (academicSupport.getInsertedRules().size() != 0) {
                TextView explanation2 = (TextView) findViewById(R.id.ExplanationEmptyRules2);
                explanation2.setText(R.string.removal_empty_prod_step_2);
                TableLayout grammarWithNewRules = (TableLayout) findViewById(R.id.AddingRulesTable);
                Grammar blueGrammar = new Grammar(joinGrammars(gc, g));
                UtilActivities.printGrammarWithNewRules(blueGrammar,
                        grammarWithNewRules,
                        academicSupport, this);
            } else {
                TextView explanation2 = (TextView) findViewById(R.id.ExplanationEmptyRules2);
                explanation2.setText(R.string.removal_empty_prod_step_2_1);
            }

            //Configuração do passo 3 (Gramática com as regras irregulares removida)
            if (academicSupport.getIrregularRules().size() != 0) {
                TextView explanation3 = (TextView) findViewById(R.id.ExplanationEmptyRules3);
                explanation3.setText(R.string.removal_empty_prod_step_3);
                TableLayout grammarWithoutOldRules = (TableLayout) findViewById(R.id.RemovingRulesTable);
                Grammar redGrammar = new Grammar(joinGrammars(gc, g));
                UtilActivities.printGrammarWithoutOldRules(redGrammar,
                        grammarWithoutOldRules, academicSupport, this);
            } else {
                TextView explanation3 = (TextView) findViewById(R.id.ExplanationEmptyRules3);
                explanation3.setText(R.string.removal_empty_prod_step_3_1);
            }

        } else {
            TextView result = (TextView) findViewById(R.id.AnswerOfEmptyProductions);
            result.setText(R.string.no_empty_prod);
        }
    }

}