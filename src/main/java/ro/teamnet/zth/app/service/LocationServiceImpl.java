package ro.teamnet.zth.app.service;

import ro.teamnet.zth.app.dao.LocationDao;
import ro.teamnet.zth.app.domain.Location;

import java.util.List;

/**
 * Created by Mi on 5/8/2015.
 */
public class LocationServiceImpl implements LocationService {
    @Override
    public List<Location> findAllLocations() {
        LocationDao locationDao = new LocationDao();
        return locationDao.getAllLocations();
    }

    @Override
    public Location findOneLocation(int id) {
        LocationDao locationDao = new LocationDao();
        return locationDao.getLocationById(id);
    }
}
