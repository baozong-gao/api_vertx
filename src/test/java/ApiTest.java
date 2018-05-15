import okhttp3.*;

/**
 * @Author: gaobaozong
 * @Description: 测试
 * @Date: Created in 2017/10/25 - 13:09
 * @Version: V1.0
 */
public class ApiTest {

    public static void main(String[] args) {
        try {
            String json = "{\n" +
                    "\t\"instid\" : \"xxx\",\n" +
                    "\t\"agentid\" : \"xxx\",\n" +
                    "\t\"appid\" : \"xxx\",\n" +
                    "\t\"accesstoken\" : \"xxx\",\n" +
                    "\t\"transcode\" : \"uc0001\",\n" +
                    "\t\"requesttime\": \"20170403111111\",\n" +
                    "\t\"requestbody\" : \"{\\\"registerType\\\":\\\"00\\\",\\\"registerUserType\\\":\\\"00\\\",\\\"mobile\\\":\\\"15021919057\\\",\\\"loginId\\\":\\\"laughin\\\",\\\"password\\\":\\\"123456\\\"}\"\n" +
                    "}";
            MediaType j = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(j, json);
            Request request = new Request.Builder().url("http://172.16.1.35:8001/api/allround")
                    .post(body).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println(response.body().string());
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

}
