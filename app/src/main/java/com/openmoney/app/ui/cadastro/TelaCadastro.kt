package com.openmoney.app.ui.cadastro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.openmoney.app.ui.theme.OpenMoneyGreen
import com.openmoney.app.ui.theme.OpenMoneyGreenPressed
import com.openmoney.app.ui.theme.OpenMoneyTextPrimary
import com.openmoney.app.ui.theme.OpenMoneyTheme

@Composable
fun TelaCadastro(
    nome: String,
    email: String,
    senha: String,
    confirmarSenha: String,
    senhaVisivel: Boolean,
    confirmarSenhaVisivel: Boolean,
    erroNome: String?,
    erroEmail: String?,
    erroSenha: String?,
    erroConfirmarSenha: String?,
    mensagemCadastro: String?,
    aoAlterarNome: (String) -> Unit,
    aoAlterarEmail: (String) -> Unit,
    aoAlterarSenha: (String) -> Unit,
    aoAlterarConfirmarSenha: (String) -> Unit,
    aoAlternarVisibilidadeSenha: () -> Unit,
    aoAlternarVisibilidadeConfirmarSenha: () -> Unit,
    aoClicarCadastrar: () -> Unit,
    aoClicarEntrar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        val scrollState = rememberScrollState()

        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 40.dp)
                .imePadding()
                .padding(top = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(74.dp),
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(22.dp),
            ) {
                androidx.compose.foundation.layout.Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Criar conta",
                        style = TextStyle(
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimary,
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Cadastre-se para comecar a organizar suas financas",
                color = MaterialTheme.colorScheme.secondary,
                style = TextStyle(fontSize = 16.sp),
            )

            Spacer(modifier = Modifier.height(34.dp))

            RotuloCampoAutenticacao(texto = "Nome")
            Spacer(modifier = Modifier.height(10.dp))
            CampoAutenticacao(
                valor = nome,
                aoAlterarValor = aoAlterarNome,
                opcoesTeclado = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                mensagemErro = erroNome,
            )

            Spacer(modifier = Modifier.height(14.dp))

            RotuloCampoAutenticacao(texto = "E-mail")
            Spacer(modifier = Modifier.height(10.dp))
            CampoAutenticacao(
                valor = email,
                aoAlterarValor = aoAlterarEmail,
                opcoesTeclado = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
                mensagemErro = erroEmail,
            )

            Spacer(modifier = Modifier.height(14.dp))

            RotuloCampoAutenticacao(texto = "Senha")
            Spacer(modifier = Modifier.height(10.dp))
            CampoAutenticacao(
                valor = senha,
                aoAlterarValor = aoAlterarSenha,
                opcoesTeclado = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next,
                ),
                transformacaoVisual = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                iconeFinal = {
                    IconButton(onClick = aoAlternarVisibilidadeSenha) {
                        Icon(
                            imageVector = if (senhaVisivel) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (senhaVisivel) "Ocultar senha" else "Exibir senha",
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                },
                mensagemErro = erroSenha,
            )

            Spacer(modifier = Modifier.height(14.dp))

            RotuloCampoAutenticacao(texto = "Confirmar senha")
            Spacer(modifier = Modifier.height(10.dp))
            CampoAutenticacao(
                valor = confirmarSenha,
                aoAlterarValor = aoAlterarConfirmarSenha,
                opcoesTeclado = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                acoesTeclado = KeyboardActions(onDone = { aoClicarCadastrar() }),
                transformacaoVisual = if (confirmarSenhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                iconeFinal = {
                    IconButton(onClick = aoAlternarVisibilidadeConfirmarSenha) {
                        Icon(
                            imageVector = if (confirmarSenhaVisivel) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (confirmarSenhaVisivel) "Ocultar senha" else "Exibir senha",
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                },
                mensagemErro = erroConfirmarSenha,
            )

            if (mensagemCadastro != null) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = mensagemCadastro,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error,
                    style = TextStyle(fontSize = 14.sp),
                )
            }

            Spacer(modifier = Modifier.height(38.dp))

            Button(
                onClick = aoClicarCadastrar,
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OpenMoneyGreen,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = OpenMoneyGreenPressed,
                ),
            ) {
                Text(
                    text = "Cadastrar",
                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Normal),
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Ja tem conta?",
                    color = OpenMoneyTextPrimary,
                    style = TextStyle(fontSize = 18.sp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(
                    onClick = aoClicarEntrar,
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Text(
                        text = "Entrar",
                        color = OpenMoneyGreenPressed,
                        style = TextStyle(fontSize = 18.sp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2B2B2B)
@Composable
private fun PreviaTelaCadastro() {
    OpenMoneyTheme {
        TelaCadastro(
            nome = "",
            email = "",
            senha = "",
            confirmarSenha = "",
            senhaVisivel = false,
            confirmarSenhaVisivel = false,
            erroNome = null,
            erroEmail = null,
            erroSenha = "A senha deve ter no minimo 8 caracteres, contendo letras e numeros.",
            erroConfirmarSenha = null,
            mensagemCadastro = null,
            aoAlterarNome = {},
            aoAlterarEmail = {},
            aoAlterarSenha = {},
            aoAlterarConfirmarSenha = {},
            aoAlternarVisibilidadeSenha = {},
            aoAlternarVisibilidadeConfirmarSenha = {},
            aoClicarCadastrar = {},
            aoClicarEntrar = {},
        )
    }
}
