# Support Center Simulation (Destek Merkezi Simülasyonu)

Bu proje, bir müşteri destek merkezine gelen anlık taleplerin ve mesajların müşteri temsilcilerine (Agent) nesne yönelimli programlama (OOP) ve tasarım desenleri (Design Patterns) prensiplerine uygun olarak dağıtılmasını simüle eden bir Java uygulamasıdır.

## 🚀 Öne Çıkan Özellikler

- *Esnek Yönlendirme Mimarisi (Strategy Pattern):* Gelen mesajlar farklı yönlendirme algoritmalarına göre temsilcilere atanabilir.
- *Kişi ve Mesaj İlişkisi (Contact & Message):* Mesajlar, mesajı gönderen kişilerin (Contact) bilgileriyle (Ad, Email vb.) ilişkilendirilmiştir.
- *Dinamik Kuyruk Yönetimi (Queue Management):* Tüm temsilciler meşgul olduğunda gelen mesajlar sıraya (FIFO) alınır. Temsilcilerden biri işini bitirdiğinde (completeTask) kuyruktaki ilk mesaj otomatik olarak o temsilciye yönlendirilir.
- *İstatistik ve Raporlama:* Anlık temsilci durumları (müsait/meşgul), bekleyen mesaj sayısı ve temsilci bazlı toplam iş yükü raporlanabilir.

---

## 🛠️ Kullanılan Tasarım Desenleri ve Mimari

### Strategy Pattern (Strateji Deseni)
Yönlendirme mantığı RoutingStrategy arayüzü (interface) üzerinden soyutlanmıştır. Bu sayede ana iş mantığına (SupportCenter) dokunmadan yeni yönlendirme stratejileri eklenebilir.

- FirstAvailableRoutingStrategy: Bulduğu ilk müsait temsilciye iş atar.
- LeastBusyRoutingStrategy: Müsait olan temsilciler arasından daha önce en az iş üstlenmiş olanı tercih ederek iş yükünü dengeler.

---

## 📂 Proje Yapısı

```text
support-center-simulation/
├── src/
│   └── com/
│       ├── model/
│       │   ├── Agent.java        # Müşteri temsilcisi modeli
│       │   ├── Contact.java      # Müşteri / Kişi bilgi modeli
│       │   └── Message.java      # Gelen mesaj modeli
│       ├── service/
│       │   └── SupportCenter.java # Destek merkezi ana servis sınıfı
│       ├── strategy/
│       │   ├── RoutingStrategy.java                # Strateji arayüzü
│       │   ├── FirstAvailableRoutingStrategy.java  # İlk müsait atama stratejisi
│       │   └── LeastBusyRoutingStrategy.java     # Dengeli yük atama stratejisi
│       └── Main.java             # Simülasyon başlangıç sınıfı
└── README.md
Örnek Ekran  Çıktısı
=== DESTEK MERKEZİ SİMÜLASYONU BAŞLIYOR ===

--- MESAJLAR GELİYOR ---
Mesaj (M1 - Ahmet Yilmaz) -> Temsilci 1 temsilcisine atandi.
Mesaj (M2 - Ayse Kaya) -> Temsilci 2 temsilcisine atandi.
Mesaj (M3 - Mehmet Demir) -> Temsilci 3 temsilcisine atandi.
Tum temsilciler dolu! Mesaj (M4 - Zeynep Celik) kuyruga alindi.

--- ANLIK DURUM ---
Musait Temsilci Sayisi : 0
Mesgul Temsilci Sayisi : 3
Bekleyen Mesaj Sayisi  : 1

--- İŞ TAMAMLAMA VE KUYRUK YÖNETİMİ ---
<--- Is Tamamlandi: Temsilci [Temsilci 1] isini bitirdi.
Kuyruktaki Mesaj (M4 - Zeynep Celik) -> Temsilci 1 temsilcisine atandi.

--- SIMULASYON OZETI VE ISTATISTIKLER ---
Temsilci: Temsilci 1 | Toplam Aldigi Is: 2
Temsilci: Temsilci 2 | Toplam Aldigi Is: 1
Temsilci: Temsilci 3 | Toplam Aldigi Is: 1