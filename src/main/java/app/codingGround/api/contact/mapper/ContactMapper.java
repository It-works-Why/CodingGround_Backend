package app.codingGround.api.contact.mapper;

import app.codingGround.api.contact.dto.response.ContactListDto;
import app.codingGround.api.admin.dto.response.ContactDetailDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContactMapper {
    List<ContactListDto> getContactList(String searchInput);
    ContactDetailDto getContactDetail(Long contactNum);
}
