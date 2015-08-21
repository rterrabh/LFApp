package com.lfapp.lfapp_01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import vo.*;



public class MainActivity2 extends ActionBarActivity {

    private TextView t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12;
    private LinearLayout l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12;
    private boolean z1, z2, z3, z4, z5, z6, z7, z8, z9, z10, z11, z12;
    private Button button_Voltar;
    private TextView step1_1, step1_2, step2_1, step2_2, step2_3, step3_1, step3_2, step3_3, step4_1, step4_2, step4_3;
    private TextView step5_1, step5_2, step5_3, step6_1, step6_2, step6_3, step7_1, step7_2, step7_3, step8_1, step8_2, step8_3;
    private TextView step9_1, step9_2, step9_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out);

        String mensagem = new String();
        //pegando Intent para retirar o que foi enviado da tela anterior
        Intent intent = getIntent();
        if (intent != null) {
            Bundle dados = new Bundle();
            dados = intent.getExtras();
            if (dados != null) {
                mensagem = dados.getString("msg");

                Grammar g = new Grammar(mensagem);

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


        acordionMenu();
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
        this.t1 = (TextView) findViewById(R.id.layouttext1);
        this.t2 = (TextView) findViewById(R.id.layouttext2);
        this.t3 = (TextView) findViewById(R.id.layouttext3);
        this.t4 = (TextView) findViewById(R.id.layouttext4);
        this.t5 = (TextView) findViewById(R.id.layouttext5);
        this.t6 = (TextView) findViewById(R.id.layouttext6);
        this.t7 = (TextView) findViewById(R.id.layouttext7);
        this.t8 = (TextView) findViewById(R.id.layouttext8);
        this.t9 = (TextView) findViewById(R.id.layouttext9);
        this.t10 = (TextView) findViewById(R.id.layouttext10);
        this.t11 = (TextView) findViewById(R.id.layouttext11);
        this.t12 = (TextView) findViewById(R.id.layouttext12);

        this.l1 = (LinearLayout) findViewById(R.id.layout1);
        this.l2 = (LinearLayout) findViewById(R.id.layout2);
        this.l3 = (LinearLayout) findViewById(R.id.layout3);
        this.l4 = (LinearLayout) findViewById(R.id.layout4);
        this.l5 = (LinearLayout) findViewById(R.id.layout5);
        this.l6 = (LinearLayout) findViewById(R.id.layout6);
        this.l7 = (LinearLayout) findViewById(R.id.layout7);
        this.l8 = (LinearLayout) findViewById(R.id.layout8);
        this.l9 = (LinearLayout) findViewById(R.id.layout9);
        this.l10 = (LinearLayout) findViewById(R.id.layout10);
        this.l11 = (LinearLayout) findViewById(R.id.layout11);
        this.l12 = (LinearLayout) findViewById(R.id.layout12);

        this.l1.setVisibility(View.GONE);
        this.l2.setVisibility(View.GONE);
        this.l3.setVisibility(View.GONE);
        this.l4.setVisibility(View.GONE);
        this.l5.setVisibility(View.GONE);
        this.l6.setVisibility(View.GONE);
        this.l7.setVisibility(View.GONE);
        this.l8.setVisibility(View.GONE);
        this.l9.setVisibility(View.GONE);
        this.l10.setVisibility(View.GONE);
        this.l11.setVisibility(View.GONE);
        this.l12.setVisibility(View.GONE);


        z1 = true;
        this.t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (z1) {
                    l1.setVisibility(View.GONE);
                    z1 = false;
                } else {
                    l1.setVisibility(View.VISIBLE);
                    z1 = true;
                }
            }
        });

        z2 = true;
        this.t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (z2) {
                    l2.setVisibility(View.GONE);
                    z2 = false;
                } else {
                    l2.setVisibility(View.VISIBLE);
                    z2 = true;
                }
            }
        });

        z3 = true;
        this.t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (z3) {
                    l3.setVisibility(View.GONE);
                    z3 = false;
                } else {
                    l3.setVisibility(View.VISIBLE);
                    z3 = true;
                }
            }
        });

        z4 = true;
        this.t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (z4) {
                    l4.setVisibility(View.GONE);
                    z4 = false;
                } else {
                    l4.setVisibility(View.VISIBLE);
                    z4 = true;
                }
            }
        });

        z5 = true;
        this.t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (z5) {
                    l5.setVisibility(View.GONE);
                    z5 = false;
                } else {
                    l5.setVisibility(View.VISIBLE);
                    z5 = true;
                }
            }
        });

        z6 = true;
        this.t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (z6) {
                    l6.setVisibility(View.GONE);
                    z6 = false;
                } else {
                    l6.setVisibility(View.VISIBLE);
                    z6 = true;
                }
            }
        });

        z7 = true;
        this.t7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (z7) {
                    l7.setVisibility(View.GONE);
                    z7 = false;
                } else {
                    l7.setVisibility(View.VISIBLE);
                    z7 = true;
                }
            }
        });

        z8 = true;
        this.t8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (z8) {
                    l8.setVisibility(View.GONE);
                    z8 = false;
                } else {
                    l8.setVisibility(View.VISIBLE);
                    z8 = true;
                }
            }
        });

        z9 = true;
        this.t9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (z9) {
                    l9.setVisibility(View.GONE);
                    z9 = false;
                } else {
                    l9.setVisibility(View.VISIBLE);
                    z9 = true;
                }
            }
        });

        z10 = true;
        this.t10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (z10) {
                    l10.setVisibility(View.GONE);
                    z10 = false;
                } else {
                    l10.setVisibility(View.VISIBLE);
                    z10 = true;
                }
            }
        });

        z11 = true;
        this.t11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (z11) {
                    l11.setVisibility(View.GONE);
                    z11 = false;
                } else {
                    l11.setVisibility(View.VISIBLE);
                    z11 = true;
                }
            }
        });

        z12 = true;
        this.t12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (z12) {
                    l12.setVisibility(View.GONE);
                    z12 = false;
                } else {
                    l12.setVisibility(View.VISIBLE);
                    z12 = true;
                }
            }
        });
    }

    public void removingInitialRecursiveSymbol(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        StringBuilder academicSupport = new StringBuilder();
        gc = g.getGrammarWithInitialSymbolNotRecursive(g, academicSupport);
        String txtGrammar = printRules(gc);

        step1_1 = (TextView) findViewById(R.id.DescricaoAlgoritmo1);
        step1_1.setText(academicSupport);

        step1_2 = (TextView) findViewById(R.id.Algoritmo1);
        step1_2.setText(txtGrammar);
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
