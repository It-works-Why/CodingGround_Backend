package app.codingGround.api.admin.mapper;

import app.codingGround.api.admin.dto.response.UserManageListDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserManageMapper {
    List<UserManageListDto> getUserManageList(String searchInput, int pageNum);
    int getTotalPage(String searchInput);
}
