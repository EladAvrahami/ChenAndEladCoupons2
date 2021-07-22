package com.ChenAndEladCoupons2.ChenAndEladCoupons2.Services;

import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.Company;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.Coupon;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.Customer;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Exceptions.CouponCustomExceptions;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Exceptions.CouponException;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Repositories.Couponrepo;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Repositories.Customerrepo;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.enums.Category;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.util.TablePrinter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
/**
 * a Service that allows the user to send orders to the DB as a facade
 */
public class CustomerService implements ClientService {
    @Autowired
    Customerrepo customerrepo;
    @Autowired
    Couponrepo couponrepo;

    Customer customerLoggedIn;



    public Customer getCustomerLoggedIn() {
        return customerLoggedIn;
    }

    /**
     * a method that prints and gets one Company from the Company table (by Company Object)
     * @param customerID the id of specific Customer by which we identify the Customer
     * @return a Customer Object
     */
    public Customer getOneCustomer(int customerID) {
        return customerrepo.getOne (customerID);
    }

    public boolean purchaseCoupon(int couponID) throws CouponCustomExceptions {
        try {
            Customer customerLI = (customerrepo.getOne (customerLoggedIn.getId()));
            Coupon couponToPurchase = couponrepo.getOneById (couponID);
            //cant purchase the same coupon more than once
            if (customerLI.getCoupons ().contains (couponToPurchase)) {
                throw new CouponException ("the customer already has this coupon... find something else");
            }
            if (!couponrepo.existsById (couponID)) {
                throw new CouponCustomExceptions ("Coupon not found");
            }
            if (couponToPurchase.getAmount () == 0) {
                throw new CouponException ("not enough in stock");
            }
            if (couponToPurchase.getEndDate ().before (new Date ())) {
                throw new CouponException ("the coupon has expired");
            } else {
                List<Coupon> coupons = (customerrepo.getOne (customerLoggedIn.getId())).getCoupons ();
                coupons.add (couponrepo.getOneById (couponID));
                customerLI.setCoupons (coupons);
                customerrepo.saveAndFlush (customerLI);
                couponToPurchase.setAmount (couponToPurchase.getAmount () - 1);
                couponrepo.saveAndFlush (couponToPurchase);
                System.out.println ("coupon number :" + couponID + " was purchased by :" + "customer number " + customerLoggedIn.getId());
                return true;
            }

        } catch (Exception err) {
            System.out.println ("CustomerService.purchaseCoupon: " + err.getMessage ());
        }
        return false;
    }

    /**
     * a method that deletes a coupon purchase and removes the Coupon from the Customer's Coupon list
     * @param couponID the id of specific Coupon by which we identify the Coupon
     * @return boolean answer true if the coupon was deleted ,or false.
     */
    public boolean deleteCouponPurchase(int couponID) {
        Customer customerLI = (customerrepo.getOne (customerLoggedIn.getId()));
        Coupon couponToDelete = couponrepo.getOneById (couponID);
        for (Coupon coupon : customerrepo.findCustomerById (customerLoggedIn.getId()).getCoupons ()) {
            if (coupon.getId() == couponID) {
            }
            List<Coupon> coupons = (customerrepo.getOne (customerLoggedIn.getId())).getCoupons ();
            coupons.remove (couponrepo.getOneById (couponID));
            customerLI.setCoupons (coupons);
            customerrepo.saveAndFlush (customerLI);
            couponToDelete.setAmount (couponToDelete.getAmount () + 1);
            couponrepo.saveAndFlush (couponToDelete);
            System.out.println ("coupon number :" + couponID + " was deleted by :" + "customer number " + customerLoggedIn.getId());
            return true;
        }
        return false;
    }

    /**
     * a method that get and prints the list of coupons of a Customer that logged in
     * @return a list of Coupons
     */
    public List<Coupon> getAllCouponsPerCustomer() {
        Customer customerLI = (customerrepo.getOne (customerLoggedIn.getId()));
        try {
            TablePrinter.print ( customerLI.getCoupons ());
            //for (Coupon coupon : customerLI.getCoupons ()) {
            //    System.out.println (coupon);
           // }
        } catch (Exception err) {
            System.out.println (err.getMessage ());

        }
        return customerLI.getCoupons ();
    }

    public List<Coupon> getOneCustomerCoupons(int id) {
        Customer customer = (customerrepo.getOne (id));
        try {
            TablePrinter.print (customer.getCoupons ());
            //for (Coupon coupon2 : companyrepo.findCompanyById (companyID).getCoupons ()) {
            // TablePrinter.print (coupon2);
            //}

            return customer.getCoupons ();

        } catch (Exception err) {
            System.out.println (err.getMessage ());

        }
        return null;

    }


    /**
     * a method that get and prints the list of coupons of a Customer filtered by Category
     * @param category  the enum item that we want to filter the Coupon list by.
     * @return a list of Coupons
     */
    public List<Coupon> getAllCouponsByCategoryPerCustomer(Category category) {
        Customer customerLI = (customerrepo.getOne (customerLoggedIn.getId()));
        try {
            for (Coupon coupon : customerLI.getCoupons ()) {
               if (coupon.getCategory () == category)
                    TablePrinter.print (coupon);
           }
            return customerLI.getCoupons().stream().filter(coupon -> coupon.getCategory() == category).collect(Collectors.toList());
        } catch (Exception err) {
            System.out.println (err.getMessage ());
        }
        return null;

    }

    /**
     * a method that gets and prints all the Coupons that belongs to the Customer by price
     * @param maxPrice the high price that we filter the list by
     * @return a list of Coupons
     */
    public List<Coupon> getAllCouponsByMaxPricePerCustomer(double maxPrice) {
        Customer customerLI = (customerrepo.getOne (customerLoggedIn.getId()));
        try {
            for (Coupon coupon : customerLI.getCoupons ()) {
                if (coupon.getPrice () <= maxPrice)
                  TablePrinter.print (coupon);
          }
            return customerLI.getCoupons ();
        } catch (Exception err) {
            System.out.println (err.getMessage ());
        }
        return null;
    }


    @Override
    /**
     * a methods that checks the validity of the Customer's email and password and return a boolean answer
     */
    public boolean login(String email, String password) {
        try {
            if (!((customerrepo.findByEmailAndPassword (email, password)) == null)) {
                customerLoggedIn = customerrepo.findByEmailAndPassword (email, password);
                return true;
            }
        } catch (Exception err) {
            System.out.println ("login wrong - please try again" + err.getMessage ());
        }
        return false;
    }
}
