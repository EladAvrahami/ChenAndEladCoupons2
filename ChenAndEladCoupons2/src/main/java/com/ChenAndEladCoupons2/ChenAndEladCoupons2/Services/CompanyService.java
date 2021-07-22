package com.ChenAndEladCoupons2.ChenAndEladCoupons2.Services;

import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.Company;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.Coupon;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Exceptions.CouponCustomExceptions;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Repositories.Companyrepo;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Repositories.Couponrepo;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Repositories.Customerrepo;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.enums.Category;
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
public class CompanyService implements ClientService {

    @Autowired
    Companyrepo companyrepo;
    @Autowired
    Couponrepo couponrepo;
    @Autowired
    Customerrepo customerrepo;
    Company companyLoggedIn;


    public Company getCompanyLoggedIn() {
        return companyLoggedIn;
    }

    /**
     * a method that adds a Coupon to the Coupon table in the DB
     * @param coupon the Coupon we want to add
     * @return boolean answer true if the Coupon was added ,or false.
     */
    public boolean addCoupon(Coupon coupon) {
        try {
            Coupon retObject = null;
            if (!couponrepo.existsById (coupon.getId()))
                if (couponrepo.findByTitle (coupon.getTitle ()) == null)
                    retObject = couponrepo.save (coupon);
            retObject.getId();
            System.out.println ("coupon number :" + coupon.getId() + " was added");
            return true;
        } catch (Exception err) {
            System.out.println ("cant add a coupon since it already exists " + err.getMessage ());
            return false;
        }
    }

    /**
     * a method that updates Coupon's details
     * @param coupon the Coupon we want to update
     * @throws CouponCustomExceptions an exception that operates when an error occures
     */
    public void updateCoupon(Coupon coupon) throws CouponCustomExceptions {
        if (couponrepo.existsById (coupon.getId())) {
            couponrepo.save (coupon);
            System.out.println ("coupon number " + coupon.getId() + " was updated");
        } else {
            System.out.println ("coupon not found");


        }
    }

    /**
     * a method that deletes a Coupon from the Coupon table in the DB
     * @param couponID the id of specific Coupon by which we identify the Coupon
     * @return boolean answer true if the Coupon was deleted ,or false.
     */
    public boolean deleteCoupon(int couponID) {
        try {
            if (couponrepo.existsById (couponID)) {
                couponrepo.deleteByCouponId (couponID);
                System.out.println ("coupon id number " + couponID + " was deleted");
                return true;
            }
            throw new CouponCustomExceptions ("coupon id does not exist");
        } catch (Exception err) {
            System.out.println (err.getMessage ());
        }
        return false;
    }

    /**
     * a method that gets and prints all the Coupons that belongs to the Company
     * @return a list of Coupons
     */
    public List<Coupon> getAllCouponsPerCompany() {
        Company companyLI = (companyrepo.getOne (companyLoggedIn.getId()));
        try {
            TablePrinter.print (companyLI.getCoupons ());
            //for (Coupon coupon2 : companyrepo.findCompanyById (companyID).getCoupons ()) {
               // TablePrinter.print (coupon2);
            //}

            return companyLI.getCoupons ();

        } catch (Exception err) {
            System.out.println (err.getMessage ());

        }
        return null;

    }
    public Company getCompanyThatLoggedIn() {
        System.out.println (companyrepo.findCompanyById (companyLoggedIn.getId()));
        System.out.println ("-----------------------------------------------------------------------");
        for (Coupon coupon : companyrepo.findCompanyById (companyLoggedIn.getId()).getCoupons ()) {
            int num = 1;
            System.out.println ("coupon number " + num + "\n" + coupon);
            num += 1;
            System.out.println ("-------------------------------------------------------------------");
        }
        return companyrepo.findCompanyById (companyLoggedIn.getId());
    }

    public List<Coupon> getAllCouponsPerCompany(int id) {
        Company company = (companyrepo.getOne (id));
        try {
            TablePrinter.print (company.getCoupons ());
            //for (Coupon coupon2 : companyrepo.findCompanyById (companyID).getCoupons ()) {
            // TablePrinter.print (coupon2);
            //}

            return company.getCoupons ();

        } catch (Exception err) {
            System.out.println (err.getMessage ());

        }
        return null;

    }

    /**
     * a method that gets and prints all the Coupons that belongs to the Company by a specific category
     * @param companyID the id of specific company by which we identify the Company
     * @param category  the enum item that we want to filter the Coupon list by.
     * @return a list of Coupons
     */
    public List<Coupon> getAllCouponsByCategoryPerCompany(int companyID, Category category) {
        try {
            TablePrinter.print (couponrepo.findAllByCompanyIDAndCategory (companyID, category));
            //for (Coupon coupon2 : couponrepo.findAllByCompanyIDAndCategory (companyID, category)) {
            //    System.out.println (coupon2);
            //}
        } catch (Exception err) {
            System.out.println (err.getMessage ());
        }
        return  couponrepo.findAllByCompanyIDAndCategory (companyID, category);

    }

    /**
     * a method that gets and prints all the Coupons that belongs to the Company by price
     * @param companyID the id of specific company by which we identify the Company
     * @param lowPrice  the low price that we filter the list by
     * @param highPrice the high price that we filter the list by
     * @return a list of Coupons
     */
    public List<Coupon> getAllCouponsByPricePerCompany(int companyID, double lowPrice, double highPrice) {
        try {
            TablePrinter.print (couponrepo.findAllByCompanyIDAndPriceBetween (companyID, lowPrice, highPrice));
            //for (Coupon coupon2 : couponrepo.findAllByCompanyIDAndPriceBetween (companyID, lowPrice, highPrice)) {
           //     System.out.println (coupon2);
          //  }
            couponrepo.findAllByCompanyIDAndPriceBetween (companyID, lowPrice, highPrice);

        } catch (Exception err) {
            System.out.println (err.getMessage ());

        }
        return null;

    }
    @Override
    /**
     * a methods that checks the validity of the Company's email and password and return a boolean answer
     */
    public boolean login(String email, String password) {
        try {
            if (!((companyrepo.findByEmailAndPassword (email, password)) == null)) {
                companyLoggedIn = companyrepo.findByEmailAndPassword (email, password);
                return true;
            }
        } catch (Exception err) {
            System.out.println ("login wrong - please try again" + err.getMessage ());
        }
        return false;
    }
}
