package com.ChenAndEladCoupons2.ChenAndEladCoupons2.Clr;

import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.Company;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.Coupon;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Repositories.Companyrepo;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Repositories.Couponrepo;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Repositories.Customerrepo;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Services.AdminService;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Services.CompanyService;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Services.LoginManager;
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
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Order(2)
/**
 * a class that runs all the Company logics by the services (implements Clr for it)
 */
public class CompanyTest implements CommandLineRunner {


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



    public void run(String... args) throws Exception {

        Coupon coupon = Coupon.builder ()
                .amount (7)
                .companyID (1)
                .description ("great coupon")
                .endDate (Date.valueOf ("2022-05-05"))
                .image ("image5")
                .category (Category.clothes)
                .price (134.00)
                .startDate (Date.valueOf ("2021-05-05"))
                .title ("the title")
                .build ();


        Coupon coupon1 = Coupon.builder ()
                .title ("absolutely great")
                .price (134.00)
                .image ("image6")
                .companyID (1)
                .category (Category.movies)
                .startDate (Date.valueOf ("2021-06-07"))
                .endDate (Date.valueOf ("2021-05-07"))
                .amount (8)
                .description ("loves animals")
                .build ();


        Coupon coupon4 = Coupon.builder ()
                .title ("video game coupon")
                .price (255.00)
                .companyID (1)
                .startDate (Date.valueOf ("2021-06-07"))
                .endDate (Date.valueOf ("2021-05-07"))
                .amount (7)
                .image ("image7")
                .description ("quiet at noon")
                .category (Category.flights)
                .build ();

        Coupon coupon5 = Coupon.builder ()
                .title ("air france coupon")
                .price (24.00)
                .companyID (4)
                .startDate (Date.valueOf ("2021-06-07"))
                .endDate (Date.valueOf ("2021-05-07"))
                .amount (4)
                .image ("image8")
                .description ("desc")
                .category (Category.flights)
                .build ();


        Company company = Company.builder ()
                .name ("our successful company")
                .email ("chen@gmail.com")
                .password ("1234")
                .coupons (Arrays.asList (coupon5))
                .build ();

        Company company2 = Company.builder ()
                .name ("second company")
                .email ("dudu@gmail.com")
                .password ("5432")
                .coupons (Arrays.asList (coupon))
                .build ();

        Company company3 = Company.builder ()
                .name ("third company")
                .email ("ilan@gmail.com")
                .password ("8765")
                .coupons (Arrays.asList (coupon5))
                .build ();

        //***********************************************************************************************

        System.out.println ("***********************COMPANY SERVICE TEST *******************************");

        //COMPANY LOGIN FAILED (BY PASSWORD)
        System.out.println ("this is a demonstration of a  not successful login of an company - by password");
        try {
            CompanyService companyService = (CompanyService) loginManager.login ("dudu@gmail.com", "wrong password", ClientType.company);
        } catch (Exception err) {
            System.out.println ("Cannot login - check your email and password again");
        }
        MyUtil.printRow ();

        //COMPANY LOGIN FAILED (BY EMAIL)
        System.out.println ("this is a demonstration of a  not successful login of an company - by email");
        try {
            CompanyService companyService = (CompanyService) loginManager.login ("wrongEmail@walla.com", "5432", ClientType.company);
        } catch (Exception err) {
            System.out.println ("Cannot login - check your email and password again");
        }
        MyUtil.printRow ();

        //LOGIN SUCCESSFUL
        System.out.println ("this is a demonstration of a successful login of a company");
        CompanyService companyService = (CompanyService) loginManager.login ("dudu@gmail.com", "5432", ClientType.company);
        MyUtil.printRow ();





        //ADD NEW COUPON SUCCESSFUL
        System.out.println ("this is a demonstration of an company adding a new coupon");
        companyService.addCoupon (coupon5);
        MyUtil.printRow ();

        //COUPON UPDATE SUCCESSFULLY
        System.out.println ("this is a demonstration of an company updating a coupon successfully");
        coupon4.setAmount (8);
        coupon4.setPrice (55);
        companyService.updateCoupon (coupon5);
        TablePrinter.print (coupon5);
        MyUtil.printRow ();

        //COUPON UPDATE FAILED (COUPON_ID)
        System.out.println ("this is a demonstration of an company updating a coupon id not successfully");
        //there is no coupon.setid because we made the id updatable = false
        MyUtil.printRow ();


        //COUPON UPDATE FAILED (COMPANY_ID)
        System.out.println ("this is a demonstration of an company updating a coupon companyID not successfully");
        System.out.println ("this is the coupon before the change");
        TablePrinter.print (couponrepo.findById (coupon5.getId()));
        coupon4.setCompanyID (5);
        companyService.updateCoupon (coupon5);
        System.out.println ("this is the coupon after the change: you can see the companyID stayed the same");
        TablePrinter.print (couponrepo.findById (coupon5.getId()));
        MyUtil.printRow ();

        //GET ALL COMPANY'S COUPONS
        adminService.addCompany (company);
        System.out.println ("this is a demonstration of an company presenting all company's coupons");
        companyService.getAllCouponsPerCompany ();
        MyUtil.printRow ();

        //GET ALL COMPANIES COUPONS BY CATEGORY
        System.out.println ("this is a demonstration of an company presenting all company's coupons by category");
        System.out.println ("these are the coupons for movies:");
        companyService.getAllCouponsByCategoryPerCompany (company.getId(), Category.movies);
        System.out.println ("these are the coupons for clothes:");
        companyService.getAllCouponsByCategoryPerCompany (company.getId(), Category.clothes);
        System.out.println ("these are the coupons for flights:");
        companyService.getAllCouponsByCategoryPerCompany (company.getId(), Category.flights);
        MyUtil.printRow ();

        //GET ALL COMPANIES COUPONS BELOW MAX PRICE
        System.out.println ("this is a demonstration of an company presenting all company's coupons below max price");
        System.out.println ("these are the coupons that costs below 100nis");
        companyService.getAllCouponsByPricePerCompany (company.getId(), 0.0, 100.0);
        System.out.println ("these are the coupons that costs below 150nis");
        companyService.getAllCouponsByPricePerCompany (company.getId(), 0.0, 150.0);
        MyUtil.printRow ();

        //GET ALL COMPANY'S DETAILS BY ID
        System.out.println ("this is a demonstration of a company details show");
        adminService.getCompanyById (company.getId());
        MyUtil.printRow ();

        //DELETE COUPON (AND ALL OF HIS PURCHASES)
        System.out.println ("this is a demonstration of an company deleting a coupon successfully");
        //companyService.deleteCoupon (coupon.getId ());
        companyService.deleteCoupon (coupon5.getId());
        MyUtil.printRow ();
    }


}
