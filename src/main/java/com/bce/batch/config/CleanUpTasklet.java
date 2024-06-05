package com.bce.batch.config;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class CleanUpTasklet implements Tasklet {
    
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // Your task logic goes here
        System.out.println("Executing my cleaneup tasklet...");
        // Return RepeatStatus.FINISHED if the task is successful
        return RepeatStatus.FINISHED;
    }
}