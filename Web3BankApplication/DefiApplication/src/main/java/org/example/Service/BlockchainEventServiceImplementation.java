package org.example.Service;

import org.example.Entity.Event;
import org.example.Repository.EventParametersRepository;
import org.reactivestreams.Subscription;
import org.web3j.model.DSCEngine;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;

public class BlockchainEventServiceImplementation implements BlockchainEventService {
    private final DSCEngine dscEngine;
    private final EventParametersRepository eventParametersRepository;

    public BlockchainEventServiceImplementation(DSCEngine dscEngine, EventParametersRepository eventParametersRepository) {
        this.dscEngine = dscEngine;
        this.eventParametersRepository = eventParametersRepository;
    }

    @Override
    public void listenToLiquidateEvent() {
        EthFilter filter = new EthFilter(
                DefaultBlockParameter.valueOf("latest"), // Start from latest block
                DefaultBlockParameter.valueOf("latest"),   // Optional: end block
                dscEngine.getContractAddress()         // Contract address
        );

        // Subscribe to the event
        Subscription subscription = (Subscription) dscEngine.liquidationEventEventFlowable(filter)
                .subscribe(event -> {
                    // Handle the emitted event
                    Event transferEvent = Event.builder()
                            .from(event.user)
                            .amount(event.debtToCover)
                            .liquidator(event.liquidator)
                            .eventType("Liquidation")
                            .build();
                    eventParametersRepository.save(transferEvent);

                });
    }

    @Override
    public void listenTokenDepositEvent() {
        EthFilter filter = new EthFilter(
                DefaultBlockParameter.valueOf("latest"), // Start from latest block
                DefaultBlockParameter.valueOf("latest"),   // Optional: end block
                dscEngine.getContractAddress()         // Contract address
        );

        // Subscribe to the event
        Subscription subscription = (Subscription) dscEngine.collateralDepositedEventFlowable(filter)
                .subscribe(event -> {
                    // Handle the emitted event
                    Event transferEvent = Event.builder()
                            .from(event.user)
                            .amount(event.amount)
                            .collateral(event.token)
                            .eventType("CollateralDepositedEvent")
                            .build();
                    eventParametersRepository.save(transferEvent);

                });

    }

    @Override
    public void listenTokenRedeemEvent() {
        EthFilter filter = new EthFilter(
                DefaultBlockParameter.valueOf("latest"), // Start from latest block
                DefaultBlockParameter.valueOf("latest"),   // Optional: end block
                dscEngine.getContractAddress()         // Contract address
        );

        // Subscribe to the event
        Subscription subscription = (Subscription) dscEngine.collateralRedeemedEventEventFlowable(filter)
                .subscribe(event -> {
                    // Handle the emitted event
                    Event transferEvent = Event.builder()
                            .to(event.redeemTo)
                            .collateral(event.token)
                            .amount(event.amount)
                            .eventType("CollateralRedeemedEvent")
                            .build();
                    eventParametersRepository.save(transferEvent);

                });

    }

    @Override
    public void listenTokenMintEvent() {
        EthFilter filter = new EthFilter(
                DefaultBlockParameter.valueOf("latest"), // Start from latest block
                DefaultBlockParameter.valueOf("latest"),   // Optional: end block
                dscEngine.getContractAddress()         // Contract address
        );

        // Subscribe to the event
        Subscription subscription = (Subscription) dscEngine.tokenMintEventEventFlowable(filter)
                .subscribe(event -> {
                    // Handle the emitted event
                    Event transferEvent = Event.builder()
                            .to(event.to)
                            .amount(event.amount)
                            .eventType("TokenMintEvent")
                            .build();
                    eventParametersRepository.save(transferEvent);

                });

    }

    @Override
    public void listenTokenBurnEvent() {
        EthFilter filter = new EthFilter(
                DefaultBlockParameter.valueOf("latest"), // Start from latest block
                DefaultBlockParameter.valueOf("latest"),   // Optional: end block
                dscEngine.getContractAddress()         // Contract address
        );

        // Subscribe to the event
        Subscription subscription = (Subscription) dscEngine.tokenBurnEventEventFlowable(filter)
                .subscribe(event -> {
                    // Handle the emitted event
                    Event transferEvent = Event.builder()
                            .from(event.from)
                            .amount(event.amount)
                            .eventType("TokenBurnEvent")
                            .build();
                    eventParametersRepository.save(transferEvent);

                });
    }

    @Override
    public void listenCollateralTransferToBankEvent() {
        EthFilter filter = new EthFilter(
                DefaultBlockParameter.valueOf("latest"), // Start from latest block
                DefaultBlockParameter.valueOf("latest"),   // Optional: end block
                dscEngine.getContractAddress()         // Contract address
        );

        // Subscribe to the event
        Subscription subscription = (Subscription) dscEngine.collateralDepositedByBankEventEventFlowable(filter)
                .subscribe(event -> {
                    // Handle the emitted event
                    Event transferEvent = Event.builder()
                            .from(event.user)
                            .amount(event.amount)
                            .collateral(event.token)
                            .eventType("CollateralTransferToBank")
                            .build();
                    eventParametersRepository.save(transferEvent);

                });

    }
}




