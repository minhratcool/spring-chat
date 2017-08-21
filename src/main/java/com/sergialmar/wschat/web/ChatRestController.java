package com.sergialmar.wschat.web;

import com.sergialmar.wschat.dto.MessageDTO;
import com.sergialmar.wschat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by mac on 8/17/17.
 */
@RestController
public class ChatRestController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "api/offer/{offerId}/messages", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public List<MessageDTO> getMessagesOnOffer (@PathVariable("offerId") Long offerId, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {

        List<MessageDTO> results = messageService.getLatestMessagesByOffer(offerId, page, pageSize);
        return results;
    }
}
