package org.example.kafkauser.adapter.in.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.kafkauser.common.annotation.trace.TraceGrpcServer;
import org.example.kafkauser.dto.jpa.UsersDto;
import org.example.kafkauser.event.UsersSignupEvent;
import org.example.kafkauser.grpc.UserProto;
import org.example.kafkauser.grpc.UserServiceGrpc;
import org.example.kafkauser.mapper.UsersMapper;
import org.example.kafkauser.port.in.UsersOutboxUseCase;
import org.example.kafkauser.port.in.UsersUseCase;
import org.example.kafkauser.port.out.UsersCrudPort;

@Slf4j
@RequiredArgsConstructor
@GrpcService
public class UsersGrpcServiceAdapter extends UserServiceGrpc.UserServiceImplBase {
    private final UsersUseCase usersUseCase;
    private final UsersMapper usersMapper;
    private final UsersOutboxUseCase usersOutboxUseCase;

    /*
    * @Param request          - 요청 데이터
    * @Param responseObserver - 응답 데이터
    * @ApiNote - id로 회원 조회
    * */
    @Override
    @TraceGrpcServer
    public void getUsersById(
            UserProto.UsersIdRequest request,
            StreamObserver<UserProto.UsersRetrieveResponse> responseObserver
    ) {
        UsersSignupEvent event = new UsersSignupEvent(request.getUserId());
        try {
            UsersDto usersDto = UsersDto.of(request.getUserId());
            UsersDto findDto = usersUseCase.findByUserId(usersDto);
            UserProto.UsersRetrieveResponse response = usersMapper.toProto(findDto);

            responseObserver.onNext(response);
            responseObserver.onCompleted();

            // outbox 이벤트 성공 처리
            usersOutboxUseCase.markOutboxEventSuccess(event);
        } catch (Exception e) {
            // 예외 발생 시, outbox 이벤트 실패 처리
            usersOutboxUseCase.markOutboxEventFailed(event);
            responseObserver.onError(e);
            throw e; // 예외를 다시 던져, AOP 에서 처리되도록 함.
        }
    }
}
