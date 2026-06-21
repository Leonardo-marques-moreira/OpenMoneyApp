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
import com.openmoney.app.domain.conta.CalculadoraSaldoConta
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
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class RelatorioViewModel(
    private val servicoConta: ServicoContaLocal,
    private val servicoCategoria: ServicoCategoriaLocal,
    private val servicoTransacao: ServicoTransacaoLocal,
    private val usuarioAutenticado: Usuario,
) : ViewModel() {

    private val localeBrasil = Locale.forLanguageTag("pt-BR")
    private var mesReferenciaSelecionado: YearMonth? = null

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
            val periodosDisponiveis = montarPeriodosDisponiveis(transacoes)
            val mesReferencia = resolverMesReferencia(
                transacoes = transacoes,
                periodosDisponiveis = periodosDisponiveis,
            )
            val transacoesMesReferencia = filtrarTransacoesPorMes(
                transacoes = transacoes,
                mesReferencia = mesReferencia,
            )

            val saldoAtual = CalculadoraSaldoConta.calcularSaldoTotal(contas)
            val totalReceitas = somarPorTipo(transacoesMesReferencia, TipoTransacao.RECEITA)
            val totalDespesas = somarPorTipo(transacoesMesReferencia, TipoTransacao.DESPESA)

            val receitasPorCategoria = agruparPorCategoria(
                transacoes = transacoesMesReferencia,
                categoriasPorId = categoriasPorId,
                tipoTransacao = TipoTransacao.RECEITA,
            )
            val despesasPorCategoria = agruparPorCategoria(
                transacoes = transacoesMesReferencia,
                categoriasPorId = categoriasPorId,
                tipoTransacao = TipoTransacao.DESPESA,
            )
            val comparativoMensal = montarComparativoMensal(
                transacoes = transacoes,
                mesReferencia = mesReferencia,
            )

            withContext(Dispatchers.Main) {
                estado = EstadoTelaRelatorios(
                    identificadorPeriodoSelecionado = mesReferencia.toString(),
                    periodoReferencia = formatarPeriodoReferencia(mesReferencia),
                    periodosDisponiveis = periodosDisponiveis,
                    saldoAtual = saldoAtual,
                    saldoMovimentado = totalReceitas - totalDespesas,
                    totalReceitas = totalReceitas,
                    totalDespesas = totalDespesas,
                    receitasPorCategoria = receitasPorCategoria,
                    despesasPorCategoria = despesasPorCategoria,
                    comparativoMensal = comparativoMensal,
                    carregando = false,
                )
            }
        }
    }

    fun selecionarPeriodo(identificadorPeriodo: String) {
        val novoMesReferencia = runCatching {
            YearMonth.parse(identificadorPeriodo)
        }.getOrNull() ?: return

        if (mesReferenciaSelecionado == novoMesReferencia) {
            return
        }

        mesReferenciaSelecionado = novoMesReferencia
        recarregarRelatorios()
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

    private fun resolverMesReferencia(
        transacoes: List<Transacao>,
        periodosDisponiveis: List<ItemPeriodoRelatorio>,
    ): YearMonth {
        val identificadoresDisponiveis = periodosDisponiveis
            .map(ItemPeriodoRelatorio::identificador)
            .toSet()

        val mesSelecionado = mesReferenciaSelecionado
        if (mesSelecionado != null && identificadoresDisponiveis.contains(mesSelecionado.toString())) {
            return mesSelecionado
        }

        val dataMaisRecente = transacoes.maxOfOrNull(Transacao::data)
        return if (dataMaisRecente != null) {
            YearMonth.from(dataMaisRecente)
        } else {
            YearMonth.now()
        }
    }

    private fun montarPeriodosDisponiveis(
        transacoes: List<Transacao>,
    ): List<ItemPeriodoRelatorio> {
        val mesesDisponiveis = transacoes
            .map { YearMonth.from(it.data) }
            .distinct()
            .sortedDescending()

        val mesesBase = if (mesesDisponiveis.isEmpty()) {
            listOf(YearMonth.now())
        } else {
            mesesDisponiveis
        }

        return mesesBase.map { mes ->
            ItemPeriodoRelatorio(
                identificador = mes.toString(),
                rotulo = formatarPeriodoReferencia(mes),
            )
        }
    }

    private fun filtrarTransacoesPorMes(
        transacoes: List<Transacao>,
        mesReferencia: YearMonth,
    ): List<Transacao> {
        return transacoes.filter { YearMonth.from(it.data) == mesReferencia }
    }

    private fun montarComparativoMensal(
        transacoes: List<Transacao>,
        mesReferencia: YearMonth,
    ): List<ItemComparativoMensalRelatorio> {
        val transacoesPorMes = transacoes.groupBy { YearMonth.from(it.data) }

        return (3 downTo 0).map { deslocamento ->
            val mes = mesReferencia.minusMonths(deslocamento.toLong())
            val transacoesMes = transacoesPorMes[mes].orEmpty()

            ItemComparativoMensalRelatorio(
                rotuloMes = formatarRotuloMes(mes, localeBrasil),
                totalReceitas = somarPorTipo(transacoesMes, TipoTransacao.RECEITA),
                totalDespesas = somarPorTipo(transacoesMes, TipoTransacao.DESPESA),
                emDestaque = mes == mesReferencia,
            )
        }
    }

    private fun formatarPeriodoReferencia(mesReferencia: YearMonth): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", localeBrasil)
        val textoFormatado = mesReferencia.atDay(1).format(formatter)
        return textoFormatado.replaceFirstChar { letra ->
            if (letra.isLowerCase()) {
                letra.titlecase(localeBrasil)
            } else {
                letra.toString()
            }
        }
    }

    private fun formatarRotuloMes(
        mesReferencia: YearMonth,
        locale: Locale,
    ): String {
        val rotulo = mesReferencia.month
            .getDisplayName(TextStyle.SHORT, locale)
            .replace(".", "")

        return rotulo.replaceFirstChar { letra ->
            if (letra.isLowerCase()) {
                letra.titlecase(locale)
            } else {
                letra.toString()
            }
        }
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
