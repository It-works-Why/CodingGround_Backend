package app.codingGround.api.admin.service;

import app.codingGround.api.admin.dto.AdminQuestionRegisterDto;
import app.codingGround.api.admin.repository.AdminQuestionRepository;
import app.codingGround.api.admin.repository.AdminTestCaseRepository;
import app.codingGround.api.entity.Question;
import app.codingGround.api.entity.TestCase;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AdminQuestionService {

    private final AdminQuestionRepository adminQuestionRepository;
    private final AdminTestCaseRepository adminTestCaseRepository;

    @Transactional
    public DefaultResultDto postQuestion(AdminQuestionRegisterDto adminQuestionRegisterDto) {

        Question question = new Question();
        question.setQuestionTitle(adminQuestionRegisterDto.getQuestionTitle());
        question.setQuestionLimitTime(adminQuestionRegisterDto.getQuestionLimitTime());
        question.setQuestionDifficult(adminQuestionRegisterDto.getQuestionDifficult());
        question.setQuestionContent(adminQuestionRegisterDto.getQuestionContent());

        adminQuestionRepository.save(question);

        // TestCase1
        TestCase testCase1 = new TestCase();
        testCase1.setQuestion(question);
        testCase1.setTestCaseInput(adminQuestionRegisterDto.getTestCaseInput1());
        testCase1.setTestCaseOutput(adminQuestionRegisterDto.getTestCaseOutput1());
        adminTestCaseRepository.save(testCase1);

        // TestCase2
        TestCase testCase2 = new TestCase();
        testCase2.setQuestion(question);
        testCase2.setTestCaseInput(adminQuestionRegisterDto.getTestCaseInput2());
        testCase2.setTestCaseOutput(adminQuestionRegisterDto.getTestCaseOutput2());
        adminTestCaseRepository.save(testCase2);

        // TestCase3
        TestCase testCase3 = new TestCase();
        testCase3.setQuestion(question);
        testCase3.setTestCaseInput(adminQuestionRegisterDto.getTestCaseInput3());
        testCase3.setTestCaseOutput(adminQuestionRegisterDto.getTestCaseOutput3());
        adminTestCaseRepository.save(testCase3);

        return DefaultResultDto.builder().success(true).message("문제가 등록되었습니다.").build();
    }

    public Page<Question> getQuestionList(Pageable pageable) {
        return adminQuestionRepository.findAllByUseStatus(pageable, 1);
    }

    public Page<Question> getSearchQuestionList(Pageable pageable, String keyword) {
        return adminQuestionRepository.findAllByQuestionNumAndUseStatus(pageable, keyword, 1);
    }
}
