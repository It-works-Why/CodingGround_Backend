package app.codingGround.api.contact.mapper;

import app.codingGround.api.contact.dto.response.ContactListDto;
import app.codingGround.api.admin.dto.response.ContactDetailDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Objects;

@Mapper
public interface ContactMapper {
    List<ContactListDto> getContactList(String searchInput, int pageNum);
    int getTotalPage(String searchInput);
    ContactDetailDto getContactDetail(Long contactNum);
}
