package app.codingGround.api.account.dto;

import app.codingGround.api.entity.Community;

import lombok.Getter;
import lombok.ToString;

import java.text.SimpleDateFormat;

@Getter
@ToString
public class CommunityListDto {
    private Long postNum;
    private String postTitle;
    private String postContent;
    private String userNickname;
    private String userProfileImg;
    private String postTime;

    public CommunityListDto(Community community) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        this.postNum = community.getPostNum();
        this.postTitle = community.getPostTitle();
        this.postContent = community.getPostContent();
        this.userNickname = community.getUser().getUserNickname();
        this.userProfileImg = community.getUser().getUserProfileImg();
        this.postTime = format.format(community.getPostTime());
    }
}
