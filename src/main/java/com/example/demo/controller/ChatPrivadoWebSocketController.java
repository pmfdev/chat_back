package com.example.demo.controller;

import com.example.demo.entity.Mensaje;
import com.example.demo.entity.PrivateMessage;
import com.example.demo.entity.Sala;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.MensajeRepository;
import com.example.demo.repository.SalaRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class ChatPrivadoWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Escucha mensajes que llegan a /app/chat.privado
    @MessageMapping("/chat.privado")
    public void enviarMensajePrivado(PrivateMessage privateMessage, Authentication authentication) {
        Usuario remitente = usuarioRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Usuario destinatario = usuarioRepository.findById(privateMessage.getDestinatarioId())
                .orElseThrow(() -> new RuntimeException("Destinatario no encontrado"));

        // Encontrar o crear la sala privada
        String salaPrivadaNombre = "privado-" + remitente.getId() + "-" + destinatario.getId();
        Sala salaPrivada = salaRepository.findByNombreAndTipo(salaPrivadaNombre, "PRIVADO")
                .orElseGet(() -> {
                    Sala nuevaSala = new Sala();
                    nuevaSala.setNombre(salaPrivadaNombre);
                    nuevaSala.setTipo("PRIVADO");
                    salaRepository.save(nuevaSala);
                    return nuevaSala;
                });

        // Crear el mensaje
        Mensaje mensaje = new Mensaje();
        mensaje.setUsuario(remitente);
        mensaje.setSala(salaPrivada);
        mensaje.setContenido(privateMessage.getContenido());
        mensaje.setTimestamp(new Date());

        mensajeRepository.save(mensaje);

        // Enviar el mensaje al destinatario por WebSocket
        messagingTemplate.convertAndSendToUser(destinatario.getUsername(), "/queue/private", mensaje);
    }
}
