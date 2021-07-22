package com.ChenAndEladCoupons2.ChenAndEladCoupons2.Clr;

import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.Company;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.Coupon;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.Customer;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Exceptions.CouponCustomExceptions;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Exceptions.LoginException;
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
@Order(1)
/**
 * a class that runs all the Admin logics by the services (implements Clr for it)
 */
public class AdminTest implements CommandLineRunner {
    @Autowired
    Couponrepo couponrepo;
    @Autowired
    Companyrepo companyrepo;
    @Autowired
    Customerrepo customerrepo;
    @Autowired
    LoginManager loginManager;
    @Autowired
    CompanyService companyService;
    @Autowired
    CustomerService customerService;


    public void run(String... args) throws CouponCustomExceptions, LoginException {

        Coupon coupon = Coupon.builder ()
                .amount (7)
                .companyID (4)
                .description ("quite good coupon")
                .endDate (Date.valueOf ("2022-05-05"))
                .image ("image1")
                .category (Category.clothes)
                .price (34.00)
                .startDate (Date.valueOf ("2021-05-05"))
                .title ("titled")
                .build ();


        Coupon coupon1 = Coupon.builder ()
                .title ("my unique coupon")
                .price (434.00)
                .image ("image2")
                .companyID (4)
                .category (Category.movies)
                .startDate (Date.valueOf ("2021-06-07"))
                .endDate (Date.valueOf ("2022-05-07"))
                .amount (8)
                .description ("described")
                .build ();

        Coupon coupon3 = Coupon.builder ()
                .description ("one in a lifetime")
                .amount (4)
                .startDate (Date.valueOf ("2021-06-07"))
                .endDate (Date.valueOf ("2022-05-07"))
                .companyID (4)
                .category (Category.vacation)
                .image ("image")
                .price (113.44)
                .title ("title")
                .build ();

        Coupon coupon4 = Coupon.builder ()
                .title ("video game coupon")
                .price (255.00)
                .companyID (4)
                .startDate (Date.valueOf ("2021-06-07"))
                .endDate (Date.valueOf ("2050-05-07"))
                .amount (7)
                .image ("image4")
                .description ("first hand")
                .category (Category.clothes)
                .build ();

        Company company = Company.builder ()
                .name ("our successful company")
                .email ("chen@gmail.com")
                .password ("1234")
                .coupon (coupon4)
                .build ();

        Company company2 = Company.builder ()
                .name ("second company")
                .email ("dudu@gmail.com")
                .password ("5432")
                .coupon (coupon)
                .build ();

        Company company3 = Company.builder ()
                .name ("third company")
                .email ("ilan@gmail.com")
                .password ("8765")
                .coupon (coupon3)
                .build ();

        Company sameEmailMockCompany = Company.builder ()
                .name ("just a name")
                .email ("chen@gmail.com")
                .password ("1234")
                .coupon (coupon4)
                .build ();

        Company sameNameMockCompany = Company.builder ()
                .name ("our successful company")
                .email ("wonderful@gmail.com")
                .password ("1234")
                .coupon(coupon4)
                .build ();


        Customer customer = Customer.builder ()
                .email ("ourFavoritCustomer@gmail.com")
                .password ("2323")
                .firstName ("aviad")
                .lastName ("davidovich")
                .build ();

        Customer customer2 = Customer.builder ()
                .email ("ourFavoritCustomer2@gmail.com")
                .password ("8765")
                .firstName ("itay")
                .lastName ("manoach")
                .build ();

        Customer sameEmailMockCustomer = Customer.builder ()
                .email ("ourFavoritCustomer@gmail.com")
                .password ("2323")
                .firstName ("mickey")
                .lastName ("elazar")
                .build ();
        //**************************************admin test**********************************************
        System.out.println ("***********************ADMIN SERVICE TEST *******************************");


        //LOGIN FAILED
        System.out.println ("this is a demonstration of a non! successful login of an admin");
        try {
            AdminService adminService = (AdminService) loginManager.login ("admin1@admin.com", "admin", ClientType.admin);
        } catch (Exception err) {
            System.out.println ("Cannot login");
        }
        MyUtil.printRow ();


        //LOGIN SUCCESSFUL
        System.out.println ("this is a demonstration of a successful login of an admin");
        MyUtil.printRow ();
        AdminService adminService = (AdminService) loginManager.login ("admin@admin.com", "admin", ClientType.admin);
        MyUtil.printRow ();


        //ADD NEW COMPANY SUCCESSFUL
        System.out.println ("this is a demonstration of an admin adding new company");

        adminService.addCompany (company);
        adminService.addCompany (company2);
        adminService.addCompany (company3);
        adminService.addCustomer (customer2);
        //companyService.addCoupon (coupon);
        //companyService.addCoupon (coupon1);
        //companyService.addCoupon (coupon3);
        //companyService.addCoupon (coupon4);
        MyUtil.printRow ();


        //ADD COMPANY - FAILED (IDENTICAL COMPANY NAME)
        System.out.println ("this is a demonstration of an admin failed to add a new company because a company with this name already exists");
        adminService.addCompany (sameNameMockCompany);


        //ADD COMPANY - FAILED (IDENTICAL EMAIL)
        System.out.println ("this is a demonstration of an admin failed to add a new company because a company with this email already exists");
        adminService.addCompany (sameEmailMockCompany);
        MyUtil.printRow ();

        // COMPANY UPDATE - SUCCESSFUL
        System.out.println (" this is a demonstration of an admin updating a company's email and password - successfully");
        company.setEmail ("test@walla.com");
        company.setPassword ("test new password");
        adminService.updateCompany (company);
        TablePrinter.print (company);
        MyUtil.printRow ();


        // COMPANY UPDATE -  NOT SUCCESSFUL (BY ID)
        System.out.println (" this is a demonstration of an admin trying to update a company's id -  not successfully");
        company.setId(6);
        adminService.updateCompany (company);
        company.setId(1);
        MyUtil.printRow ();

        // COMPANY UPDATE -  NOT SUCCESSFUL (BY NAME)
        System.out.println (" this is a demonstration of an admin trying to update a company's name -  not successfully");
        System.out.println ("this is the company before the change");
        adminService.getCompanyById (company.getId());
        company.setName ("name test");
        adminService.updateCompany (company);
        System.out.println ("this is the company after the change: you can see the name stayed the same");
        adminService.getCompanyById (company.getId());
        MyUtil.printRow ();

        //GET ALL COMPANIES - SUCCESSFUL
        System.out.println (" this is a demonstration of an admin presenting all of the companies successfully: ");
        adminService.getAllCompanies ();
        MyUtil.printRow ();

        //GET ONE COMPANY BY COMPANYID
        System.out.println (" this is a demonstration of an admin presenting one of the companies successfully: ");
        adminService.getCompanyById (company.getId());
        MyUtil.printRow ();

        //DELETE COMPANY - SUCCESSFUL
        System.out.println (" this is a demonstration of an admin deleting a company successfully: ");
        adminService.deleteCompany (company.getId());
        MyUtil.printRow ();


        //ADD ONE CUSTOMER - SUCCESSFUL
        System.out.println (" this is a demonstration of an admin adding a customer successfully: ");
        adminService.addCustomer (customer);
        MyUtil.printRow ();

        //ADD ONE CUSTOMER -  NOT SUCCESSFUL (BY EMAIL)
        System.out.println (" this is a demonstration of an admin adding a customer with the same email not successfully: ");
        adminService.addCustomer (sameEmailMockCustomer);
        MyUtil.printRow ();

        //UPDATE EXIST CUSTOMER - SUCCESSFUL
        System.out.println (" this is a demonstration of an admin updating a customer successfully: ");
        customer.setFirstName ("meir");
        customer.setLastName ("cohen");
        adminService.updateCustomer (customer);
        MyUtil.printRow ();

        System.out.println(customer);

        // CUSTOMER UPDATE -  NOT SUCCESSFUL (BY NAME)
        System.out.println (" this is a demonstration of an admin trying to update a customer's name -  not successfully");
        System.out.println ("this is the customer before the change");
        adminService.getOneCustomer (customer.getId());
        company.setName ("name test");
        adminService.updateCustomer (customer);
        System.out.println ("this is the customer after the change: you can see the name stayed the same");
        adminService.getOneCustomer (customer.getId());
        MyUtil.printRow ();

        //GET ONE CUSTOMER (BY ID)
        System.out.println (" this is a demonstration of an admin presenting one customer successfully: ");
        adminService.getOneCustomer (customer.getId());
        MyUtil.printRow ();

        //GET ALL CUSTOMERS
        System.out.println (" this is a demonstration of an admin presenting all customers successfully: ");
        adminService.getAllCustomers ();
        MyUtil.printRow ();

        //DELETE ONE CUSTOMER - SUCCESSFUL
        System.out.println (" this is a demonstration of an admin deleting a customer  successfully: ");
        //adminService.deleteCustomer (customer.getId ());
        MyUtil.printRow ();


    }
}







