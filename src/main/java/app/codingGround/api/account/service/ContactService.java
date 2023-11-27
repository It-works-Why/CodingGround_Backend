package app.codingGround.api.account.service;

import app.codingGround.api.account.repository.AccountRepository;
import app.codingGround.api.account.dto.response.ContactListDto;
import app.codingGround.api.account.mapper.ContactMapper;
import app.codingGround.api.account.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final AccountRepository accountRepository;
    private final ContactMapper contactMapper;

    public List<ContactListDto> getContactList() {
        return contactMapper.getContactList();
    }


}
