package app.codingGround.api.admin.dto;

import app.codingGround.api.entity.Contact;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.text.SimpleDateFormat;

@Getter
@Setter
@ToString
public class ContactAnswerDto {

    private Long contactNum;
    private String contactTitle;
    private String contactContent;
    private String contactTime;
    private String contactAnswer;

    public ContactAnswerDto(Contact contact) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        this.contactNum = contact.getContactNum();
        this.contactTitle = contact.getContactTitle();
        this.contactContent = contact.getContactContent();
        this.contactTime = simpleDateFormat.format(contact.getContactTime());
        this.contactAnswer = contact.getContactAnswer();
    }

}
