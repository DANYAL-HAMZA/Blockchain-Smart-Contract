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
    public void listenToTransferEvent() {
            // Create a filter for the event
            EthFilter filter = new EthFilter(
                    DefaultBlockParameter.valueOf("latest"), // Start from latest block
                    DefaultBlockParameter.valueOf("latest"),   // Optional: end block
                    dscEngine.getContractAddress()         // Contract address
            );

            // Subscribe to the event
            Subscription subscription = (Subscription) dscEngine.tokenTransferEventEventFlowable(filter)
                    .subscribe(event -> {
                        // Handle the emitted event
                        Event transferEvent = Event.builder()
                                .from(event.from)
                                .to(event.to)
                                .amount(event.amount)
                                .eventType("TransferEvent")
                                .build();
                        eventParametersRepository.save(transferEvent);

                    });
        }

    @Override
    public void listenToMintEvent() {
        EthFilter filter = new EthFilter(
                DefaultBlockParameter.valueOf("latest"), // Start from latest block
                DefaultBlockParameter.valueOf("latest"),   // Optional: end block
                dscEngine.getContractAddress()         // Contract address
        );

        // Subscribe to the event
        Subscription subscription = (Subscription) dscEngine.bankTokenMintEventEventFlowable(filter)
                .subscribe(event -> {
                    // Handle the emitted event
                    Event mintEvent = Event.builder()
                            .to(event.to)
                            .amount(event.amount)
                            .eventType("MintEvent")
                            .build();
                    eventParametersRepository.save(mintEvent);

                });
    }

    @Override
    public void listenToBurnEvent() {
        EthFilter filter = new EthFilter(
                DefaultBlockParameter.valueOf("latest"), // Start from latest block
                DefaultBlockParameter.valueOf("latest"),   // Optional: end block
                dscEngine.getContractAddress()         // Contract address
        );

        // Subscribe to the event
        Subscription subscription = (Subscription) dscEngine.tokenBurnEventEventFlowable(filter)
                .subscribe(event -> {
                    // Handle the emitted event
                    Event burnEvent = Event.builder()
                            .to(event.from)
                            .amount(event.amount)
                            .eventType("BurnEvent")
                            .build();
                    eventParametersRepository.save(burnEvent);

                });
    }

}




