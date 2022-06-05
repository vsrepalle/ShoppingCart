package com.shoppingcart.repository;

import com.shoppingcart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	@Query("select p from Product p order by p.rating.rating DESC")
	List<Product> findByOrderByRating_RatingDesc();
	@Query(value = "select * from product p where p.prod_name like :prodName%",nativeQuery = true)
	List<Product> findByProductName(@Param("prodName") String prodName);
	List<Product> findByCategory(String category);

	@Query("select p from Product p group by p.stockQty having p.stockQty=min(p.stockQty)")
	Optional<Product> getLowStockProduct();

}
