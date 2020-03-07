package br.com.totustuus.ceep_alura.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;

import br.com.totustuus.ceep_alura.R;
import br.com.totustuus.ceep_alura.dao.NotaDAO;
import br.com.totustuus.ceep_alura.model.Nota;

import static br.com.totustuus.ceep_alura.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.totustuus.ceep_alura.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.totustuus.ceep_alura.ui.activity.NotaActivityConstantes.CODIGO_RESULTADO_NOTA_CRIADA;

public class FormularioNotaActivity extends AppCompatActivity {

    private int posicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

        Intent dadosRecebidos = getIntent();

        if(dadosRecebidos.hasExtra(CHAVE_NOTA) &&
                dadosRecebidos.hasExtra(CHAVE_POSICAO)){

            Nota notaRecebida = (Nota) dadosRecebidos.getSerializableExtra(CHAVE_NOTA);

            /*
            Quando utilizamos getInExtra() precisamos enviar um valor padrão caso a informação que
            esperamos não seja recebida. Para este caso usaremos o valor -1, que indicará que a
            informação da posicao é inválida, pois não se trata de um índice válido dentro da lista.
             */
            posicao = dadosRecebidos.getIntExtra(CHAVE_POSICAO, -1);


            TextView titulo = findViewById(R.id.formulario_nota_titulo);
            titulo.setText(notaRecebida.getTitulo());
            TextView descricao = findViewById(R.id.formulario_nota_descricao);
            descricao.setText(notaRecebida.getDescricao());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(isMenuSalvaNota(item)){

            Nota notaCriada = criaNota();
            retornaNota(notaCriada);

            // finish() para finalizar a Activity
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void retornaNota(Nota notaCriada) {

        /*
        Para indicar que o resultado será enviado, adicionaremos setResult() antes de finalizá-la (finish).
        Como parâmetro, vamos inserir um resultCode ("Código de Resultado", em português).
        Assim, o combinaremos a requestCode, obtendo a confirmação de que a ação de devolver uma resposta foi atendida.

        A princípio, aplicaremos resultCode: 2 e Intent como parâmetros para o envio da nota,
        pois além da requisição, é necessário enviar os dados coletados em notaCriada.
        O parâmetro Intent permitirá a transição de informações, no caso objetos, via Extra.

        Abaixo, faremos com que seja adicionado um Extra, com putExtra(), e definiremos como
        parâmetro uma string para identificar o que estamos enviando, neste caso uma nota.
        Feito isso, a nota criada será enviada com notaCriada como sendo o segundo parâmetro.

        Em seguida, dicionaremos resultadoInsercao como segundo parâmetro de setResult().
         */
        Intent intent = new Intent();
        intent.putExtra(CHAVE_NOTA, notaCriada);
        intent.putExtra(CHAVE_POSICAO, posicao);

        setResult(CODIGO_RESULTADO_NOTA_CRIADA, intent);
    }

    private Nota criaNota() {
        EditText titulo = findViewById(R.id.formulario_nota_titulo);
        EditText descricao = findViewById(R.id.formulario_nota_descricao);
        return new Nota(titulo.getText().toString(), descricao.getText().toString());
    }

    private boolean isMenuSalvaNota(@NonNull MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_nota_ic_salva;
    }
}
