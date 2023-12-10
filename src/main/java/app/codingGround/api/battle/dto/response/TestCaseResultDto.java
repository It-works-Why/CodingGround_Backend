package app.codingGround.api.battle.dto.response;

import app.codingGround.api.entity.Language;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.ToString;

import java.lang.reflect.Type;
import java.util.List;

@Data
@ToString
public class TestCaseResultDto {
    private String source_code;
    private int language_id;
    private String stdin;
    private String expected_output;
    private String stdout;
    private int status_id;
    private String created_at;
    private String finished_at;
    private String time;
    private String memory;
    private String stderr;
    private String token;
    private int number_of_runs;
    private String cpu_time_limit;
    private String cpu_extra_time;
    private String wall_time_limit;
    private int memory_limit;
    private int stack_limit;
    private int max_processes_and_or_threads;
    private Boolean enable_per_process_and_thread_time_limit;
    private Boolean enable_per_process_and_thread_memory_limit;
    private int max_file_size;
    private String compile_output;
    private Integer exit_code;
    private String exit_signal;
    private String message;
    private String wall_time;
    private String compiler_options;
    private String command_line_arguments;
    private Boolean redirect_stderr_to_stdout;
    private String callback_url;
    private String additional_files;
    private Boolean enable_network;
    private Boolean isCorrect;
    private StatusDTO status;
    private LanguageResult language;
    public static TestCaseResultDto changeTestCaseResultDto(String responseBody, TestCaseDto dto) {
        Gson gson = new Gson();
        Type type = new TypeToken<TestCaseResultDto>() {}.getType();
        TestCaseResultDto testCaseResultDto = gson.fromJson(responseBody, type);
        if(testCaseResultDto.getStdout() != null && removeLastSpaceAndNewline(testCaseResultDto.getStdout()).equals(dto.getOutput())){
            testCaseResultDto.setIsCorrect(true);
        }else{
            testCaseResultDto.setIsCorrect(false);
        }

        return testCaseResultDto;
    }
    @Data
    public class StatusDTO {
        private int id;
        private String description;
    }

    @Data
    public class LanguageResult{
        private int id;
        private String name;
    }

    public static String removeLastSpaceAndNewline(String input) {
        // 입력 문자열이 null 또는 길이가 0인 경우 원본 문자열 그대로 반환
        if (input == null || input.length() == 0) {
            return input;
        }

        int length = input.length();

        // 문자열 끝부터 시작해서 마지막 공백(' ') 또는 개행 문자('\n')을 찾음
        int lastIndex = length - 1;
        while (lastIndex >= 0 && (input.charAt(lastIndex) == ' ' || input.charAt(lastIndex) == '\n')) {
            lastIndex--;
        }

        // 공백과 개행 문자를 제외한 부분을 새로운 문자열로 반환
        return input.substring(0, lastIndex + 1);
    }

}
