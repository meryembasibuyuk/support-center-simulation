# Destek Merkezi Yönlendirme Simülasyonu

Bu proje, bir destek merkezine gelen mesajların müsait temsilcilere esnek stratejilerle yönlendirilmesini simüle eden tek izlekli (single-threaded) bir Java uygulamasıdır.

## Mimari Tasarım
Projede değişen yönlendirme kurallarına uyum sağlayabilmek amacıyla *Strategy Design Pattern* kullanılmıştır.
- Message: Müşteri taleplerini temsil eden veri modeli.
- Agent: Temsilci durumlarını ve performansını takip eden model.
- RoutingStrategy: Dağıtım kurallarını soyutlayan arayüz.
- SupportCenter: Kuyruk yönetimini (FIFO) ve iş akışını koordine eden ana servis.

## Çalıştırma Adımları
