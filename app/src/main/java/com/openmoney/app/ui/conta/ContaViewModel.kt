package com.openmoney.app.ui.conta

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.openmoney.app.data.local.armazenamento.ArmazenamentoContasArquivo
import com.openmoney.app.data.repositorio.RepositorioContaLocal
import com.openmoney.app.domain.conta.ResultadoCadastroContaLocal
import com.openmoney.app.domain.conta.ServicoContaLocal
import com.openmoney.app.domain.conta.ValidadorCadastroConta
import com.openmoney.app.domain.model.TipoConta
import com.openmoney.app.domain.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContaViewModel(
    private val servicoConta: ServicoContaLocal,
    private val usuarioAutenticado: Usuario,
) : ViewModel() {

    var estado by mutableStateOf(EstadoTelaConta(carregando = true))
        private set

    init {
        carregarContas()
    }

    fun irParaListaContas() {
        estado = estado.copy(
            destinoAtual = DestinoConta.LISTA_CONTAS,
            nomeConta = "",
            tipoContaSelecionado = null,
            corSelecionada = null,
            saldoInicial = "",
            erroNomeConta = null,
            erroTipoConta = null,
            erroSaldoInicial = null,
            mensagemErro = null,
        )
    }

    fun irParaNovaConta() {
        estado = estado.copy(
            destinoAtual = DestinoConta.NOVA_CONTA,
            nomeConta = "",
            tipoContaSelecionado = null,
            corSelecionada = null,
            saldoInicial = "",
            erroNomeConta = null,
            erroTipoConta = null,
            erroSaldoInicial = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun atualizarNomeConta(novoNome: String) {
        estado = estado.copy(
            nomeConta = novoNome,
            erroNomeConta = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun selecionarTipoConta(tipoConta: TipoConta) {
        estado = estado.copy(
            tipoContaSelecionado = tipoConta,
            corSelecionada = tipoConta.corPadrao,
            erroTipoConta = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun selecionarCorConta(cor: String) {
        estado = estado.copy(corSelecionada = cor)
    }

    fun atualizarSaldoInicial(novoSaldo: String) {
        estado = estado.copy(
            saldoInicial = novoSaldo,
            erroSaldoInicial = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun limparMensagemSucesso() {
        estado = estado.copy(mensagemSucesso = null)
    }

    fun cadastrarConta() {
        val estadoAtual = estado
        val resultadoValidacao = ValidadorCadastroConta.validar(
            nomeConta = estadoAtual.nomeConta,
            tipoConta = estadoAtual.tipoContaSelecionado,
            saldoInicial = estadoAtual.saldoInicial,
        )

        if (resultadoValidacao.temErro) {
            estado = estadoAtual.copy(
                erroNomeConta = resultadoValidacao.erroNomeConta,
                erroTipoConta = resultadoValidacao.erroTipoConta,
                erroSaldoInicial = resultadoValidacao.erroSaldoInicial,
                mensagemErro = null,
                mensagemSucesso = null,
            )
            return
        }

        val saldoNormalizado = requireNotNull(resultadoValidacao.saldoNormalizado)

        viewModelScope.launch(Dispatchers.IO) {
            when (
                val resultado = servicoConta.cadastrar(
                    usuarioId = usuarioAutenticado.id,
                    nomeConta = estadoAtual.nomeConta,
                    tipoConta = estadoAtual.tipoContaSelecionado,
                    corConta = estadoAtual.corSelecionada,
                    saldoInicial = saldoNormalizado,
                )
            ) {
                is ResultadoCadastroContaLocal.Erro -> {
                    withContext(Dispatchers.Main) {
                        estado = estado.copy(
                            mensagemErro = resultado.mensagem,
                            mensagemSucesso = null,
                        )
                    }
                }

                is ResultadoCadastroContaLocal.Sucesso -> {
                    val contasAtualizadas = servicoConta.listarPorUsuario(usuarioAutenticado.id)
                    withContext(Dispatchers.Main) {
                        estado = estado.copy(
                            destinoAtual = DestinoConta.LISTA_CONTAS,
                            contas = contasAtualizadas,
                            nomeConta = "",
                            tipoContaSelecionado = null,
                            corSelecionada = null,
                            saldoInicial = "",
                            erroNomeConta = null,
                            erroTipoConta = null,
                            erroSaldoInicial = null,
                            mensagemErro = null,
                            mensagemSucesso = "Conta cadastrada com sucesso",
                        )
                    }
                }
            }
        }
    }

    fun recarregarContas() {
        carregarContas()
    }

    private fun carregarContas() {
        viewModelScope.launch(Dispatchers.IO) {
            val contas = servicoConta.listarPorUsuario(usuarioAutenticado.id)
            withContext(Dispatchers.Main) {
                estado = estado.copy(
                    contas = contas,
                    carregando = false,
                )
            }
        }
    }
}

class ContaViewModelFactory(
    private val context: Context,
    private val usuarioAutenticado: Usuario,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContaViewModel::class.java)) {
            val armazenamento = ArmazenamentoContasArquivo(context.applicationContext)
            val repositorio = RepositorioContaLocal(armazenamento)
            val servico = ServicoContaLocal(repositorio)
            @Suppress("UNCHECKED_CAST")
            return ContaViewModel(servico, usuarioAutenticado) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida: ${modelClass.name}")
    }
}
