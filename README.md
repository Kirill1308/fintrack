# 💰 Finance Management System API

## Overview
This API provides a comprehensive set of endpoints to manage financial data, including wallets, budgets, transactions, user profiles, and wallet members. It supports creating, updating, and retrieving information for financial planning and tracking purposes.

## Key Features

### 👜 Wallet Management
- **Create, update, delete, and retrieve** wallet information.
- **Manage members** of a wallet by inviting users to join, accepting invitations, and excluding members from the wallet.

### 📊 Budget Management
- Manage budgets associated with wallets, including **creating, updating, and deleting** budgets.

### 💸 Transaction Management
- **Add, update, retrieve, and delete** financial transactions.

### 👤 User Profile Management
- Manage user profiles, including **uploading and updating profile images**.

### 📈 Reporting
- Generate financial reports in various formats (**PDF, XLSX**).

## 🔒 Security
This API uses **JWT (JSON Web Token)** for authentication and authorization. Ensure to include the bearer token in the authorization header for secured endpoints.

## 📜 Documentation
This API is documented with **Swagger**, providing an interactive interface for exploring the available endpoints and their functionalities.

Access the Swagger documentation at: [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

## 📝 Version
**1.0**

## 🛠️ Technologies Used
- **Java 17**
- **Spring Boot**
- **PostgreSQL**
- **Redis**
- **MinIO**
- **Liquibase**
- **Docker**

## Docker Pull Command
```sh
docker pull kirill1308/fintrack-app
