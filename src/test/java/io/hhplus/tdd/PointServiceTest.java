package io.hhplus.tdd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import io.hhplus.tdd.point.PointService;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointHistory;

public class PointServiceTest {

    private PointService pointService;
    private UserPointTable userPointTable;
    private PointHistoryTable pointHistoryTable;

    @BeforeEach
    void setUp() {
        pointHistoryTable = new PointHistoryTable();
        userPointTable = new UserPointTable();
        pointService = new PointService(userPointTable, pointHistoryTable);
    }

    @Test
    @DisplayName("특정 유저의 포인트 조회 테스트")
    public void getUserPointTest(){
        //given
        final Long userId = 1L;

        // when
        UserPoint userPoint = pointService.getUserPoint(userId);

        // then 
        assertThat(userPoint.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("특정 유저의 포인트 충전/이용 내역 조회 테스트")
    public void getPointHistoryTest(){
        //given
        final Long userId = 1L;

        // when
        pointHistoryTable.insert(1L, 100L, TransactionType.CHARGE, System.currentTimeMillis());
        List<PointHistory> pointHistory = pointService.getPointHistory(userId);

        // then
        assertThat(pointHistory).isNotEmpty();
    }

    @Test
    @DisplayName("특정 유저의 포인트 충전 테스트")
    public void chargePointTest(){
        //given
        final Long userId = 1L;
        final Long amount = 1000L;

        // when
        UserPoint userPoint = pointService.chargePoint(userId, amount);

        // then 
        assertThat(userPoint.point()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("특정 유저의 포인트 사용 테스트 - 에러 버전")
    public void usePointTest_ReturnError(){
        //given
        final Long userId = 1L;
        final Long useAmount = 100L;

        // when
        assertThatThrownBy(() -> pointService.usePoint(userId, useAmount))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Not enough points");
    }

    @Test
    @DisplayName("특정 유저의 포인트 사용 테스트 - 성공 버전")
    public void usePointTest_ReturnSuccess(){
        //given
        final Long userId = 1L;
        final Long chargeAmount = 1000L;
        final Long useAmount = 100L;

        // when
        pointService.chargePoint(userId, chargeAmount);
        UserPoint userPoint = pointService.usePoint(userId, useAmount);

        // then 
        assertThat(userPoint.point()).isEqualTo(900L);
    }
}