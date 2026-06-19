package com.openmoney.app.ui.meta

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.openmoney.app.data.local.armazenamento.ArmazenamentoMetasArquivo
import com.openmoney.app.data.repositorio.RepositorioMetaEconomiaLocal
import com.openmoney.app.domain.meta.ResultadoCadastroMetaEconomiaLocal
import com.openmoney.app.domain.meta.ServicoMetaEconomiaLocal
import com.openmoney.app.domain.meta.ValidadorCadastroMetaEconomia
import com.openmoney.app.domain.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MetaViewModel(
    private val servicoMetaEconomia: ServicoMetaEconomiaLocal,
    private val usuarioAutenticado: Usuario,
) : ViewModel() {

    var estado by mutableStateOf(EstadoTelaMeta(carregando = true))
        private set

    init {
        carregarMetas()
    }

    fun irParaListaMetas() {
        estado = estado.copy(
            destinoAtual = DestinoMeta.LISTA_METAS,
            nomeMeta = "",
            valorMeta = "",
            valorAtual = "",
            dataLimite = "",
            erroNomeMeta = null,
            erroValorMeta = null,
            erroValorAtual = null,
            erroDataLimite = null,
            mensagemErro = null,
        )
    }

    fun irParaCriarMeta() {
        estado = estado.copy(
            destinoAtual = DestinoMeta.CRIAR_META,
            nomeMeta = "",
            valorMeta = "",
            valorAtual = "",
            dataLimite = "",
            erroNomeMeta = null,
            erroValorMeta = null,
            erroValorAtual = null,
            erroDataLimite = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun atualizarNomeMeta(novoNome: String) {
        estado = estado.copy(
            nomeMeta = novoNome,
            erroNomeMeta = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun atualizarValorMeta(novoValor: String) {
        estado = estado.copy(
            valorMeta = novoValor,
            erroValorMeta = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun atualizarValorAtual(novoValor: String) {
        estado = estado.copy(
            valorAtual = novoValor,
            erroValorAtual = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun atualizarDataLimite(novaData: String) {
        estado = estado.copy(
            dataLimite = novaData,
            erroDataLimite = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun cadastrarMeta() {
        val estadoAtual = estado
        val resultadoValidacao = ValidadorCadastroMetaEconomia.validar(
            nomeMeta = estadoAtual.nomeMeta,
            valorMetaInformado = estadoAtual.valorMeta,
            valorAtualInformado = estadoAtual.valorAtual,
            dataLimiteInformada = estadoAtual.dataLimite,
        )

        if (resultadoValidacao.temErro) {
            estado = estadoAtual.copy(
                erroNomeMeta = resultadoValidacao.erroNomeMeta,
                erroValorMeta = resultadoValidacao.erroValorMeta,
                erroValorAtual = resultadoValidacao.erroValorAtual,
                erroDataLimite = resultadoValidacao.erroDataLimite,
                mensagemErro = null,
                mensagemSucesso = null,
            )
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            when (
                val resultado = servicoMetaEconomia.cadastrar(
                    usuarioId = usuarioAutenticado.id,
                    nomeMeta = estadoAtual.nomeMeta,
                    valorMeta = requireNotNull(resultadoValidacao.valorMetaNormalizado),
                    valorAtual = requireNotNull(resultadoValidacao.valorAtualNormalizado),
                    dataLimite = requireNotNull(resultadoValidacao.dataLimiteNormalizada),
                )
            ) {
                is ResultadoCadastroMetaEconomiaLocal.Erro -> {
                    withContext(Dispatchers.Main) {
                        estado = estado.copy(
                            mensagemErro = resultado.mensagem,
                            mensagemSucesso = null,
                        )
                    }
                }

                is ResultadoCadastroMetaEconomiaLocal.Sucesso -> {
                    val metasAtualizadas = servicoMetaEconomia.listarPorUsuario(usuarioAutenticado.id)
                    withContext(Dispatchers.Main) {
                        estado = estado.copy(
                            destinoAtual = DestinoMeta.LISTA_METAS,
                            metas = metasAtualizadas,
                            nomeMeta = "",
                            valorMeta = "",
                            valorAtual = "",
                            dataLimite = "",
                            erroNomeMeta = null,
                            erroValorMeta = null,
                            erroValorAtual = null,
                            erroDataLimite = null,
                            mensagemErro = null,
                            mensagemSucesso = "Meta cadastrada com sucesso!",
                        )
                    }
                }
            }
        }
    }

    fun recarregarMetas() {
        carregarMetas()
    }

    private fun carregarMetas() {
        viewModelScope.launch(Dispatchers.IO) {
            val metas = servicoMetaEconomia.listarPorUsuario(usuarioAutenticado.id)
            withContext(Dispatchers.Main) {
                estado = estado.copy(
                    metas = metas,
                    carregando = false,
                )
            }
        }
    }
}

class MetaViewModelFactory(
    private val context: Context,
    private val usuarioAutenticado: Usuario,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MetaViewModel::class.java)) {
            val armazenamento = ArmazenamentoMetasArquivo(context.applicationContext)
            val repositorio = RepositorioMetaEconomiaLocal(armazenamento)
            val servico = ServicoMetaEconomiaLocal(repositorio)

            @Suppress("UNCHECKED_CAST")
            return MetaViewModel(servico, usuarioAutenticado) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida: ${modelClass.name}")
    }
}
