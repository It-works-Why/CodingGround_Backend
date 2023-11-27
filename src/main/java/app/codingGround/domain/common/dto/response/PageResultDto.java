package app.codingGround.domain.common.dto.response;

import app.codingGround.domain.common.dto.Pagination;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PageResultDto<T> {

    @ApiModelProperty(value = "데이터")
    private List<T> data;

    @ApiModelProperty(value = "페이지")
    private Pagination pagination;

    @Builder
    public PageResultDto(List<T> data, Pagination pagination) {
        this.data = data;
        this.pagination = pagination;
    }

}
