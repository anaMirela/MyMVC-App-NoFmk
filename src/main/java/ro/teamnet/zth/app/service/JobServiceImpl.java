package ro.teamnet.zth.app.service;

import ro.teamnet.zth.app.dao.JobDao;
import ro.teamnet.zth.app.domain.Job;

import java.util.List;

/**
 * Created by Mi on 5/8/2015.
 */
public class JobServiceImpl implements JobService {
    @Override
    public List<Job> findAllJobs() {
        JobDao jobDao = new JobDao();
        return jobDao.getAllJobs();
    }

    @Override
    public Job findOneJob(int id) {
        JobDao jobDao = new JobDao();
        return jobDao.getJobById(id);
    }
}
