package app.codingGround.api.admin.service;

import app.codingGround.api.admin.dto.response.UserManageListDto;
import app.codingGround.api.admin.mapper.UserManageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManageService {

    private final UserManageMapper userManageMapper;

    public List<UserManageListDto> getUserManageList(String searchInput) {
        return userManageMapper.getUserManageList(searchInput);
    }
}
