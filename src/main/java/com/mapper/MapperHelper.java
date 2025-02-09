package com.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.customer.repository.FeedbackRepository;

@Component
public class MapperHelper {
    private static FeedbackRepository feedbackRepo;
    
    @Autowired
    public void setFeedbackRepository(FeedbackRepository repo) {
        MapperHelper.feedbackRepo = repo;
    }
    
    public static FeedbackRepository getFeedbackRepo() {
        return feedbackRepo;
    }
}