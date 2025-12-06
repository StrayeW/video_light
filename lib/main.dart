import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

// Platform Kanalımızın adını tanımlıyoruz.
const platform = MethodChannel('com.example.whatsapp_border_light/overlay');

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Sınır Işık Uygulaması',
      theme: ThemeData(primarySwatch: Colors.blue),
      home: HomePage(),
    );
  }
}

class HomePage extends StatelessWidget {
  // 💡 Android Servisi Başlatma Metodu
  void _startOverlayService() async {
    try {
      // Android'e 'startOverlay' metodunu çağırma komutunu gönderiyoruz.
      final String result = await platform.invokeMethod('startOverlay');
      print('Overlay Servisi Başlatıldı: $result');
    } on PlatformException catch (e) {
      // Eğer kanal bulunamazsa veya bir hata olursa (örneğin izin yoksa)
      print("Overlay servisi başlatılamadı: '${e.message}'.");
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Sınır Işık Uygulaması')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            const Text('WhatsApp Üzerine Çizim Efekti'),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: _startOverlayService,
              child: const Text('Işık Efektini Başlat'),
            ),
            const Padding(
              padding: EdgeInsets.all(16.0),
              child: Text(
                'NOT: Bu özellik için Android ayarlarından "Diğer Uygulamaların Üzerine Çiz" izninin verilmesi gerekebilir.',
                textAlign: TextAlign.center,
                style: TextStyle(fontSize: 12, color: Colors.red),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
