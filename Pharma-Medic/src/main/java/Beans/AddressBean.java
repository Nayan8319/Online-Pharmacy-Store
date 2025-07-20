import EJB.UserLocal;
import Entity.Address;
import Entity.Cart;
import Entity.Users;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.servlet.http.HttpServletRequest;

@Named
@SessionScoped
public class AddressBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private UserLocal addressService; // EJB that handles database operations

    private String street;
    private String city;
    private String state;
    private String zipCode;

    private Integer addressId;
    
    private Integer userId;
    
    private Users user;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    // Getters and setters
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    // Method to fetch userId from session using HttpSession
    public Integer getCurrentUserId() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);  // Get the current session
        if (session != null) {
            Object loggedInUserId = session.getAttribute("userId");
            if (loggedInUserId != null) {
                return (Integer) loggedInUserId;  // Return userId if present in session
            }
        }
        return null; // Return null if userId is not found in session
    }

        public Users getCurrentUser() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);  // Get the current session
        if (session != null) {
            Object loggedInUser = session.getAttribute("user");  // Assuming the 'user' object is stored in session
            if (loggedInUser != null && loggedInUser instanceof Users) {
                return (Users) loggedInUser;  // Return the user object if present in session
            }
        }
        return null;  // Return null if user object is not found in session
    }
    
    // Method to handle address addition
    public String addAddress() {
        // Retrieve user ID from session
        userId = getCurrentUserId();

        if (userId == null) {
            System.err.println("User is not logged in. Cannot add address.");
            return "login.jsf?faces-redirect=true"; // Redirect to login page if user is not logged in
        }

        System.out.println("Adding address for user with ID: " + userId);

        // Call the EJB method to add the address
        boolean success = addressService.addAddress(userId, street, city, state, zipCode);

        if (success) {
            System.out.println("Address added successfully.");
            return "Address.jsf?faces-redirect=true"; // Redirect to success page
        } else {
            System.err.println("Failed to add address.");
            return "addressError.jsf?faces-redirect=true"; // Redirect to error page
        }
    }
    

    private Address selectedAddress; // To store the selected address

public Address getSelectedAddress() {
    return selectedAddress;
}

public void setSelectedAddress(Address selectedAddress) {
    this.selectedAddress = selectedAddress;
}

    @Inject
    private HttpServletRequest request;  // Injecting the HttpServletRequest to access the session

    private Collection<Address> userAddresses; // To store the list of addresses for the user

    // Method to fetch the user's addresses from the session
    public Collection<Address> getUserAddresses() {
        HttpSession session = request.getSession();

        // Retrieve the userId from the session
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId != null) {
            // Fetch addresses for the given userId using the EJB service
            userAddresses = addressService.getByUserId(userId);
        } else {
            // Handle the case where the userId is not found in the session
            System.err.println("User not found in session.");
            userAddresses = null;
        }

        return userAddresses;
    }
    
    
       public String editAddress(Integer addressId) {
        if (addressId == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid address selected."));
            return null; // Stay on the same page
        }

        // Fetch the address by its ID
        Address address = addressService.getAddressByid(addressId);

        if (address != null) {
            // Fetch user information associated with the address
            user = address.getUserId();  // Assuming Address entity has a reference to Users (userId)

            // Populate the form fields
            this.addressId = address.getAddressId();
            this.street = address.getStreet();
            this.city = address.getCity();
            this.state = address.getState();
            this.zipCode = address.getZipCode();

            // Redirect to the profile page to edit the address
            return "Checkout.jsf?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Address not found."));
            return null; // Stay on the same page
        }
    }
       
           private Address selectAddress;
           Collection<Cart> cartProducts;

    public Collection<Cart> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(Collection<Cart> cartProducts) {
        this.cartProducts = cartProducts;
    }


    public UserLocal getAddressService() {
        return addressService;
    }

    public void setAddressService(UserLocal addressService) {
        this.addressService = addressService;
    }

    public Address getSelectAddress() {
        return selectAddress;
    }

    public void setSelectAddress(Address selectAddress) {
        this.selectAddress = selectAddress;
    }
           
    public BigDecimal getSubtotal() {
    BigDecimal subtotal = BigDecimal.ZERO;  // Initialize as BigDecimal.ZERO
    for (Cart item : cartProducts) {
        subtotal = subtotal.add(item.getTotalPrice());  // Add the BigDecimal value directly
    }
    return subtotal;
}
    
       private String paymentMethod = "Cash";
    private BigDecimal totalPrice;

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
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
}
