let codigoAtual = null;

window.onload = () => {
    verificarAutorizacaoAdmin();
    $('#inputEmail').on('keyup', e => { if(e.key == 'Enter') buscarUsuario(); });
}

function cabecalhoAuth(xhr){
    xhr.setRequestHeader("Authorization", 'Bearer ' + localStorage.getItem('token'));
}

function exibirUsuario(usuario){
    codigoAtual = usuario.codigo;
    $('#nomeUsuario').text(usuario.nome);
    $('#emailUsuario').text(usuario.email);
    $('#codigoUsuario').text(usuario.codigo);
    $('#adminUsuario').text(usuario.admin ? 'Sim' : 'Não');
    $('#btnPromover').toggle(!usuario.admin);
    $('#btnRemover').toggle(!!usuario.admin);
    $('#cardUsuario').show();
    $('#msgErro').hide();
}

function buscarUsuario(){
    const email = $('#inputEmail').val().trim();
    if(!email) return;

    $.ajax({
        method: "GET",
        url: "/usuarios/" + encodeURIComponent(email),
        beforeSend: cabecalhoAuth
    }).done(function(usuario){
        exibirUsuario(usuario);
    }).fail(function(err){
        $('#cardUsuario').hide();
        if(err.status == 403) tratarErro(err);
        else $('#msgErro').text('Usuário não encontrado.').show();
    });
}

function alterarAdmin(admin){
    const senha = $('#senhaSistema').val();
    if(!senha || !codigoAtual){
        gerarMessageBox(2, "Informe a senha do sistema!", "Ok");
        return;
    }

    $.ajax({
        method: "PUT",
        url: "/usuarios/" + codigoAtual + "/admin",
        data: JSON.stringify({admin: admin, senhaSistema: senha}),
        contentType: "application/json; charset=utf-8",
        beforeSend: cabecalhoAuth
    }).done(function(usuario){
        exibirUsuario(usuario);
        $('#senhaSistema').val("");
        gerarMessageBox(1, admin ? "Usuário promovido a admin com sucesso!" : "Acesso de admin removido com sucesso!", "Ok");
    }).fail(function(err){
        tratarErro(err);
    });
}
