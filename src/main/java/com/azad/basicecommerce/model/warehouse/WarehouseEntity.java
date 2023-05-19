//package com.azad.basicecommerce.model.warehouse;
//
//import com.azad.basicecommerce.model.address.AddressEntity;
//import com.azad.basicecommerce.model.store.StoreEntity;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@NoArgsConstructor
//@Data
//@Entity
//@Table(name = "warehouses")
//public class WarehouseEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "warehouse_id")
//    private Long id;
//
//    @Column(name = "warehouse_uid", nullable = false, unique = true)
//    private String warehouseUid;    // warehouseName + storeName
//
//    @Column(name = "warehouse_name", nullable = false)
//    private String warehouseName;
//
//    @OneToOne(mappedBy = "warehouse")
//    private AddressEntity address;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "store_id")
//    private StoreEntity store;
//}
