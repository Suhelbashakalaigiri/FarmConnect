package com.farmmarket.biddingservice.exception;

public class BidNotAllowedException extends RuntimeException {
    public BidNotAllowedException(String message) {
        super(message);
    }
}
