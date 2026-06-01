package com.farmmarket.biddingservice.exception;

public class BidAlreadyAcceptedException extends RuntimeException {
    public BidAlreadyAcceptedException(String message) {
        super(message);
    }
}
