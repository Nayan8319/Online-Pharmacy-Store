package EJB;

import Entity.Address;
import Entity.Cart;
import Entity.Orderdetails;
import Entity.Orders;
import Entity.Payment;
import Entity.Product;
import Entity.Users;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;
import javax.ejb.Local;

/**
 * Local interface for user-related operations.
 *
 * @author Pratham Sarang
 */
@Local
public interface UserLocal {

    // User operations
    boolean register(String name, String email, String password, String phone, Integer roleId);

    Integer getIdByRole(Integer role);

    boolean resetpass(Integer regId, Integer role, String password);

    boolean update(Integer regId, String name, String email, String phone);

    Users findUserByEmail(String userEmail);

    // Product operations
    Collection<Product> getAllProducts();

    Optional<Product> getById(Integer pid);

    Product getProductById(Integer productId);

    Collection<Product> getallProductByCat(Integer categoryId);

    Collection<Product> searchProducts(String name, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice);

    // Payment operations
    boolean addPayment(Integer oId, String paymentStatus, String paymentMethod, BigDecimal amt);

    Collection<Payment> getPaymentsByUser(Integer userId);

    Collection<Payment> getPaymentsByOrder(Integer orderId);

    // Address operations
    boolean addAddress(Integer userId, String street, String city, String state, String zipCode);

    boolean delAddress(Integer addressId, Integer userId);

    boolean upAddress(Integer addressId, String street, String city, String state, String zipCode, Integer userId);

    Collection<Address> getByUserId(Integer userId);

    Address getAddressByid(Integer addressId);

    Users getUsersByid(Integer userId);

    // Cart operations
    boolean addProductToCart(Integer userId, Integer productId, Integer quantity);

    boolean addOrder(Integer userId, Integer addressId, String paymentMethod, BigDecimal totalPrice);

    boolean removeProductFromCart(Integer userId, Integer productId);

    boolean updateCartQuantity(Integer userId, Integer productId, Integer quantity);

    Collection<Cart> viewCart(Integer userId);

    // Order operations
    boolean placeOrder(Integer regId, Integer addId);

    Collection<Orderdetails> getOrderDetailsById(Integer regId);

    Collection<Orders> getOrderHistory(Integer userId);

    String getOrderStatus(Integer orderId);

    Collection<Cart> getAllProductFromCart(int uid);

    Collection<Cart> getCartByUserId(Integer userId);

    Collection<Cart> getAllCart();

    boolean deleteCart(Integer cartId);

    void removeCartItem(int cartId);

    boolean deleteCartByProductId(Integer productId);

    Collection<Orders> getAllOrdersByUserId(int uid);

    Collection<Users> getAllUsers();

    Collection<Users> getUserById(Integer userId);
//    boolean deleteCartByUserAndProduct(Integer userId, Integer productId);

    long getUserCount();

    long getProductCount();

    long getTotalSales();

}
