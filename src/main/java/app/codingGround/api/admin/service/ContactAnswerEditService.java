package app.codingGround.api.admin.service;

import app.codingGround.api.account.repository.ContactRepository;
import app.codingGround.api.admin.dto.request.ContactAnswerEditDto;
import app.codingGround.api.entity.Contact;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ContactAnswerEditService {

    private final ContactRepository contactRepository;

    @Transactional
    public DefaultResultDto editContactAnswer(ContactAnswerEditDto contactAnswerEditDto, Long contactNum) {

        Contact contact = contactRepository.findById(contactNum)
                .orElseThrow(() -> new NullPointerException("해당 문의사항이 존재하지 않습니다."));

        contact.setContactAnswer(contactAnswerEditDto.getContactAnswer());

        contactRepository.save(contact);

        return DefaultResultDto.builder().success(true).message("답변이 등록/수정 되었습니다").build();
    }
}
