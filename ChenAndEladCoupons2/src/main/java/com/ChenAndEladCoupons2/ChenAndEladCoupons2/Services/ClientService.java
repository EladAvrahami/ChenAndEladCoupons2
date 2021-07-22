package com.ChenAndEladCoupons2.ChenAndEladCoupons2.Services;

/**
 * an interface that contains the login method used by the Services implementing it
 */
public interface ClientService {

    boolean login(String email, String password);
}
