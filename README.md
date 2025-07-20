

# 💊 Pharma Medic – Online Pharmacy Store

🌐 **Pharma Medic** is a powerful 💻 full-stack **online pharmacy web app** that transforms the way people access healthcare. Whether you're in a city or a remote village 🏞️, Pharma Medic ensures **authentic medicines**, **secure payments**, and **user friendly** right from your screen 📱 to your home 🏠.

---

## 🚀 Project Highlights

✨ **One-Stop Medicine Hub** – Search, filter, and purchase healthcare products effortlessly.

🛡️ **Secure & Verified** – 100% genuine products, prescription validation, and encrypted data.

📦 **Track Orders Real-Time** – Stay updated with live delivery tracking and order status.

🧾 **Easy Checkout** – Multiple payment options and downloadable invoices.

📊 **Admin Superpower** – Full control over inventory, sales reports, users, and categories.

---

## 🎯 Key Objectives

- 🩺 **Accessibility** – Deliver medicines to every corner, even rural areas.
- ⚡ **Convenience** – Skip the pharmacy queues and get essentials delivered.
- 💰 **Affordability** – Competitive pricing, generic alternatives, and health tips.
- 🔐 **Compliance** – Follows all healthcare & pharmacy regulations.

---

## 🧩 Core Features

### 👤 User Features
- 🔐 **Login/Register**
- 🛒 **Add to Cart & Checkout**
- 📜 **Upload Prescriptions**
- 💳 **Secure Payments**
- 📦 **Track Orders**
- 🧑‍💼 **Manage Profile & Address**

### 🛠️ Admin Features
- 📋 **Manage Products, Categories & Users**
- 🧾 **Approve Orders & Prescriptions**
- 📈 **View Sales & Reports**
- 🗂️ **Monitor Inventory**

---

## 📱 Future Enhancements

🧠 **AI Suggestions** – Recommend products based on usage & history  
📱 **Mobile App** – Native apps for Android & iOS  
📞 **Teleconsultation** – Chat or video with certified doctors  
🔁 **Medicine Subscriptions** – Auto-refill for regular meds  
💡 **Smart Health Tips** – Personalized notifications & insights

---

## 🧪 Tech Stack

| Layer        | 🧠 Technology           |
|--------------|------------------------|
| Front-End    | 🎨 JSF, PrimeFaces     |
| Back-End     | 🔧 Java (NetBeans 13)  |
| Database     | 🗄️ MySQL               |
| Tools        | 🧰 NetBeans, MySQL WB  |
| OS           | 💻 Windows 11          |

---

## 🧠 Sample Test Cases

| ✅ Test | 🎯 Input | 📌 Expected Output | 🧪 Status |
|--------|----------|--------------------|-----------|
| Login  | Valid creds | Dashboard loads | ✅ Passed |
| Login  | Invalid creds | Error shown | ✅ Passed |
| Add to Cart | Product ID | Item in cart | ✅ Passed |
| Checkout | Details filled | Order placed | ✅ Passed |

---


## 🗃️ Database Schema – MySQL Workbench

Here’s how the heart ❤️ of **Pharma Medic** is structured! This well-normalized schema keeps everything fast, clean, and reliable.

---

### 🔐 `Role` – Who's Who?

| 🧾 Column Name | 🧠 Data Type    | 🔐 Constraints     |
|----------------|----------------|--------------------|
| `role_id`      | INT            | 🔑 Primary Key     |
| `role_name`    | VARCHAR(255)   | ❌ Not Null        |

---

### 👤 `User` – Registered Customers & Admins

| 🧾 Column Name    | 🧠 Data Type    | 🔐 Constraints                          |
|------------------|----------------|-----------------------------------------|
| `user_id`        | INT            | 🔑 Primary Key                           |
| `user_name`      | VARCHAR(20)    | ❌ Not Null                              |
| `user_email`     | VARCHAR(50)    | 💎 Unique, ❌ Not Null                   |
| `user_password`  | VARCHAR(255)   | ❌ Not Null                              |
| `user_phone`     | VARCHAR(15)    | ❌ Not Null                              |
| `role_id`        | INT            | 🔗 Foreign Key → `Role.role_id`         |

---

### 🏷️ `Category` – Organizing Products

| 🧾 Column Name     | 🧠 Data Type     | 🔐 Constraints     |
|-------------------|------------------|--------------------|
| `category_id`     | INT              | 🔑 Primary Key     |
| `category_name`   | VARCHAR(255)     | ❌ Not Null        |

---

### 📦 `Product` – Medicine & Health Essentials

| 🧾 Column Name       | 🧠 Data Type     | 🔐 Constraints                           |
|----------------------|------------------|------------------------------------------|
| `product_id`         | INT              | 🔑 Primary Key                            |
| `product_name`       | VARCHAR(100)     | ❌ Not Null                               |
| `category_id`        | INT              | 🔗 Foreign Key → `Category.category_id`   |
| `product_details`    | VARCHAR(500)     | ❌ Not Null                               |
| `product_price`      | DECIMAL(10,2)    | ❌ Not Null                               |
| `product_image`      | VARCHAR(100)     | ❌ Not Null                               |
| `product_quantity`   | INT              | ❌ Not Null                               |

---

### 🧾 `Order` – Tracking Each Purchase

| 🧾 Column Name      | 🧠 Data Type     | 🔐 Constraints                          |
|---------------------|------------------|------------------------------------------|
| `order_id`          | INT              | 🔑 Primary Key                            |
| `user_id`           | INT              | 🔗 Foreign Key → `User.user_id`          |
| `address_id`        | INT              | 🔗 Foreign Key                            |
| `total_price`       | DECIMAL(10,2)    | ❌ Not Null                               |
| `payment_method`    | VARCHAR(100)     | ❌ Not Null                               |
| `payment_status`    | VARCHAR(100)     | ❌ Not Null                               |
| `placed_on`         | TIMESTAMP        | 🕓 Default to current timestamp           |

---

### 📋 `OrderDetails` – Product-wise Order Info

| 🧾 Column Name       | 🧠 Data Type     | 🔐 Constraints                             |
|----------------------|------------------|--------------------------------------------|
| `order_detail_id`    | INT              | 🔑 Primary Key                              |
| `order_id`           | INT              | 🔗 Foreign Key → `Order.order_id`           |
| `product_id`         | INT              | 🔗 Foreign Key → `Product.product_id`       |
| `quantity`           | INT              | ❌ Not Null                                 |
| `unit_price`         | DECIMAL(10,2)    | ❌ Not Null                                 |
| `tax_amount`         | DECIMAL(10,2)    | ❌ Not Null                                 |
| `gst_amount`         | DECIMAL(10,2)    | ❌ Not Null                                 |
| `sgst_amount`        | DECIMAL(10,2)    | ❌ Not Null                                 |

---

### 💳 `Payment` – 💸 Payment Confirmation Log

| 🧾 Column Name      | 🧠 Data Type     | 🔐 Constraints                          |
|---------------------|------------------|------------------------------------------|
| `payment_id`        | INT              | 🔑 Primary Key                            |
| `order_id`          | INT              | 🔗 Foreign Key → `Order.order_id`        |
| `payment_status`    | VARCHAR(100)     | ❌ Not Null                               |
| `payment_amount`    | DECIMAL(10,2)    | ❌ Not Null                               |
| `payment_method`    | VARCHAR(100)     | ❌ Not Null                               |

---

## 🖼️ UI Snapshots

> 📂 Place these screenshots in the `screenshots/` folder of your GitHub repo for preview.


### 📝 Registration Page
![Register](https://github.com/Nayan8319/Online-Pharmacy-Store/blob/main/Module%20Images/Register.png)

### 🔑 Login Page
![Login](https://github.com/Nayan8319/Online-Pharmacy-Store/blob/main/Module%20Images/Login.png)

### 🏠 Home Page (Before Login)
![Home](https://github.com/Nayan8319/Online-Pharmacy-Store/blob/main/Module%20Images/Comman.png)

### 🛍️ Product Page
![Product List](https://github.com/Nayan8319/Online-Pharmacy-Store/blob/main/Module%20Images/User%20Product.png)

### 🧾 Selected Product Page
![Selected Product](https://github.com/Nayan8319/Online-Pharmacy-Store/blob/main/Module%20Images/Selected%20Product.png)

### 🛒 Cart Page
![Cart](https://github.com/Nayan8319/Online-Pharmacy-Store/blob/main/Module%20Images/Cart%20Page.png)

### 💳 Checkout Page
![Checkout](https://github.com/Nayan8319/Online-Pharmacy-Store/blob/main/Module%20Images/Check%20Out%20Page.png)

### 📬 Add Address Page
![Add Address](https://github.com/Nayan8319/Online-Pharmacy-Store/blob/main/Module%20Images/Check%20Out%20Page.png)

---

### 👨‍💼 Admin Dashboard

### 📊 Admin Panel (Dashboard)
![Admin Dashboard](https://github.com/Nayan8319/Online-Pharmacy-Store/blob/main/Module%20Images/Admin%20Dashboard.png)

### 🗃️ Add Product
![Add Product](https://github.com/Nayan8319/Online-Pharmacy-Store/blob/main/Module%20Images/Add%20Product.png)

### ✏️ Edit Product
![Edit Product](https://github.com/Nayan8319/Online-Pharmacy-Store/blob/main/Module%20Images/Edit%20Product.png)

### 🗂️ Add Category
![Add Category](https://github.com/Nayan8319/Online-Pharmacy-Store/blob/main/Module%20Images/Add%20Category.png)

### ✏️ Edit Category
![Edit Category](https://github.com/Nayan8319/Online-Pharmacy-Store/blob/main/Module%20Images/Edit%20Category.png)

### 📋 All Orders
![All Orders](https://github.com/Nayan8319/Online-Pharmacy-Store/blob/main/Module%20Images/All%20Orders.png)

### 👥 All Users
![All Users](https://github.com/Nayan8319/Online-Pharmacy-Store/blob/main/Module%20Images/All%20Users.png)

---

## 👨‍💻 Authors

| 🧑‍🎓 Name | 🌐 GitHub |
|-----------|-----------|
| Sarang Rishit Bhupendra | [@Rishitsarang](https://github.com/Rishitsarang) |
| Nayan Padhiyar Prakashbhai | [@Nayan8319](https://github.com/Nayan8319) |

> Submitted as part of an academic project at 🎓 **J.P. Dawer Institute of Information Science & Technology**, Surat.

---

## 🔗 References

- 🌐 [NetMeds](https://www.netmeds.com/)
- 🌐 [Apollo Pharmacy](https://www.apollopharmacy.in/)
- 💡 [UI Inspiration Repo](https://github.com/irfanhsajid/MediMart-E-Shop-v1/wiki/UI)

---

## 📄 License

📚 This repository is for **educational purposes only**. All rights reserved by the authors. 📘

---

> 💬 _“Helping you access quality healthcare anytime, anywhere.”_

🌟 Don’t forget to ⭐ star this repo if you find it helpful!
