package com.ufla.lfapp.activities.grammar.algorithms;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.grammar.HeaderGrammarActivity;
import com.ufla.lfapp.activities.utils.UtilActivities;
import com.ufla.lfapp.vo.grammar.AcademicSupport;
import com.ufla.lfapp.vo.grammar.Grammar;
import com.ufla.lfapp.vo.grammar.Rule;

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
                setTitle("LFApp - FNC - 2/6");
                break;
            case GREIBACH_NORMAL_FORM:
                setTitle("LFApp - FNG - 2/8");
                break;
            case REMOVE_LEFT_RECURSION:
                setTitle("LFApp - Recursão à Esq - 2/7");
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
        for (Rule element : grammar1.getRules(grammar1.getInitialSymbol())) {
            newG.append(element).append("\n");
        }
        for (Rule element : grammar2.getRules()) {
            if (element.getLeftSide().equals(grammar1.getInitialSymbol()) && !grammar1.getRules().contains(element)) {
                newG.append(element);
                newG.append("\n");
            }
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
        StringBuilder algol = new StringBuilder();
        algol.append("NULL = {A | {A -> λ} ∈ P}<br>");
        algol.append("<b>repita</b><br>");
        algol.append("&nbsp;&nbsp;&nbsp;&nbsp;PREV = NULL<br>");
        algol.append("&nbsp;&nbsp;&nbsp;&nbsp;<b>para cada</b> A ∈ V <b>faça</b><br>");
        algol.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>se</b> A → w e w ∈ PREV<sup>∗</sup> <b>faça</b><br>");
        algol.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;NULL = NULL ∪ {A}<br>");
        algol.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;NULL = NULL ∪ {A}<br>");
        algol.append("<b>até</b> NULL == PREV");
        return algol.toString();
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
        String align = "justify";
        academicInfoComments.append(Html.fromHtml("<p align=" + align +
                ">O algoritmo para remoção de regras λ consiste em 3 passos:"));
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
            explanation1.setText(Html.fromHtml("(1) Determinar o conjunto das variáveis anuláveis.</p>"));
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
                explanation2.setText("(2) Adicionar regras em que as ocorrências de variáveis nulas são omitidas. Por exemplo, assuma a regra A -> BABa e B" +
                        " é uma variável anulável. Logo, são inseridas as seguintes regras: A -> ABa, A -> BAa e A -> Aa.");
                TableLayout grammarWithNewRules = (TableLayout) findViewById(R.id.AddingRulesTable);
                Grammar blueGrammar = new Grammar(joinGrammars(gc, g));
                UtilActivities.printGrammarWithNewRules(blueGrammar,
                        grammarWithNewRules,
                        academicSupport, this);
            } else {
                TextView explanation2 = (TextView) findViewById(R.id.ExplanationEmptyRules2);
                explanation2.setText("(2) Não há regras a serem inseridas.");
            }

            //Configuração do passo 3 (Gramática com as regras irregulares removida)
            if (academicSupport.getIrregularRules().size() != 0) {
                TextView explanation3 = (TextView) findViewById(R.id.ExplanationEmptyRules3);
                explanation3.setText("(3) Remover as regras λ.\n" +
                        "OBS: se símbolo inicial produz λ, não remover esta regra.");
                TableLayout grammarWithoutOldRules = (TableLayout) findViewById(R.id.RemovingRulesTable);
                Grammar redGrammar = new Grammar(joinGrammars(gc, g));
                UtilActivities.printGrammarWithoutOldRules(redGrammar,
                        grammarWithoutOldRules, academicSupport, this);
            } else {
                TextView explanation3 = (TextView) findViewById(R.id.ExplanationEmptyRules3);
                explanation3.setText("(3) Não há regras a serem removidas.");
            }

        } else {
            TextView result = (TextView) findViewById(R.id.AnswerOfEmptyProductions);
            result.setText("A gramática inserida não possui produções vazias.");
        }
    }

}