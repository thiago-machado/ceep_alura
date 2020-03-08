package br.com.totustuus.ceep_alura.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import br.com.totustuus.ceep_alura.ui.recyclerview.helper.callback.NotaItemTouchHlelperCallback;

import static br.com.totustuus.ceep_alura.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.totustuus.ceep_alura.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.totustuus.ceep_alura.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_EDITA_NOTA;
import static br.com.totustuus.ceep_alura.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.totustuus.ceep_alura.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

public class ListaNotasActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Notas";
    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        setTitle(TITULO_APPBAR);

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
                chamaFormularioNotaAcitivityAdicao();
            }
        });
    }

    private void chamaFormularioNotaAcitivityAdicao() {
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
        if(isResultadoInsereNota(requestCode, data)) { // INSERCAO DE UM NOVO REGISTRO

            /*
            Na documentação do Android, é recomendado que a verificação do resultCode seja realizada
            separada das outras verificações.
            Por que isso? Pelo seguinte motivo: pode acontecer de retornar a "nota", mas o "resultcode"
            ser diferente de RESULT_OK. O reseultCode pode ser RESULT_CANCELED, por exemplo.
            Ou seja, nos retornou a nota, mas o resultado é diferente de OK, sinalzando que podemos
            tomar uma atitude quanto a isso.
             */
            if(isCodResOK(resultCode)) {
                Nota nota = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                adiciona(nota);
            }

        } else if(isResultadoEditaNota(requestCode, data)) { // EDICAO DE UM REGISTRO

            if(isCodResOK(resultCode)) {
                Nota nota = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                int posicao = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);

                if (isPosicaoValida(posicao)) {
                    edita(nota, posicao);
                } else {
                    Toast.makeText(this, "Ocorreu um problema na alteração da nota", Toast.LENGTH_LONG).show();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void edita(Nota nota, int posicao) {
        new NotaDAO().altera(posicao, nota);
        adapter.altera(posicao, nota);
    }

    private boolean isPosicaoValida(int posicao) {
        return posicao > POSICAO_INVALIDA;
    }

    private boolean isResultadoEditaNota(int requestCode, @Nullable Intent data) {
        return isCodReqEditaNota(requestCode) && hasNota(data);
    }

    private boolean isCodReqEditaNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_EDITA_NOTA;
    }

    private void adiciona(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adiciona(nota);
    }

    private boolean isResultadoInsereNota(int requestCode, @Nullable Intent data) {
        return isCodReqInsereNota(requestCode) && hasNota(data);
    }

    private boolean hasNota(@Nullable Intent data) {
        /*
         Verifica se Intent é diferente de NULL, pois caso o usuário aperte
         para voltar na tela de cadastro/edição, o Intent será nulo.
         Então essa validação nos protegerá de um crash na aplicação.
         */
        return data != null && data.hasExtra(CHAVE_NOTA);
    }

    private boolean isCodResOK(int resultCode) {
        return resultCode == RESULT_OK;
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

        /*for (int i = 0; i < 10; i++){
            notaDAO.insere(new Nota("Título " + (i + 1), "Descrição " + (i + 1)));
        }*/

        return notaDAO.todos();
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recycler_view);
        configuraAdapter(todasNotas, listaNotas);
        configuraItemTouchHelper(listaNotas);

    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {

        /*
        ItemTouchHelper lidará com o comportamento de toque de item.
        Para poder definir os comportamentos de toque de item, ItemTouchHelper espera
        que haja a implementação de um Callback, trata-se de uma entidade que ficará
        responsável em fazer toda a configuração de movimento ou de deslize para ItemTouchHelper().

        Com isso, conseguiremos definir a direção do deslizamento, e outros efeitos.
        A partir do callback conseguiremos também tomar uma ação, pois o deslize neste
        caso tem a função de apagar uma nota.
         */
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHlelperCallback(adapter));

        /*
        Criaremos um objeto de itemTouchHelper. A partir desse objeto teremos a capacidade de
        anexar ou fixar os comportamentos com o auxílio do método attachToRecyclerView(),
        que receberá como parâmetro listaNotas.
         */
        itemTouchHelper.attachToRecyclerView(listaNotas);
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
                chamaFormularioNotaActivityParaAlteracao(nota, posicao);
            }
        });
    }

    private void chamaFormularioNotaActivityParaAlteracao(Nota nota, int posicao) {

        Log.i("click", "onclick do listaNotasAdapter...");

        Intent abreFormularioNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        abreFormularioNota.putExtra(CHAVE_NOTA, nota);
        abreFormularioNota.putExtra(CHAVE_POSICAO, posicao);

        startActivityForResult(abreFormularioNota, CODIGO_REQUISICAO_EDITA_NOTA);
    }
}
