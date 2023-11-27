package app.codingGround.domain.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pagination {

    public final int POSTS_PER_BLOCK = 9;
    public final int PAGES_PER_BLOCK = 5;
    private int page;
    private int currentBlock;
    private int totalPost;
    private int totalPage;
    private int totalBlock;
    private int startPage;
    private int endPage;
    private int prevBlock;
    private int nextBlock;
    private int startIndex;

    public Pagination(int currentPage, int totalPost) {
        this.page = currentPage;
        this.totalPost = totalPost;

        this.currentBlock = (int) Math.ceil((this.page * 1.0) / PAGES_PER_BLOCK);
        this.totalPage = (int) Math.ceil(this.totalPost * 1.0 / POSTS_PER_BLOCK);
        this.totalBlock = (int) Math.ceil(this.totalPage * 1.0 / PAGES_PER_BLOCK);
        this.startPage = ((this.currentBlock - 1) * PAGES_PER_BLOCK + 1);
        this.endPage = startPage + PAGES_PER_BLOCK - 1;

        if (endPage > totalPage) endPage = totalPage;
        prevBlock = (currentBlock * PAGES_PER_BLOCK) - PAGES_PER_BLOCK;

        if (nextBlock > totalPage) nextBlock = totalPage;

        startIndex = (this.page - 1) * PAGES_PER_BLOCK;
    }
}
