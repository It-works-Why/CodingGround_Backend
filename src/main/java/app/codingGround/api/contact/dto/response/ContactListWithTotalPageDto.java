package app.codingGround.api.contact.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ContactListWithTotalPageDto {
    private List<ContactListDto> contactListDtoList;
    private int totalPage;
}
