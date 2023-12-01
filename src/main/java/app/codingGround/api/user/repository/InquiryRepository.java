package app.codingGround.api.user.repository;

import app.codingGround.api.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<Contact, Long> {

}
