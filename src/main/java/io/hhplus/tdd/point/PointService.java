package io.hhplus.tdd.point;

import java.util.List;

import org.springframework.stereotype.Service;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.database.PointHistoryTable;


@Service
public class PointService {
    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    public PointService(UserPointTable userPointTable, PointHistoryTable pointHistoryTable) {
        this.userPointTable = userPointTable;
        this.pointHistoryTable = pointHistoryTable;
    }

    public UserPoint getUserPoint(Long id){
        return userPointTable.selectById(id);
    }

    public List<PointHistory> getPointHistory(Long id){
        return pointHistoryTable.selectAllByUserId(id);
    }

    public UserPoint chargePoint(Long id, Long amount){
        UserPoint userPoint = userPointTable.selectById(id);
        long newAmount = userPoint.point() + amount;
        UserPoint updatedUserPoint = userPointTable.insertOrUpdate(id, newAmount);
        pointHistoryTable.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());
        return updatedUserPoint;
    }

    public UserPoint usePoint(Long id, Long amount){
        UserPoint userPoint = userPointTable.selectById(id);
        if(userPoint.point() < amount){
            throw new IllegalArgumentException("Not enough points");
        }
        long newAmount = userPoint.point() - amount;
        UserPoint updatedUserPoint = userPointTable.insertOrUpdate(id, newAmount);
        pointHistoryTable.insert(id, amount, TransactionType.USE, System.currentTimeMillis());
        return updatedUserPoint;
    }
}