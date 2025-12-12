    package dev.cong.v.springcomereme.entity;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.util.List;

    @Entity
    @Table(name = "address")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public class Address {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private  Long id;

        @Column(name = "full_name")
        private String fullName;

        @Column(name = "phone_number")
        private  String phoneNumber;

        @Column(name = "district")
        private String district;

        @Column(name = "province")
        private  String province;

        @Column(name = "ward")
        private  String ward;

        @Column(name = "specify")
        private  String specify;

        @Column(name = "is_default")
        private  Boolean isDefault;

        @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}
                    , fetch = FetchType.LAZY
            )
        @JoinColumn(name = "user_id")
        private User user;

        @OneToMany(fetch = FetchType.LAZY,
                cascade = {CascadeType.DETACH,CascadeType.MERGE,
                CascadeType.PERSIST,CascadeType.REFRESH},
                mappedBy = "address"
        )
        private List<Order> orders;

        @Override
        public String toString() {
            return "Address{" +
                    "id=" + id +
                    ", fullName='" + fullName + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", district='" + district + '\'' +
                    ", province='" + province + '\'' +
                    ", ward='" + ward + '\'' +
                    ", specify='" + specify + '\'' +
                    ", isDefault=" + isDefault +
                    '}';
        }
    }
