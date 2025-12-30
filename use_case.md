usecaseDiagram
actor "Ogrenci" as Student
actor "Ogretmen" as Teacher
actor "Yonetici (Admin)" as Admin

    %% Use Cases
    usecase "Sisteme Giris (Login)" as UC1
    usecase "Kayit Ol (Register)" as UC2
    usecase "Quiz Coz" as UC3
    usecase "Puani Goruntule" as UC4
    usecase "Soru Ekle" as UC5
    usecase "Soru Sil" as UC6
    usecase "Izinli ID Ekle" as UC7
    usecase "Izinli ID Sil" as UC8
    usecase "ID Listesini Gor" as UC9

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