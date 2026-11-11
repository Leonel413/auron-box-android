function verificarLogin(){
    let logado = (localStorage.getItem('logado') == 'logado') ? true : false;
    return logado;
}

function verificarAutorizacao(){
    if(verificarLogin()) return true;
    else location.href="index.html";
}

function verificarAutorizacaoAdmin(){
    if(localStorage.getItem('admin') == "true") return true;
    else location.href="index.html";
}

function renderizarQuantidade(valor){
    localStorage.setItem('quantidadeItens', valor);
    let quantidade = (verificarLogin()) ? localStorage.getItem('quantidadeItens') : 0;
    $('#quantidade-carrinho').html(quantidade);
}

function alterarSessao(){
    let mensagem = (verificarLogin()) ? "Olá "+localStorage.getItem('nome') : "Faça login";
    $("#estadoUSuario").html(mensagem);
}

function abrirMeusDados(){
    (verificarLogin()) ? location.href="meusDados.html":
    gerarMessageBox(2, "É necessário fazer login para acessar seus dados!!", "Ok");
}

function abrirMeusPedidos(){
    (verificarLogin()) ? location.href="meusPedidos.html":
    gerarMessageBox(2, "É necessário fazer login para acessar seus pedidos!!", "Ok");
}

function travarTela() {
    document.documentElement.style.overflow = 'hidden';
    document.body.scroll = "no";
}

function destravarTela() {
    document.documentElement.style.overflow = 'auto';
    document.body.scroll = "yes";
}

function deslogar(){
    localStorage.logado="";
    localStorage.codigo="";
    localStorage.token="";
    localStorage.nome="";
    localStorage.quantidadeItens="";
    localStorage.endereco="";
    location.href="index.html";
}

function tratarErro(err){
    if(err.status == 403) gerarMessageBox(2, "Sem autorização:<br><br> Seu token de autenticação expirou ou não existe!! Por motivos de segurança você será deslogado e redirecionado para a página inicial!", "Entendido");
    else gerarMessageBox(2, err.responseJSON.message, "Ok");
}

function gerarMessageBox(cor, mensagem, textoBtn){
    $('#esconder').addClass('ativo');
    $('#textoMensagem').html(mensagem);
    $('#btnMessage').html(textoBtn);

    travarTela();
}

function fecharMessageBox(){
    destravarTela();
    $('#esconder').removeClass('ativo');

    if($('#btnMessage').html() == "Prosseguir") location.reload();
    if($('#btnMessage').html() == "Entendido") deslogar();
}
