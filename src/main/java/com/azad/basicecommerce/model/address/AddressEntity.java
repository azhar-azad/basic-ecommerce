package com.azad.basicecommerce.model.address;

import com.azad.basicecommerce.model.auth.AppUserEntity;
//import com.azad.basicecommerce.model.warehouse.WarehouseEntity;
import com.azad.basicecommerce.model.warehouse.WarehouseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "addresses")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column(name = "address_uid", nullable = false, unique = true)
    private String uid;     // addressType + apartment + house + subDistrict + district

    @Column(name = "address_type", nullable = false)
    private String addressType;

    @Column(name = "apartment")
    private String apartment;

    @Column(name = "house")
    private String house;

    @Column(name = "street")
    private String street;

    @Column(name = "sub_district")
    private String subDistrict;

    @Column(name = "district")
    private String district;

    @Column(name = "division")
    private String division;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUserEntity user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "warehouse_id", referencedColumnName = "warehouse_id")
    private WarehouseEntity warehouse;
}
