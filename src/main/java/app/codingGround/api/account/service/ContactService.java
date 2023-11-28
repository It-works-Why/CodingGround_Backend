package app.codingGround.api.account.service;

import app.codingGround.api.account.repository.AccountRepository;
import app.codingGround.api.account.dto.response.ContactListDto;
import app.codingGround.api.account.mapper.ContactMapper;
import app.codingGround.api.account.repository.ContactRepository;
import app.codingGround.api.admin.dto.response.ContactDetailDto;
import app.codingGround.global.config.exception.CustomException;
import app.codingGround.global.config.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactMapper contactMapper;

    public List<ContactListDto> getContactList() {
        return contactMapper.getContactList();
    }

    public ContactDetailDto getContactDetail(Long contactNum) {
        ContactDetailDto contactDetailDto = contactMapper.getContactDetail(contactNum);
        if (contactDetailDto.getUseStatus() == 0) {
            throw new CustomException("이미 삭제된 게시물 입니다.", ErrorCode.NOT_USE_POST);
        }
        return contactDetailDto;
    }


}
