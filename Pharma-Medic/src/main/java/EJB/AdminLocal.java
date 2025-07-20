/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package EJB;

import Entity.Category;
import Entity.Orders;
import Entity.Payment;
import Entity.Product;
import Entity.Users;
import java.math.BigDecimal;
import java.util.Collection;
import javax.ejb.Local;

/**
 *
 * @author pratham sarang
 */
@Local
public interface AdminLocal {
        // Product Management
    Collection<Product> getAllProducts();
boolean addProduct(String productName, String productDetails, BigDecimal productPrice, Integer productQuantity, String productImage, Integer categoryId);    boolean deleteProduct(Integer productId);
    boolean updateProduct(Integer productId, String productName, String productDetails, BigDecimal productPrice, String productImage, int productQuantity, Integer categoryId);
    Product getProductById(Integer productId);
    Collection<Product> searchProducts(String productName, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice);
    boolean updateProductStock(Integer productId, int newQuantity);

    // Payment Management
    Collection<Payment> getAllPayments();
    boolean updatePaymentStatus(Integer paymentId, String status);

    // Category Management
//    boolean addCategory(String categoryName);
    boolean categoryExists(String categoryName) ;
//    boolean addCategory(Category category) ;
    boolean addCategory(String categoryName);
    
    boolean deleteCategory(Integer categoryId);
    boolean updateCategory(Integer categoryId, String categoryName);
    Collection<Category> getAllCategories();
    Category getCategoryByid(Integer categoryId);

    // Order Management
    Collection<Orders> getAllOrders();
    boolean updateOrderStatus(Integer orderId, String newStatus);
    
    // User Management
    Collection<Users> getAllUsers();
    boolean updateUser(Integer userId, String userName, String userEmail, String userPhone,Integer roleId);
    boolean deleteUser(Integer userId);

}
