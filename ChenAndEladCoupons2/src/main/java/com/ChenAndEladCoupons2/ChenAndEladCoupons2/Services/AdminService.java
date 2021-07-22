package com.ChenAndEladCoupons2.ChenAndEladCoupons2.Services;

import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.Company;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.Coupon;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.Customer;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Exceptions.CouponCustomExceptions;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Repositories.Companyrepo;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Repositories.Customerrepo;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.util.TablePrinter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
/**
 * a Service that allows the user to send orders to the DB as a facade
 */
public class AdminService implements ClientService {


    @Autowired
    Companyrepo companyrepo;
    @Autowired
    Customerrepo customerrepo;

    private final String adminEmail = "admin@admin.com";
    private final String adminPass = "admin";

    /**
     * this method add a new company to our coupons System if doesn't exist already.
     * @param company is a new object that we going create and add to our list of companies
     * @return boolean answer true if the Company was added ,or false.
     */
    public boolean addCompany(Company company)  {

        //double check so that we won't add a double company by name or ID
        Company retObject = null;
        try {
            if (!companyrepo.existsById (company.getId()) &&
                    companyrepo.findByNameOrEmail (company.getName (), company.getEmail ()) == null) {
                retObject = companyrepo.save (company);
                companyrepo.save (company);
                System.out.println ("new company number " + company.getId() + " was added");
                retObject.getId(); // get id here
                return true;
            } else {
                System.out.println ("cant add company since it already exists");
            }
        } catch (Exception e) {
            System.out.println ("AdminService.addCompany: + " + e.getMessage ());
        }
        return false;
    }

    /**
     *this method updates company's details
     * @param companyID the id of specific company by which we identify the Company
     * @throws CouponCustomExceptions an exception that operates when an error occures
     */
    public void updateCompany2(int companyID) throws CouponCustomExceptions {
        if (companyrepo.existsById (companyID)) {
            companyrepo.save (companyrepo.findCompanyById (companyID));
        }
        throw new CouponCustomExceptions ("company not found");

    }

    /**
     * this method updates company's details
     * @param company the company that we will update
     * @throws CouponCustomExceptions an exception that operates when an error occures
     */
    public void updateCompany(Company company) throws CouponCustomExceptions {

        if (companyrepo.existsById (company.getId())) {
            companyrepo.save (company);
            System.out.println ("company number : " + company.getId() + " was updated");
        } else {

            System.out.println ("company number : " + company.getId() + " cannot be updated or it does not exist");
        }
    }

    /**
     * this method deletes a Company
     * @param companyID the id of specific company by which we identify the Company
     * @return boolean answer true if the Company was deleted ,or false.
     */
    public boolean deleteCompany(int companyID) {
        try {
            if (companyrepo.existsById (companyID)) {
                companyrepo.delete (companyrepo.getOne (companyID));
                System.out.println ("company number " + companyID + " was deleted");
                return true;
            }
            throw new CouponCustomExceptions ("company id does not exist");
        } catch (Exception err) {
            System.out.println (err.getMessage ());
        }
        return false;

    }

    /**
     * a method that prints and gets all the Companies in the table
     * @return A list of companies from the Company table
     */
    public List<Company> getAllCompanies() {
        System.out.println ("======================================================================");
        for (Company company : companyrepo.findAll ()) {
            TablePrinter.print (company);
            System.out.println ("======================================================================");
            System.out.println ("this is the coupons list for company number: " + company.getId());
            for (Coupon coupon : company.getCoupons ()) {
                int num = 1;
                System.out.println ("coupon number " + num + "\n" + coupon);
                num += 1;
                System.out.println ("-------------------------------------------------------------------");
            }
        }
        return companyrepo.findAll ();
    }

    /**
     * a method that prints and gets one Company from the Company table
     * @param companyid the id of specific company by which we identify the Company
     * @return a Company Object
     */
    public Company getCompanyById(int companyid) {
        TablePrinter.print (companyrepo.findCompanyById (companyid));
        System.out.println ("the company coupons: ");
        TablePrinter.print (companyrepo.findCompanyById (companyid).getCoupons ());
        //for (Coupon coupon : companyrepo.findCompanyById (companyid).getCoupons ()) {
           // int num = 1;
            //System.out.println ("coupon number " + num + "\n");
            //num += 1;
            //System.out.println ("-------------------------------------------------------------------");
        return companyrepo.findCompanyById (companyid);
    }

    /**
     * a method that prints and gets one Company from the Company table (by Company Object)
     * @param company the company that we get
     *  @return a Company Object
     */
    public Company getCompany(Company company) {
        System.out.println (companyrepo.findCompanyById (company.getId()));
        System.out.println ("-----------------------------------------------------------------------");
        for (Coupon coupon : companyrepo.findCompanyById (company.getId()).getCoupons ()) {
            int num = 1;
            System.out.println ("coupon number " + num + "\n" + coupon);
            num += 1;
            System.out.println ("-------------------------------------------------------------------");
        }
        return companyrepo.findCompanyById (company.getId());
    }

    /**
     * a method that adds a Customer Object to the Customer table in the DB
     * @param customer the Customer we want to add
     * @return  boolean answer true if the customer was added ,or false.
     */
    public boolean addCustomer(Customer customer) {
        Customer retObject = null;
        try {
            if (!customerrepo.existsById (customer.getId()) &&
                    (customerrepo.findByFirstName (customer.getFirstName ()) == null) &&
                    (customerrepo.findByEmail (customer.getEmail ()) == null)) {
                retObject = customerrepo.save (customer);
                customerrepo.save (customer);
                retObject.getId();
                System.out.println ("new Customer number " + customer.getId() + " was added");
                return true;
            } else {
                System.out.println ("cant add customer");
                return false;
            }
        } catch (Exception err) {
            System.out.println (err.getMessage ());
        }
        return false;

    }

    /**
     * this method updates customer's details
     * @param customer the Customer Object that we want to update
     * @throws CouponCustomExceptions an exception that operates when an error occures
     */
    public void updateCustomer(Customer customer) throws CouponCustomExceptions {
        if (customerrepo.existsById (customer.getId())) {
            customerrepo.save (customer);
            System.out.println ("customer number : " + customer.getId() + " was updated");
        } else {
            System.out.println ("company number : " + customer.getId() + " cannot be updated or it does not exist");
        }
    }

    /**
     * a method that deletes a Customer from the Customer table in the DB
     * @param customerID the id of specific Customer by which we identify the Customer
     * @return boolean answer true if the customer was deleted ,or false.
     */
    public boolean deleteCustomer(int customerID) {
        try {
            if (customerrepo.existsById (customerID)) {
                customerrepo.delete (customerrepo.getOne (customerID));
                System.out.println ("customer number " + customerID + " was deleted");
                return true;
            }
            throw new CouponCustomExceptions ("customer id does not exist");
        } catch (Exception err) {
            System.out.println (err.getMessage ());
        }
        return false;
    }

    /**
     * a method that gets all the customers from the Customers table in the DB
     * @return a list of Customers
     */
    public List<Customer> getAllCustomers() {
        for (Customer customer : customerrepo.findAll ()) {
            TablePrinter.print (customer);
            for (Coupon coupon : customer.getCoupons ()) {
                TablePrinter.print (coupon);
            }
        }
        return customerrepo.findAll ();
    }

    /**
     * a method that gets one Customer from the Customers Table in the DB
     * @param customerID the id of specific Customer by which we identify the Customer
     * @return a Customer Object
     */
    public Customer getOneCustomer(int customerID) {
        try {
            TablePrinter.print (customerrepo.findById (customerID));
            System.out.println ("the customer's coupons are: ");
            TablePrinter.print (customerrepo.findById (customerID).get ().getCoupons ());
            //for (Coupon coupon2 : customerrepo.findById (customerID).get ().getCoupons ()) {
              //  System.out.println (coupon2);
           // }
        } catch (Exception err) {
            System.out.println (err.getMessage ());
        }
        return customerrepo.getOne (customerID);

    }


    @Override
    /**
     * a methods that checks the validity of the admin's email and password and return a boolean answer
     */
    public boolean login(String email, String password) {
        try {
            if (email.equalsIgnoreCase (adminEmail) && password.equals (adminPass)) {
                return true;
            }
        } catch (Exception e) {
            System.out.println ("AdminService.Login " + e.getMessage ());
        }
        return false;
    }


}
