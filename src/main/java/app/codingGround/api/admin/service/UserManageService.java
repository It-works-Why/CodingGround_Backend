package app.codingGround.api.admin.service;

import app.codingGround.api.admin.dto.request.EditUserStatusDto;
import app.codingGround.api.admin.dto.response.UserManageListWithTotalPageDto;
import app.codingGround.api.admin.mapper.UserManageMapper;
import app.codingGround.api.entity.User;
import app.codingGround.api.user.repository.UserRepository;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserManageService {

    private final UserManageMapper userManageMapper;
    private final UserRepository userRepository;

    public UserManageListWithTotalPageDto getUserManageList(String searchInput, int pageNum) {

        UserManageListWithTotalPageDto userManageListWithTotalPageDto = new UserManageListWithTotalPageDto();

        if(pageNum == 0) {
            pageNum = 1;
        } else {
            pageNum = (pageNum-1)*10;
        }
        userManageListWithTotalPageDto.setUserManageListDtoList(userManageMapper.getUserManageList(searchInput, pageNum));
        userManageListWithTotalPageDto.setTotalPage(userManageMapper.getTotalPage(searchInput));

        return userManageListWithTotalPageDto;
    }

    @Transactional
    public DefaultResultDto editUserStatus(EditUserStatusDto editUserStatusDto) {

        User user = userRepository.findByUserNum(editUserStatusDto.getUserNum());

        user.setUserStatus(editUserStatusDto.getUserStatus());
        userRepository.save(user);

        return DefaultResultDto.builder().success(true).message("회원상태가 정상적으로 수정 되었습니다.").build();
    }
}