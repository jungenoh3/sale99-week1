package io.hhplus.tdd.point;

import java.util.List;

import org.springframework.stereotype.Service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;

@Service
public class PointService {
    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    public PointService(UserPointTable userPointTable, PointHistoryTable pointHistoryTable) {
        this.userPointTable = userPointTable;
        this.pointHistoryTable = pointHistoryTable;
    }

    public UserPoint getUserPoint(Long id){
        // userPointTable에서 id를 가진 UserPoint를 조회
        return userPointTable.selectById(id);
    }

    public List<PointHistory> getPointHistory(Long id){
        // pointHistoryTable에서 id를 가진 모든 PointHistory를 조회
        return pointHistoryTable.selectAllByUserId(id);
    }

    public UserPoint chargePoint(Long id, Long amount){
        // userPointTable에서 id를 가진 UserPoint를 조회
        UserPoint userPoint = userPointTable.selectById(id);

        // 포인트 계산
        long newAmount = userPoint.point() + amount;

        // userPointTable에 업데이트 및 pointHistoryTable에 기록
        UserPoint newUserPoint = userPointTable.insertOrUpdate(id, newAmount);
        pointHistoryTable.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());

        return newUserPoint;
    }

    public UserPoint usePoint(Long id, Long amount){
        // userPointTable에서 id를 가진 UserPoint를 조회
        UserPoint userPoint = userPointTable.selectById(id);

        // 
        if(userPoint.point() < amount){
            throw new IllegalArgumentException("Not enough points");
        }

        long newAmount = userPoint.point() - amount;
        
        UserPoint newUserPoint = userPointTable.insertOrUpdate(id, newAmount);
        pointHistoryTable.insert(id, amount, TransactionType.USE, System.currentTimeMillis());
        
        return newUserPoint;
    }
}