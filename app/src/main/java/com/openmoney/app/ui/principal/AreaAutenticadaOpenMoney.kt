package com.openmoney.app.ui.principal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.openmoney.app.domain.model.TipoTransacao
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
import com.openmoney.app.ui.perfil.PerfilViewModel
import com.openmoney.app.ui.perfil.PerfilViewModelFactory
import com.openmoney.app.ui.perfil.TelaGerenciarPerfil
import com.openmoney.app.ui.relatorio.RelatorioViewModel
import com.openmoney.app.ui.relatorio.RelatorioViewModelFactory
import com.openmoney.app.ui.relatorio.TelaRelatorios
import com.openmoney.app.ui.transacao.TelaNovaTransacao
import com.openmoney.app.ui.transacao.TelaTransacoesCadastradas
import com.openmoney.app.ui.transacao.TransacaoViewModel
import com.openmoney.app.ui.transacao.TransacaoViewModelFactory
import kotlinx.coroutines.launch

private data class ConfiguracaoMensagemSucesso(
    val mensagem: String,
    val textoBotao: String,
    val aoConfirmar: () -> Unit,
)

@Composable
fun AreaAutenticadaOpenMoney(
    usuario: Usuario,
    aoClicarSair: () -> Unit,
    aoAtualizarUsuarioAutenticado: (Usuario) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

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

    val fabricaPerfil = remember(context, usuario.id) {
        PerfilViewModelFactory(
            context = context.applicationContext,
            usuarioAutenticado = usuario,
        )
    }
    val perfilViewModel: PerfilViewModel = viewModel(
        key = "perfil-${usuario.id}",
        factory = fabricaPerfil,
    )
    val estadoPerfil = perfilViewModel.estado

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

    val abrirTransacoesCadastradas = {
        transacaoViewModel.limparMensagemSucesso()
        transacaoViewModel.limparMensagemErro()
        transacaoViewModel.carregarTransacoesCadastradas()
        destinoLancamento = DestinoLancamento.TRANSACOES_CADASTRADAS
        destinoAreaLogada = DestinoAreaLogada.LANCAR
    }

    val navegarParaDestinoPrincipal: (DestinoAreaLogada) -> Unit = { destinoSelecionado ->
        when (destinoSelecionado) {
            DestinoAreaLogada.DASHBOARD -> {
                destinoAreaLogada = DestinoAreaLogada.DASHBOARD
            }

            DestinoAreaLogada.CONTAS -> {
                contaViewModel.irParaListaContas()
                destinoAreaLogada = DestinoAreaLogada.CONTAS
            }

            DestinoAreaLogada.LANCAR -> {
                destinoLancamento = DestinoLancamento.NOVA_TRANSACAO
                destinoAreaLogada = DestinoAreaLogada.LANCAR
            }

            DestinoAreaLogada.METAS -> {
                metaViewModel.irParaListaMetas()
                destinoAreaLogada = DestinoAreaLogada.METAS
            }

            DestinoAreaLogada.RELATORIOS -> {
                destinoAreaLogada = DestinoAreaLogada.RELATORIOS
            }

            DestinoAreaLogada.PERFIL -> {
                destinoAreaLogada = DestinoAreaLogada.PERFIL
            }
        }
    }

    val configuracaoMensagemSucesso = when {
        destinoAreaLogada == DestinoAreaLogada.CONTAS &&
            estadoConta.destinoAtual == DestinoConta.LISTA_CONTAS &&
            !estadoConta.mensagemSucesso.isNullOrBlank() -> {
            ConfiguracaoMensagemSucesso(
                mensagem = requireNotNull(estadoConta.mensagemSucesso),
                textoBotao = "Acessar Dashboard",
                aoConfirmar = {
                    contaViewModel.limparMensagemSucesso()
                    destinoAreaLogada = DestinoAreaLogada.DASHBOARD
                },
            )
        }

        destinoAreaLogada == DestinoAreaLogada.LANCAR &&
            destinoLancamento == DestinoLancamento.NOVA_CATEGORIA &&
            !estadoCategoria.mensagemSucesso.isNullOrBlank() -> {
            val categoriaCriadaId = estadoCategoria.categoriaCriadaId
            ConfiguracaoMensagemSucesso(
                mensagem = requireNotNull(estadoCategoria.mensagemSucesso),
                textoBotao = "Acessar Dashboard",
                aoConfirmar = {
                    transacaoViewModel.recarregarCategorias(categoriaPreferida = categoriaCriadaId)
                    categoriaViewModel.prepararNovaCategoria(estadoTransacao.tipoSelecionado)
                    destinoLancamento = DestinoLancamento.NOVA_TRANSACAO
                    destinoAreaLogada = DestinoAreaLogada.DASHBOARD
                },
            )
        }

        destinoAreaLogada == DestinoAreaLogada.LANCAR &&
            destinoLancamento == DestinoLancamento.NOVA_TRANSACAO &&
            !estadoTransacao.mensagemSucesso.isNullOrBlank() -> {
            val textoBotao = if (estadoTransacao.tipoSelecionado == TipoTransacao.DESPESA) {
                "Acessar Minhas Contas"
            } else {
                "Transações Cadastradas"
            }

            ConfiguracaoMensagemSucesso(
                mensagem = requireNotNull(estadoTransacao.mensagemSucesso),
                textoBotao = textoBotao,
                aoConfirmar = {
                    transacaoViewModel.limparMensagemSucesso()
                    if (estadoTransacao.tipoSelecionado == TipoTransacao.DESPESA) {
                        destinoLancamento = DestinoLancamento.NOVA_TRANSACAO
                        destinoAreaLogada = DestinoAreaLogada.CONTAS
                    } else {
                        abrirTransacoesCadastradas()
                    }
                },
            )
        }

        destinoAreaLogada == DestinoAreaLogada.METAS &&
            estadoMeta.destinoAtual == DestinoMeta.LISTA_METAS &&
            !estadoMeta.mensagemSucesso.isNullOrBlank() -> {
            ConfiguracaoMensagemSucesso(
                mensagem = requireNotNull(estadoMeta.mensagemSucesso),
                textoBotao = "Metas Cadastradas",
                aoConfirmar = metaViewModel::limparMensagemSucesso,
            )
        }

        destinoAreaLogada == DestinoAreaLogada.PERFIL &&
            !estadoPerfil.mensagemSucesso.isNullOrBlank() -> {
            ConfiguracaoMensagemSucesso(
                mensagem = requireNotNull(estadoPerfil.mensagemSucesso),
                textoBotao = "Acessar Dashboard",
                aoConfirmar = {
                    perfilViewModel.limparMensagemSucesso()
                    destinoAreaLogada = DestinoAreaLogada.DASHBOARD
                },
            )
        }

        else -> null
    }

    val exibirMensagemSucesso = configuracaoMensagemSucesso != null

    val exibirBarraNavegacao = when (destinoAreaLogada) {
        DestinoAreaLogada.DASHBOARD -> true
        DestinoAreaLogada.CONTAS -> estadoConta.destinoAtual == DestinoConta.LISTA_CONTAS
        DestinoAreaLogada.LANCAR -> false
        DestinoAreaLogada.METAS -> estadoMeta.destinoAtual == DestinoMeta.LISTA_METAS
        DestinoAreaLogada.RELATORIOS -> true
        DestinoAreaLogada.PERFIL -> true
    }
    val exibirMenuLateral = when (destinoAreaLogada) {
        DestinoAreaLogada.DASHBOARD -> false
        DestinoAreaLogada.CONTAS -> estadoConta.destinoAtual == DestinoConta.LISTA_CONTAS
        DestinoAreaLogada.LANCAR -> false
        DestinoAreaLogada.METAS -> estadoMeta.destinoAtual == DestinoMeta.LISTA_METAS
        DestinoAreaLogada.RELATORIOS -> true
        DestinoAreaLogada.PERFIL -> true
    }

    val abrirMenuLateral = {
        if (exibirMenuLateral) {
            coroutineScope.launch {
                drawerState.open()
            }
        }
    }

    val selecionarDestinoMenuLateral: (DestinoAreaLogada) -> Unit = { destinoSelecionado ->
        navegarParaDestinoPrincipal(destinoSelecionado)
        coroutineScope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        modifier = modifier.clipToBounds(),
        drawerState = drawerState,
        gesturesEnabled = exibirMenuLateral && !exibirMensagemSucesso,
        scrimColor = Color.Black.copy(alpha = 0.42f),
        drawerContent = {
            MenuLateralOpenMoney(
                aoClicarPerfil = { selecionarDestinoMenuLateral(DestinoAreaLogada.PERFIL) },
                aoClicarInicio = { selecionarDestinoMenuLateral(DestinoAreaLogada.DASHBOARD) },
                aoClicarLancar = { selecionarDestinoMenuLateral(DestinoAreaLogada.LANCAR) },
                aoClicarRelatorios = { selecionarDestinoMenuLateral(DestinoAreaLogada.RELATORIOS) },
                aoClicarContas = { selecionarDestinoMenuLateral(DestinoAreaLogada.CONTAS) },
                aoClicarMetas = { selecionarDestinoMenuLateral(DestinoAreaLogada.METAS) },
                aoClicarSair = aoClicarSair,
            )
        },
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (exibirMensagemSucesso) {
                            Modifier.blur(16.dp)
                        } else {
                            Modifier
                        },
                    ),
                containerColor = MaterialTheme.colorScheme.background,
                bottomBar = {
                    if (exibirBarraNavegacao) {
                        BarraNavegacaoPrincipal(
                            destinoSelecionado = destinoAreaLogada,
                            aoSelecionarDestino = navegarParaDestinoPrincipal,
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
                                    aoClicarMenu = abrirMenuLateral,
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
                                    aoAbrirTransacoesCadastradas = abrirTransacoesCadastradas,
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

                            DestinoLancamento.TRANSACOES_CADASTRADAS -> {
                                TelaTransacoesCadastradas(
                                    transacoes = estadoTransacao.transacoesCadastradas,
                                    carregando = estadoTransacao.carregandoTransacoesCadastradas,
                                    mensagemErro = estadoTransacao.mensagemErro,
                                    aoClicarVoltar = {
                                        transacaoViewModel.limparMensagemErro()
                                        destinoLancamento = DestinoLancamento.NOVA_TRANSACAO
                                    },
                                    aoClicarNovaTransacao = {
                                        transacaoViewModel.limparMensagemErro()
                                        destinoLancamento = DestinoLancamento.NOVA_TRANSACAO
                                    },
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
                                    aoClicarMenu = abrirMenuLateral,
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
                            aoClicarMenu = abrirMenuLateral,
                            aoSelecionarPeriodo = relatorioViewModel::selecionarPeriodo,
                            modifier = modifierConteudo,
                        )
                    }

                    DestinoAreaLogada.PERFIL -> {
                        TelaGerenciarPerfil(
                            nomeCompleto = estadoPerfil.nomeCompleto,
                            email = estadoPerfil.email,
                            senhaAtual = estadoPerfil.senhaAtual,
                            novaSenha = estadoPerfil.novaSenha,
                            confirmarNovaSenha = estadoPerfil.confirmarNovaSenha,
                            senhaAtualVisivel = estadoPerfil.senhaAtualVisivel,
                            novaSenhaVisivel = estadoPerfil.novaSenhaVisivel,
                            confirmarNovaSenhaVisivel = estadoPerfil.confirmarNovaSenhaVisivel,
                            erroNomeCompleto = estadoPerfil.erroNomeCompleto,
                            erroEmail = estadoPerfil.erroEmail,
                            erroSenhaAtual = estadoPerfil.erroSenhaAtual,
                            erroNovaSenha = estadoPerfil.erroNovaSenha,
                            erroConfirmarNovaSenha = estadoPerfil.erroConfirmarNovaSenha,
                            mensagemErro = estadoPerfil.mensagemErro,
                            mensagemSucesso = estadoPerfil.mensagemSucesso,
                            aoAlterarNomeCompleto = perfilViewModel::atualizarNomeCompleto,
                            aoAlterarEmail = perfilViewModel::atualizarEmail,
                            aoAlterarSenhaAtual = perfilViewModel::atualizarSenhaAtual,
                            aoAlterarNovaSenha = perfilViewModel::atualizarNovaSenha,
                            aoAlterarConfirmarNovaSenha = perfilViewModel::atualizarConfirmarNovaSenha,
                            aoAlternarVisibilidadeSenhaAtual = perfilViewModel::alternarVisibilidadeSenhaAtual,
                            aoAlternarVisibilidadeNovaSenha = perfilViewModel::alternarVisibilidadeNovaSenha,
                            aoAlternarVisibilidadeConfirmarNovaSenha = perfilViewModel::alternarVisibilidadeConfirmarNovaSenha,
                            aoClicarMenu = abrirMenuLateral,
                            aoClicarSalvar = {
                                perfilViewModel.salvarAlteracoes(aoAtualizarUsuarioAutenticado)
                            },
                            modifier = modifierConteudo,
                        )
                    }
                }
            }

            if (configuracaoMensagemSucesso != null) {
                TelaMensagemSucesso(
                    mensagem = configuracaoMensagemSucesso.mensagem,
                    textoBotao = configuracaoMensagemSucesso.textoBotao,
                    aoClicarBotao = configuracaoMensagemSucesso.aoConfirmar,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
