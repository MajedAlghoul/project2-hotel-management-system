# hotel-management-system

## Project Overview

The Hotel Management System is designed to streamline operations for both hotel employees and guests. This system offers functionalities for customers to search available rooms, make reservations, check-in, check-out, and generate invoices. Employees can manage room availability, maintain customer profiles, and oversee housekeeping schedules. The project includes developing a set of RESTful APIs using Spring Boot to support these functionalities and the necessary front-end interfaces for web and mobile platforms.

![Alt text](/Images/img.png "ER Diagram")

## Features

1. **Customer Management**
    - User registration and login
    - Profile management (view, update, change password)

2. **Employee Management**
    - Admin management of hotel employees and staff

3. **Search Functionality**
    - Search reservations by customer name, ID, and date
    - Search customer info and available rooms with details such as price, facilities, capacity, size, and features

4. **Reservation Management**
    - Book, modify, and cancel reservations (with admin approval for cancellations)

5. **Room Management**
    - Manage room types, availability, and status by admin users

6. **Check-In/Check-Out Process**
    - Manage customer arrivals and departures by admin users

7. **Housekeeping Management**
    - Schedule and track housekeeping tasks and employees

8. **Billing**
    - Generate and manage invoices for customer reservations

9. **Role-Based Access Control**
    - Different functionalities available based on user roles (admin, customer)

## Getting Started

### Prerequisites

- Java 17 or higher
- Docker
- Docker Compose
- Git

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/MajedAlghoul/project2-hotel-management-system.git
   cd project2-hotel-management-system

## OpenAPI specification Design

    - [API documentation on swaggerhub](https://app.swaggerhub.com/apis-docs/ahmadmtera/HMS/1.0)
    - ERD:
    ![HMS ERD drawio](https://github.com/MajedAlghoul/project2-hotel-management-system/assets/89265941/41a11c81-c045-40e2-b9b0-e257871b6254)

