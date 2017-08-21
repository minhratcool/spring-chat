package com.sergialmar.wschat.service;

import com.sergialmar.wschat.dto.MessageDTO;

import java.util.List;

/**
 * Created by mac on 8/8/17.
 */
public interface MessageService {
    List<MessageDTO> getLatestMessagesByOffer (Long offerId, Integer page, Integer pageSize);
    void saveMessage(MessageDTO messageDTO);

}
