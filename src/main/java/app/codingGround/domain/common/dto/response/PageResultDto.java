package app.codingGround.domain.common.dto.response;

import app.codingGround.domain.common.dto.Pagination;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PageResultDto<T> {

    @ApiModelProperty(value = "데이터")
    private T data;

    @ApiModelProperty(value = "페이지")
    private Pagination pagination;

    @Builder
    public PageResultDto(T data, Pagination pagination) {
        this.data = data;
        this.pagination = pagination;
    }

}
