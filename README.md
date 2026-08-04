# Destek Merkezi Simülasyonu (Support Center Simulation)

Bu proje, müşteri mesajlarının müsait destek temsilcilerine yönlendirilmesini simüle eden tek thread'li bir Java uygulamasıdır.

## Proje Mimarisi

- *model*: Agent ve Message veri sınıflarını içerir.
- *service*: SupportCenter ana iş mantığını ve kuyruk (Queue) yönetimini yürütür.
- *strategy*: RoutingStrategy arayüzü ile Strategy Design Pattern uygulanmıştır. Esnek yönlendirme kurallarını barındırır.

## Nasıl Çalıştırılır?

Proje kök dizininde VS Code PowerShell terminalini açıp şu komutu çalıştırabilirsiniz:

```powershell
javac -d bin (Get-ChildItem -Recurse -Filter *.java src).FullName; java -cp bin com.Main