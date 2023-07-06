package com.jtestrunner.core.api.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtestrunner.Config;
import com.jtestrunner.model.TestInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ApiClient
{
    private static final String BASE_URL = "http://localhost:8081/";
    private static final ApiClient apiClient = new ApiClient();
    private TestInfo testInfo = new TestInfo();
    private ApiClient()
    {

    }

    public static ApiClient getInstance()
    {
        return apiClient;
    }

    public static ApiClient getApiClient() {
        return apiClient;
    }

    public TestInfo getTestInfo() {
        return testInfo;
    }

    public void setTestInfo(TestInfo testInfo) {
        this.testInfo = testInfo;
    }

    public void createTransaction()
    {
        if(!Config.SYNC_API) return ;
        ObjectMapper o = new ObjectMapper();
        try {
            System.out.println(o.writeValueAsString(testInfo));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Long> responseEntity =  restTemplate.postForEntity(BASE_URL + "transaction/create-transaction", testInfo, Long.class);
        long transactionId = responseEntity.getBody();
        testInfo.setTransactionId(transactionId);
    }

    public void updateTransaction() {
        if(!Config.SYNC_API) return ;
        ObjectMapper o = new ObjectMapper();
        try {
            System.out.println(o.writeValueAsString(testInfo));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(BASE_URL + "transaction/update-transaction", testInfo);
    }
}
