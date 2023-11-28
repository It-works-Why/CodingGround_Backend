package app.codingGround.api.account.mapper;

import app.codingGround.api.account.dto.response.ContactListDto;
import app.codingGround.api.admin.dto.response.ContactDetailDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContactMapper {
    List<ContactListDto> getContactList(String searchInput);
    ContactDetailDto getContactDetail(Long contactNum);
}
