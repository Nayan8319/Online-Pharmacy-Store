/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Address;
import Entity.Cart;
import Entity.Orderdetails;
import Entity.Orders;
import Entity.Payment;
import Entity.Product;
import Entity.Role;
import Entity.Users;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.glassfish.soteria.identitystores.hash.PasswordHashCompare;
import org.glassfish.soteria.identitystores.hash.Pbkdf2PasswordHashImpl;

/**
 *
 * @author pratham sarang
 */
@Stateless
public class User implements UserLocal {

    Pbkdf2PasswordHashImpl pb;
    PasswordHashCompare pbc;

    @PersistenceContext(unitName = "PharmacyPu")
    EntityManager em;

    @Override
    public boolean register(String name, String email, String password, String phone, Integer roleId) {
        try {
            // Validate input
            if (name == null || email == null || password == null || phone == null || roleId == null) {
                throw new IllegalArgumentException("Please Enter All Fields");
            } else {
                // Find the role using roleId
                Role role = em.find(Role.class, roleId);
                if (role == null) {
                    // If role doesn't exist, create a new role
                    role = new Role();
                    role.setRoleId(roleId);
                    role.setRoleName("User");  // Default role name if the role doesn't exist
                    em.persist(role);
                }

//                // Hash the password using Pbkdf2PasswordHashImpl
                Pbkdf2PasswordHashImpl pb = new Pbkdf2PasswordHashImpl();
                String encryptedPassword = pb.generate(password.toCharArray());
                // Create new user entity
                Users user = new Users();
                user.setUserName(name);
                user.setUserEmail(email);
                user.setUserPassword(password);
                user.setUserPhone(phone);
                user.setRoleId(role);  // Set the role for the user

                // Update the role's users collection
                Collection<Users> usersCollection = role.getUsersCollection();
                if (usersCollection == null) {
                    usersCollection = new HashSet<>();  // Initialize the collection if it's null
                }
                usersCollection.add(user);
                role.setUsersCollection(usersCollection);  // Update the role's users collection

                // Persist the user and update the role
                em.persist(user);
                em.merge(role);  // Merge the role to persist changes to its collection

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Integer getIdByRole(Integer roleId) {
        // JPQL query to fetch the user_id based on role_id
        String jpql = "SELECT u.userId FROM Users u WHERE u.role.roleId = :roleId"; // Corrected entity and field names

        TypedQuery<Integer> query = em.createQuery(jpql, Integer.class);
        query.setParameter("roleId", roleId);  // Correct parameter name

        try {
            return query.getSingleResult();
        } catch (Exception e) {
            // Handle exceptions (e.g., NoResultException if no user found)
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean resetpass(Integer regId, Integer roleId, String password) {
        try {
            // Find the role entity by roleId
            Role role = em.find(Role.class, roleId);
            if (role == null) {
                throw new IllegalArgumentException("Role not found");
            }

            // Find the user entity by regId
            Users user = em.find(Users.class, regId);
            if (user == null) {
                throw new IllegalArgumentException("User not found");
            }

            // Check if the user is part of the given role (optional, based on your logic)
            if (!role.getUsersCollection().contains(user)) {
                throw new IllegalArgumentException("User does not belong to the specified role");
            }

            // Hash the password using Pbkdf2PasswordHashImpl
            Pbkdf2PasswordHashImpl pb = new Pbkdf2PasswordHashImpl();
            String encryptedPassword = pb.generate(password.toCharArray());

            // Update the user's password
            user.setUserPassword(encryptedPassword);
            em.merge(user);  // Merge to persist the changes

            return true;  // Return true if everything is successful
        } catch (Exception e) {
            e.printStackTrace();
            return false;  // Return false if there's an exception
        }
    }

    @Override
    public boolean update(Integer regId, String name, String email, String phone) {
        try {
            // Find the user by regId
            Users user = em.find(Users.class, regId);
            if (user == null) {
                throw new IllegalArgumentException("User not found");
            }

            // Update only user details (excluding the role)
            user.setUserName(name);
            user.setUserEmail(email);
            user.setUserPhone(phone);

            // Persist the changes
            em.merge(user);  // Merge to save the updated user details

            return true;  // Return true if the update was successful
        } catch (Exception e) {
            e.printStackTrace();
            return false;  // Return false if any exception occurs
        }
    }

    @Override
    public Collection<Product> getAllProducts() {
        try {
            // Execute the named query to get all products
            Collection<Product> products = em.createNamedQuery("Product.findAll", Product.class).getResultList();
            return products;
        } catch (Exception e) {
            // Log the error or handle it as needed
            e.printStackTrace();
            return Collections.emptyList(); // Return an empty list in case of error
        }
    }

    @Override
    public Optional<Product> getById(Integer pid) {
        try {
            // Fetch the product by its ID
            Product product = em.find(Product.class, pid);

            // Wrap the result in an Optional
            return Optional.ofNullable(product);
        } catch (Exception e) {
            // Log the exception and handle it appropriately
            e.printStackTrace();
            return Optional.empty(); // Return an empty Optional in case of an error
        }
    }

    @Override
    public Product getProductById(Integer productId) {
        try {
            // Validate the input: Ensure product ID is not null
            if (productId == null) {
                throw new IllegalArgumentException("Product ID cannot be null.");
            }

            // Fetch the product by its ID using the entity manager
            Product product = em.find(Product.class, productId);

            // Check if the product exists in the database
            if (product == null) {
                System.out.println("Product with ID " + productId + " not found.");
                return null;  // Return null if the product was not found
            }

            return product;  // Return the found product
        } catch (Exception e) {
            e.printStackTrace();  // Log any exception that occurs
            return null;  // Return null if an exception happens
        }
    }

    /*
    @Override
    public Product getById(Integer pid) {
        try {
            // Fetch the product by its ID
            Product product = em.find(Product.class, pid);

            // Check if the product is found
            if (product == null) {
                // You can log this or throw an exception if you want to signal a missing product
                System.out.println("Product with ID " + pid + " not found.");
            }

            return product;
        } catch (Exception e) {
            // Log the exception and handle it appropriately
            e.printStackTrace();
            return null; // Optionally, return null or throw a custom exception
        }
    }
     */
    @Override
    public boolean addPayment(Integer oId, String payment_status, String payment_method, BigDecimal amt) {
        try {
            Orders o = em.find(Orders.class, oId);

            if (oId == null || amt.compareTo(o.getTotalPrice()) != 0) {
                throw new IllegalArgumentException("Please Enter All Field");
            } else {
                Collection<Payment> order = o.getPaymentCollection();

                Payment p = new Payment();
                p.setPaymentMethod(payment_method);
                p.setPaymentStatus(payment_status);
                p.setPaymentAmount(amt);
                p.setOrderId(o);

                order.add(p);
                o.setPaymentCollection(order);

                if ("Completed".equalsIgnoreCase(payment_status)) {
                    o.setPaymentStatus("Paid");
                    em.merge(order);
                }
                em.persist(p);
                em.merge(o);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Collection<Payment> getPaymentsByUser(Integer userId) {
        Users user = em.find(Users.class, userId);
        if (user == null) {
            return Collections.emptyList();
        }
        return user.getOrdersCollection().stream()
                .flatMap(order -> order.getPaymentCollection().stream())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Payment> getPaymentsByOrder(Integer orderId) {
        Orders order = em.find(Orders.class, orderId);
        if (order == null) {
            return Collections.emptyList();
        }
        return order.getPaymentCollection();
    }

    @Override
    public boolean addAddress(Integer userId, String street, String city, String state, String zipCode) {
        try {
            // Check for null values in required fields
            if (userId == null || street == null || city == null || state == null || zipCode == null) {
                throw new IllegalArgumentException("Please enter all fields.");
            }

            // Find the user by ID
            Users user = em.find(Users.class, userId);
            if (user == null) {
                throw new IllegalArgumentException("User not found");
            }

            // Retrieve the user's existing address collection
            Collection<Address> addresses = user.getAddressCollection();

            // Create a new Address entity and set the fields
            Address address = new Address();
            address.setStreet(street);
            address.setCity(city);
            address.setState(state);
            address.setZipCode(zipCode);
            address.setUserId(user);  // Link address to user

            // Add the new address to the user's address collection
            addresses.add(address);
            user.setAddressCollection(addresses);

            // Persist the new address and update the user
            em.persist(address);
            em.merge(user);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delAddress(Integer addressId, Integer userId) {
        try {
            // Check for null input values
            if (addressId == null || userId == null) {
                throw new IllegalArgumentException("Please enter all required fields.");
            }

            // Find the Address entity by addressId
            Address address = em.find(Address.class, addressId);
            if (address == null) {
                throw new IllegalArgumentException("Address not found");
            }

            // Find the User entity by userId
            Users user = em.find(Users.class, userId);
            if (user == null) {
                throw new IllegalArgumentException("User not found");
            }

            // Retrieve the user's address collection
            Collection<Address> addresses = user.getAddressCollection();

            // Check if the address exists in the user's collection and remove it
            if (addresses.contains(address)) {
                addresses.remove(address);  // Remove from the collection
                em.remove(address);  // Remove the address from the database
                user.setAddressCollection(addresses);  // Update the user's address collection
                em.merge(user);  // Persist the updated user entity
            } else {
                throw new IllegalArgumentException("Address not linked to the user.");
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean upAddress(Integer addressId, String street, String city, String state, String zipCode, Integer userId) {
        try {
            // Check for null values in required fields
            if (addressId == null || street == null || city == null || state == null || zipCode == null || userId == null) {
                throw new IllegalArgumentException("Please enter all fields.");
            }

            // Find the Address entity by addressId
            Address address = em.find(Address.class, addressId);
            if (address == null) {
                throw new IllegalArgumentException("Address not found");
            }

            // Find the User entity by userId
            Users user = em.find(Users.class, userId);
            if (user == null) {
                throw new IllegalArgumentException("User not found");
            }

            // Check if the address is linked to the user
            if (address.getUserId().equals(user)) {
                // Update the address details
                address.setStreet(street);
                address.setCity(city);
                address.setState(state);
                address.setZipCode(zipCode);

                // Merge the updated address
                em.merge(address);
                return true;
            } else {
                throw new IllegalArgumentException("Address does not belong to this user.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Address getAddressByid(Integer addressId) {
        Address category = em.find(Address.class, addressId);
        return category;
    }
    
    @Override
      public  Users getUsersByid(Integer userId){
          Users user = em.find(Users.class, userId);
          return user;
      }
      
    @Override
    public Collection<Address> getByUserId(Integer userId) {
        try {
            // Check if the userId is null
            if (userId == null) {
                throw new IllegalArgumentException("Please provide a valid user ID.");
            }

            // Find the User entity by userId
            Users user = em.find(Users.class, userId);
            if (user == null) {
                throw new IllegalArgumentException("User not found.");
            }

            // Get the collection of addresses for the user
            Collection<Address> addresses = user.getAddressCollection();
            return addresses;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    @Override
//public boolean addProductToCart(Integer userId, Integer productId, Integer quantity) {
//    try {
//        // Fetch the product and user from the database
//        Product product = em.find(Product.class, productId);
//        Users user = em.find(Users.class, userId);
//
//        // Validate the product, user, and quantity
//        if (product == null) {
//            System.err.println("Product not found with ID: " + productId);
//            return false;
//        }
//
//        if (user == null) {
//            System.err.println("User not found with ID: " + userId);
//            return false;
//        }
//
//        if (quantity <= 0) {
//            System.err.println("Quantity must be greater than 0");
//            return false;
//        }
//
//        if (product.getProductQuantity() < quantity) {
//            System.err.println("Insufficient stock for product ID: " + productId);
//            return false;
//        }
//
//        // Check if the product already exists in the user's cart
//        Cart existingCartItem = findCartItemByUserIdAndProductId(userId, productId);
//        if (existingCartItem != null) {
//            // If exists, update the quantity
//            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
//            BigDecimal totalPrice = existingCartItem.getTotalPrice()
//                    .add(product.getProductPrice().multiply(BigDecimal.valueOf(quantity)));
//            existingCartItem.setTotalPrice(totalPrice);
//            em.merge(existingCartItem); // Update the existing cart item
//        } else {
//            // If not, create a new cart item
//            BigDecimal productPrice = product.getProductPrice();
//            BigDecimal totalPrice = productPrice.multiply(BigDecimal.valueOf(quantity));
//
//            Cart cart = new Cart();
//            cart.setProductId(product);
//            cart.setUserId(user);
//            cart.setQuantity(quantity);
//            cart.setTotalPrice(totalPrice);
//
//            em.persist(cart); // Persist the new cart item
//        }
//
//        // Update the stock in the product
//        product.setProductQuantity(product.getProductQuantity() - quantity);
//        em.merge(product);
//
//        System.out.println("Product successfully added to cart");
//        return true;
//
//    } catch (Exception e) {
//        e.printStackTrace();
//        System.err.println("Error adding product to cart: " + e.getMessage());
//        return false;
//    }
//}
//private Cart findCartItemByUserIdAndProductId(Integer userId, Integer productId) {
//    try {
//        String query = "SELECT c FROM Cart c WHERE c.userId.userId = :userId AND c.productId.productId = :productId";
//        return em.createQuery(query, Cart.class)
//                 .setParameter("userId", userId)
//                 .setParameter("productId", productId)
//                 .getSingleResult();
//    } catch (Exception e) {
//        return null; // No existing cart item found
//    }
//}
    @Override
    public boolean addProductToCart(Integer userId, Integer productId, Integer quantity) {
        try {
            // Fetch user and product from the database
            Users user = em.find(Users.class, userId);
            Product product = em.find(Product.class, productId);

            // Check if user and product are valid
            if (user == null) {
                System.err.println("User not found with id: " + userId);
                return false;
            }
            if (product == null) {
                System.err.println("Product not found with id: " + productId);
                return false;
            }

            // Check if there's enough stock for the product
            if (product.getProductQuantity() < quantity) {
                System.err.println("Not enough stock for product: " + productId + ". Available: "
                        + product.getProductQuantity() + ", Requested: " + quantity);
                return false;
            }

            // Calculate total price
            BigDecimal totalPrice = product.getProductPrice().multiply(new BigDecimal(quantity));

            // Create a new Cart entry
            Cart cart = new Cart();
            cart.setUserId(user);
            cart.setProductId(product);
            cart.setQuantity(quantity);
            cart.setTotalPrice(totalPrice);

            // Persist the cart entry
            em.persist(cart);
            System.out.println("Product added to cart successfully.");

            // Optionally, update the stock quantity of the product (if needed)
            product.setProductQuantity(product.getProductQuantity() - quantity);
            em.merge(product); // Persist updated product

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeProductFromCart(Integer userId, Integer pId) {
        try {
            // Fetch the user and product entities
            Users user = em.find(Users.class, userId); // Fetching user by userId (assuming 'Users' is your entity)
            Product product = em.find(Product.class, pId); // Fetching product by productId (assuming 'Product' is your entity)

            if (user == null) {
                throw new IllegalArgumentException("User not found with ID: " + userId);
            }

            if (product == null) {
                throw new IllegalArgumentException("Product not found with ID: " + pId);
            }

            // Query to find the specific cart item by user and product
            TypedQuery<Cart> query = em.createQuery(
                    "SELECT c FROM Cart c WHERE c.userId = :user AND c.productId = :product", Cart.class);
            query.setParameter("user", user); // Set the parameter for userId
            query.setParameter("product", product); // Set the parameter for productId

            List<Cart> carts = query.getResultList();

            if (!carts.isEmpty()) {
                Cart cartItem = carts.get(0); // Assuming one cart entry per product per user
                em.remove(cartItem); // Remove the product from the cart
                System.out.println("Product removed from the user's cart.");
            } else {
                System.out.println("No such product found in the user's cart.");
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Collection<Cart> viewCart(Integer userId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("Invalid ID!!!");
            }

            // Fetch the Users entity based on userId
            Users user = em.find(Users.class, userId);
            if (user == null) {
                throw new IllegalArgumentException("User with ID " + userId + " not found!");
            }

            // Query to fetch the user's cart items
            TypedQuery<Cart> query = em.createQuery("SELECT c FROM Cart c WHERE c.userId = :user", Cart.class);
            query.setParameter("user", user); // Set the parameter for userId

            // Retrieve the result list of cart items
            Collection<Cart> result = query.getResultList();

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

@Override
    public boolean addOrder(Integer userId, Integer addressId, String paymentMethod, BigDecimal totalPrice) {
        try {
            // Fetch user and address entities from the database using the provided IDs
            Users user = em.find(Users.class, userId);
            Address address = em.find(Address.class, addressId);

            // Validate if user and address exist
            if (user == null) {
                System.err.println("User not found with id: " + userId);
                return false;
            }
            if (address == null) {
                System.err.println("Address not found with id: " + addressId);
                return false;
            }

            // Create a new order and populate the fields
            Orders order = new Orders();
            order.setUserId(user);  // Associate user with the order
            order.setAddressId(address);  // Associate address with the order
            order.setPaymentMethod(paymentMethod);  // Set payment method
            order.setPaymentStatus("Pending");  // Set payment status
            order.setTotalPrice(totalPrice);  // Set total price
            order.setPlacedOn(new Date());  // Set order placed timestamp

            // Persist the order in the database
            em.persist(order);
            System.out.println("Order created successfully with ID: " + order.getOrderId());

            // Optionally, update any other related entities or take additional actions

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;  // Return false if there was an error
        }
    }
        
    @Override
    public boolean updateCartQuantity(Integer userId, Integer productId, Integer quantity) {
        try {
            // Fetch the cart item for the given user and product
            TypedQuery<Cart> query = em.createQuery("SELECT c FROM Cart c WHERE c.userId.userId = :userId AND c.productId.productId = :productId", Cart.class);
            query.setParameter("userId", userId);
            query.setParameter("productId", productId);
            Cart cartItem = query.getSingleResult();

            // Check if the cart item exists and quantity is valid
            if (cartItem != null && quantity > 0) {
                // Update the quantity
                cartItem.setQuantity(quantity);

                // Update the total price for the product
                BigDecimal productPrice = cartItem.getProductId().getProductPrice(); // Assuming getPrice() returns BigDecimal

                BigDecimal totalPrice = productPrice.multiply(new BigDecimal(quantity));
                cartItem.setTotalPrice(totalPrice);

                // Persist the updated cart item
                em.merge(cartItem);

                return true;
            } else {
                return false;  // Return false if cart item not found or quantity is invalid
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean placeOrder(Integer regId, Integer addid) {
        try {
            // Fetch the user (Registermaster in your case) from the Users table
            Users registermaster = em.find(Users.class, regId);

            // Fetch the cart items for the user (from the Cart table)
            TypedQuery<Cart> query = em.createQuery("SELECT c FROM Cart c WHERE c.userId = :registermaster", Cart.class);
            query.setParameter("registermaster", registermaster);
            Collection<Cart> cartItems = query.getResultList();

            BigDecimal totalAmount = BigDecimal.ZERO;
            System.out.println("After getting cart items");

            if (!cartItems.isEmpty()) {
                Date odate = new Date();
                Address address = em.find(Address.class, addid); // Fetch the address for the order (Address table)
                if (address == null) {
                    System.out.println("Invalid address");
                    return false;
                }

                // Create a new order in the Orders table
                Orders order = new Orders();
                order.setUserId(registermaster);  // Set the user who placed the order
                order.setAddressId(address);      // Set the shipping address for the order
                order.setPlacedOn(odate);         // Set the order date

                // Persist the order before setting it in the order details
                em.persist(order);

                // Loop through the cart items to process each product
                for (Cart cartItem : cartItems) {
                    Product product = em.find(Product.class, cartItem.getProductId());
                    System.out.println("Processing product: " + cartItem.getProductId());

                    if (product != null) {
                        if (product.getProductQuantity() >= cartItem.getQuantity()) {
                            // Calculate the total price for this product
                            BigDecimal productPrice = product.getProductPrice();
                            BigDecimal productTotal = productPrice.multiply(new BigDecimal(cartItem.getQuantity()));

                            // Update the product stock
                            product.setProductQuantity(product.getProductQuantity() - cartItem.getQuantity());

                            // Create order details entry in Orderdetails table
                            Orderdetails orderDetails = new Orderdetails();
                            orderDetails.setOrderId(order);  // Set the full Orders entity
                            orderDetails.setProductId(product);
                            orderDetails.setQuantity(cartItem.getQuantity());
                            orderDetails.setUnitPrice(productTotal);  // Set the total price for this product in the order

                            // Add to the total amount
                            totalAmount = totalAmount.add(productTotal);

                            // Persist the order details and update the product stock
                            em.persist(orderDetails);
                            em.merge(product); // Update the product stock in the Product table
                        } else {
                            System.out.println("Insufficient stock for product: " + product.getProductName());
                            return false;
                        }
                    } else {
                        System.out.println("Product not found for cart item");
                    }
                }

                // Set the grand total in the order and persist the order
                order.setTotalPrice(totalAmount); // Use BigDecimal for total price
                em.merge(order); // Merge the order after adding order details

                // Clear the user's cart after placing the order
                for (Cart cartItem : cartItems) {
                    em.remove(cartItem); // Remove each item from the cart after the order is placed
                }
            } else {
                System.out.println("Cart is empty!");
                return false;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Collection<Orders> getOrderHistory(Integer userId) {
        try {
            // Fetch orders for the user
            TypedQuery<Orders> query = em.createQuery("SELECT o FROM Orders o WHERE o.userId.userId = :userId ORDER BY o.placedOn DESC", Orders.class);
            query.setParameter("userId", userId);
            return query.getResultList();  // Return the list of orders
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getOrderStatus(Integer orderId) {
        try {
            // Fetch the order using the provided orderId
            Orders order = em.find(Orders.class, orderId);

            if (order != null) {
                // Return the payment status or any other relevant order status
                return order.getPaymentStatus();  // Assuming the order status is stored in paymentStatus
            } else {
                return "Order not found";  // Return this if the order ID is invalid
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching order status";
        }
    }

    @Override
    public Collection<Orderdetails> getOrderDetailsById(Integer regId) {
        // Fetch the user based on regId
        Users reg = em.find(Users.class, regId);

        // Fetch the orders associated with the user
        Collection<Orders> orders = reg.getOrdersCollection();

        // Initialize a collection to hold all order details
        Collection<Orderdetails> allOrderDetails = new ArrayList<>();

        // Iterate through each order to fetch the associated order details
        for (Orders order : orders) {
            // Add the order's details to the allOrderDetails collection
            allOrderDetails.addAll(order.getOrderdetailsCollection());
        }

        return allOrderDetails;
    }

    @Override
    public Collection<Product> searchProducts(String name, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        try {
            StringBuilder queryStr = new StringBuilder("SELECT p FROM Product p WHERE 1=1");
            List<Object> parameters = new ArrayList<>();

            // Add filters dynamically
            if (name != null && !name.trim().isEmpty()) {
                queryStr.append(" AND LOWER(p.productName) LIKE ?");
                parameters.add("%" + name.toLowerCase() + "%");
            }

            if (categoryId != null) {
                queryStr.append(" AND p.categoryId.categoryId = ?");
                parameters.add(categoryId);
            }

            if (minPrice != null) {
                queryStr.append(" AND p.productPrice >= ?");
                parameters.add(minPrice);
            }

            if (maxPrice != null) {
                queryStr.append(" AND p.productPrice <= ?");
                parameters.add(maxPrice);
            }

            System.out.println("Generated Query: " + queryStr);
            System.out.println("Parameters: " + parameters);

            // Create and set query parameters
            Query query = em.createQuery(queryStr.toString());
            for (int i = 0; i < parameters.size(); i++) {
                query.setParameter(i + 1, parameters.get(i));
            }

            return query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public Users findUserByEmail(String userEmail) {

        return em.createNamedQuery("Users.findByUserEmail", Users.class)
                .setParameter("userEmail", userEmail)
                .getSingleResult();

    }

    @Override
    public Collection<Product> getallProductByCat(Integer categoryId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Collection<Cart> getCartByUserId(Integer userId) {
        TypedQuery<Cart> query = em.createQuery(
                "SELECT c FROM Cart c WHERE c.userId = :userId", Cart.class
        );
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public Collection<Cart> getAllProductFromCart(int uid) {
        return em.createQuery("SELECT c FROM Cart c WHERE c.userId.userId = :uid").setParameter("uid", uid).getResultList();
    }

    @Override
    public Collection<Cart> getAllCart() {
        try {
            // Execute the named query to get all products
            Collection<Cart> cart = em.createNamedQuery("Cart.fndAll", Cart.class).getResultList();
            return cart;
        } catch (Exception e) {
            // Log the error or handle it as needed
            e.printStackTrace();
            return Collections.emptyList(); // Return an empty list in case of error
        }
    }

    ;
    
@Override
    public boolean deleteCart(Integer cartId) {
        try {
            // Log the attempt to find the cart by cartId
            System.out.println("Attempting to delete cart with cartId: " + cartId);

            Cart cart = em.find(Cart.class, cartId);
            if (cart != null) {
                em.remove(cart);
                System.out.println("Cart with cartId " + cartId + " deleted successfully.");
                return true;
            } else {
                System.out.println("Cart with cartId " + cartId + " not found.");
            }
        } catch (Exception e) {
            System.err.println("Error during deletion: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteCartByProductId(Integer productId) {
        try {
            // Query to find the cart item by product ID
            Cart cart = em.createQuery("SELECT c FROM Cart c WHERE c.productId.productId = :productId", Cart.class)
                    .setParameter("productId", productId)
                    .getSingleResult();

            if (cart != null) {
                // Remove the cart item
                em.remove(cart);
                return true; // Return true if deletion was successful
            }
        } catch (Exception e) {
            System.out.println("No cart found for product ID: " + productId);
        }
        // Log the exception for debugging purposes
        return false; // Return false if deletion failed
    }

    @Override
    public void removeCartItem(int cartId) {
        Cart cartItem = em.find(Cart.class, cartId);
        if (cartItem != null) {
            em.remove(cartItem);
        }
    }

    @Override
    public Collection<Orders> getAllOrdersByUserId(int uid) {
        return em.createQuery("SELECT o FROM Orders o WHERE o.userId.userId = :uid", Orders.class)
                .setParameter("uid", uid)
                .getResultList();
    }

    @Override
    public Collection<Users> getUserById(Integer userId) {
        TypedQuery<Users> query = em.createQuery(
                "SELECT u FROM Users u WHERE u.userId = :userId", Users.class
        );
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public Collection<Users> getAllUsers() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public long getUserCount() {
        return (long) em.createQuery("SELECT COUNT(u) FROM Users u").getSingleResult();
    }

    @Override
    public long getProductCount() {
        return (long) em.createQuery("SELECT COUNT(p) FROM Product p").getSingleResult();
    }

    @Override
    public long getTotalSales() {
        return (long) em.createQuery("SELECT COUNT(o) FROM Orders o").getSingleResult();
    }
}
