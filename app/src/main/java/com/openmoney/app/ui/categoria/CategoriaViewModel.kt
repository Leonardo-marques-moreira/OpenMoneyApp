package com.openmoney.app.ui.categoria

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.openmoney.app.data.local.armazenamento.ArmazenamentoCategoriasArquivo
import com.openmoney.app.data.repositorio.RepositorioCategoriaLocal
import com.openmoney.app.domain.categoria.ResultadoCadastroCategoriaLocal
import com.openmoney.app.domain.categoria.ServicoCategoriaLocal
import com.openmoney.app.domain.categoria.ValidadorCadastroCategoria
import com.openmoney.app.domain.model.TipoTransacao
import com.openmoney.app.domain.model.Usuario
import com.openmoney.app.ui.conta.CoresContaDisponiveis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriaViewModel(
    private val servicoCategoria: ServicoCategoriaLocal,
    private val usuarioAutenticado: Usuario,
) : ViewModel() {

    var estado by mutableStateOf(EstadoTelaCategoria(carregando = true))
        private set

    init {
        carregarCategorias()
    }

    fun prepararNovaCategoria(tipoPadrao: TipoTransacao) {
        estado = estado.copy(
            tipoSelecionado = tipoPadrao,
            nomeCategoria = "",
            iconeSelecionado = IconesCategoriaDisponiveis.first().codigo,
            corSelecionada = CoresContaDisponiveis.first(),
            categoriaCriadaId = null,
            erroNomeCategoria = null,
            erroTipoCategoria = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun selecionarTipo(tipo: TipoTransacao) {
        estado = estado.copy(
            tipoSelecionado = tipo,
            erroTipoCategoria = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun atualizarNomeCategoria(novoNome: String) {
        estado = estado.copy(
            nomeCategoria = novoNome,
            erroNomeCategoria = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun selecionarIcone(codigoIcone: String) {
        estado = estado.copy(iconeSelecionado = codigoIcone)
    }

    fun selecionarCor(cor: String) {
        estado = estado.copy(corSelecionada = cor)
    }

    fun cadastrarCategoria() {
        val estadoAtual = estado
        val resultadoValidacao = ValidadorCadastroCategoria.validar(
            nomeCategoria = estadoAtual.nomeCategoria,
            tipoCategoria = estadoAtual.tipoSelecionado,
        )

        if (resultadoValidacao.temErro) {
            estado = estadoAtual.copy(
                erroNomeCategoria = resultadoValidacao.erroNomeCategoria,
                erroTipoCategoria = resultadoValidacao.erroTipoCategoria,
                mensagemErro = null,
                mensagemSucesso = null,
            )
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            when (
                val resultado = servicoCategoria.cadastrar(
                    usuarioId = usuarioAutenticado.id,
                    nomeCategoria = estadoAtual.nomeCategoria,
                    tipoCategoria = estadoAtual.tipoSelecionado,
                    iconeCategoria = estadoAtual.iconeSelecionado,
                    corCategoria = estadoAtual.corSelecionada,
                )
            ) {
                is ResultadoCadastroCategoriaLocal.Erro -> {
                    withContext(Dispatchers.Main) {
                        estado = estado.copy(
                            mensagemErro = resultado.mensagem,
                            mensagemSucesso = null,
                        )
                    }
                }

                is ResultadoCadastroCategoriaLocal.Sucesso -> {
                    val categoriasAtualizadas = servicoCategoria.listarPorUsuario(usuarioAutenticado.id)
                    withContext(Dispatchers.Main) {
                        estado = estado.copy(
                            categorias = categoriasAtualizadas,
                            nomeCategoria = "",
                            categoriaCriadaId = resultado.categoria.id,
                            erroNomeCategoria = null,
                            erroTipoCategoria = null,
                            mensagemErro = null,
                            mensagemSucesso = "Categoria cadastrada com sucesso",
                        )
                    }
                }
            }
        }
    }

    fun limparMensagens() {
        estado = estado.copy(
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    private fun carregarCategorias() {
        viewModelScope.launch(Dispatchers.IO) {
            val categorias = servicoCategoria.listarPorUsuario(usuarioAutenticado.id)
            withContext(Dispatchers.Main) {
                estado = estado.copy(
                    categorias = categorias,
                    carregando = false,
                )
            }
        }
    }
}

class CategoriaViewModelFactory(
    private val context: Context,
    private val usuarioAutenticado: Usuario,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriaViewModel::class.java)) {
            val armazenamento = ArmazenamentoCategoriasArquivo(context.applicationContext)
            val repositorio = RepositorioCategoriaLocal(armazenamento)
            val servico = ServicoCategoriaLocal(repositorio)
            @Suppress("UNCHECKED_CAST")
            return CategoriaViewModel(servico, usuarioAutenticado) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida: ${modelClass.name}")
    }
}
