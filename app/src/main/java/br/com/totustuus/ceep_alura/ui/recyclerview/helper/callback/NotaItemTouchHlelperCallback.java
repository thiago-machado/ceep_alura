package br.com.totustuus.ceep_alura.ui.recyclerview.helper.callback;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import br.com.totustuus.ceep_alura.dao.NotaDAO;
import br.com.totustuus.ceep_alura.ui.recyclerview.adapter.ListaNotasAdapter;

/**
 * Define a direção do deslizamento, e outros efeitos.
 * A partir do callback conseguiremos também tomar uma ação,
 * pois o deslize neste caso tem a função de apagar uma nota.
 */
public class NotaItemTouchHlelperCallback extends ItemTouchHelper.Callback {

    private final ListaNotasAdapter adapter;

    public NotaItemTouchHlelperCallback(ListaNotasAdapter adapter) {
        this.adapter = adapter;
    }

    /*
    getMovementFlags() define o que será permitido quanto animação,
    como por exemplo se a nota deve deslizar para esquerda, direita ou ambas.

    Esse método indica marcações ou flags de deslize, para isso usaremos
    um inteiro que chamaremos de marcacoesDeDeslize.
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

        /*
         Permite deslizar para a esquerda e para a direita

         Para os valores usaremos constantes do ItemTouchHelper que nos ajudarão
         a definir a direção das notas, para a esquerda usaremos LEFT e para a direita RIGHT.

         Nós queremos que a nota possa deslizar tanto para a direita quanto para esquerda,
         portanto usaremos | para separar as duas declarações.
         */
        int marcacaoDeslize = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;

        /*
        Apenas fazer as marcações não é o suficiente, precisamos fazer com os movimentos sejam
        de fato criados. Para isso, usaremos o método makeMovementFlags(), em que podemos enviar
        o comportamento esperado para os elementos. É sugerido para nós o comportamento dragFrags,
        que é justamente o de arrastar elementos, mas nós não iremos trabalhar com esse efeito
        por enquanto, portanto configuraremos o valor 0 para essa opção.

        A próxima sugestão é swipeFlagse para este caso simplesmente incluiremos marcacoesDeDeslize.

        O método makeMovementFlags() faz um cálculo sobre as marcações que enviamos e retorna
        uma resposta que pode ser um inteiro, em outras palavras, essa resposta é justamente o
        elemento que devemos enviar para getMovementFlags() para que os movimentos sejam de fato
        criados, portanto substituiremos return 0 por return makeMovementFlags(dragFlags: 0, marcacoesDeDeslize).
         */

        // permite mover para cima, baixo, baixo e acima
        int marcacoesArrastar = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;


        return makeMovementFlags(marcacoesArrastar, marcacaoDeslize);
    }

    /*
    onMove()será a chamada para quando um elemento for arrastado.

    Tanto onMove, quanto onSwiped, atuam de forma similar aos listeners,
    quando ocorrer uma ação específica eles são evocados.

    Nos parâmetros do método onMove() temos a referência do RecyverView que é modificado,
    dois view holders , sendo um deles o que sofre a modificação do comportamento de
    "arraste" do elemento e o outro um target, isto é, aquele que é sobreposto pelo
    elemento modificado.
    Para ficar mais claro: ao arrastarmos "Titulo 1" sobre "Titulo 2", o primeiro caso
    seria o viewHolder e o segundo target.
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

        /*
        Coletaremos a posição inicial de ViewHolder utilizando o método getAdapterPosition().
        A partir do target, coletaremos a posição final.
         */
        int posicaoInicial = viewHolder.getAdapterPosition();
        int alvo = target.getAdapterPosition();

        trocaNotas(posicaoInicial, alvo);

        /*
        Para finalizar a implementação no método onMove(), notaremos que por padrão
        recebemos como retorno um false.
        Caso ele seja mantido, ainda teremos o comportamento esperado em nossa aplicação.
        Contudo, considerando a interface implementada, o callback espera que seja devolvido true
        caso o movimento ocorra, por isso faremos a modificação.
         */
        return true;
    }

    private void trocaNotas(int posicaoInicial, int alvo) {
        new NotaDAO().troca(posicaoInicial, alvo);
        adapter.troca(posicaoInicial, alvo);
    }

    /*
    onSwiped() será responsável pelo movimento de deslize
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        int posicaoNotaDeslizada = viewHolder.getAdapterPosition();
        removeNota(posicaoNotaDeslizada);
    }

    private void removeNota(int posicao) {
        new NotaDAO().remove(posicao);
        adapter.remove(posicao);
    }
}
