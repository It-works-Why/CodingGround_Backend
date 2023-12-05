package app.codingGround.api.contact.service;

import app.codingGround.api.contact.dto.response.ContactListDto;
import app.codingGround.api.contact.dto.response.ContactListWithTotalPageDto;
import app.codingGround.api.contact.mapper.ContactMapper;
import app.codingGround.api.admin.dto.response.ContactDetailDto;
import app.codingGround.global.config.exception.CustomException;
import app.codingGround.global.config.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactMapper contactMapper;

    public ContactListWithTotalPageDto getContactList(String searchInput, int pageNum) {
        ContactListWithTotalPageDto contactListWithTotalPageDto = new ContactListWithTotalPageDto();

        pageNum = (pageNum-1)*10;

        contactListWithTotalPageDto.setContactListDtoList(contactMapper.getContactList(searchInput, pageNum));
        contactListWithTotalPageDto.setTotalPage(contactMapper.getTotalPage(searchInput));

        return contactListWithTotalPageDto;
    }

    public ContactDetailDto getContactDetail(Long contactNum) {
        ContactDetailDto contactDetailDto = contactMapper.getContactDetail(contactNum);
        if (contactDetailDto.getUseStatus() == 0) {
            throw new CustomException("이미 삭제된 게시물 입니다.", ErrorCode.NOT_USE_POST);
        }
        return contactDetailDto;
    }


}
