package app.codingGround.api.admin.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@ToString
public class ContactDetailDto {

    private Long contactNum;
    private String contactTitle;
    private String contactContent;
    private String userNickname;
    private String contactTime;
    private String contactAnswer;

//    public ContactAnswerDto(Contact contact) {
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//
//        this.contactNum = contact.getContactNum();
//        this.contactTitle = contact.getContactTitle();
//        this.contactContent = contact.getContactContent();
//        this.contactTime = simpleDateFormat.format(contact.getContactTime());
//        this.contactAnswer = contact.getContactAnswer();
//    }
}
