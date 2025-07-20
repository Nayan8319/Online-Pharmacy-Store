/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package Beans;

import EJB.AdminLocal;
import Entity.Category;
import Entity.Orders;
import Entity.Payment;
import Entity.Product;
import Entity.Users;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author pratham sarang
 */
@Named(value = "adminCDI")
@SessionScoped
public class AdminCDI implements Serializable {

    @EJB
    private AdminLocal adminLocal;

    // Order Management Start
    private Integer orderId;
    private String newStatus;
    private Collection<Orders> orders;

    public void getAllOrders() {
        orders = adminLocal.getAllOrders();
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public Collection<Orders> getOrders() {
        if (orders == null) {
            getAllOrders();
        }
        return orders;
    }

    public void setOrders(Collection<Orders> orders) {
        this.orders = orders;
    }

    // Order Management End
    // Payment Management Start 
    private Integer paymentId;
    private String status;
    Collection<Payment> payments;

    public void getAllPayments() {
        payments = adminLocal.getAllPayments();
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Collection<Payment> getPayments() {
        if (payments == null) {
            getAllPayments();
        }
        return payments;
    }

    public void setPayments(Collection<Payment> payments) {
        this.payments = payments;
    }

    // Payment Management End
    // User Management Start
    private Integer userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private Integer roleId;
    Collection<Users> users;

    public void getAllUsers() {
        users = adminLocal.getAllUsers();
    }

    // User Management End
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Collection<Users> getUsers() {
        if (users == null) {
            getAllUsers();
        }
        return users;
    }

    public void setUsers(Collection<Users> users) {
        this.users = users;
    }

    //Product Crud  start   
    private Integer productId;
    private String productName;
    private String productDetails;
    private BigDecimal productPrice;
    private String productImage;
    private int productQuantity;
    private Collection<Product> products;

    public void getAllProducts() {
        products = adminLocal.getAllProducts();
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Collection<Product> getProducts() {
        if (products == null) {
            getAllProducts();
        }
        return products;
    }

    public void setProducts(Collection<Product> products) {
        this.products = products;
    }

    //Product Management  End   
    //Category Management  start   
    private Integer categoryId;
    private String categoryName;
    private Collection<Category> categories;

    // Method to fetch all categories
    public void getAllCategories() {
        categories = adminLocal.getAllCategories();
    }

    // Method to fetch a category by ID
    public String getCategoryById(Integer categoryId) {
        Category category = adminLocal.getCategoryByid(categoryId);
        if (category != null) {
            this.categoryId = category.getCategoryId();
            this.categoryName = category.getCategoryName();
            return "AllCategories.jsf"; // Navigate to edit page
        } else {
            System.out.println("Category not found.");
            return null; // Stay on the same page
        }
    }

    // Getters and setters
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Collection<Category> getCategories() {
        if (categories == null) {
            getAllCategories();
        }
        return categories;
    }

    public void setCategories(Collection<Category> categories) {
        this.categories = categories;
    }

//    For Add Category Method
//    public String addCategory() {
//        FacesContext context = FacesContext.getCurrentInstance();
//
//        try {
//            // Check if the category already exists
//            if (adminLocal.categoryExists(categoryName)) {
//                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                        "Category already exists!", null));
//                return null; // Stay on the same page
//            }
//
//            // Add new category
//            Category category = new Category();
//            category.setCategoryName(categoryName);
//            adminLocal.addCategory(categoryName);
//
//            // Success message
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
//                    "Category added successfully!", null));
//
//            adminLocal.getAllCategories();
//            
//            return "AllCategories.jsf?faces-redirect=true"; // Redirect to the list of categories
//        } catch (Exception e) {
//            // Log and show error message
//            e.printStackTrace();
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                    "An error occurred while adding the category.", null));
//            return null;
//        }
//    }
    public void refreshCategories() {
        categories = null;
        getAllCategories();
    }

    public String addCategory() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (categoryName == null || categoryName.trim().isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Category name cannot be empty."));
            return null;
        }
        try {
            boolean isAdded = adminLocal.addCategory(categoryName);
            if (isAdded) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Category added successfully!"));
                categoryName = null; // Reset form field
                refreshCategories(); // Reload categories
                return "AllCategories.jsf?faces-redirect=true";
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to add category."));
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unexpected error: " + e.getMessage()));
            e.printStackTrace();
        }
        return null;
    }

//    For Delete Category Method
    public boolean deleteCategory(Integer categoryId) {
        try {
            adminLocal.deleteCategory(categoryId);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Category deleted successfully."));
            refreshCategories(); // Refresh categories
            return true;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to delete category."));
            e.printStackTrace();
            return false;
        }
    }

// Method to set category for editing
    public String editCategory(Integer categoryId) {
        if (categoryId == null) {
            System.out.println("Error: categoryId is null.");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid category selected."));
            return null; // Stay on the same page
        }
        System.out.println("Editing category with ID: " + categoryId);

        Category category = adminLocal.getCategoryByid(categoryId);
        if (category != null) {
            this.categoryId = category.getCategoryId();
            this.categoryName = category.getCategoryName();
            return "EditCategory.jsf?faces-redirect=true"; // Redirect to the edit page
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Category not found."));
            return null; // Stay on the same page
        }
    }

    // Method to update the category
    public String updateCategory() {
        try {
            // Validate input
            if (categoryId == null || categoryName == null || categoryName.trim().isEmpty()) {
                throw new IllegalArgumentException("Category ID and Category Name are required.");
            }

            // Call service to update the category
            boolean isUpdated = adminLocal.updateCategory(categoryId, categoryName);

            if (isUpdated) {
                // Show success message
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Category updated successfully."));
                categoryName = null; // Reset form field
                refreshCategories(); // Reload categories
                return "AllCategories.jsf?faces-redirect=true";
            } else {
                // Show error message if category not found
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Category not found with ID: " + categoryId));
                return null; // Stay on the current page
            }
        } catch (IllegalArgumentException e) {
            // Handle validation errors
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", e.getMessage()));
            return null; // Stay on the current page
        } catch (Exception e) {
            // Handle unexpected errors
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "An unexpected error occurred: " + e.getMessage()));
            e.printStackTrace(); // Log the full stack trace
            return null; // Stay on the current page
        }
    }

    public void refreshProduct() {
        products = null;
        getAllProducts();
    }

    // Method to add a product
    public String addProduct() {
        try {
            boolean isAdded = adminLocal.addProduct(productName, productDetails, productPrice, productQuantity, productImage, categoryId);

            if (isAdded) {
                // Clear the fields after successful addition
                return "AllProducts.jsf?faces-redirect=true"; // Redirect to product list page
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to add product."));
                return null; // Stay on the same page
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Exception", e.getMessage()));
            return null;
        }
    }

    //    For Delete Category Method
    public String deleteProduct(Integer productId) {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            adminLocal.deleteProduct(productId);
            refreshProduct();

            return "AllProducts.jsf?faces-redirect=true";
        } catch (Exception e) {
            // Log and show error message
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "An error occurred while adding the Product.", null));
            return null;
        }
    }

    private void resetFields() {
        productId = null;
        productName = null;
        productDetails = null;
        productPrice = null;
        productImage = null;
        productQuantity = 0;
        categoryId = null;
    }

    public String updateProduct() {
        try {
            // Validate input
            if (productId == null || productName == null || productName.trim().isEmpty()
                    || productDetails == null || productPrice == null || productImage == null
                    || productQuantity < 0 || categoryId == null) {
                throw new IllegalArgumentException("All fields are required, and product quantity must be non-negative.");
            }

            // Call service to update the product
            boolean isUpdated = adminLocal.updateProduct(productId, productName, productDetails,
                    productPrice, productImage, productQuantity, categoryId);

            if (isUpdated) {
                // Show success message
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Product updated successfully."));
                resetFields(); // Reset form fields
                refreshProduct(); // Reload products (if needed)
                return "AllProducts.jsf?faces-redirect=true"; // Redirect to the product list page
            } else {
                // Show error message if product or category not found
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Product or category not found."));
                return null; // Stay on the current page
            }
        } catch (IllegalArgumentException e) {
            // Handle validation errors
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", e.getMessage()));
            return null; // Stay on the current page
        } catch (Exception e) {
            // Handle unexpected errors
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "An unexpected error occurred: " + e.getMessage()));
            e.printStackTrace(); // Log the full stack trace
            return null; // Stay on the current page
        }
    }

    public String editProduct(Integer productId) {
        if (productId == null) {
            System.out.println("Error: productId is null.");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid product selected."));
            return null; // Stay on the same page
        }
        System.out.println("Editing product with ID: " + productId);

        try {
            // Fetch the product by its ID
            Product product = adminLocal.getProductById(productId);
            if (product != null) {
                // Populate the bean properties with product data for editing
                this.productId = product.getProductId();
                this.productName = product.getProductName();
                this.productDetails = product.getProductDetails();
                this.productPrice = product.getProductPrice();
                this.productImage = product.getProductImage();
                this.productQuantity = product.getProductQuantity();
                this.categoryId = product.getCategoryId().getCategoryId(); // Corrected line

                return "EditProduct.jsf?faces-redirect=true"; // Redirect to the edit page
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Product not found."));
                return null; // Stay on the same page
            }
        } catch (Exception e) {
            // Handle unexpected errors
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "An unexpected error occurred: " + e.getMessage()));
            e.printStackTrace(); // Log the full stack trace
            return null; // Stay on the same page
        }
    }

    public void refreshUsers() {
        users = null;
        getAllUsers();
    }

    public String deleteUser(Integer UserId) {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            adminLocal.deleteUser(UserId); // Call the method to delete user by ID
            refreshUsers(); // Optional: Reload the users if necessary

            return "AllUsers.jsf?faces-redirect=true"; // Redirect to the user list page
        } catch (Exception e) {
            // Log and show error message
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "An error occurred while deleting the User.", null));
            return null;
        }
    }
    
    @PersistenceContext(unitName = "PharmacyPu")
    private EntityManager em;

    @Transactional
    public void markOrderAsComplete(Integer orderId) {
        Orders order = em.find(Orders.class, orderId);
        if (order != null && "Pending".equals(order.getPaymentStatus())) {
            order.setPaymentStatus("Complete");
            em.merge(order); // Save the updated entity back to the database
        }
        refreshOrders(); // Refresh the orders list after updating the order
    }

    public void refreshOrders() {
        orders = null;
        getAllOrders();
    }
    
//    public boolean isAnyOrderPending() {
//    return orders.stream().anyMatch(order -> "Pending".equals(order.getPaymentStatus()));
//}

    /**
     * Creates a new instance of AdminCDI
     */
    public AdminCDI() {
    }

}
