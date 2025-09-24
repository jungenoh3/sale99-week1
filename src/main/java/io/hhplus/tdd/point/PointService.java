package io.hhplus.tdd.point;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PointService {
    public UserPoint getUserPoint(Long id){
        return new UserPoint(0, 0, 0);
    }

    public List<PointHistory> getPointHistory(Long id){
        return List.of();
    }

    public UserPoint chargePoint(Long id, Long amount){
        return new UserPoint(0, 0, 0);
    }

    public UserPoint usePoint(Long id, Long amount){
        return new UserPoint(0, 0, 0);
    }
}