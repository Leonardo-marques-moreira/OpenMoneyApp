package com.openmoney.app.ui.perfil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openmoney.app.ui.autenticacao.CampoAutenticacao
import com.openmoney.app.ui.autenticacao.RotuloCampoAutenticacao
import com.openmoney.app.ui.principal.CabecalhoSecaoAutenticada
import com.openmoney.app.ui.principal.TipoAcaoCabecalhoEsquerda
import com.openmoney.app.ui.theme.OpenMoneyGreen
import com.openmoney.app.ui.theme.OpenMoneyTheme

@Composable
fun TelaGerenciarPerfil(
    nomeCompleto: String,
    email: String,
    senhaAtual: String,
    novaSenha: String,
    confirmarNovaSenha: String,
    senhaAtualVisivel: Boolean,
    novaSenhaVisivel: Boolean,
    confirmarNovaSenhaVisivel: Boolean,
    erroNomeCompleto: String?,
    erroEmail: String?,
    erroSenhaAtual: String?,
    erroNovaSenha: String?,
    erroConfirmarNovaSenha: String?,
    mensagemErro: String?,
    mensagemSucesso: String?,
    aoAlterarNomeCompleto: (String) -> Unit,
    aoAlterarEmail: (String) -> Unit,
    aoAlterarSenhaAtual: (String) -> Unit,
    aoAlterarNovaSenha: (String) -> Unit,
    aoAlterarConfirmarNovaSenha: (String) -> Unit,
    aoAlternarVisibilidadeSenhaAtual: () -> Unit,
    aoAlternarVisibilidadeNovaSenha: () -> Unit,
    aoAlternarVisibilidadeConfirmarNovaSenha: () -> Unit,
    aoClicarMenu: () -> Unit,
    aoClicarSalvar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding(),
        ) {
            CabecalhoSecaoAutenticada(
                titulo = "",
                aoClicarAcaoEsquerda = aoClicarMenu,
                tipoAcaoEsquerda = TipoAcaoCabecalhoEsquerda.MENU,
                conteudoInferior = {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Avatar do usuário",
                        tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.72f),
                        modifier = Modifier.height(90.dp),
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = nomeCompleto.ifBlank { "Usuário" },
                        style = TextStyle(
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimary,
                        ),
                    )

                    Text(
                        text = email.ifBlank { "email@exemplo.com" },
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.92f),
                        ),
                    )
                },
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 22.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                RotuloCampoAutenticacao(texto = "Nome completo")
                CampoAutenticacao(
                    valor = nomeCompleto,
                    aoAlterarValor = aoAlterarNomeCompleto,
                    opcoesTeclado = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                    mensagemErro = erroNomeCompleto,
                )

                RotuloCampoAutenticacao(texto = "E-mail")
                CampoAutenticacao(
                    valor = email,
                    aoAlterarValor = aoAlterarEmail,
                    opcoesTeclado = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                    mensagemErro = erroEmail,
                )

                Spacer(modifier = Modifier.height(4.dp))

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.85f),
                    thickness = 1.dp,
                )

                Spacer(modifier = Modifier.height(4.dp))

                RotuloCampoAutenticacao(texto = "Senha atual")
                CampoSenhaPerfil(
                    valor = senhaAtual,
                    senhaVisivel = senhaAtualVisivel,
                    mensagemErro = erroSenhaAtual,
                    aoAlterarValor = aoAlterarSenhaAtual,
                    aoAlternarVisibilidade = aoAlternarVisibilidadeSenhaAtual,
                    imeAction = ImeAction.Next,
                )

                RotuloCampoAutenticacao(texto = "Nova senha")
                CampoSenhaPerfil(
                    valor = novaSenha,
                    senhaVisivel = novaSenhaVisivel,
                    mensagemErro = erroNovaSenha,
                    aoAlterarValor = aoAlterarNovaSenha,
                    aoAlternarVisibilidade = aoAlternarVisibilidadeNovaSenha,
                    imeAction = ImeAction.Next,
                )

                RotuloCampoAutenticacao(texto = "Confirmar nova senha")
                CampoSenhaPerfil(
                    valor = confirmarNovaSenha,
                    senhaVisivel = confirmarNovaSenhaVisivel,
                    mensagemErro = erroConfirmarNovaSenha,
                    aoAlterarValor = aoAlterarConfirmarNovaSenha,
                    aoAlternarVisibilidade = aoAlternarVisibilidadeConfirmarNovaSenha,
                    imeAction = ImeAction.Done,
                    aoConcluir = aoClicarSalvar,
                )

                if (mensagemErro != null) {
                    Text(
                        text = mensagemErro,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.error,
                        style = TextStyle(fontSize = 14.sp),
                    )
                }

                if (mensagemSucesso != null) {
                    Text(
                        text = mensagemSucesso,
                        modifier = Modifier.fillMaxWidth(),
                        color = OpenMoneyGreen,
                        style = TextStyle(fontSize = 14.sp),
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = aoClicarSalvar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OpenMoneyGreen,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                    Text(
                        text = "Salvar alterações",
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Normal,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun CampoSenhaPerfil(
    valor: String,
    senhaVisivel: Boolean,
    mensagemErro: String?,
    aoAlterarValor: (String) -> Unit,
    aoAlternarVisibilidade: () -> Unit,
    imeAction: ImeAction,
    aoConcluir: (() -> Unit)? = null,
) {
    CampoAutenticacao(
        valor = valor,
        aoAlterarValor = aoAlterarValor,
        opcoesTeclado = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction,
        ),
        acoesTeclado = KeyboardActions(
            onDone = {
                aoConcluir?.invoke()
            },
        ),
        transformacaoVisual = if (senhaVisivel) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        iconeFinal = {
            IconButton(onClick = aoAlternarVisibilidade) {
                Icon(
                    imageVector = if (senhaVisivel) {
                        Icons.Filled.VisibilityOff
                    } else {
                        Icons.Filled.Visibility
                    },
                    contentDescription = if (senhaVisivel) {
                        "Ocultar senha"
                    } else {
                        "Exibir senha"
                    },
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        },
        mensagemErro = mensagemErro,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF2B2B2B)
@Composable
private fun PreviaTelaGerenciarPerfil() {
    OpenMoneyTheme {
        TelaGerenciarPerfil(
            nomeCompleto = "João da Silva",
            email = "joao@email.com",
            senhaAtual = "",
            novaSenha = "",
            confirmarNovaSenha = "",
            senhaAtualVisivel = false,
            novaSenhaVisivel = false,
            confirmarNovaSenhaVisivel = false,
            erroNomeCompleto = null,
            erroEmail = null,
            erroSenhaAtual = null,
            erroNovaSenha = null,
            erroConfirmarNovaSenha = null,
            mensagemErro = null,
            mensagemSucesso = null,
            aoAlterarNomeCompleto = {},
            aoAlterarEmail = {},
            aoAlterarSenhaAtual = {},
            aoAlterarNovaSenha = {},
            aoAlterarConfirmarNovaSenha = {},
            aoAlternarVisibilidadeSenhaAtual = {},
            aoAlternarVisibilidadeNovaSenha = {},
            aoAlternarVisibilidadeConfirmarNovaSenha = {},
            aoClicarMenu = {},
            aoClicarSalvar = {},
        )
    }
}
