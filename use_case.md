## 4. Use Case Diagram

```mermaid
flowchart TD
    Student("👤 Student")
    Teacher("👤 Teacher")
    Admin("👤 Admin")

    UC1["🔑  Login"]
    UC2["📝  Register"]
    
    UC3["✍️ Solve Quiz"]
    UC4["📊 Review grade"]
    
    UC5["➕ Add question"]
    UC6["🗑️ Remove question"]
    
    UC7["✅ Add Allowed ID"]
    UC8["🚫 Erase ID"]
    UC9["📋 Show ID list"]

    Student --> UC1
    Student --> UC2
    Teacher --> UC1
    Teacher --> UC2
    Admin --> UC1
    
    Student --> UC3
    Student --> UC4
    
    Teacher --> UC5
    Teacher --> UC6
    
    Admin --> UC7
    Admin --> UC8
    Admin --> UC9