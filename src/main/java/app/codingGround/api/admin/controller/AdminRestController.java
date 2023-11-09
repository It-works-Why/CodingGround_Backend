package app.codingGround.api.admin.controller;


import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = "어드민 관련 API")
@RequestMapping("/api/admin")
public class AdminRestController {


//    @ApiOperation(value = "메인카테고리 조회.", notes = "메인카테고리 조회할때 쓰는 API.")
//    @GetMapping("/main/category")
//    public ResponseEntity<ApiResponse<List<GetMainCategoryDto>>> getMainCategoryList(@RequestBody(required = false) String search) {
//        List<GetMainCategoryDto> questionList = adminService.getMainCategoryList(search);
//        return ResponseEntity.ok(new ApiResponse<>(questionList));
//    }
}

