package app.codingGround.api.user.dto.response;

import app.codingGround.api.contact.dto.response.ContactListDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.List;


@Data
@NoArgsConstructor
@Alias("PageNumDto")
public class PageNumDto {

    private int totalPages;

}
