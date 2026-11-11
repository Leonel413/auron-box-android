package com.auronbox.auronbox.service;

import com.auronbox.auronbox.dto.request.EnderecoRequestDTO;
import com.auronbox.auronbox.exception.RequestException;
import com.auronbox.auronbox.model.CarrinhoUsuarioModel;
import com.auronbox.auronbox.model.UsuarioModel;
import com.auronbox.auronbox.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${senha.sistema}")
    private String senhaSistema;


    public List<UsuarioModel> listarUsuarios(){
        return usuarioRepository.findAll();
    }

    public UsuarioModel buscarUsuarioPorEmail(String email){
        return isUserByEmail(email);
    }

    public  UsuarioModel salvarUsuario(UsuarioModel usuario) {
        if(usuarioRepository.buscarPorEmail(usuario.getEmail()).isPresent())
            throw new RequestException("O email digitado já foi cadastrado, por favor digite outro!");
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public UsuarioModel atualizarUsuario(UsuarioModel usuario){
        Integer quantiadeTotal = 0;
        Double valorTotal = 0.0;

        for(CarrinhoUsuarioModel itens: usuario.getItens()){
            quantiadeTotal += itens.getQuantidade();
            valorTotal += itens.getPrecoFinal();
        }

        usuario.setQuantidadeItens(quantiadeTotal);
        usuario.setValorTotalItens(valorTotal);
        return  usuarioRepository.save(usuario);
    }

    public UsuarioModel fazerLogin(String email, String senha){
        if(validarSenha(email, senha)) return isUserByEmail(email);
        else throw new RequestException("Credenciais incorretas");
    }

    public UsuarioModel alterarTipoDeUsuario(Long codigo, Boolean admin, String senha, Long codigoSolicitante){
        if(senha == null || !MessageDigest.isEqual(senha.getBytes(StandardCharsets.UTF_8), senhaSistema.getBytes(StandardCharsets.UTF_8)))
            throw new RequestException("Senha do sistema incorreta!");
        if(!admin && codigo.equals(codigoSolicitante))
            throw new RequestException("Você não pode remover seu próprio acesso de admin!");

        UsuarioModel usuario = isUserByCode(codigo);
        usuario.setAdmin(admin);
        usuario.setRole(admin ? "ROLE_ADMIN" : "ROLE_USER");
        return usuarioRepository.save(usuario);
    }

    public UsuarioModel alterarEndereco(Long codigo, EnderecoRequestDTO endereco){
        UsuarioModel usuario = isUserByCode(codigo);

        usuario.setCep(endereco.getCep());
        usuario.setEstado(endereco.getEstado());
        usuario.setCidade(endereco.getCidade());
        usuario.setBairro(endereco.getBairro());
        usuario.setRua(endereco.getRua());
        usuario.setNumero(endereco.getNumero());
        usuario.setComplemento(endereco.getComplemento());

        return usuarioRepository.save(usuario);
    }

    public String alterarSenha(Long codigo, String senhaAtual, String senhaNova){
        UsuarioModel usuario = isUserByCode(codigo);
        if(validarSenha(usuario.getEmail(), senhaAtual)){
            usuario.setSenha(passwordEncoder.encode(senhaNova));
            usuarioRepository.save(usuario);
            return "Senha alterada com sucesso!";
        }
        throw new RequestException("Senha atual incorreta");
    }

    public String excluirTodosUsuarios(){
        usuarioRepository.deleteAll();
        return "Usuários deletados com sucesso!!";
    }

    public String excluirUsuarioPorID(Long codigo){
        usuarioRepository.deleteById(isUserByCode(codigo).getCodigo());
        return "Usuário deletado com sucesso!!";
    }

    //Validações
    private Boolean validarSenha(String email,String senha){
       return   passwordEncoder.matches(senha, isUserByEmail(email).getSenha());
    }

    public UsuarioModel isUserByCode(Long codigo){
        Optional<UsuarioModel> usuario = usuarioRepository.buscarPorId(codigo);
        if(usuario.isEmpty()) throw new RequestException("Usuário inexistente");
        else return usuario.get();
    }

    private UsuarioModel isUserByEmail(String email){
        Optional<UsuarioModel> usuario = usuarioRepository.buscarPorEmail(email);
        if(usuario.isEmpty()) throw new RequestException("Usuário inexistente");
        else return usuario.get();
    }
}
