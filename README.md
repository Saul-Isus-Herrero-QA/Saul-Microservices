## OVERVIEW:
This project modernizes order processing by transforming a traditional system into an asynchronous ecosystem. 
When a customer clicks **"Place Order"**, the system instantly secures data in the database while broadcasting a secure background event. 
Instantly and independently, the **Inventory Service** deducts stock using high-concurrency protection, and the **Shipping Service** prepares the delivery.
This ensures seamless, error-free scaling during massive global traffic peaks.

## SCOPE
**Microservices Architecture**, **Event-Driven Systems**, and **Cloud Infrastructure (AWS & GCP)**.

## STACK

| Category | Technologies |
| :--- | :--- |
| **Backend & Core** | Java, Spring Boot, Spring Cloud, JPA / Hibernate, Node.js |
| **Cloud & Infra** | AWS (SQS, SNS, EC2, Lambda), Google Cloud Platform (GCP), Docker, Kubernetes |
| **Databases** | PostgreSQL |
| **Architecture** | Microservices, Event-Driven Architecture (EDA), RESTful APIs, Saga Pattern |
| **CI/CD & Tools** | Git, GitHub Actions, Jenkins, Maven, Docker Compose |
