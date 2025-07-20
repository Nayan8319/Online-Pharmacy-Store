/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Category;
import Entity.Orders;
import Entity.Payment;
import Entity.Product;
import Entity.Role;
import Entity.Users;
import java.math.BigDecimal;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author pratham sarang
 */
@Stateless
public class Admin implements AdminLocal {
    
    @PersistenceContext(unitName = "PharmacyPu")
    EntityManager em;

    // Product Management
    @Override
    public Collection<Product> getAllProducts() {
        Collection<Product> products = em.createNamedQuery("Product.findAll").getResultList();
        return products;
    }

@Override
    public boolean addProduct(String productName, String productDetails, BigDecimal productPrice, Integer productQuantity, String productImage, Integer categoryId) {
         try {
            // Validate input
            if (productName == null || productName.isEmpty()
                    || productDetails == null || productDetails.isEmpty()
                    || productPrice == null || productPrice.compareTo(BigDecimal.ZERO) <= 0
                    || productImage == null || productImage.isEmpty()
                    || productQuantity <= 0
                    || categoryId == null) {
                throw new IllegalArgumentException("Please provide all fields with valid values.");
            }

            // Check if the category exists
            Category category = em.find(Category.class, categoryId);  // Retrieve the category using the category ID
            if (category == null) {
                System.out.println("Invalid category ID");
                return false;  // Return false if the category is not found
            }

            // Create a new Product instance and set its fields
            Product product = new Product();
            product.setProductName(productName);  // Set product name
            product.setProductDetails(productDetails);  // Set product details
            product.setProductPrice(productPrice);  // Set product price
            product.setProductImage(productImage);  // Set product image
            product.setProductQuantity(productQuantity);  // Set product quantity
            product.setCategoryId(category);  // Set the category reference

            // Persist the new product entity to the database
            em.persist(product);

            return true;  // Return true if the product is successfully added
        } catch (Exception e) {
            e.printStackTrace();
            return false;  // Return false if an exception occurs
        }
    }

    @Override
    public boolean deleteProduct(Integer productId) {
        try {
            // Validate input parameters
            if (productId == null) {
                throw new IllegalArgumentException("Product ID not be null.");
            }

            // Fetch the product based on the product ID
            Product product = em.find(Product.class, productId);

            // Check if the product exists and belongs to the given category
            if (product == null) {
                System.out.println("Product with ID " + productId + " not found.");
                return false; // Product not found
            }

            // Optional: You might want to check if the product is referenced elsewhere, such as in cart or order details.
            // For now, we'll assume there's no such constraint, but you can add checks if necessary.
            // Remove the product from the database
            em.remove(product);

            System.out.println("Product with ID " + productId + " successfully deleted.");
            return true; // Successfully deleted the product
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false if an exception occurs
        }
    }

    @Override
    public boolean updateProduct(Integer productId, String productName, String productDetails, BigDecimal productPrice, String productImage, int productQuantity, Integer categoryId) {
        try {
            // Validate input parameters
            if (productId == null || productName == null || productDetails == null || productPrice == null || productImage == null || productQuantity < 0 || categoryId == null) {
                throw new IllegalArgumentException("All fields must be provided, and product quantity must be non-negative.");
            }

            // Fetch the product based on product ID
            Product product = em.find(Product.class, productId);

            // Check if the product exists
            if (product == null) {
                System.out.println("Product with ID " + productId + " not found.");
                return false; // Product not found
            }

            // Fetch the category based on category ID (if needed)
            Category category = em.find(Category.class, categoryId);

            // Check if the category exists
            if (category == null) {
                System.out.println("Category with ID " + categoryId + " not found.");
                return false; // Category not found
            }

            // Update product fields
            product.setProductName(productName);
            product.setProductDetails(productDetails);
            product.setProductPrice(productPrice);
            product.setProductImage(productImage);
            product.setProductQuantity(productQuantity);
            product.setCategoryId(category); // Set the category for the product

            // Merge the updated product entity into the database
            em.merge(product);

            System.out.println("Product with ID " + productId + " successfully updated.");
            return true; // Successfully updated the product
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false if an exception occurs
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

    @Override
    public Collection<Product> searchProducts(String productName, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        try {
            // Start building the base query
            StringBuilder queryBuilder = new StringBuilder("SELECT p FROM Product p WHERE 1=1");

            // Add conditions to the query based on provided parameters
            if (productName != null && !productName.trim().isEmpty()) {
                queryBuilder.append(" AND p.productName LIKE :productName");
            }
            if (categoryId != null) {
                queryBuilder.append(" AND p.categoryId.categoryId = :categoryId");
            }
            if (minPrice != null) {
                queryBuilder.append(" AND p.productPrice >= :minPrice");
            }
            if (maxPrice != null) {
                queryBuilder.append(" AND p.productPrice <= :maxPrice");
            }

            // Create the query from the string
            TypedQuery<Product> query = em.createQuery(queryBuilder.toString(), Product.class);

            // Set parameters if provided
            if (productName != null && !productName.trim().isEmpty()) {
                query.setParameter("productName", "%" + productName + "%");
            }
            if (categoryId != null) {
                query.setParameter("categoryId", categoryId);
            }
            if (minPrice != null) {
                query.setParameter("minPrice", minPrice);
            }
            if (maxPrice != null) {
                query.setParameter("maxPrice", maxPrice);
            }

            // Execute the query and return the results
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();  // Log any exception that occurs
            return null;  // Return null in case of error
        }
    }

    @Override
    public boolean updateProductStock(Integer productId, int newQuantity) {
        try {
            // Validate input parameters
            if (productId == null || newQuantity < 0) {
                throw new IllegalArgumentException("Product ID cannot be null and quantity must be non-negative.");
            }

            // Fetch the product based on product ID
            Product product = em.find(Product.class, productId);

            // Check if the product exists
            if (product == null) {
                System.out.println("Product with ID " + productId + " not found.");
                return false;  // Product not found
            }

            // Update the product quantity
            product.setProductQuantity(newQuantity);

            // Merge the updated product entity into the database
            em.merge(product);

            System.out.println("Product stock for product with ID " + productId + " successfully updated.");
            return true;  // Successfully updated the product stock
        } catch (Exception e) {
            e.printStackTrace();  // Log any exception that occurs
            return false;  // Return false if an exception happens
        }
    }

    //payment Managment
    @Override
    public Collection<Payment> getAllPayments() {
        try {
            // Fetch all payment records from the database using a query
            Collection<Payment> payments = em.createQuery("SELECT p FROM Payment p", Payment.class).getResultList();

            // If there are no payments, we log this (optional)
            if (payments.isEmpty()) {
                System.out.println("No payments found.");
            }

            return payments;  // Return the collection of payment records
        } catch (Exception e) {
            e.printStackTrace();  // Log any exceptions that occur
            return null;  // Return null if there's an error
        }
    }

    @Override
    public boolean updatePaymentStatus(Integer paymentId, String status) {
        try {
            // Validate input parameters
            if (paymentId == null || status == null || status.trim().isEmpty()) {
                throw new IllegalArgumentException("Payment ID and status must be provided.");
            }

            // Fetch the payment entity by its ID
            Payment payment = em.find(Payment.class, paymentId);

            // Check if the payment exists
            if (payment == null) {
                System.out.println("Payment with ID " + paymentId + " not found.");
                return false;  // Payment not found
            }

            // Get the payment method (cash/card/online)
            String paymentMethod = payment.getPaymentMethod();

            // Handle based on the payment method
            if ("Cash".equalsIgnoreCase(paymentMethod)) {
                // For cash payments, manually set the status
                if ("Completed".equalsIgnoreCase(status)) {
                    // Only allow marking as completed if status is valid for cash
                    payment.setPaymentStatus(status);
                    em.merge(payment);
                    System.out.println("Cash payment status updated to 'Completed'.");
                    return true;
                } else {
                    // If the status isn't 'Completed' for cash, throw an exception
                    System.out.println("Invalid status for cash payment. Only 'Completed' is allowed.");
                    return false;
                }
            } else if ("Card".equalsIgnoreCase(paymentMethod) || "Online".equalsIgnoreCase(paymentMethod)) {
                // For card/online payments, automatically set the status to 'Completed'
                payment.setPaymentStatus("Completed");
                em.merge(payment);
                System.out.println("Card/Online payment status automatically updated to 'Completed'.");
                return true;
            } else {
                // If an invalid payment method is found
                System.out.println("Unknown payment method: " + paymentMethod);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log any exception that occurs
            return false;  // Return false in case of an error
        }
    }

    //Category Management
    /**
     * Checks if a category with the given name already exists.
     */
    public boolean categoryExists(String categoryName) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(c) FROM Category c WHERE c.categoryName = :categoryName", Long.class
        );
        query.setParameter("categoryName", categoryName);
        return query.getSingleResult() > 0;
    }

    /**
     * Adds a new category to the database.
     */
    @Override
    public boolean addCategory(String categoryName) {
        try {
            Category category = new Category();
            category.setCategoryName(categoryName);
            em.persist(category);  // Persist the new category
            em.flush();  // Ensure the entity is properly committed
            return true;
        } catch (Exception e) {
            e.printStackTrace();  // Log the error for better debugging
            throw new RuntimeException("Error adding category: " + e.getMessage(), e);
        }
    }
    
    /**
     * Adds a delete category from the database.
     */
    @Override
    public boolean deleteCategory(Integer categoryId) {
        try {
            Category category = em.find(Category.class, categoryId);
            em.remove(category);
        } catch (Exception e) {
            e.getMessage();
        }
        return false;
    }
    
    /**
     * Adds a update category to the database.
     * @param categoryId
     * @param categoryName
     * @return 
     */
    @Override
    public boolean updateCategory(Integer categoryId, String categoryName) {
        try {
            if (categoryId == null) {
                throw new IllegalArgumentException("Please Enter Category ID and Category Name");
            } else {
                Category category = em.find(Category.class, categoryId);
                if (category == null) {
                    System.out.println("Category not found with ID: " + categoryId);
                    return false;
                }
                category.setCategoryName(categoryName);
                em.merge(category);

            }

            return true;

        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public Collection<Category> getAllCategories() {
        Collection<Category> category = em.createNamedQuery("Category.findAll").getResultList();
        return category;
    }

    @Override
    public Category getCategoryByid(Integer cid) {
        Category category = em.find(Category.class, cid);
        return category;
    }

    // Order Management
    @Override
    public Collection<Orders> getAllOrders() {
        Collection<Orders> order = em.createNamedQuery("Orders.findAll").getResultList();
        return order;
    }

    @Override
    public boolean updateOrderStatus(Integer orderId, String newStatus) {
        try {
            // Validate input
            if (orderId == null || newStatus == null || newStatus.isEmpty()) {
                throw new IllegalArgumentException("Order ID and status cannot be null or empty.");
            }

            // Fetch the order from the database by its ID
            Orders order = em.find(Orders.class, orderId);

            // Check if the order exists
            if (order == null) {
                System.out.println("Order with ID " + orderId + " not found.");
                return false; // Order not found
            }

            // Update the order's status
            order.setPaymentStatus(newStatus);  // Assuming paymentStatus is the field to update

            // Merge the updated order into the database
            em.merge(order);

            // Optionally, print a success message or log it
            System.out.println("Order with ID " + orderId + " successfully updated to status: " + newStatus);

            return true;  // Successfully updated the order
        } catch (IllegalArgumentException e) {
            // Handle invalid input arguments
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return false;
        }
    }

    //User Managment
    @Override
    public Collection<Users> getAllUsers() {
        Collection<Users> users = em.createNamedQuery("Users.findAll").getResultList();
        return users;
    }

    @Override
    public boolean updateUser(Integer userId, String userName, String userEmail, String userPhone, Integer roleId) {
        try {
            // Validate input
            if (userId == null || userName == null || userEmail == null || userPhone == null || roleId == null) {
                throw new IllegalArgumentException("All fields (userId, userName, userEmail, userPhone, roleId) must be provided.");
            }

            // Check if the roleId is valid (either Admin (1) or User (2))
            if (roleId != 1 && roleId != 2) {
                throw new IllegalArgumentException("Invalid role ID. Only 1 (Admin) and 2 (User) are allowed.");
            }

            // Fetch the user by userId
            Users user = em.find(Users.class, userId);

            // Check if the user exists
            if (user == null) {
                System.out.println("User with ID " + userId + " not found.");
                return false; // User not found
            }

            // Fetch the role based on roleId (1 for Admin, 2 for User)
            Role role = null;
            if (roleId == 1) {
                // Admin role
                role = em.find(Role.class, 1); // Fetch the Admin role
            } else if (roleId == 2) {
                // User role
                role = em.find(Role.class, 2); // Fetch the User role
            }

            // Check if the role exists
            if (role == null) {
                System.out.println("Role with ID " + roleId + " not found.");
                return false; // Role not found
            }

            // Update the user's fields
            user.setUserName(userName);
            user.setUserEmail(userEmail);
            user.setUserPhone(userPhone);

            // Update the user's role
            user.setRoleId(role); // Assign the new role to the user

            // Merge the updated user entity into the database
            em.merge(user);

            System.out.println("User with ID " + userId + " successfully updated.");
            return true; // Successfully updated the user
        } catch (IllegalArgumentException e) {
            // Handle invalid input arguments
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUser(Integer userId) {
        try {
            // Validate the userId input
            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null.");
            }

            // Fetch the user by userId
            Users user = em.find(Users.class, userId);

            // Check if the user exists
            if (user == null) {
                System.out.println("User with ID " + userId + " not found.");
                return false; // User not found
            }

            // Remove the user from the database
            em.remove(user);

            System.out.println("User with ID " + userId + " successfully deleted.");
            return true; // Successfully deleted the user
        } catch (IllegalArgumentException e) {
            // Handle invalid input arguments
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return false;
        }
    }
}
