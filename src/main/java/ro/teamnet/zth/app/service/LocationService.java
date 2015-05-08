package ro.teamnet.zth.app.service;

import ro.teamnet.zth.app.domain.Location;

import java.util.List;

/**
 * Created by Mi on 5/8/2015.
 */
public interface LocationService {
    List<Location> findAllLocations();
    Location findOneLocation(int id);
}
