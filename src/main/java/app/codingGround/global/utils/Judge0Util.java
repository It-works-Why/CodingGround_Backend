package app.codingGround.global.utils;

import app.codingGround.api.battle.dto.request.CodeData;
import app.codingGround.api.battle.dto.request.Judge0;
import app.codingGround.api.battle.dto.response.TestCaseDto;
import app.codingGround.api.battle.dto.response.TestCaseResultDto;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Judge0Util {

    public static List<String> runCode(CodeData codeData, List<TestCaseDto> testCaseDtos) {

        try {
            String url = "http://BSDEV16-Judge0-NLB-48a8c6e34fa5d6bd.elb.ap-east-1.amazonaws.com:2358/submissions/";
            OkHttpClient client = new OkHttpClient();
            Response response = null;
            List<String> tokens = new ArrayList<>();
            String code = codeData.getCode();
            Judge0 judge0 = new Judge0();
            judge0.setSource_code(codeData.getCode());
            judge0.setLanguage_id(codeData.getLanguageCode());
            for (TestCaseDto dto : testCaseDtos) {
                judge0.setStdin(dto.getInput());
                Gson gson = new Gson();
                String requestBody = gson.toJson(judge0);

                Request request = new Request.Builder().addHeader("content-type", "application/json").addHeader("Content-Type", "application/json").url(url).post(RequestBody.create(MediaType.parse("application/json"), requestBody)).build();

                response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                    if (jsonObject.has("token")) {
                        String token = jsonObject.get("token").getAsString();
                        tokens.add(token);
                    }
                }
            }
            return tokens;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public static List<TestCaseResultDto> readTokens(List<String> tokens, List<TestCaseDto> testCaseDtos) {
        List<TestCaseResultDto> list = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        int i = 0;
        try {
            Thread.sleep(5000);
            for (String token : tokens) {
                String url = "http://BSDEV16-Judge0-NLB-48a8c6e34fa5d6bd.elb.ap-east-1.amazonaws.com:2358/submissions/" + token + "?base64_encoded=false&fields=*";
                Request request = new Request.Builder().url(url).get().build();
                Response response = client.newCall(request).execute();
                TestCaseResultDto testCaseResultDto = TestCaseResultDto.changeTestCaseResultDto(response.body().string(), testCaseDtos.get(i));
                System.out.println(testCaseResultDto);
                list.add(testCaseResultDto);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
