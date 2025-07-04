package com.cineflex.api.service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.dto.BankTransfer;
import com.cineflex.api.model.Account;
import com.cineflex.api.model.BillingDetail;
import com.cineflex.api.model.Subscription;
import com.cineflex.api.repository.BillingDetailRepository;
import com.cineflex.api.repository.SubscriptionRepository;

@Service
public class OrderService {
    private final BillingDetailRepository billingDetailRepository;
    private final AuthenticationService authenticationService;
    private final SubscriptionRepository subscriptionRepository;
    private final EmailService emailService;
    private final AccountDetailService accountDetailService;
    private final WebSocketPushService webSocketPushService;
    
    public OrderService (
        BillingDetailRepository billingDetailRepository,
        AuthenticationService authenticationService,
        EmailService emailService,
        SubscriptionRepository subscriptionRepository,
        AccountDetailService accountDetailService,
        WebSocketPushService webSocketPushService
    ) {
        this.billingDetailRepository = billingDetailRepository;
        this.authenticationService = authenticationService;
        this.emailService = emailService;
        this.subscriptionRepository = subscriptionRepository;
        this.accountDetailService = accountDetailService;
        this.webSocketPushService = webSocketPushService;
    }

    public Subscription getUserSubscription(UUID id) {
        try {
            Subscription subscription = subscriptionRepository.getActivatedByAccount(id);

            return subscription;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Transactional
    public BillingDetail createOrder (
        Double amount
    ) {

        try {
            Account user = authenticationService.getAccount();

            Subscription subscription = getUserSubscription(user.getId());

            BillingDetail billingDetail;

            billingDetail = billingDetailRepository.getAccountUnpaid(user.getId());

            if (billingDetail != null) {
                return billingDetail;
            }

            Random random = new Random();
            int tc = random.nextInt(99999999);
            String transactionCode = String.format("GD%08d", tc); 

            System.out.println(tc);

            billingDetail = BillingDetail.builder()
                .id(UUID.randomUUID())
                .account(user.getId())
                .subscription(subscription != null ? subscription.getId() : null)
                .amount(amount)
                .createdTime(LocalDateTime.now())
                .paidTime(null)
                .paid(false)
                .transactionCode(transactionCode)
                .build();
            
            billingDetailRepository.create(billingDetail);

            BillingDetail insertedValue = billingDetailRepository.read(billingDetail.getId());

            return insertedValue;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public BillingDetail getOrderById(UUID id) {
        try {
            BillingDetail b = billingDetailRepository.read(id);

            return b;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR ,e.getMessage());
        }
    }

    @Transactional
    public void confirmPayment(BankTransfer b) {
        if (b.getTransferType() == "out") {
            return;
        }

        String code = b.getCode();

        if (code == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot find transaction code, reference code: " + b.getReferenceCode() + ", transaction code: " + b.getCode());
        }

        BillingDetail billingDetail = billingDetailRepository.getByTransactionCode(code);


        try {


            if (billingDetail == null) {
                throw new RuntimeException("Cannot find order, reference code: " + b.getReferenceCode() + ", transaction code: " + b.getCode());
            }



            if (!Objects.equals(billingDetail.getAmount(), b.getTransferAmount())) {
                Account user = accountDetailService.getById(billingDetail.getAccount());
                emailService.sendEmail("Please contact admin to get your money back", (user != null)?user.getEmail():"z1ml3w1s123@gmail.com", "Bank transfer not equal");
                return;
            }

            if (billingDetail.getPaid()) {
                webSocketPushService.sendToBillngQueue(billingDetail.getId(), "This order is paid already");
                return;
            }

            // Check if the billing detail has already have a subscription bound to it
            Subscription subscription;

            if (billingDetail.getSubscription() != null && subscriptionRepository.read(billingDetail.getSubscription()) != null) {
                // If yes then get the subscription from the database
                subscription = subscriptionRepository.read(billingDetail.getSubscription());
                subscription.setEndTime(subscription.getEndTime().plusMonths(1));
                subscriptionRepository.update(subscription.getId(), subscription);
            }
            else {
                // If no then create the subscription
                UUID sid = UUID.randomUUID();
                subscription = Subscription.builder()
                    .id(sid)
                    .startTime(LocalDateTime.now())
                    .account(billingDetail.getAccount())
                    .endTime(LocalDateTime.now().plusMonths(1))
                    .build();
                
                subscriptionRepository.create(subscription);
                billingDetail.setSubscription(sid);
            }

            billingDetail.setPaid(true);
            billingDetail.setPaidTime(LocalDateTime.now());
            billingDetailRepository.update(billingDetail.getId(), billingDetail);

            webSocketPushService.sendToBillngQueue(billingDetail.getId(), "PAID");
        
        }
        catch (Exception e) {
            
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
