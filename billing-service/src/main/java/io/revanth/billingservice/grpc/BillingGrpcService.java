package io.revanth.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {

    private static final Logger log =
            LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(
            BillingRequest billingRequest,
            StreamObserver<BillingResponse> responseObserver) {

        log.info("Create billing request received: {}", billingRequest);

        BillingResponse billingResponse = BillingResponse.newBuilder()
                .setAccountId("12345")
                .setStatus("Active")
                .build();

        responseObserver.onNext(billingResponse);
        responseObserver.onCompleted();
    }
}