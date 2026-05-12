

\---



\## 👥 Görev Dağılımı



Bu dağılımda, bir kişi daha çok \*\*Veri ve Altyapı (Backend \& DevOps)\*\* odaklıyken, diğeri \*\*Arayüz ve Kullanıcı Deneyimi (Frontend \& Mobil)\*\* odaklıdır. Ancak her iki öğrenci de GitHub commit dengesi için kodun her alanına dokunmalıdır.



\### \*\*Öğrenci 1: Sistem Mimarı \& Backend Geliştirici\*\*



\* 

\*\*Microservices \& API Gateway:\*\* Projenin mikroservis yapısını kurgulamak ve tüm trafiği yöneten Gateway'i (Kong vb.) kurmak.





\* 

\*\*Veritabanı Yönetimi:\*\* JDBC (İlişkisel - PostgreSQL/MySQL) ve NoSQL (Redis/MongoDB) katmanlarını izole bir şekilde kurmak.





\* 

\*\*Hata \& Test:\*\* Standart HTTP durum kodları ile hata yönetimi ve TDD (Test-Driven Development) süreçlerini yönetmek.





\* 

\*\*DevOps:\*\* Tüm sistemi Dockerize ederek `docker-compose up` ile çalışır hale getirmek.







\### \*\*Öğrenci 2: Kullanıcı Deneyimi \& Mobil Geliştirici\*\*



\* 

\*\*Mobil GUI (Android/JavaFX):\*\* Projenin ana arayüzünü Java kullanarak geliştirmek.





\* 

\*\*İş Mantığı \& Generic Yapılar:\*\* Tip güvenliğini sağlayan `Generic<T>` sınıfları ve koleksiyonları kurgulamak.





\* 

\*\*Performans Analizi:\*\* Jmeter veya k6 ile yük testlerini yapıp raporlamak.





\* 

\*\*Dokümantasyon:\*\* GitHub üzerinde Mermaid/Markdown kullanarak teknik raporu hazırlamak.







\---



\## 📅 4 Haftalık Proje Planı



\### \*\*1. Hafta: Analiz, Tasarım ve Temel Yapı\*\*



\* 

\*\*Ortak:\*\* Gereksinim analizi ve Mermaid ile sistem mimarisinin çizilmesi.





\* \*\*Öğrenci 1:\*\* Docker ortamının hazırlanması, Mikroservis iskeletlerinin oluşturulması.

\* 

\*\*Öğrenci 2:\*\* Mobil uygulama ekran tasarımlarının (Kurye ekranı, Paket takip ekranı) Java ile kodlanmaya başlanması.







\### \*\*2. Hafta: Veri Katmanı ve İş Mantığı\*\*



\* 

\*\*Öğrenci 1:\*\* JDBC ve NoSQL bağlantılarının SOLID prensiplerine uygun (Repository Pattern vb.) yazılması.





\* 

\*\*Öğrenci 2:\*\* Kargo yönetimindeki `Generic` sınıfların yazılması (Örn: `Package<T>` kargo tipi yönetimi).





\* 

\*\*Ortak:\*\* Servislerin JSON üzerinden haberleşmeye başlaması.







\### \*\*3. Hafta: Gateway, Mobil Entegrasyon ve Test\*\*



\* 

\*\*Öğrenci 1:\*\* API Gateway kurulumu ve servis yönlendirmeleri.





\* \*\*Öğrenci 2:\*\* Mobil arayüzün API servisleri ile bağlanması ve verilerin çekilmesi.

\* 

\*\*Ortak:\*\* TDD döngüsü ile (Red-Green-Refactor) eksik fonksiyonların test edilmesi.







\### \*\*4. Hafta: Performans, Raporlama ve Final\*\*



\* 

\*\*Öğrenci 1:\*\* Tüm sistemin Dockerize testleri ve hata yönetiminin (4xx, 5xx) son kontrolü.





\* 

\*\*Öğrenci 2:\*\* Jmeter/k6 ile yük testlerinin yapılması ve GitHub teknik raporunun tamamlanması.





\* 

\*\*Ortak:\*\* Final sunum hazırlığı.







\---



\## ⚠️ Kritik Hatırlatmalar



\* 

\*\*GitHub Dengesi:\*\* İkinci notta belirtildiği üzere, ikinizin de commit sayıları birbirine yakın ve düzenli olmalıdır.





\* 

\*\*Dil Kısıtı:\*\* Tüm bileşenlerde (Mobil dahil) mutlaka \*\*Java\*\* kullanmalısınız; başka bir dil kullanımı doğrudan 0 puan almanıza neden olur.





\* 

\*\*SOLID:\*\* Kodun nesne yönelimli prensiplere uygunluğu projenin %10'unu, yani başarınızı doğrudan etkiler.







Bu planla hem 65 puanlık zorunlu kısmı garantiye alabilir hem de mikroservis ve mobil arayüz gibi ek özelliklerle 100 tam puana ulaşabilirsiniz. Başarılar dilerim!

