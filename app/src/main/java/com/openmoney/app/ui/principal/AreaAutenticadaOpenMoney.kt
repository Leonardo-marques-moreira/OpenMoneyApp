package com.openmoney.app.ui.principal

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.openmoney.app.domain.model.Usuario
import com.openmoney.app.ui.categoria.CategoriaViewModel
import com.openmoney.app.ui.categoria.CategoriaViewModelFactory
import com.openmoney.app.ui.categoria.TelaNovaCategoria
import com.openmoney.app.ui.conta.ContaViewModel
import com.openmoney.app.ui.conta.ContaViewModelFactory
import com.openmoney.app.ui.conta.DestinoConta
import com.openmoney.app.ui.conta.TelaMinhasContas
import com.openmoney.app.ui.conta.TelaNovaConta
import com.openmoney.app.ui.meta.DestinoMeta
import com.openmoney.app.ui.meta.MetaViewModel
import com.openmoney.app.ui.meta.MetaViewModelFactory
import com.openmoney.app.ui.meta.TelaCriarMeta
import com.openmoney.app.ui.meta.TelaMetasEconomia
import com.openmoney.app.ui.painel.PainelViewModel
import com.openmoney.app.ui.painel.PainelViewModelFactory
import com.openmoney.app.ui.relatorio.RelatorioViewModel
import com.openmoney.app.ui.relatorio.RelatorioViewModelFactory
import com.openmoney.app.ui.relatorio.TelaRelatorios
import com.openmoney.app.ui.transacao.TelaNovaTransacao
import com.openmoney.app.ui.transacao.TransacaoViewModel
import com.openmoney.app.ui.transacao.TransacaoViewModelFactory

@Composable
fun AreaAutenticadaOpenMoney(
    usuario: Usuario,
    aoClicarSair: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    var destinoAreaLogada by rememberSaveable(usuario.id) {
        mutableStateOf(DestinoAreaLogada.DASHBOARD)
    }
    var destinoLancamento by rememberSaveable(usuario.id) {
        mutableStateOf(DestinoLancamento.NOVA_TRANSACAO)
    }

    val fabricaConta = remember(context, usuario.id) {
        ContaViewModelFactory(
            context = context.applicationContext,
            usuarioAutenticado = usuario,
        )
    }
    val contaViewModel: ContaViewModel = viewModel(
        key = "conta-${usuario.id}",
        factory = fabricaConta,
    )
    val estadoConta = contaViewModel.estado

    val fabricaCategoria = remember(context, usuario.id) {
        CategoriaViewModelFactory(
            context = context.applicationContext,
            usuarioAutenticado = usuario,
        )
    }
    val categoriaViewModel: CategoriaViewModel = viewModel(
        key = "categoria-${usuario.id}",
        factory = fabricaCategoria,
    )
    val estadoCategoria = categoriaViewModel.estado

    val fabricaTransacao = remember(context, usuario.id) {
        TransacaoViewModelFactory(
            context = context.applicationContext,
            usuarioAutenticado = usuario,
        )
    }
    val transacaoViewModel: TransacaoViewModel = viewModel(
        key = "transacao-${usuario.id}",
        factory = fabricaTransacao,
    )
    val estadoTransacao = transacaoViewModel.estado

    val fabricaMeta = remember(context, usuario.id) {
        MetaViewModelFactory(
            context = context.applicationContext,
            usuarioAutenticado = usuario,
        )
    }
    val metaViewModel: MetaViewModel = viewModel(
        key = "meta-${usuario.id}",
        factory = fabricaMeta,
    )
    val estadoMeta = metaViewModel.estado

    val fabricaPainel = remember(context, usuario.id) {
        PainelViewModelFactory(
            context = context.applicationContext,
            usuarioAutenticado = usuario,
        )
    }
    val painelViewModel: PainelViewModel = viewModel(
        key = "painel-${usuario.id}",
        factory = fabricaPainel,
    )
    val estadoPainel = painelViewModel.estado

    val fabricaRelatorio = remember(context, usuario.id) {
        RelatorioViewModelFactory(
            context = context.applicationContext,
            usuarioAutenticado = usuario,
        )
    }
    val relatorioViewModel: RelatorioViewModel = viewModel(
        key = "relatorio-${usuario.id}",
        factory = fabricaRelatorio,
    )
    val estadoRelatorio = relatorioViewModel.estado

    LaunchedEffect(estadoConta.contas) {
        painelViewModel.recarregarPainel()
        relatorioViewModel.recarregarRelatorios()
    }

    LaunchedEffect(estadoTransacao.versaoSaldoContas) {
        if (estadoTransacao.versaoSaldoContas > 0L) {
            contaViewModel.recarregarContas()
            painelViewModel.recarregarPainel()
            relatorioViewModel.recarregarRelatorios()
        }
    }

    LaunchedEffect(destinoAreaLogada, destinoLancamento, estadoCategoria.mensagemSucesso) {
        if (
            destinoAreaLogada == DestinoAreaLogada.LANCAR &&
            destinoLancamento == DestinoLancamento.NOVA_CATEGORIA &&
            estadoCategoria.mensagemSucesso != null
        ) {
            transacaoViewModel.recarregarCategorias()
            transacaoViewModel.informarMensagemSucesso(estadoCategoria.mensagemSucesso)
            destinoLancamento = DestinoLancamento.NOVA_TRANSACAO
            categoriaViewModel.prepararNovaCategoria(estadoTransacao.tipoSelecionado)
        }
    }

    val exibirBarraNavegacao = when (destinoAreaLogada) {
        DestinoAreaLogada.DASHBOARD -> true
        DestinoAreaLogada.CONTAS -> estadoConta.destinoAtual == DestinoConta.LISTA_CONTAS
        DestinoAreaLogada.LANCAR -> false
        DestinoAreaLogada.METAS -> estadoMeta.destinoAtual == DestinoMeta.LISTA_METAS
        DestinoAreaLogada.RELATORIOS -> true
        DestinoAreaLogada.PERFIL -> true
    }

    Scaffold(
        modifier = modifier.clipToBounds(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (exibirBarraNavegacao) {
                BarraNavegacaoPrincipal(
                    destinoSelecionado = destinoAreaLogada,
                    aoSelecionarDestino = { destinoSelecionado ->
                        if (destinoSelecionado == DestinoAreaLogada.LANCAR) {
                            destinoLancamento = DestinoLancamento.NOVA_TRANSACAO
                        }
                        destinoAreaLogada = destinoSelecionado
                    },
                )
            }
        },
    ) { innerPadding ->
        val modifierConteudo = Modifier.padding(innerPadding)

        when (destinoAreaLogada) {
            DestinoAreaLogada.DASHBOARD -> {
                TelaDashboard(
                    usuario = usuario,
                    estadoPainel = estadoPainel,
                    aoClicarSair = aoClicarSair,
                    modifier = modifierConteudo,
                )
            }

            DestinoAreaLogada.CONTAS -> {
                when (estadoConta.destinoAtual) {
                    DestinoConta.LISTA_CONTAS -> {
                        TelaMinhasContas(
                            usuario = usuario,
                            contas = estadoConta.contas,
                            carregando = estadoConta.carregando,
                            mensagemSucesso = estadoConta.mensagemSucesso,
                            aoClicarVoltar = { destinoAreaLogada = DestinoAreaLogada.DASHBOARD },
                            aoClicarNovaConta = contaViewModel::irParaNovaConta,
                            modifier = modifierConteudo,
                        )
                    }

                    DestinoConta.NOVA_CONTA -> {
                        TelaNovaConta(
                            nomeConta = estadoConta.nomeConta,
                            tipoContaSelecionado = estadoConta.tipoContaSelecionado,
                            corSelecionada = estadoConta.corSelecionada,
                            saldoInicial = estadoConta.saldoInicial,
                            erroNomeConta = estadoConta.erroNomeConta,
                            erroTipoConta = estadoConta.erroTipoConta,
                            erroSaldoInicial = estadoConta.erroSaldoInicial,
                            mensagemErro = estadoConta.mensagemErro,
                            aoAlterarNomeConta = contaViewModel::atualizarNomeConta,
                            aoSelecionarTipoConta = contaViewModel::selecionarTipoConta,
                            aoSelecionarCor = contaViewModel::selecionarCorConta,
                            aoAlterarSaldoInicial = contaViewModel::atualizarSaldoInicial,
                            aoClicarVoltar = contaViewModel::irParaListaContas,
                            aoClicarCancelar = contaViewModel::irParaListaContas,
                            aoClicarSalvar = contaViewModel::cadastrarConta,
                            modifier = modifierConteudo,
                        )
                    }
                }
            }

            DestinoAreaLogada.LANCAR -> {
                when (destinoLancamento) {
                    DestinoLancamento.NOVA_TRANSACAO -> {
                        TelaNovaTransacao(
                            tipoSelecionado = estadoTransacao.tipoSelecionado,
                            valor = estadoTransacao.valor,
                            descricao = estadoTransacao.descricao,
                            dataLancamento = estadoTransacao.dataLancamento,
                            contaSelecionadaId = estadoTransacao.contaSelecionadaId,
                            categoriaSelecionadaId = estadoTransacao.categoriaSelecionadaId,
                            contasDisponiveis = estadoTransacao.contasDisponiveis,
                            categoriasDisponiveis = estadoTransacao.categoriasDisponiveis,
                            carregando = estadoTransacao.carregando,
                            erroValor = estadoTransacao.erroValor,
                            erroDescricao = estadoTransacao.erroDescricao,
                            erroCategoria = estadoTransacao.erroCategoria,
                            erroConta = estadoTransacao.erroConta,
                            erroData = estadoTransacao.erroData,
                            mensagemErro = estadoTransacao.mensagemErro,
                            mensagemSucesso = estadoTransacao.mensagemSucesso,
                            aoSelecionarTipo = transacaoViewModel::selecionarTipo,
                            aoAlterarValor = transacaoViewModel::atualizarValor,
                            aoAlterarDescricao = transacaoViewModel::atualizarDescricao,
                            aoAlterarData = transacaoViewModel::atualizarData,
                            aoSelecionarCategoria = transacaoViewModel::selecionarCategoria,
                            aoSelecionarConta = transacaoViewModel::selecionarConta,
                            aoAbrirNovaCategoria = {
                                categoriaViewModel.prepararNovaCategoria(estadoTransacao.tipoSelecionado)
                                destinoLancamento = DestinoLancamento.NOVA_CATEGORIA
                            },
                            aoClicarVoltar = { destinoAreaLogada = DestinoAreaLogada.DASHBOARD },
                            aoClicarSalvar = transacaoViewModel::cadastrarTransacao,
                            modifier = modifierConteudo,
                        )
                    }

                    DestinoLancamento.NOVA_CATEGORIA -> {
                        TelaNovaCategoria(
                            tipoSelecionado = estadoCategoria.tipoSelecionado,
                            nomeCategoria = estadoCategoria.nomeCategoria,
                            iconeSelecionado = estadoCategoria.iconeSelecionado,
                            corSelecionada = estadoCategoria.corSelecionada,
                            erroNomeCategoria = estadoCategoria.erroNomeCategoria,
                            erroTipoCategoria = estadoCategoria.erroTipoCategoria,
                            mensagemErro = estadoCategoria.mensagemErro,
                            mensagemSucesso = estadoCategoria.mensagemSucesso,
                            aoSelecionarTipo = categoriaViewModel::selecionarTipo,
                            aoAlterarNomeCategoria = categoriaViewModel::atualizarNomeCategoria,
                            aoSelecionarIcone = categoriaViewModel::selecionarIcone,
                            aoSelecionarCor = categoriaViewModel::selecionarCor,
                            aoClicarCancelar = {
                                transacaoViewModel.recarregarCategorias()
                                destinoLancamento = DestinoLancamento.NOVA_TRANSACAO
                            },
                            aoClicarSalvar = categoriaViewModel::cadastrarCategoria,
                            modifier = modifierConteudo,
                        )
                    }
                }
            }

            DestinoAreaLogada.METAS -> {
                when (estadoMeta.destinoAtual) {
                    DestinoMeta.LISTA_METAS -> {
                        TelaMetasEconomia(
                            metas = estadoMeta.metas,
                            carregando = estadoMeta.carregando,
                            mensagemSucesso = estadoMeta.mensagemSucesso,
                            aoClicarVoltar = { destinoAreaLogada = DestinoAreaLogada.DASHBOARD },
                            aoClicarNovaMeta = metaViewModel::irParaCriarMeta,
                            modifier = modifierConteudo,
                        )
                    }

                    DestinoMeta.CRIAR_META -> {
                        TelaCriarMeta(
                            nomeMeta = estadoMeta.nomeMeta,
                            valorMeta = estadoMeta.valorMeta,
                            valorAtual = estadoMeta.valorAtual,
                            dataLimite = estadoMeta.dataLimite,
                            erroNomeMeta = estadoMeta.erroNomeMeta,
                            erroValorMeta = estadoMeta.erroValorMeta,
                            erroValorAtual = estadoMeta.erroValorAtual,
                            erroDataLimite = estadoMeta.erroDataLimite,
                            mensagemErro = estadoMeta.mensagemErro,
                            aoAlterarNomeMeta = metaViewModel::atualizarNomeMeta,
                            aoAlterarValorMeta = metaViewModel::atualizarValorMeta,
                            aoAlterarValorAtual = metaViewModel::atualizarValorAtual,
                            aoAlterarDataLimite = metaViewModel::atualizarDataLimite,
                            aoClicarVoltar = metaViewModel::irParaListaMetas,
                            aoClicarCancelar = metaViewModel::irParaListaMetas,
                            aoClicarSalvar = metaViewModel::cadastrarMeta,
                            modifier = modifierConteudo,
                        )
                    }
                }
            }

            DestinoAreaLogada.RELATORIOS -> {
                TelaRelatorios(
                    estado = estadoRelatorio,
                    aoVoltarInicio = { destinoAreaLogada = DestinoAreaLogada.DASHBOARD },
                    modifier = modifierConteudo,
                )
            }

            DestinoAreaLogada.PERFIL -> {
                TelaModuloEmConstrucao(
                    titulo = "Perfil",
                    descricao = "O modulo de perfil fica para uma etapa posterior, depois do nucleo financeiro.",
                    aoVoltarInicio = { destinoAreaLogada = DestinoAreaLogada.DASHBOARD },
                    modifier = modifierConteudo,
                )
            }
        }
    }
}
