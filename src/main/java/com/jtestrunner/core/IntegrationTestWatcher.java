package com.jtestrunner.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtestrunner.Config;
import com.jtestrunner.Driver;
import com.jtestrunner.core.api.client.ApiClient;
import com.jtestrunner.model.Status;
import org.junit.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by yusufaltun on 30/04/2017.
 */
public class IntegrationTestWatcher extends TestWatcher
{
    @Override
    public Statement apply(Statement base, Description description) {
        return super.apply(base, description);
    }

    @Override
    protected void failed(Throwable e, Description description) {
//        ApiClient.getInstance().getTestInfo().setStatus(Status.FAILURE);
    }

    @Override
    protected void succeeded(Description description) {
        System.out.println(description.getDisplayName() + " " + "success!\n");
//        ApiClient.getInstance().getTestInfo().setStatus(Status.SUCCESS);
    }

    @Override
    protected void skipped(AssumptionViolatedException e, Description description) {
        System.out.println("skiped"+ description.getDisplayName() + " " + e.getClass()
                .getSimpleName());
    }

    @Override
    protected void starting(Description description)
    {
//        System.out.println(description.getClassName() + "#"+ description.getMethodName());
//        ApiClient.getInstance().getTestInfo().setScenarioName(description.getClassName() + "#"+ description.getMethodName());
//        ApiClient.getInstance().getTestInfo().setStatus(Status.RUNNING);
//        ApiClient.getInstance().getTestInfo().setJobExecutionId(Config.EXECUTION_ID);
//
//        String videoName = description.getClassName() + "_"+ description.getMethodName() +"_"+ new Date().getTime();
//        ApiClient.getApiClient().getTestInfo().setVideoName(videoName.replace(".", "_"));
//        ApiClient.getApiClient().createTransaction();
    }

    @Override
    protected void finished(Description description)
    {
//        ObjectMapper objectMapper = new ObjectMapper();
//        try
//        {
//            System.out.println(objectMapper.writeValueAsString(ApiClient.getInstance().getTestInfo()));
//        }
//        catch (JsonProcessingException e)
//        {
//            e.printStackTrace();
//        }
//        System.out.println("finished"+ description.getDisplayName() + " ");


//        try (Stream<Path> walk = Files.walk(Paths.get("/tmp/videos"))) {
//
//            List<String> result = walk.map(x -> x.toString())
//                    .filter(f -> f.contains(ApiClient.getApiClient().getTestInfo().getVideoName())).collect(Collectors.toList());
//
//            if(result.size() > 0)
//            {
//                ApiClient.getApiClient().getTestInfo().setVideoName(result.get(0));
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ApiClient.getApiClient().updateTransaction();
    }
}
