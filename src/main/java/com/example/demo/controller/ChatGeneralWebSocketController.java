package com.example.demo.controller;

import com.example.demo.entity.ChatMessage;
import com.example.demo.entity.Mensaje;
import com.example.demo.entity.Sala;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.MensajeRepository;
import com.example.demo.repository.SalaRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@Controller
public class ChatGeneralWebSocketController {

    // Logger para agregar logs
    private static final Logger logger = LoggerFactory.getLogger(ChatGeneralWebSocketController.class);

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Escucha mensajes que llegan a /app/chat.general
    @MessageMapping("/chat.general")
    @SendTo("/topic/public") // Envía a todos los que estén suscritos a /topic/public
    public Mensaje enviarMensajeGeneral(ChatMessage chatMessage) {
        // Log para verificar que el mensaje fue recibido
        logger.info("Mensaje recibido desde el frontend: {}", chatMessage);

        // Aquí tomamos el nombre de usuario desde el mensaje enviado desde el front
        String username = chatMessage.getUsername();

        // Log para verificar el nombre de usuario
        logger.info("Buscando usuario: {}", username);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("Usuario no encontrado: {}", username);
                    return new RuntimeException("Usuario no encontrado");
                });

        // Log para verificar si se encuentra la sala
        logger.info("Buscando la sala pública 'general'");

        Sala salaPublica = salaRepository.findByNombreAndTipo("general", "PUBLICO")
                .orElseThrow(() -> {
                    logger.error("Sala pública 'general' no encontrada");
                    return new RuntimeException("Sala no encontrada");
                });

        // Log para verificar la creación del mensaje
        logger.info("Creando el mensaje con contenido: {}", chatMessage.getContenido());

        Mensaje mensaje = new Mensaje();
        mensaje.setUsuario(usuario);
        mensaje.setSala(salaPublica);
        mensaje.setContenido(chatMessage.getContenido());
        mensaje.setTimestamp(new Date());

        // Log para verificar que el mensaje se va a guardar
        logger.info("Guardando el mensaje en la base de datos");

        mensajeRepository.save(mensaje);

        // Log indicando que el mensaje será enviado a los suscriptores
        logger.info("Enviando el mensaje a todos los suscriptores del tema /topic/public");

        return mensaje; // Este mensaje será enviado a todos los suscriptores del tema /topic/public
    }
}
