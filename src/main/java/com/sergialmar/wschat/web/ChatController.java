package com.sergialmar.wschat.web;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

import com.sergialmar.wschat.domain.User;
import com.sergialmar.wschat.dto.MessageDTO;
import com.sergialmar.wschat.service.MessageService;
import com.sergialmar.wschat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.sergialmar.wschat.domain.ChatMessage;
import com.sergialmar.wschat.domain.SessionProfanity;
import com.sergialmar.wschat.event.LoginEvent;
import com.sergialmar.wschat.event.ParticipantRepository;
import com.sergialmar.wschat.exception.TooMuchProfanityException;
import com.sergialmar.wschat.util.ProfanityChecker;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller that handles WebSocket chat messages
 * 
 * @author Sergi Almar
 */
@Controller
public class ChatController {

	@Autowired private ProfanityChecker profanityFilter;
	
	@Autowired private SessionProfanity profanity;
	
	@Autowired private ParticipantRepository participantRepository;
	
	@Autowired private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired private MessageService messageService;

	@Autowired private UserService userService;
	
	
	@SubscribeMapping("/chat.participants")
	public Collection<LoginEvent> retrieveParticipants() {
		return participantRepository.getActiveSessions().values();
	}


	@MessageMapping("/chat.{username}.on.{offerId}")
	public void filterPrivateMessageOnOffer(@Payload ChatMessage message, @DestinationVariable("username") String username, @DestinationVariable("offerId") Long offerId) {
		checkProfanityAndSanitize(message);

		System.out.println("DEBUGGGGGGGG");

		message.setUsername(username);

		User user = userService.getUserByName(username);

		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setUserId(user.getId());
		messageDTO.setOfferId(offerId);
		messageDTO.setText(message.getMessage());

		messageService.saveMessage(messageDTO);



		simpMessagingTemplate.convertAndSend("/topic/offer/" + offerId + "/messages", message);
	}
	
	private void checkProfanityAndSanitize(ChatMessage message) {
		long profanityLevel = profanityFilter.getMessageProfanity(message.getMessage());
		profanity.increment(profanityLevel);
		message.setMessage(profanityFilter.filter(message.getMessage()));
	}
	
	@MessageExceptionHandler
	@SendToUser(value = "/exchange/amq.direct/errors", broadcast = false)
	public String handleProfanity(TooMuchProfanityException e) {
		return e.getMessage();
	}

}