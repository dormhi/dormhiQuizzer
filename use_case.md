## 4. Use Case Diagram

```mermaid
flowchart TD
    %% Aktorler
    Student("👤 Ogrenci")
    Teacher("👤 Ogretmen")
    Admin("👤 Yonetici (Admin)")

    %% Kullanim Durumlari (Use Cases)
    UC1["🔑 Sisteme Giris (Login)"]
    UC2["📝 Kayit Ol (Register)"]
    
    UC3["✍️ Quiz Coz"]
    UC4["📊 Puani Goruntule"]
    
    UC5["➕ Soru Ekle"]
    UC6["🗑️ Soru Sil"]
    
    UC7["✅ Izinli ID Ekle"]
    UC8["🚫 Izinli ID Sil"]
    UC9["📋 ID Listesini Gor"]

    %% Ortak Islemler
    Student --> UC1
    Student --> UC2
    Teacher --> UC1
    Teacher --> UC2
    Admin --> UC1
    
    %% Ogrenci Islemleri
    Student --> UC3
    Student --> UC4
    
    %% Ogretmen Islemleri
    Teacher --> UC5
    Teacher --> UC6
    
    %% Admin Islemleri
    Admin --> UC7
    Admin --> UC8
    Admin --> UC9