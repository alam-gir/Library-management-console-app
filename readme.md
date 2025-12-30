# 📚 Library Management System (Console Based - Java)

A simple but complete **console-based Library Management System** created using **pure Java**.  
The system stores data inside **.txt files** (works like a mini database) and follows **MVC layered structure**,  
which makes the project look clean, professional, and easy to understand even for beginners.

---

## ✨ Features

### 👨‍🎓 Student Panel
- Explore all books (with pagination)
- Search books by title/author/ISBN
- Request to borrow a book
- View borrowed books
- View borrow request history
- Read notifications

### 🧑‍💼 Staff Panel
- View pending borrow requests
- Approve/Reject requests
- Checkout books to students
- Return accepted books
- Manage Books (Add / Update / Remove)
- Manage Book Copies
- Manage Students
- View notifications

### 🔄 System Handles Automatically
- Status change when borrowing/returning
- Notification generation
- Prevents borrowing unavailable copies

---

## 🗂 Project Structure

Library_management_system/
├── controller/ # Console menu and flow handling
│ ├── MainController.java
│ ├── StudentController.java
│ ├── StaffController.java
│ └── feature/ # Organized sub features
│ ├── checkout/
│ ├── studentManagement/
│ └── bookManagement/
│
├── model/ # All entities
│ ├── User.java
│ ├── Student.java
│ ├── Staff.java
│ ├── Book.java
│ ├── BookCopy.java
│ ├── BorrowRequest.java
│ ├── Notification.java
│ └── enums/
│ ├── Role.java
│ ├── BookStatus.java
│ ├── RequestStatus.java
│ └── NotificationType.java
│
├── repository/ # File based CRUD like database
├── service/ # Business logic layer
├── util/ # Helper utilities for printing UI
│
├── Main.java # Entry point
|
└── data/ # Stores application data as .txt files
│ ├── users.txt
│ ├── students.txt
│ ├── staffs.txt
│ ├── books.txt
│ ├── book_copies.txt
│ ├── borrow_requests.txt
│ ├── notifications.txt


---

## 📁 Data Format Example

Each file line works like a row in database:

id|field1|field2|field3|...

Example for `books.txt`:
id|field1|field2|field3|...

---

## 🧪 Sample Login Credentials

|   Role  | User Name | Password |
|---------|---------|------------|
| Student | Atik    | pass1      |
| Staff   | Admin   | admin123   |

---

## 🚀 How to Run

1. Install **Java 17 or above**
2. Open project in **IntelliJ / VS Code / Eclipse**
3. Make sure **/data files exist**
4. Run the `Main.java` class


