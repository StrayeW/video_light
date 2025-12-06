import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

const platform = MethodChannel('com.example.whatsapp_border_light/overlay');

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Sınır Işık Uygulaması',
      theme: ThemeData(primarySwatch: Colors.blue),
      home: HomePage(),
    );
  }
}

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  // Varsayılan değerler
  double _borderWidth = 10.0;
  Color _selectedColor = Colors.white;

  // Seçilebilir renkler listesi
  final List<Color> _colors = [
    Colors.white,
    Colors.red,
    Colors.green,
    Colors.blue,
    Colors.purple,
    Colors.cyan,
    Colors.yellow,
  ];

  // 💡 Android Servisi Başlatma Metodu
  void _startOverlayService() async {
    try {
      // Android'e verileri (renk ve kalınlık) gönderiyoruz
      final Map<String, dynamic> args = {
        'width': _borderWidth,
        'color': _selectedColor.value, // Renk kodunu integer olarak gönderir
      };

      final String result = await platform.invokeMethod('startOverlay', args);
      print('Overlay Servisi Başlatıldı: $result');
    } on PlatformException catch (e) {
      print("Overlay servisi başlatılamadı: '${e.message}'.");
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Sınır Işık Ayarları')),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(20.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              const Text(
                'WhatsApp Işık Efekti',
                style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 40),

              // --- KALINLIK AYARI ---
              Text('Çizgi Kalınlığı: ${_borderWidth.toInt()} px'),
              Slider(
                value: _borderWidth,
                min: 5.0,
                max: 100.0,
                divisions: 25,
                label: _borderWidth.round().toString(),
                onChanged: (double value) {
                  setState(() {
                    _borderWidth = value;
                  });
                },
              ),

              const SizedBox(height: 20),

              // --- RENK AYARI ---
              const Text('Renk Seçimi'),
              const SizedBox(height: 10),
              Wrap(
                spacing: 10,
                children: _colors.map((color) {
                  return GestureDetector(
                    onTap: () {
                      setState(() {
                        _selectedColor = color;
                      });
                    },
                    child: Container(
                      width: 40,
                      height: 40,
                      decoration: BoxDecoration(
                          color: color,
                          shape: BoxShape.circle,
                          border: Border.all(
                            color: _selectedColor == color
                                ? Colors.black
                                : Colors.grey,
                            width: _selectedColor == color ? 3 : 1,
                          ),
                          boxShadow: [
                            if (_selectedColor == color)
                              BoxShadow(
                                  color: color.withOpacity(0.5),
                                  blurRadius: 10,
                                  spreadRadius: 2)
                          ]),
                    ),
                  );
                }).toList(),
              ),

              const SizedBox(height: 40),

              // --- BAŞLAT BUTONU ---
              ElevatedButton(
                style: ElevatedButton.styleFrom(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 40, vertical: 15),
                ),
                onPressed: _startOverlayService,
                child: const Text('Efekti Uygula / Güncelle',
                    style: TextStyle(fontSize: 16)),
              ),

              const SizedBox(height: 20),
              const Text(
                'NOT: Android "Diğer Uygulamaların Üzerine Çiz" izni gereklidir.',
                textAlign: TextAlign.center,
                style: TextStyle(fontSize: 12, color: Colors.grey),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
