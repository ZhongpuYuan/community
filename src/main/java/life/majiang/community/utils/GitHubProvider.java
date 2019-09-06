package life.majiang.community.utils;

import com.alibaba.fastjson.JSON;
import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GitHubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component // 加载到spring容器
public class GitHubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){

        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accessTokenDTO));

        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();

            try (Response response = client.newCall(request).execute()) {
                String string = response.body().string();
                System.out.println(string);
                // access_token=193841aabeb82e3a9dd8ee3a84ca216c7036a434&scope=user&token_type=bearer
                /**
                 *  String[] split = string.split("&");// 先按照&切分字符串
                 *  String tokenstr = split[0];
                 *  String[] split1 = tokenstr.split("=");// 再按照=切分
                 *  String token = split1[1];
                 */
                String token = string.split("&")[0].split("=")[1];// 链式编程
                return token;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
    }

    public GitHubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            // 将string解析为Java类对象
            GitHubUser gitHubUser = JSON.parseObject(string, GitHubUser.class);
            return gitHubUser;
        }catch (IOException e){

        }
        return null;
    }
}
