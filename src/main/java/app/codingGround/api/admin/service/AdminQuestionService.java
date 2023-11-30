package app.codingGround.api.admin.service;

import app.codingGround.api.admin.dto.AdminQuestionListDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        return adminQuestionRepository.findAllByQuestionTitleContainingAndUseStatus(pageable, keyword, 1);
    }

    public List<AdminQuestionListDto> getQuestionDetail(Long questionNum) {
        List<TestCase> testCaseList = adminTestCaseRepository.findAllByQuestion_UseStatusAndQuestion_QuestionNum(1, questionNum);

        List<AdminQuestionListDto> adminQuestionList = new ArrayList<>();

        AdminQuestionListDto adminQuestionListDto1 = new AdminQuestionListDto(testCaseList.get(0));
        AdminQuestionListDto adminQuestionListDto2 = new AdminQuestionListDto(testCaseList.get(1));
        AdminQuestionListDto adminQuestionListDto3 = new AdminQuestionListDto(testCaseList.get(2));

        adminQuestionList.add(adminQuestionListDto1);
        adminQuestionList.add(adminQuestionListDto2);
        adminQuestionList.add(adminQuestionListDto3);

        return adminQuestionList;
    }

    @Transactional
    public DefaultResultDto editQuestion(AdminQuestionRegisterDto adminQuestionRegisterDto, Long questionNum) {

        Question question = adminQuestionRepository.findByQuestionNumAndUseStatus(questionNum, 1);

        question.setQuestionTitle(adminQuestionRegisterDto.getQuestionTitle());
        question.setQuestionLimitTime(adminQuestionRegisterDto.getQuestionLimitTime());
        question.setQuestionDifficult(adminQuestionRegisterDto.getQuestionDifficult());
        question.setQuestionContent(adminQuestionRegisterDto.getQuestionContent());

        adminQuestionRepository.save(question);

        List<TestCase> testCaseList = adminTestCaseRepository.findAllByQuestion_UseStatusAndQuestion_QuestionNum(1, questionNum);
        testCaseList.get(0).setTestCaseInput(adminQuestionRegisterDto.getTestCaseInput1());
        testCaseList.get(0).setTestCaseOutput(adminQuestionRegisterDto.getTestCaseOutput1());
        testCaseList.get(1).setTestCaseInput(adminQuestionRegisterDto.getTestCaseInput2());
        testCaseList.get(1).setTestCaseOutput(adminQuestionRegisterDto.getTestCaseOutput2());
        testCaseList.get(2).setTestCaseInput(adminQuestionRegisterDto.getTestCaseInput3());
        testCaseList.get(2).setTestCaseOutput(adminQuestionRegisterDto.getTestCaseOutput3());

        adminTestCaseRepository.save(testCaseList.get(0));
        adminTestCaseRepository.save(testCaseList.get(1));
        adminTestCaseRepository.save(testCaseList.get(2));

        return DefaultResultDto.builder().success(true).message("문제가 수정되었습니다.").build();
    }

    @Transactional
    public DefaultResultDto deleteQuestion(Long questionNum) {

        Question question = adminQuestionRepository.findByQuestionNumAndUseStatus(questionNum, 1);
        question.setUseStatus(0);

        adminQuestionRepository.save(question);

        return DefaultResultDto.builder().success(true).message("문제가 삭제 되었습니다.").build();
    }
}
