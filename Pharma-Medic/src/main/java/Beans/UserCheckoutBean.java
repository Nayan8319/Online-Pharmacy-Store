
import Beans.UserCDI;
import EJB.UserLocal;
import Entity.Cart;
import java.math.BigDecimal;
import java.util.Collection;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Named("userCheckoutBean") // Marks this as a CDI managed bean
@RequestScoped // Ensures the bean lives for the duration of the HTTP request
public class UserCheckoutBean {

    @EJB
    private UserLocal orderService; // Assuming you have an OrderService that handles business logic

    @Inject
    private AddressBean addressBean; // CDI-style injection for AddressBean

    @Inject
    private UserCDI userCDI; // CDI-style injection for UserCDI

    // Method to process checkout
//    public String processCheckout() {
//        Integer userId = addressBean.getUser().getUserId();
//        Integer addressId = addressBean.getAddressId();
//        String paymentMethod = "Cash"; // This can be dynamically set based on user input
//        BigDecimal totalPrice = userCDI.getSubtotal();
//
//        // Call the addOrder method to persist the order
//        boolean orderCreated = orderService.addOrder(userId, addressId, paymentMethod, totalPrice);
//
//        if (orderCreated) {
//
//            // Redirect or show success message
//            return "ThankYou.jsf"; // Navigate to an order confirmation page
//        } else {
//            // Show an error message
//            return "Forbidden.jsf"; // Navigate to an error page or show a message
//        }
//    }

    
    public String processCheckout() {
    Integer userId = addressBean.getUser().getUserId();
    Integer addressId = addressBean.getAddressId();
    String paymentMethod = "Cash"; // This can be dynamically set based on user input
    BigDecimal totalPrice = userCDI.getSubtotal();

    // Call the addOrder method to persist the order
    boolean orderCreated = orderService.addOrder(userId, addressId, paymentMethod, totalPrice);

    if (orderCreated) {
        // Clear the cart after the order is created
        try {
            clearCartByUserId(userId);  // Clear the cart after successful checkout
        } catch (Exception e) {
            // Log the error if something goes wrong while clearing the cart
            System.err.println("Error clearing cart for user ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }

        // Redirect or show success message
        return "ThankYou.jsf"; // Navigate to an order confirmation page
    } else {
        // Show an error message
        return "Forbidden.jsf"; // Navigate to an error page or show a message
    }
}

@PersistenceContext(unitName = "PharmacyPu")
private EntityManager em;

@Transactional
public void clearCartByUserId(Integer userId) {
    try {
        // Query to fetch all cart items for the given user ID
        Collection<Cart> cartItems = em.createQuery("SELECT c FROM Cart c WHERE c.userId.userId = :userId", Cart.class)
                                       .setParameter("userId", userId)
                                       .getResultList();

        // Remove each cart item
        for (Cart cartItem : cartItems) {
            em.remove(cartItem);  // This will trigger a delete operation
        }

        // Optional: Clear any cached data or perform additional operations
        em.flush();
    } catch (Exception e) {
        System.err.println("Error clearing cart for user ID " + userId + ": " + e.getMessage());
        throw e; // rethrow the exception if necessary
    }
}


}
