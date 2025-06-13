package org.example.Service;

import org.springframework.stereotype.Service;

@Service
public interface BlockchainEventService {

    void listenToTransferEvent();
    void listenToMintEvent();
    void listenToBurnEvent();


}
