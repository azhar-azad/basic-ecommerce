package com.azad.basicecommerce.model.store;

import com.azad.basicecommerce.model.auth.AppUserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name = "stores")
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(name = "store_uid", nullable = false)
    private String uid;    // storeName + storeOwnerEmail

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Column(name = "discount")
    private Double discount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_owner_id")
    private AppUserEntity storeOwner;

//    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<WarehouseEntity> warehouses;
}
