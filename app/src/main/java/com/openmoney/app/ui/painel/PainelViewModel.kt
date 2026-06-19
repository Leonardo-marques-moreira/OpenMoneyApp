package com.openmoney.app.ui.painel

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

class PainelViewModel(
    private val servicoConta: ServicoContaLocal,
    private val servicoCategoria: ServicoCategoriaLocal,
    private val servicoTransacao: ServicoTransacaoLocal,
    private val usuarioAutenticado: Usuario,
) : ViewModel() {

    var estado by mutableStateOf(EstadoTelaDashboard(carregando = true))
        private set

    init {
        recarregarPainel()
    }

    fun recarregarPainel() {
        estado = estado.copy(carregando = true)

        viewModelScope.launch(Dispatchers.IO) {
            val contas = servicoConta.listarPorUsuario(usuarioAutenticado.id)
            val categoriasPorId = servicoCategoria.listarPorUsuario(usuarioAutenticado.id)
                .associateBy(Categoria::id)
            val transacoes = servicoTransacao.listarPorUsuario(usuarioAutenticado.id)

            val saldoTotal = contas.fold(BigDecimal.ZERO) { acumulado, conta -> acumulado + conta.saldo }
            val totalReceitas = transacoes
                .filter { it.tipo == TipoTransacao.RECEITA }
                .fold(BigDecimal.ZERO) { acumulado, transacao -> acumulado + transacao.valor }
            val totalDespesas = transacoes
                .filter { it.tipo == TipoTransacao.DESPESA }
                .fold(BigDecimal.ZERO) { acumulado, transacao -> acumulado + transacao.valor }

            val ultimasTransacoes = transacoes
                .take(3)
                .map { transacao ->
                    transacao.toItemDashboard(
                        categoria = categoriasPorId[transacao.categoriaId],
                    )
                }

            withContext(Dispatchers.Main) {
                estado = EstadoTelaDashboard(
                    saldoTotal = saldoTotal,
                    totalReceitas = totalReceitas,
                    totalDespesas = totalDespesas,
                    ultimasTransacoes = ultimasTransacoes,
                    carregando = false,
                )
            }
        }
    }

    private fun Transacao.toItemDashboard(categoria: Categoria?): ItemUltimaTransacaoDashboard {
        return ItemUltimaTransacaoDashboard(
            id = id,
            descricao = descricao,
            categoriaNome = categoria?.nome ?: "Categoria",
            data = data,
            valor = valor,
            tipo = tipo,
            codigoIconeCategoria = categoria?.icone ?: "wallet",
            corCategoria = categoria?.cor ?: "#25A67C",
        )
    }
}

class PainelViewModelFactory(
    private val context: Context,
    private val usuarioAutenticado: Usuario,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PainelViewModel::class.java)) {
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
            return PainelViewModel(
                servicoConta = servicoConta,
                servicoCategoria = servicoCategoria,
                servicoTransacao = servicoTransacao,
                usuarioAutenticado = usuarioAutenticado,
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida: ${modelClass.name}")
    }
}
