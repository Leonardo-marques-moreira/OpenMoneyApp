package com.openmoney.app.ui.transacao

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
import com.openmoney.app.domain.model.Conta
import com.openmoney.app.domain.model.TipoTransacao
import com.openmoney.app.domain.model.Usuario
import com.openmoney.app.domain.transacao.ResultadoCadastroTransacaoLocal
import com.openmoney.app.domain.transacao.ServicoTransacaoLocal
import com.openmoney.app.domain.transacao.ValidadorCadastroTransacao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransacaoViewModel(
    private val servicoTransacao: ServicoTransacaoLocal,
    private val servicoConta: ServicoContaLocal,
    private val servicoCategoria: ServicoCategoriaLocal,
    private val usuarioAutenticado: Usuario,
) : ViewModel() {

    var estado by mutableStateOf(EstadoTelaTransacao(carregando = true))
        private set

    init {
        carregarDados(
            tipoPreferido = estado.tipoSelecionado,
            contaPreferida = null,
            categoriaPreferida = null,
            exibirCarregando = true,
        )
    }

    fun selecionarTipo(tipoTransacao: TipoTransacao) {
        if (estado.tipoSelecionado == tipoTransacao) {
            return
        }

        estado = estado.copy(
            tipoSelecionado = tipoTransacao,
            erroTipoTransacao = null,
            erroCategoria = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )

        carregarDados(
            tipoPreferido = tipoTransacao,
            contaPreferida = estado.contaSelecionadaId,
            categoriaPreferida = null,
        )
    }

    fun atualizarValor(novoValor: String) {
        estado = estado.copy(
            valor = novoValor,
            erroValor = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun atualizarDescricao(novaDescricao: String) {
        estado = estado.copy(
            descricao = novaDescricao,
            erroDescricao = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun atualizarData(novaData: String) {
        estado = estado.copy(
            dataLancamento = novaData,
            erroData = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun selecionarConta(contaId: Long) {
        estado = estado.copy(
            contaSelecionadaId = contaId,
            erroConta = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun selecionarCategoria(categoriaId: Long) {
        estado = estado.copy(
            categoriaSelecionadaId = categoriaId,
            erroCategoria = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun recarregarCategorias() {
        carregarDados(
            tipoPreferido = estado.tipoSelecionado,
            contaPreferida = estado.contaSelecionadaId,
            categoriaPreferida = estado.categoriaSelecionadaId,
        )
    }

    fun informarMensagemSucesso(mensagem: String) {
        estado = estado.copy(
            mensagemSucesso = mensagem,
            mensagemErro = null,
        )
    }

    fun cadastrarTransacao() {
        val estadoAtual = estado
        val resultadoValidacao = ValidadorCadastroTransacao.validar(
            tipoTransacao = estadoAtual.tipoSelecionado,
            valorInformado = estadoAtual.valor,
            descricao = estadoAtual.descricao,
            dataInformada = estadoAtual.dataLancamento,
            contaId = estadoAtual.contaSelecionadaId,
            categoriaId = estadoAtual.categoriaSelecionadaId,
        )

        if (resultadoValidacao.temErro) {
            estado = estadoAtual.copy(
                erroTipoTransacao = resultadoValidacao.erroTipoTransacao,
                erroValor = resultadoValidacao.erroValor,
                erroDescricao = resultadoValidacao.erroDescricao,
                erroCategoria = resultadoValidacao.erroCategoria,
                erroConta = resultadoValidacao.erroConta,
                erroData = resultadoValidacao.erroData,
                mensagemErro = null,
                mensagemSucesso = null,
            )
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            when (
                val resultado = servicoTransacao.cadastrar(
                    usuarioId = usuarioAutenticado.id,
                    tipoTransacao = estadoAtual.tipoSelecionado,
                    valor = requireNotNull(resultadoValidacao.valorNormalizado),
                    descricao = estadoAtual.descricao,
                    data = requireNotNull(resultadoValidacao.dataNormalizada),
                    contaId = estadoAtual.contaSelecionadaId,
                    categoriaId = estadoAtual.categoriaSelecionadaId,
                )
            ) {
                is ResultadoCadastroTransacaoLocal.Erro -> {
                    withContext(Dispatchers.Main) {
                        estado = estado.copy(
                            mensagemErro = resultado.mensagem,
                            mensagemSucesso = null,
                        )
                    }
                }

                is ResultadoCadastroTransacaoLocal.Sucesso -> {
                    val contasAtualizadas = servicoConta.listarPorUsuario(usuarioAutenticado.id)
                    val categoriasAtualizadas = servicoCategoria.listarPorUsuarioETipo(
                        usuarioAutenticado.id,
                        estadoAtual.tipoSelecionado,
                    )

                    withContext(Dispatchers.Main) {
                        estado = estado.copy(
                            valor = "",
                            descricao = "",
                            dataLancamento = EstadoTelaTransacao().dataLancamento,
                            contasDisponiveis = contasAtualizadas,
                            categoriasDisponiveis = categoriasAtualizadas,
                            contaSelecionadaId = resolverContaSelecionada(
                                contasAtualizadas,
                                estadoAtual.contaSelecionadaId,
                            ),
                            categoriaSelecionadaId = resolverCategoriaSelecionada(
                                categoriasAtualizadas,
                                estadoAtual.categoriaSelecionadaId,
                            ),
                            erroTipoTransacao = null,
                            erroValor = null,
                            erroDescricao = null,
                            erroCategoria = null,
                            erroConta = null,
                            erroData = null,
                            mensagemErro = null,
                            mensagemSucesso = "Transacao registrada com sucesso!",
                            versaoSaldoContas = estado.versaoSaldoContas + 1L,
                        )
                    }
                }
            }
        }
    }

    private fun carregarDados(
        tipoPreferido: TipoTransacao,
        contaPreferida: Long?,
        categoriaPreferida: Long?,
        exibirCarregando: Boolean = false,
    ) {
        if (exibirCarregando) {
            estado = estado.copy(carregando = true)
        }

        viewModelScope.launch(Dispatchers.IO) {
            val contas = servicoConta.listarPorUsuario(usuarioAutenticado.id)
            val categorias = servicoCategoria.listarPorUsuarioETipo(
                usuarioAutenticado.id,
                tipoPreferido,
            )

            withContext(Dispatchers.Main) {
                estado = estado.copy(
                    tipoSelecionado = tipoPreferido,
                    contasDisponiveis = contas,
                    categoriasDisponiveis = categorias,
                    contaSelecionadaId = resolverContaSelecionada(contas, contaPreferida),
                    categoriaSelecionadaId = resolverCategoriaSelecionada(categorias, categoriaPreferida),
                    carregando = false,
                )
            }
        }
    }

    private fun resolverContaSelecionada(
        contas: List<Conta>,
        contaPreferida: Long?,
    ): Long? {
        return contaPreferida?.takeIf { id -> contas.any { it.id == id } }
            ?: contas.firstOrNull()?.id
    }

    private fun resolverCategoriaSelecionada(
        categorias: List<Categoria>,
        categoriaPreferida: Long?,
    ): Long? {
        return categoriaPreferida?.takeIf { id -> categorias.any { it.id == id } }
            ?: categorias.firstOrNull()?.id
    }
}

class TransacaoViewModelFactory(
    private val context: Context,
    private val usuarioAutenticado: Usuario,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransacaoViewModel::class.java)) {
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
            return TransacaoViewModel(
                servicoTransacao = servicoTransacao,
                servicoConta = servicoConta,
                servicoCategoria = servicoCategoria,
                usuarioAutenticado = usuarioAutenticado,
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida: ${modelClass.name}")
    }
}
