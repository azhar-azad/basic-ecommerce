# basic ecommerce

A simple architecture for an ecommerce application:

**Product Service:**
Responsible for managing the products in the ecommerce store. It provides functionalities for adding, updating, and deleting products. It also exposes APIs for searching, filtering, and retrieving products.

**Order Service:** 
Responsible for managing the orders placed by customers. It provides functionalities for creating, updating, and cancelling orders. It also exposes APIs for retrieving order details and tracking order status.

**Payment Service:** Responsible for handling the payment transactions for the ecommerce store. It provides functionalities for processing payments using various payment gateways and APIs.

**User Service:** Responsible for managing the user accounts in the ecommerce store. It provides functionalities for user authentication, registration, and profile management. It also exposes APIs for retrieving user details and order history.

**Cart Service:** Responsible for managing the shopping carts for customers. It provides functionalities for adding, updating, and removing items from the cart. It also exposes APIs for retrieving cart details and calculating cart totals.

**Recommendation Service:** Responsible for providing product recommendations to customers based on their browsing and purchase history. It uses machine learning algorithms to analyze customer behavior and suggest products that are likely to be of interest.

**Shipping Service:** Responsible for managing the shipping of products to customers. It provides functionalities for calculating shipping rates, generating shipping labels, and tracking packages.

**Inventory Service:** Responsible for managing the inventory of products in the ecommerce store. It provides functionalities for tracking product quantities, managing stock levels, and generating alerts for low inventory.

**Notification Service:** Responsible for sending notifications for users, sellers about various events like order posting, discounts, order tracking, returns etc.

### APIs
- Product Service
    - Tables: PRODUCTS
    - /products -- CRUD
    - /products/search -- GET
- Inventory Service
    - Tables: STORES
    - /stores -- CRUD
- Order Service
    - Tables: ORDERS
    - /orders -- CRUD
- Payment Service
    - Tables: PAYMENTS
    - /payments - POST
- User Service
    - Tables: USERS, ROLES
    - /auth/register -- POST
    - /auth/login -- POST
    - /auth/me -- CRUD
- Cart Service
    - Tables: CARTS
    - /carts -- CRUD
- Shipping Service
    - Tables: SHIPPINGS
    - /shippings -- CRUD
- Inventory Service
    - Tables: STORES
    - /stores -- CRUD
- Recommendation Service
    - Machine Learning Dev will do that