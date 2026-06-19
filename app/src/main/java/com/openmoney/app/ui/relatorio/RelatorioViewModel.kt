package com.openmoney.app.ui.relatorio

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.openmoney.app.data.local.armazenamento.ArmazenamentoCategoriasArquivo
import com.openmoney.app.data.local.armazenamento.ArmazenamentoContasArquivo
import com.openmoney.app.data.local.armazenamento.ArmazenamentoTransacoesArquivo
import com.openmoney.app.data.repositorio.RepositorioCategoriaLocal
import com.openmoney.app.data.repositorio.RepositorioContaLocal
import com.openmoney.app.data.repositorio.RepositorioTransacaoLocal
import com.openmoney.app.domain.categoria.ServicoCategoriaLocal
import com.openmoney.app.domain.conta.ServicoContaLocal
import com.openmoney.app.domain.model.Categoria
import com.openmoney.app.domain.model.TipoTransacao
import com.openmoney.app.domain.model.Transacao
import com.openmoney.app.domain.model.Usuario
import com.openmoney.app.domain.transacao.ServicoTransacaoLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class RelatorioViewModel(
    private val servicoConta: ServicoContaLocal,
    private val servicoCategoria: ServicoCategoriaLocal,
    private val servicoTransacao: ServicoTransacaoLocal,
    private val usuarioAutenticado: Usuario,
) : ViewModel() {

    var estado by mutableStateOf(EstadoTelaRelatorios(carregando = true))
        private set

    init {
        recarregarRelatorios()
    }

    fun recarregarRelatorios() {
        estado = estado.copy(carregando = true)

        viewModelScope.launch(Dispatchers.IO) {
            val contas = servicoConta.listarPorUsuario(usuarioAutenticado.id)
            val categoriasPorId = servicoCategoria.listarPorUsuario(usuarioAutenticado.id)
                .associateBy(Categoria::id)
            val transacoes = servicoTransacao.listarPorUsuario(usuarioAutenticado.id)

            val saldoAtual = contas.fold(BigDecimal.ZERO) { acumulado, conta -> acumulado + conta.saldo }
            val totalReceitas = somarPorTipo(transacoes, TipoTransacao.RECEITA)
            val totalDespesas = somarPorTipo(transacoes, TipoTransacao.DESPESA)

            val receitasPorCategoria = agruparPorCategoria(
                transacoes = transacoes,
                categoriasPorId = categoriasPorId,
                tipoTransacao = TipoTransacao.RECEITA,
            )
            val despesasPorCategoria = agruparPorCategoria(
                transacoes = transacoes,
                categoriasPorId = categoriasPorId,
                tipoTransacao = TipoTransacao.DESPESA,
            )

            withContext(Dispatchers.Main) {
                estado = EstadoTelaRelatorios(
                    saldoAtual = saldoAtual,
                    saldoMovimentado = totalReceitas - totalDespesas,
                    totalReceitas = totalReceitas,
                    totalDespesas = totalDespesas,
                    receitasPorCategoria = receitasPorCategoria,
                    despesasPorCategoria = despesasPorCategoria,
                    carregando = false,
                )
            }
        }
    }

    private fun somarPorTipo(
        transacoes: List<Transacao>,
        tipoTransacao: TipoTransacao,
    ): BigDecimal {
        return transacoes
            .filter { it.tipo == tipoTransacao }
            .fold(BigDecimal.ZERO) { acumulado, transacao -> acumulado + transacao.valor }
    }

    private fun agruparPorCategoria(
        transacoes: List<Transacao>,
        categoriasPorId: Map<Long, Categoria>,
        tipoTransacao: TipoTransacao,
    ): List<ItemResumoCategoriaRelatorio> {
        return transacoes
            .filter { it.tipo == tipoTransacao }
            .groupBy { it.categoriaId }
            .map { (categoriaId, transacoesCategoria) ->
                val categoria = categoriasPorId[categoriaId]
                ItemResumoCategoriaRelatorio(
                    categoriaNome = categoria?.nome ?: "Categoria",
                    corCategoria = categoria?.cor ?: "#25A67C",
                    total = transacoesCategoria.fold(BigDecimal.ZERO) { acumulado, transacao ->
                        acumulado + transacao.valor
                    },
                )
            }
            .sortedByDescending { it.total }
    }
}

class RelatorioViewModelFactory(
    private val context: Context,
    private val usuarioAutenticado: Usuario,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RelatorioViewModel::class.java)) {
            val contextoAplicacao = context.applicationContext
            val armazenamentoContas = ArmazenamentoContasArquivo(contextoAplicacao)
            val armazenamentoCategorias = ArmazenamentoCategoriasArquivo(contextoAplicacao)
            val armazenamentoTransacoes = ArmazenamentoTransacoesArquivo(contextoAplicacao)

            val repositorioConta = RepositorioContaLocal(armazenamentoContas)
            val repositorioCategoria = RepositorioCategoriaLocal(armazenamentoCategorias)
            val repositorioTransacao = RepositorioTransacaoLocal(armazenamentoTransacoes)

            val servicoConta = ServicoContaLocal(repositorioConta)
            val servicoCategoria = ServicoCategoriaLocal(repositorioCategoria)
            val servicoTransacao = ServicoTransacaoLocal(
                repositorioTransacao = repositorioTransacao,
                repositorioConta = repositorioConta,
                repositorioCategoria = repositorioCategoria,
            )

            @Suppress("UNCHECKED_CAST")
            return RelatorioViewModel(
                servicoConta = servicoConta,
                servicoCategoria = servicoCategoria,
                servicoTransacao = servicoTransacao,
                usuarioAutenticado = usuarioAutenticado,
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida: ${modelClass.name}")
    }
}
