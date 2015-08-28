package com.lfapp.lfapp_01;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import vo.*;



public class MainActivity2 extends ActionBarActivity {

    private TableLayout tableGrammarType;
    private TextView titleGrammarType, titleLeftDerivation, titleRightDerivation, titleRemoveInitialSymbolRecursive;
    private TextView titleRemoveEmptyProductions, titleChainRules, titleNoTermSymbols, titleNoReachSymbols, titleFnc, titleRemoveLeftDirectRecursion;
    private TextView titleRemoveLeftRecursion, titleFng, titleCyk;
    private boolean controlGrammarTypeMenu, controlLeftDerivationMenu, controlRightDerivationMenu, controlInitialSymbolRecursiveMenu, controlEmptyProductionsMenu;
    private boolean controlChainRulesMenu, controlNoTermSymbolsMenu, controlNoReachSymbolsMenu, controlFncMenu, controlLeftDirectRecursionMenu, controlLeftRecursionMenu;
    private boolean controlFngMenu, controlCykMenu;
    private LinearLayout layoutGrammarType, layoutDerivationMoreLeft, layoutDerivationMoreRight, layoutInitialSymbolRecursive, layoutEmptyProductions;
    private LinearLayout layoutChainRules, layoutNoTermSymbols, layoutNoReachSymbols, layoutFnc, layoutLeftDirectRecursion;
    private LinearLayout layoutLeftRecursion, layoutFng, layoutCyk;
    private Button button_Voltar;
    private TextView step1_1, step1_2, step2_1, step2_2, step2_3, step3_1, step3_2, step3_3, step4_1, step4_2, step4_3, step5_1, step5_2, step5_3, step7_1;
    private TextView step7_2, step7_3, step8_1, step8_2, step8_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out);

        //chama método que gerencia menu
        acordionMenu();

        String mensagem = new String();

        //pegando Intent para retirar o que foi enviado da tela anterior
        Intent intent = getIntent();
        if (intent != null) {
            Bundle dados = new Bundle();
            dados = intent.getExtras();
            if (dados != null) {
                mensagem = dados.getString("msg");
                Grammar g = new Grammar(mensagem);
                grammarType(g);
                removingInitialRecursiveSymbol(g);
                removingEmptyProductions(g);
                removingChainRules(g);
                removingNotTerminalsSymbols(g);
                removingNotReachableSymbols(g);
                fnc(g);
                removingTheImmediateLeftRecursion(g);
                removingLeftRecursion(g);

                Grammar gc = (Grammar) g.clone();
            }
        }

        this.button_Voltar = (Button) findViewById(R.id.button_Voltar);
        this.button_Voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TrocaTela = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(TrocaTela);
                finish();
            }
        });
    }

    public void acordionMenu() {
        this.layoutGrammarType = (LinearLayout) findViewById(R.id.identifyGrammar);
        this.titleGrammarType = (TextView) findViewById(R.id.layouttext13);
        this.layoutGrammarType.setVisibility(View.GONE);

        this.layoutDerivationMoreLeft = (LinearLayout) findViewById(R.id.layout11);
        this.titleLeftDerivation = (TextView) findViewById(R.id.titleDerivationMoreLeft);
        this.layoutDerivationMoreLeft.setVisibility(View.GONE);

        this.titleRightDerivation = (TextView) findViewById(R.id.titleDerivationMoreRight);
        this.layoutDerivationMoreRight = (LinearLayout) findViewById(R.id.layout12);
        this.layoutDerivationMoreRight.setVisibility(View.GONE);

        this.titleRemoveInitialSymbolRecursive = (TextView) findViewById(R.id.titleRemoveInitialSymbolRecursive);
        this.layoutInitialSymbolRecursive = (LinearLayout) findViewById(R.id.layout1);
        this.layoutInitialSymbolRecursive.setVisibility(View.GONE);

        this.titleRemoveEmptyProductions = (TextView) findViewById(R.id.titleEmptyProductions);
        this.layoutEmptyProductions = (LinearLayout) findViewById(R.id.layout2);
        this.layoutEmptyProductions.setVisibility(View.GONE);

        this.titleChainRules = (TextView) findViewById(R.id.titleChainRules);
        this.layoutChainRules = (LinearLayout) findViewById(R.id.layout3);
        this.layoutChainRules.setVisibility(View.GONE);

        this.titleNoTermSymbols = (TextView) findViewById(R.id.titleNoTermSymbols);
        this.layoutNoTermSymbols = (LinearLayout) findViewById(R.id.layout4);
        this.layoutNoTermSymbols.setVisibility(View.GONE);

        this.titleNoReachSymbols = (TextView) findViewById(R.id.titleNoReachSymbols);
        this.layoutNoReachSymbols = (LinearLayout) findViewById(R.id.layout5);
        this.layoutNoReachSymbols.setVisibility(View.GONE);

        this.titleFnc = (TextView) findViewById(R.id.titleFNC);
        this.layoutFnc = (LinearLayout) findViewById(R.id.layout6);
        this.layoutFnc.setVisibility(View.GONE);

        this.titleRemoveLeftDirectRecursion = (TextView) findViewById(R.id.titleRemoveLeftDirectRecursion);
        this.layoutLeftDirectRecursion = (LinearLayout) findViewById(R.id.layout7);
        this.layoutLeftDirectRecursion.setVisibility(View.GONE);

        this.titleRemoveLeftRecursion = (TextView) findViewById(R.id.titleRemoveLeftRecursion);
        this.layoutLeftRecursion = (LinearLayout) findViewById(R.id.layout8);
        this.layoutLeftRecursion.setVisibility(View.GONE);

        this.titleFng = (TextView) findViewById(R.id.titleFNG);
        this.layoutFng = (LinearLayout) findViewById(R.id.layout9);
        this.layoutFng.setVisibility(View.GONE);

        this.titleCyk = (TextView) findViewById(R.id.titleCYK);
        this.layoutCyk = (LinearLayout) findViewById(R.id.layout10);
        this.layoutCyk.setVisibility(View.GONE);


        controlGrammarTypeMenu = true;
        this.titleGrammarType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlGrammarTypeMenu) {
                    layoutGrammarType.setVisibility(View.GONE);
                    controlGrammarTypeMenu = false;
                } else {
                    layoutGrammarType.setVisibility(View.VISIBLE);
                    controlGrammarTypeMenu = true;
                }
            }
        });

        controlLeftDerivationMenu = true;
        this.titleLeftDerivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlLeftDerivationMenu) {
                    layoutDerivationMoreLeft.setVisibility(View.GONE);
                    controlLeftDerivationMenu = false;
                } else {
                    layoutDerivationMoreLeft.setVisibility(View.VISIBLE);
                    controlLeftDerivationMenu = true;
                }
            }
        });

        controlRightDerivationMenu = true;
        this.titleRightDerivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlRightDerivationMenu) {
                    layoutDerivationMoreRight.setVisibility(View.GONE);
                    controlRightDerivationMenu = false;
                } else {
                    layoutDerivationMoreRight.setVisibility(View.VISIBLE);
                    controlRightDerivationMenu = true;
                }
            }
        });

        controlInitialSymbolRecursiveMenu = true;
        this.titleRemoveInitialSymbolRecursive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlInitialSymbolRecursiveMenu) {
                    layoutInitialSymbolRecursive.setVisibility(View.GONE);
                    controlInitialSymbolRecursiveMenu = false;
                } else {
                    layoutInitialSymbolRecursive.setVisibility(View.VISIBLE);
                    controlInitialSymbolRecursiveMenu = true;
                }
            }
        });

        controlEmptyProductionsMenu = true;
        this.titleRemoveEmptyProductions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlEmptyProductionsMenu) {
                    layoutEmptyProductions.setVisibility(View.GONE);
                    controlEmptyProductionsMenu = false;
                } else {
                    layoutEmptyProductions.setVisibility(View.VISIBLE);
                    controlEmptyProductionsMenu = true;
                }
            }
        });

        controlChainRulesMenu = true;
        this.titleChainRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlChainRulesMenu) {
                    layoutChainRules.setVisibility(View.GONE);
                    controlChainRulesMenu = false;
                } else {
                    layoutChainRules.setVisibility(View.VISIBLE);
                    controlChainRulesMenu = true;
                }
            }
        });

        controlNoTermSymbolsMenu = true;
        this.titleNoTermSymbols.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlNoTermSymbolsMenu) {
                    layoutNoTermSymbols.setVisibility(View.GONE);
                    controlNoTermSymbolsMenu = false;
                } else {
                    layoutNoTermSymbols.setVisibility(View.VISIBLE);
                    controlNoTermSymbolsMenu = true;
                }
            }
        });

        controlNoReachSymbolsMenu = true;
        this.titleNoReachSymbols.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlNoReachSymbolsMenu) {
                    layoutNoReachSymbols.setVisibility(View.GONE);
                    controlNoReachSymbolsMenu = false;
                } else {
                    layoutNoReachSymbols.setVisibility(View.VISIBLE);
                    controlNoReachSymbolsMenu = true;
                }
            }
        });

        controlFncMenu = true;
        this.titleFnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlFncMenu) {
                    layoutFnc.setVisibility(View.GONE);
                    controlFncMenu = false;
                } else {
                    layoutFnc.setVisibility(View.VISIBLE);
                    controlFncMenu = true;
                }
            }
        });

        controlLeftDirectRecursionMenu = true;
        this.titleRemoveLeftDirectRecursion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlLeftDirectRecursionMenu) {
                    layoutLeftDirectRecursion.setVisibility(View.GONE);
                    controlLeftDirectRecursionMenu = false;
                } else {
                    layoutLeftDirectRecursion.setVisibility(View.VISIBLE);
                    controlLeftDirectRecursionMenu = true;
                }
            }
        });

        controlLeftRecursionMenu = true;
        this.titleRemoveLeftRecursion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlLeftRecursionMenu) {
                    layoutLeftRecursion.setVisibility(View.GONE);
                    controlLeftRecursionMenu = false;
                } else {
                    layoutLeftRecursion.setVisibility(View.VISIBLE);
                    controlLeftRecursionMenu = true;
                }
            }
        });

        controlFngMenu = true;
        this.titleFng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlFngMenu) {
                    layoutFng.setVisibility(View.GONE);
                    controlFngMenu = false;
                } else {
                    layoutFng.setVisibility(View.VISIBLE);
                    controlFngMenu = true;
                }
            }
        });

        controlCykMenu = true;
        this.titleCyk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlCykMenu) {
                    layoutCyk.setVisibility(View.GONE);
                    controlCykMenu = false;
                } else {
                    layoutCyk.setVisibility(View.VISIBLE);
                    controlCykMenu = true;
                }
            }
        });
    }

    public void grammarType(final Grammar g) {

        this.tableGrammarType = new TableLayout(this);
        this.tableGrammarType = (TableLayout) findViewById(R.id.tableGrammarType);
        this.tableGrammarType.setStretchAllColumns(true);

        //LINHA 1
        TableRow row0 = new TableRow(this);
        TextView tv0_0 = new TextView(this);
        tv0_0.setText("GR");
        TextView tv0_1 = new TextView(this);
        tv0_1.setText("u ∈ V");
        TextView tv0_2 = new TextView(this);
        tv0_2.setText("v ∈ λ | Σ | ΣV");
        row0.addView(tv0_0);
        row0.addView(tv0_1);
        row0.addView(tv0_2);
        row0.setBackgroundColor(getResources().getColor(R.color.DarkGray));
        this.tableGrammarType.addView(row0);

        //LINHA 2
        TableRow row1 = new TableRow(this);
        TextView tv1_0 = new TextView(this);
        tv1_0.setText("GLC");
        TextView tv1_1 = new TextView(this);
        tv1_1.setText("u ∈ V");
        TextView tv1_2 = new TextView(this);
        tv1_2.setText("ν ∈ (V ∪ Σ)∗");
        row1.addView(tv1_0);
        row1.addView(tv1_1);
        row1.addView(tv1_2);
        row1.setBackgroundColor(getResources().getColor(R.color.Gainsboro));
        tableGrammarType.addView(row1);

        //LINHA 3
        TableRow row2 = new TableRow(this);
        TextView tv2_0 = new TextView(this);
        tv2_0.setText("GSC");
        TextView tv2_1 = new TextView(this);
        tv2_1.setText("u ∈ (V U Σ)+");
        TextView tv2_2 = new TextView(this);
        tv2_2.setText("v ∈ (V ∪ Σ) +");
        row2.addView(tv2_0);
        row2.addView(tv2_1);
        row2.addView(tv2_2);
        row2.setBackgroundColor(getResources().getColor(R.color.DarkGray));
        this.tableGrammarType.addView(row2);

        //LINHA 4
        TableRow row3 = new TableRow(this);
        TextView tv3_0 = new TextView(this);
        tv3_0.setText("GI");
        TextView tv3_1 = new TextView(this);
        tv3_1.setText("u ∈ (V U Σ)+");
        TextView tv3_2 = new TextView(this);
        tv3_2.setText("v ∈ (V ∪ Σ)*");
        row3.addView(tv3_0);
        row3.addView(tv3_1);
        row3.addView(tv3_2);
        row3.setBackgroundColor(getResources().getColor(R.color.Gainsboro));
        this.tableGrammarType.addView(row3);

        AcademicSupport academic = new AcademicSupport();
        academic.setComments("A classificação de uma gramática é feita pelo tipo de suas regras (u → v). " +
                "A tabela abaixo mostra o formato de regras característicos de cada gramática: \n");

        StringBuilder comments = new StringBuilder();
        academic.setSolutionDescription(comments.toString() + GrammarParser.classifiesGrammar(g, comments));

        TextView explanation = new TextView(this);
        explanation = (TextView) findViewById(R.id.explanationGrammarType);
        explanation.setText(academic.getComments());

        TextView commentsOfSolution = new TextView(this);
        commentsOfSolution = (TextView) findViewById(R.id.comments);
        commentsOfSolution.setText(comments + academic.getSolutionDescription());
    }


    public void removingInitialRecursiveSymbol(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        AcademicSupport academicSupport = new AcademicSupport();
        gc = g.getGrammarWithInitialSymbolNotRecursive(g, academicSupport);
        String txtGrammar = printRules(gc);

        step1_1 = (TextView) findViewById(R.id.DescricaoAlgoritmo1);
        step1_1.setText(academicSupport.getResult());

        step1_2 = (TextView) findViewById(R.id.Algoritmo1);
        if (academicSupport.getSituation()) {
            StringBuilder academicInfo = new StringBuilder(academicSupport.getComments());
            for (int i = 1; i < academicSupport.getFoundProblems().size(); i++) {
                academicInfo.append(academicSupport.getFoundProblems().get(i));
            }
            academicInfo.append(academicSupport.getSolutionDescription());
            step1_2.setText(academicInfo);
        } else {
            step1_2.setText("A gramática inserida não possui regras do tipo S ⇒ ∗ αSβ. Logo, nenhuma alteração foi realizada.");
        }



    }

    public void removingEmptyProductions(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        StringBuilder academicSupport = new StringBuilder();
        StringBuilder algorithm = new StringBuilder();
        algorithm.append("\t\t\tNULL  = { A | {A -> .} ∈ P}\n");
        algorithm.append("\t\t\trepita\n");
        algorithm.append("\t\t\t\t\tPREV = NULL\n");
        algorithm.append("\t\t\t\t\t\t\tpara cada A ∈ V faça:\n");
        algorithm.append("\t\t\t\t\t\t\t\t\t se A -> w e w ∈ PREV* faça:\n");
        algorithm.append("\t\t\t\t\t\t\t\t\t\t\tNULL = NULL U {A}\n");
        algorithm.append("\t\t\taté NULL == PREV\n");


        gc = g.getGrammarEssentiallyNoncontracting(g, academicSupport);
        step2_1 = (TextView) findViewById(R.id.PseudocódigoAlgoritmo2);
        step2_1.setText(algorithm);

        step2_2 = (TextView) findViewById(R.id.DescricaoAlgoritmo2);
        step2_2.setText(academicSupport);


        String txtGrammar = printRules(gc);
        step2_3 = (TextView) findViewById(R.id.Algoritmo2);
        step2_3.setText(txtGrammar);



    }

    public void removingChainRules(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        StringBuilder academicSupport = new StringBuilder();
        StringBuilder algorithm = new StringBuilder();

        algorithm.append("\t\t\tCHAIN(A) = {A}\n");
        algorithm.append("\t\t\tPREV = {}\n");
        algorithm.append("\t\t\trepita\n");
        algorithm.append("\t\t\t\t\tNEW = CHAIN(A) - PREV\n");
        algorithm.append("\t\t\t\t\tPREV = CHAIN(A)\n");
        algorithm.append("\t\t\t\t\t\t\tpara cada B ∈ NEW faça:\n");
        algorithm.append("\t\t\t\t\t\t\t\t\t para cada B -> C faça\n");
        algorithm.append("\t\t\t\t\t\t\t\t\t\t\t CHAIN(A) = CHAIN(A) U {C}\n");
        algorithm.append("\t\t\taté NULL == PREV\n");

        step3_1 = (TextView) findViewById(R.id.PseudocódigoAlgoritmo3);
        step3_1.setText(algorithm);

        gc = g.getGrammarWithoutChainRules(g, academicSupport);

        step3_2 = (TextView) findViewById(R.id.DescricaoAlgoritmo3);
        step3_2.setText(academicSupport);

        String txtGrammar = printRules(gc);
        step3_3 = (TextView) findViewById(R.id.Algoritmo3);
        step3_3.setText(txtGrammar);

    }

    public void removingNotTerminalsSymbols(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        StringBuilder academicSupport = new StringBuilder();
        StringBuilder algorithm = new StringBuilder();

        algorithm.append("\t\t\tTERM = { A | existe uma regra A -> w ∈ P, com w ∈ Σ*}\n");
        algorithm.append("\t\t\trepita\n");
        algorithm.append("\t\t\t\t\tPREV = TERM\n");
        algorithm.append("\t\t\t\t\tpara cada A ∈ V faça\n");
        algorithm.append("\t\t\t\t\t\t\tse A -> w ∈ P e w ∈ (PREV U Σ)* então\n");
        algorithm.append("\t\t\t\t\t\t\t\t\t TERM = TERM U A\n");
        algorithm.append("\t\t\taté PREV == TERM\n");

        step4_1 = (TextView) findViewById(R.id.PseudocódigoAlgoritmo4);
        step4_1.setText(algorithm);

        gc = g.getGrammarWithoutNoTerm(g, academicSupport);

        step4_2 = (TextView) findViewById(R.id.DescricaoAlgoritmo4);
        step4_2.setText(academicSupport);

        String txtGrammar = printRules(gc);
        step4_3 = (TextView) findViewById(R.id.Algoritmo4);
        step4_3.setText(txtGrammar);
    }

    public void removingNotReachableSymbols(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        StringBuilder academicSupport = new StringBuilder();
        StringBuilder algorithm = new StringBuilder();

        algorithm.append("\t\t\tREACH = {S}\n");
        algorithm.append("\t\t\tPREV = {}\n");
        algorithm.append("\t\t\trepita\n");
        algorithm.append("\t\t\t\t\tNEW = REACH - PREV\n");
        algorithm.append("\t\t\t\t\tPREV = REACH\n");
        algorithm.append("\t\t\t\t\tpara cada A ∈ NEW faça\n");
        algorithm.append("\t\t\t\t\t\t\tpara cada A -> w faça\n");
        algorithm.append("\t\t\t\t\t\t\t\t\t adicione todas as variáveis de w em REACH\n");
        algorithm.append("\t\t\taté REACH == PREV\n");

        step5_1 = (TextView) findViewById(R.id.PseudocódigoAlgoritmo5);
        step5_1.setText(algorithm);

        gc = g.getGrammarWithoutNoReach(g, academicSupport);

        step5_2 = (TextView) findViewById(R.id.DescricaoAlgoritmo5);
        step5_2.setText(academicSupport);

        String txtGrammar = printRules(gc);
        step5_3 = (TextView) findViewById(R.id.Algoritmo5);
        step5_3.setText(txtGrammar);

    }

    public void fnc(final Grammar g) {


    }

    public void removingTheImmediateLeftRecursion(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        StringBuilder academicSupport = new StringBuilder();
        StringBuilder algorithm = new StringBuilder();

        algorithm.append("\t\t\trepita\n");
        algorithm.append("\t\t\t\t\tse a regra atual possui recursão à esquerda então\n");
        algorithm.append("\t\t\t\t\t\t\t\t\t crie uma nova regra\n");
        algorithm.append("\t\t\t\t\t\t\t\t\t adicione à nova regra a sentença com a recursão consumida\n");
        algorithm.append("\t\t\t\t\t\t\t\t\t adicione à nova regra a sentença com a recursão consumida concatenada com a nova variável\n");
        algorithm.append("\t\t\t\t\t\t\t\t\t remova a recursão da antiga regra e acrescente uma regra concatenada com a nova variável\n");
        algorithm.append("\t\t\taté que todas as regras sejam verificadas\n");

        step7_1 = (TextView) findViewById(R.id.PseudocódigoAlgoritmo7);
        step7_1.setText(algorithm);

        gc = g.removingTheImmediateLeftRecursion(g, academicSupport);

        step7_2 = (TextView) findViewById(R.id.DescricaoAlgoritmo7);
        step7_2.setText(academicSupport);

        String txtGrammar = printRules(gc);
        step7_3 = (TextView) findViewById(R.id.Algoritmo7);
        step7_3.setText(txtGrammar);
    }

    public void removingLeftRecursion(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        StringBuilder academicSupport = new StringBuilder();
        StringBuilder algorithm = new StringBuilder();

        algorithm.append("\t\t\tenumerar todas as variáveis\n");
        algorithm.append("\t\t\tpara cada A ∈ V faça\n");
        algorithm.append("\t\t\t\t\tse a variável à direita possui valor menor que a variável do lado direito então\n");
        algorithm.append("\t\t\t\t\t\t\t\t\t copia todas as produções do lado direito da variável maior na menor\n");

        step8_1 = (TextView) findViewById(R.id.PseudocódigoAlgoritmo8);
        step8_1.setText(algorithm);

        gc = g.removingLeftRecursion(g, academicSupport);

        step8_2 = (TextView) findViewById(R.id.DescricaoAlgoritmo8);
        step8_2.setText(academicSupport);

        String txtGrammar = printRules(gc);
        step8_3 = (TextView) findViewById(R.id.Algoritmo8);
        step8_3.setText(txtGrammar);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String printRules(final Grammar g) {
        String out = new String();
        for (Rule element : g.getRules()) {
            if (element.getLeftSide().equals(g.getInitialSymbol())) {
                out += element.toString() + "\n";
            }
        }

        for (String variable : g.getVariables()) {
            for (Rule element : g.getRules()) {
                if (!variable.equals(g.getInitialSymbol()) && variable.equals(element.getLeftSide())) {
                    out += element.toString() + "\n";
                }
            }
        }
        return out;
    }
}
