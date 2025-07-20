/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package Beans;

import EJB.UserLocal;
import Entity.Cart;
import Entity.Orders;
import Entity.Product;
import Entity.Users;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import static java.lang.System.out;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author pratham sarang
 */
@Named(value = "userCDI")
@SessionScoped
public class UserCDI implements Serializable {

    @EJB
    private UserLocal userLocal;
    private Users user = new Users();

    private String name;
    private String email;
    private String phone;
    private String password;
    private Integer roleId = 2;
    Collection<Cart> cartProducts;

    public String register() {
        try {
            if (name == null || email == null || password == null || phone == null) {
                throw new IllegalArgumentException("Please enter all fields.");
            }

            boolean isRegistered = userLocal.register(name, email, password, phone, roleId);
            if (isRegistered) {
                // Store data in cookies
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ExternalContext externalContext = facesContext.getExternalContext();
                HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

                // Create and add cookies
                Cookie userNameCookie = new Cookie("userName", name);
                userNameCookie.setMaxAge(60 * 60 * 24); // 1 day
                userNameCookie.setPath("/"); // Make cookie available for the entire app
                response.addCookie(userNameCookie);

                Cookie userEmailCookie = new Cookie("userEmail", email);
                userEmailCookie.setMaxAge(60 * 60 * 24); // 1 day
                userEmailCookie.setPath("/");
                response.addCookie(userEmailCookie);

                Cookie userPasswordCookie = new Cookie("userPassword", password);
                userPasswordCookie.setMaxAge(60 * 60 * 24);
                userPasswordCookie.setPath("/");
                response.addCookie(userPasswordCookie);

                return "/Login.jsf?faces-redirect=true"; // Redirect to login page on successful registration
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration failed", "Something went wrong"));
                return null; // Stay on the same page
            }

        } catch (Exception e) {
            String errorMessage = "Error during registration: " + e.getMessage();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", errorMessage));
            e.printStackTrace(); // Logs full stack trace in server logs
            return null; // Stay on the same page
        }
    }


    public String login() {
        try {
            // Validate input
            if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Email and password are required.");
            }

            // Fetch user by email
            Users user = userLocal.findUserByEmail(email);

            // Compare passwords (use hashed comparison in production)
            if (!password.equals(user.getUserPassword())) { // Replace with password hashing logic
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Login", "Password is incorrect."));
                return null; // Stay on the same page
            }

            // Set session attributes
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getExternalContext().getSessionMap().put("loggedInUser", user);
            facesContext.getExternalContext().getSessionMap().put("userId", user.getUserId()); // Add user ID to session

            if (user.getRoleId() == null || user.getRoleId().getRoleId() == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "Invalid user role."));
                return null; // Stay on the same page
            } else {
                // Determine redirect URL based on role
                String redirectUrl;
                switch (user.getRoleId().getRoleId()) {
                    case 1:
                        redirectUrl = "/Pharmacy_Store/Admin/AdminDashboard.jsf?faces-redirect=true";
                        break;
                    case 2:
                        redirectUrl = "/Pharmacy_Store/Users/home.jsf?faces-redirect=true";
                        break;
                    default:
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "Invalid user role."));
                        return null; // Stay on the same page
                }

                // Add JavaScript to store userId in sessionStorage
                String script = "<script>"
                        + "sessionStorage.setItem('userId', '" + user.getUserId() + "');"
                        + "window.location.href = '" + redirectUrl + "';"
                        + "</script>";

                facesContext.getExternalContext().getResponseOutputWriter().write(script);
                facesContext.responseComplete(); // Prevent further JSF lifecycle phases
                return null;
            }
        } catch (NoResultException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "User not found."));
            return null; // Stay on the same page
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "An unexpected error occurred."));
            e.printStackTrace();
            return null; // Stay on the same page
        }
    }

    public String logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // Invalidate the session
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate(); // Completely invalidates the session
        }

        // Redirect to the login page
        return "/Pharmacy_Store/Login.jsf?faces-redirect=true";
    }

//    for Dynamic Load Product
    private Product selectedProduct;

//    for Dynamic Load Product
    public Product getSelectedProduct() {
        return selectedProduct;
    }

//    for Dynamic Load Product
    public void loadProduct() {
        FacesContext context = FacesContext.getCurrentInstance();
        String productId = context.getExternalContext().getRequestParameterMap().get("productId");
        selectedProduct = userLocal.getProductById(Integer.valueOf(productId));
//        selectedProduct = userLocal.getById(Integer.valueOf(productId));

    }
    private Integer userId;
    private Integer productId;
    private Integer quantity = 1;

    public UserLocal getUserLocal() {
        return userLocal;
    }

    public void setUserLocal(UserLocal userLocal) {
        this.userLocal = userLocal;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getCurrentUserId() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Object loggedInUserId = facesContext.getExternalContext().getSessionMap().get("loggedInUserId");
        if (loggedInUserId != null) {
            return (Integer) loggedInUserId;
        }
        return null; // Return null if no user is logged in
    }

    public void loadUserIdFromSessionStorage() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        Object sessionUserId = sessionMap.get("userId");

        if (sessionUserId == null) {
            System.out.println("UserId is not set or empty.");
        } else {
            this.userId = (Integer) sessionUserId;
            System.out.println("UserId fetched from session storage: " + userId);
        }
    }

    public String addProductToCart() {
        System.out.println("Adding product to cart...");  // Log for debugging

        boolean success = userLocal.addProductToCart(userId, selectedProduct.getProductId(), quantity);

        if (success) {
            System.out.println("Product added successfully to cart.");
            return "Cart.jsf?faces-redirect=true";
        } else {
            System.err.println("Failed to add product to cart.");
            return "cartError?faces-redirect=true";
        }
    }

    public UserLocal getUserservice() {
        return userLocal;
    }

    public void setUserservice(UserLocal userservice) {
        this.userLocal = userservice;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public void displayUserCart(int userId) {
        Collection<Cart> cartItems = userLocal.getCartByUserId(userId);
        cartItems.forEach(item -> {
            System.out.println("Product ID: " + item.getProductId());
            System.out.println("Quantity: " + item.getQuantity());
            System.out.println("Total Price: " + item.getTotalPrice());
        });
    }

    /**
     * Creates a new instance of UserCDI
     */
    public UserCDI() {
    }

    @Inject
    private HttpServletRequest request;

    public Collection<Cart> getAllCart() {
        cartProducts = userLocal.getAllCart();
        return cartProducts;

    }

    public Collection<Cart> getCartProducts() {
        HttpSession session = request.getSession();

        // Retrieve a value from the session
        int uid = (int) session.getAttribute("userId");
        return cartProducts = userLocal.getAllProductFromCart(uid);
    }

    public void setCartProducts(Collection<Cart> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public void refreshCart() {
//        cart = null
        cartProducts = null;
        getAllCart();
    }

    public String deleteCart(Integer cartId) {
        try {
            System.out.println("Entering deleteCart method. cartId: " + cartId);

            // Delete product from cart using the cartItemId
            userLocal.removeCartItem(cartId);

            System.out.println("Product deleted successfully.");

            // Add success message
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Cart item deleted successfully."));

            // Return to the Cart page with a redirect to refresh
            return "Cart.jsf?faces-redirect=true";
        } catch (Exception e) {
            // Add error message
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to delete cart item."));
            e.printStackTrace();
            return null;
        }
    }
    
public BigDecimal getSubtotal() {
    BigDecimal subtotal = BigDecimal.ZERO;  // Initialize as BigDecimal.ZERO
    for (Cart item : cartProducts) {
        subtotal = subtotal.add(item.getTotalPrice());  // Add the BigDecimal value directly
    }
    return subtotal;
}

   private Collection<Orders> orders; // To store the list of orders

    public Collection<Orders> getOrders() {
        // Retrieve the user ID from the session
                HttpSession session = request.getSession();
        int uid = (int) session.getAttribute("userId");
        
        // Fetch orders by user ID from the database using userLocal
        orders = userLocal.getAllOrdersByUserId(uid);
        
        return orders; // Return the orders list
    }
    
    private long totalProducts;
    private long totalUsers;
    private long totalSales;

    @PostConstruct
    public void init() {
        try {
            totalProducts = userLocal.getProductCount();
            totalUsers = userLocal.getUserCount();
            totalSales = userLocal.getTotalSales();
            System.out.println("Counts retrieved: Products=" + totalProducts + ", Users=" + totalUsers + ", Sales=" + totalSales);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
        public String addOrder(Integer userId, Integer addressId, String paymentMethod, BigDecimal totalPrice) {
        try {
            System.out.println("Order created with userId: " + userId + ", addressId: " + addressId + ", paymentMethod: " + paymentMethod + ", totalPrice: " + totalPrice);
            // Your order creation logic here
            return "orderConfirmation.jsf?faces-redirect=true";  // Redirect to order confirmation page
        } catch (Exception e) {
            e.printStackTrace();
            return "orderError.jsf?faces-redirect=true";  // Redirect to error page
        }
    }

    // Getters
    public long getTotalProducts() {
        return totalProducts;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public long getTotalSales() {
        return totalSales;
    }

}
