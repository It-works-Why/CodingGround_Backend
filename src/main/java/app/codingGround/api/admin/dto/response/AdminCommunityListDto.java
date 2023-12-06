package app.codingGround.api.admin.dto.response;

import app.codingGround.api.entity.Community;
import lombok.Getter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@ToString
public class AdminCommunityListDto {

    private Long postNum;
    private String postTitle;
    private String postContent;
    private String userNickname;
    private String userProfileImg;
    private Timestamp postTime;

    public AdminCommunityListDto(Community community) {

        this.postNum = community.getPostNum();
        this.postTitle = community.getPostTitle();
        this.postContent = community.getPostContent();
        this.userNickname = community.getUser().getUserNickname();
        this.userProfileImg = community.getUser().getUserProfileImg();
        this.postTime = community.getPostTime();
    }

}
