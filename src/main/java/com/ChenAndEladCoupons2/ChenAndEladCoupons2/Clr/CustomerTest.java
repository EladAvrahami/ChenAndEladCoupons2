package com.ChenAndEladCoupons2.ChenAndEladCoupons2.Clr;

import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.Coupon;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.Customer;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Repositories.Companyrepo;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Repositories.Couponrepo;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Repositories.Customerrepo;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Services.*;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.enums.Category;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.enums.ClientType;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.util.MyUtil;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.util.TablePrinter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.sql.Date;


@Component
@RequiredArgsConstructor
@Order(3)
/**
 * a class that runs all the Customer logics by the services (implements Clr for it)
 */
public class CustomerTest implements CommandLineRunner {

    @Autowired
    Couponrepo couponrepo;
    @Autowired
    Companyrepo companyrepo;
    @Autowired
    Customerrepo customerrepo;
    @Autowired
    LoginManager loginManager;
    @Autowired
    AdminService adminService;
    @Autowired
    CompanyService companyService;


    public void run(String... args) throws Exception {
        Coupon coupon = Coupon.builder ()
                .amount (7)
                .companyID (2)
                .description ("stam coupon")
                .endDate (Date.valueOf ("2022-05-05"))
                .image ("image")
                .category (Category.clothes)
                .price (34.00)
                .startDate (Date.valueOf ("2021-05-05"))
                .title ("very good coupon")
                .build ();


        Coupon coupon1 = Coupon.builder ()
                .title ("excelent coupon")
                .price (134.00)
                .image ("image")
                .companyID (1)
                .category (Category.movies)
                .startDate (Date.valueOf ("2021-09-09"))
                .endDate (Date.valueOf ("2021-09-09"))
                .amount (0)
                .description ("regular coupon")
                .build ();

        Coupon coupon5 = Coupon.builder ()
                .title ("air france coupon")
                .price (24.00)
                .companyID (4)
                .startDate (Date.valueOf ("2021-06-07"))
                .endDate (Date.valueOf ("2021-05-07"))
                .amount (4)
                .image ("image5")
                .description ("desc")
                .category (Category.flights)
                .build ();


        Customer customer = Customer.builder ()
                .email ("mail@hotmail.com")
                .password ("2323")
                .firstName ("michelle")
                .lastName ("rabi")
                .build ();

        Customer customer2 = Customer.builder ()
                .email ("ourFavoritCustomer2@gmail.com")
                .password ("8765")
                .firstName ("itay")
                .lastName ("manoach")
                .build ();
        //***********************************************************************************************
        //CUSTOMER SERVICE TEST --------
        System.out.println ("***********************CUSTOMER SERVICE TEST *******************************");

        //LOGIN  NOT SUCCESSFUL (BY EMAIL)
        System.out.println ("this is a demonstration of a not successful login of a customer");
        try {
            CustomerService customerService = (CustomerService) loginManager.login ("wrongEmail@walla.com", "8765", ClientType.customer);
        } catch (Exception err) {
            System.out.println ("Cannot login - check your email and password again");
        }
        MyUtil.printRow ();

        //COMPANY LOGIN FAILED (BY PASSWORD)
        System.out.println ("this is a demonstration of a  not successful login of an customer - by password");
        try {
            CustomerService customerService = (CustomerService) loginManager.login ("ourFavoritCustomer2@gmail.com", "5432", ClientType.customer);
        } catch (Exception err) {
            System.out.println ("Cannot login - check your email and password again");
        }
        MyUtil.printRow ();


        //LOGIN SUCCESSFUL
        System.out.println ("this is a demonstration of a successful login of a customer");
        CustomerService customerService = (CustomerService) loginManager.login ("ourFavoritCustomer2@gmail.com", "8765", ClientType.customer);
        MyUtil.printRow ();

        //COUPON PURCHASE - SUCCESSFUL
        System.out.println ("this is a demonstration of a customer purchasing a coupon");
        adminService.addCustomer (customer);
        companyService.addCoupon (coupon);
        companyService.addCoupon (coupon1);
        customerService.purchaseCoupon (coupon.getId());
        MyUtil.printRow ();

        //COUPON PURCHASE -  NOT SUCCESSFUL (COUPON WAS ALREADY PURCHASED BY CUSTOMER)
        System.out.println ("this is a demonstration of a customer not succeeding to purchase a coupon - already has the coupon");
        customerService.purchaseCoupon (coupon.getId());
        MyUtil.printRow ();

        //COUPON PURCHASE -  NOT SUCCESSFUL (NOT ENOUGH IN STOCK)
        System.out.println ("this is a demonstration of a customer not succeeding to purchase a coupon - out of stock");
        coupon1.setAmount (0);
        companyService.updateCoupon (coupon1);
        customerService.purchaseCoupon (coupon1.getId());
        MyUtil.printRow ();

        //COUPON PURCHASE -  NOT SUCCESSFUL (THE COUPON HAS EXPIRED)
        System.out.println ("this is a demonstration of a customer not succeeding to purchase a coupon - coupon has expired");
        //companyFacade.deleteCoupon((couponDBDAO.getOneCouponByCoupon(coupon)).getId());
        coupon1.setAmount (4);
        coupon1.setEndDate ((Date.valueOf ("2020-06-07")));
        companyService.updateCoupon (coupon1);
        customerService.purchaseCoupon (coupon1.getId());
        MyUtil.printRow ();


        //GET ALL THE COUPONS FOR THE CUSTOMER
        System.out.println ("this is a demonstration of a customer's coupon list");
        coupon1.setEndDate ((Date.valueOf ("2022-06-07")));
        companyService.updateCoupon (coupon1);
        customerService.purchaseCoupon (coupon1.getId());
        customerService.getAllCouponsPerCustomer();
        MyUtil.printRow ();

        //GET ALL THE COUPONS FOR THE CUSTOMER BY CATEGORY
        System.out.println ("this is a demonstration of a customer's coupon list by category");
        System.out.println ("BY CLOTHES:");
        customerService.getAllCouponsByCategoryPerCustomer (Category.clothes);
        System.out.println ("BY MOVIES");
        customerService.getAllCouponsByCategoryPerCustomer (Category.movies);
        MyUtil.printRow ();

//        //GET ALL THE COUPONS FOR THE CUSTOMER BY MAX PRICE
//        System.out.println ("these are the coupons that costs below 100nis");
//        customerService.getAllCouponsByMaxPricePerCustomer (100);
//        System.out.println ("these are the coupons that costs below 150nis");
//        customerService.getAllCouponsByMaxPricePerCustomer (150);
//        MyUtil.printRow ();

        //GET DETAILS FOR COSTUMER
        System.out.println ("this is a demonstration of a getting the customer's details");
        TablePrinter.print (customerrepo.getOne (customerService.getCustomerLoggedIn ().getId()));
        MyUtil.printRow ();

        //DELETE COUPON PURCHASE
        System.out.println ("this is a demonstration of a customer deleting a coupon purchase");
        customerService.deleteCouponPurchase (coupon1.getId());
        MyUtil.printRow ();
        customerService.purchaseCoupon (coupon1.getId());





    }


}


