package app.codingGround.api.admin.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class UserManageListWithTotalPageDto {
    private List<UserManageListDto> userManageListDtoList;
    private int totalPage;
}
