package br.com.totustuus.ceep_alura.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import br.com.totustuus.ceep_alura.R;
import br.com.totustuus.ceep_alura.dao.NotaDAO;
import br.com.totustuus.ceep_alura.model.Nota;
import br.com.totustuus.ceep_alura.ui.recyclerview.adapter.ListaNotasAdapter;

public class ListaNotasActivity extends AppCompatActivity {

    private ListaNotasAdapter listaNotasAdapter;
    private List<Nota> todasNotas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        todasNotas = configuraNotasExemplo();
        configuraRecyclerView(todasNotas);

        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iniciaFormularioNota = new Intent( ListaNotasActivity.this, FormularioNotaActivity.class);
                startActivity(iniciaFormularioNota);
            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();

        /*
        Sabendo que haverá uma atualização e que queremos nos certificar de que estamos captando dados
        novos, iremos inserir clear() para limparmos os dados.
        Na sequência, adicionaremos os novos elementos por meio de addAll().

        Dessa forma, pegamos a mesma lista enviada ao Adapter, a qual limparemos.
        Adicionaremos todos os dados, garantindo que estamos enviando todos aqueles que
        estão contidos no banco, pois se não limparmos a lista, as notas que já estão
        nela — como as que inserimos de exemplo — serão mantidas.
         */
        NotaDAO notaDAO = new NotaDAO();
        todasNotas.clear();
        todasNotas.addAll(notaDAO.todos());

        /*
         Adapter vai analisar a lsita que tem, o que mudou e renderizar as views com base no que mudou.

         O método adicionado fará com que o Adapter notifique as alterações.
         No momento em que é chamado, ele analisa a lista interna, o que foi alterado e o que
         deve ser renderizado, conforme as mudanças conferidas.

         */
        listaNotasAdapter.notifyDataSetChanged();
    }

    private List<Nota> configuraNotasExemplo() {

        NotaDAO dao = new NotaDAO();

        /*for (int i = 1; i <= 10000; i++) {
            dao.insere(new Nota("Título " + i, "Descrição " + i));
        }*/

        dao.insere(new Nota("M.P.I. Comunismo", "Esse livro descreve as farças do Comunismo"));
        dao.insere(new Nota("A Santa Inquisição", "A verdadeira história"));

        return dao.todos();
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {

        RecyclerView listaNotas = findViewById(R.id.lista_notas_recycler_view);
        configuraAdapter(todasNotas, listaNotas);

        /*
        A configuração do LayoutManager está sendo definida agora no layout.xml
        com o atributo: app:layoutManager="LinearLayoutManager"
         */
        //configuraLayoutManager(listaNotas);

    }

    private void configuraLayoutManager(RecyclerView listaNotas) {

    /*
    O layout não será apresentado exatamente da forma definida em item_nota.
    Levando isso em consideração, adicionaremos um Layout Manager (Gerenciador de Layout, em português)
    e a partir do gerenciador apresentaremos a View.

    Por padrão, o RecyclerView implementa alguns Layout Managers.
    Utilizaremos LinearLayoutManager, que é bem similar ao do ListView.
    Ele segue o padrão de preencher a largura da tela, criando o aspecto
    visual que conferimos inicialmente.
     */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listaNotas.setLayoutManager(layoutManager);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        listaNotasAdapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(listaNotasAdapter);
    }
}
