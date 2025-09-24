package io.hhplus.tdd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import io.hhplus.tdd.point.PointService;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointHistory;

public class PointServiceUnitTest {

    // 포인트 조회 테스트: 특정 유저를 조회됐을 때, pointService가 올바른 UserPoint를 리턴하는지 확인
    @Test
    @DisplayName("특정 유저의 포인트 조회 테스트")
    public void givenUserId_whenGetUserPoint_thenReturnUserPoint() {
        UserPointTable mockUserPointTable = mock(UserPointTable.class);
        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
        PointService pointService = new PointService(mockUserPointTable, mockPointHistoryTable);
        
        // given
        final Long userId = 1L;
        UserPoint expectedUserPoint = new UserPoint(userId, 0L, 1000L);
        
        // when
        when(mockUserPointTable.selectById(userId)).thenReturn(expectedUserPoint);

        // then
        UserPoint result = pointService.getUserPoint(userId);

        assertThat(result).isEqualTo(expectedUserPoint);
        verify(mockUserPointTable).selectById(userId);
    }

    // 이용 내역 조회 테스트: 특정 유저가 조회됐을 때, 반환하는 UserPoint가 올바른지 확인 (point 증가)
    @Test
    @DisplayName("특정 유저의 포인트 충전/이용 내역 조회 테스트")
    public void givenUserId_whenGetPointHistory_thenReturnPointHistory(){
        UserPointTable mockUserPointTable = mock(UserPointTable.class);
        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
        PointService pointService = new PointService(mockUserPointTable, mockPointHistoryTable);

        //given
        final Long userId = 1L;
        List<PointHistory> expectedHistory = List.of(
            new PointHistory(1L, userId, 100L, TransactionType.CHARGE, 1000L)
        );

        // when
        when(mockPointHistoryTable.selectAllByUserId(userId)).thenReturn(expectedHistory);

        // then
        List<PointHistory> result = pointService.getPointHistory(userId);

        assertThat(result).isEqualTo(expectedHistory);
        verify(mockPointHistoryTable).selectAllByUserId(userId);
    }

    // 포인트 충전 테스트: 특정 유저가 포인트 충전을 했을 때, pointService가 올바른 포인트를 가지는지 확인
    @Test
    @DisplayName("특정 유저의 포인트 충전 테스트")
    public void givenUserIdAndAmount_whenChargePoint_thenReturnUserPoint(){
        UserPointTable mockUserPointTable = mock(UserPointTable.class);
        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
        PointService pointService = new PointService(mockUserPointTable, mockPointHistoryTable);

        //given
        final Long userId = 1L;
        final Long amount = 1000L;
        final UserPoint initialUserPoint = new UserPoint(userId, 0L, 0L);
        final UserPoint expectedUserPoint = new UserPoint(userId, 1000L, 1000L);

        // when
        when(mockUserPointTable.selectById(userId)).thenReturn(initialUserPoint);
        when(mockUserPointTable.insertOrUpdate(userId, amount)).thenReturn(expectedUserPoint);

        // then 
        UserPoint userPoint = pointService.chargePoint(userId, amount);

        assertThat(userPoint).isEqualTo(expectedUserPoint);
        verify(mockUserPointTable).insertOrUpdate(userId, amount);
    }

    // 포인트 사용 테스트: 특정 유저가 포인트를 사용했을 때, 현 포인트보다 사용하는 포인트가 더 클 경우 에러를 리턴하는지 확인
    @Test
    @DisplayName("특정 유저의 포인트 사용 테스트 - 에러 버전")
    public void givenUserIdAndAmount_whenUsePoint_thenThrowException(){
        UserPointTable mockUserPointTable = mock(UserPointTable.class);
        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
        PointService pointService = new PointService(mockUserPointTable, mockPointHistoryTable);

        //given
        final Long userId = 1L;
        final Long useAmount = 100L;

        // when
        when(mockUserPointTable.selectById(userId)).thenReturn(new UserPoint(userId, 0L, 1000L)); 

        // then
        assertThatThrownBy(() -> pointService.usePoint(userId, useAmount))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Not enough points");
    }

    // 포인트 사용 테스트: 특정 유저가 포인트를 사용했을 때, 반환하는 UserPoint가 올바른지 확인 (point 감소)
    @Test
    @DisplayName("특정 유저의 포인트 사용 테스트 - 성공 버전")
    public void givenUserIdAndAmount_whenUsePoint_thenReturnUserPoint(){
        UserPointTable mockUserPointTable = mock(UserPointTable.class);
        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
        PointService pointService = new PointService(mockUserPointTable, mockPointHistoryTable);

        //given
        final Long userId = 1L;
        final Long useAmount = 100L;
        final UserPoint initialUserPoint = new UserPoint(userId, 1000L, 1000L);
        final UserPoint expectedUserPoint = new UserPoint(userId, 900L, 1000L);

        // when
        when(mockUserPointTable.selectById(userId)).thenReturn(initialUserPoint);
        when(mockUserPointTable.insertOrUpdate(userId, 900L)).thenReturn(expectedUserPoint);

        // then
        UserPoint userPoint = pointService.usePoint(userId, useAmount);

        assertThat(userPoint).isEqualTo(expectedUserPoint);
    }
}