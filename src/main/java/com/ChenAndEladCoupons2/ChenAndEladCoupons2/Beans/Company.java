package com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private int id;
    private String email;
    @Column(updatable = false)
    private String name;
    private String password;
    @Singular
    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private List<Coupon> coupons; // orphanRemoval = true
}
