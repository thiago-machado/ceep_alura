package br.com.totustuus.ceep_alura.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import br.com.totustuus.ceep_alura.R;
import br.com.totustuus.ceep_alura.dao.NotaDAO;
import br.com.totustuus.ceep_alura.model.Nota;

import static br.com.totustuus.ceep_alura.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.totustuus.ceep_alura.ui.activity.NotaActivityConstantes.CODIGO_RESULTADO_NOTA_CRIADA;

public class FormularioNotaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);
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
