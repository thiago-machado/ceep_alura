package br.com.totustuus.ceep_alura.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.totustuus.ceep_alura.R;
import br.com.totustuus.ceep_alura.dao.NotaDAO;
import br.com.totustuus.ceep_alura.model.Nota;
import br.com.totustuus.ceep_alura.ui.recyclerview.adapter.ListaNotasAdapter;
import br.com.totustuus.ceep_alura.ui.recyclerview.adapter.listener.OnItemClickListener;

import static br.com.totustuus.ceep_alura.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.totustuus.ceep_alura.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.totustuus.ceep_alura.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.totustuus.ceep_alura.ui.activity.NotaActivityConstantes.CODIGO_RESULTADO_NOTA_CRIADA;

public class ListaNotasActivity extends AppCompatActivity {

    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        //List<Nota> todasNotas = pegaTodasNotas();
        List<Nota> todasNotas = configuraNotasExemplo();

        configuraRecyclerView(todasNotas);
        botaoInsereNota();
    }

    private void botaoInsereNota() {

        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);

        botaoInsereNota.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                chamaFormularioNotaAcitivity();
            }
        });
    }

    private void chamaFormularioNotaAcitivity() {
        Intent iniciaFormularioNota = new Intent( ListaNotasActivity.this, FormularioNotaActivity.class);
        startActivityForResult(iniciaFormularioNota, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO dao = new NotaDAO();
        return dao.todos();
    }


    /*
    Agora que estamos enviando o resultado em ListaNotasActivity.java, além de enviarmos somente
    a inicialização que espera um resultado, precisamos implementar o método que ficará atento
    ao que é recebido. Esse método é o onActivityResult().

    Ele será responsável pela identificação da requisição que fizemos e se ela é atendida
    conforme o esperado.

    Os parâmetros recebidos por ela são justamente aqueles com os quais esperávamos lidar:
    1) requestCode, ou código de requisição;
    2) resultCode, ou código de resultado;
    3) data, ou o tipo de dado enviado.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        // verificando se requestCode é 1, se o resultCode é 2 e se possui o "extra" chamado "nota"
        if(isResultadoComNota(requestCode, resultCode, data)) { // INSERCAO DE UM NOVO REGISTRO
            Nota nota = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            adiciona(nota);
        } else if(requestCode == 2 &&
                resultCode == CODIGO_RESULTADO_NOTA_CRIADA &&
                data.hasExtra(CHAVE_NOTA) && data.hasExtra(CHAVE_POSICAO)) { // EDICAO DE UM REGISTRO

            Nota nota = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            int posicao = data.getIntExtra(CHAVE_POSICAO, -1);

            new NotaDAO().altera(posicao, nota);
            adapter.altera(posicao, nota);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void adiciona(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adiciona(nota);
    }

    private boolean isResultadoComNota(int requestCode, int resultCode, @Nullable Intent data) {
        return isCodReqInsereNota(requestCode) && isCodResNotaCriada(resultCode) && hasNota(data);
    }

    private boolean hasNota(@Nullable Intent data) {
        return data.hasExtra(CHAVE_NOTA);
    }

    private boolean isCodResNotaCriada(int resultCode) {
        return resultCode == CODIGO_RESULTADO_NOTA_CRIADA;
    }

    private boolean isCodReqInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private List<Nota> configuraNotasExemplo() {

        NotaDAO notaDAO = new NotaDAO();

        for (int i = 0; i < 10; i++){
            notaDAO.insere(new Nota("Título " + (i + 1), "Descrição " + (i + 1)));
        }

        return notaDAO.todos();
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recycler_view);
        configuraAdapter(todasNotas, listaNotas);
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

        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);

        /*
        Implementando uma ação de click para cada ViewHolder.
        Ler a descrição dessa implementação em NotaViewHolder.
         */
        adapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(Nota nota, int posicao) {

                Log.i("click", "onclick do listaNotasAdapter...");

                Intent abreFormularioNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
                abreFormularioNota.putExtra(CHAVE_NOTA, nota);
                abreFormularioNota.putExtra(CHAVE_POSICAO, posicao);

                startActivityForResult(abreFormularioNota, 2);

            }
        });
    }
}
