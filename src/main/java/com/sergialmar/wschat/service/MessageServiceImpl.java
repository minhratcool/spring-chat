package com.sergialmar.wschat.service;

import com.sergialmar.wschat.domain.Message;
import com.sergialmar.wschat.domain.User;
import com.sergialmar.wschat.dto.MessageDTO;
import com.sergialmar.wschat.repository.MessageRepository;
import com.sergialmar.wschat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 8/8/17.
 */
@Service
@Transactional
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<MessageDTO> getLatestMessagesByOffer(Long offerId, Integer page, Integer pageSize) {
        PageRequest pageRequest = new PageRequest(page, pageSize);
        Page<Message> messages = messageRepository.getLatestMessagesByOffer(offerId, pageRequest);
        List<Message> results = messages.getContent();

        return convertToDTOs(results);
    }

    @Override
    public void saveMessage(MessageDTO messageDTO) {
        Message message = new Message();
//        message.setOfferId(messageDTO.getOfferId());
        message.setOfferId(messageDTO.getOfferId());
        message.setText(messageDTO.getText());
        message.setUserId(messageDTO.getUserId());
        message.setCreatedOn(ZonedDateTime.now());

        messageRepository.save(message);
    }

    private List<MessageDTO> convertToDTOs(List<Message> messages) {
        List<MessageDTO> results = new ArrayList<>();

        if (!CollectionUtils.isEmpty(messages)) {
            for (Message message : messages) {
                MessageDTO dto = new MessageDTO();
                User user = userRepository.getOne(message.getUserId());
                dto.setText(message.getText());
                dto.setUserId(user.getId());
                dto.setOfferId(message.getOfferId());
                dto.setUsername(user.getName());

                results.add(dto);
            }
        }

        return results;
    }
}
