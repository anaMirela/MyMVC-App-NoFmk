package ro.teamnet.zth.app.service;

import ro.teamnet.zth.app.domain.Job;

import java.util.List;

/**
 * Created by Mi on 5/8/2015.
 */
public interface JobService {
    List<Job> findAllJobs();
    Job findOneJob(int id);
}
