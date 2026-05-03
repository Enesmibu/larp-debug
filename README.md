# Larp Debug — Meteor Client Addon

Fabric 1.21.4 için Meteor Client addon'u. Tamamen dolu arı kovanlarını tarar ve o chunk'ları 3D kutu ile işaretler.

## Modül

**LarpDebugV7** — Meteor Client'ta "Larp Debug" sekmesi altında görünür.

### Ayarlar

| Ayar | Açıklama | Varsayılan |
|---|---|---|
| `chunk-height` | Chunk kutusunun yüksekliği (blok) | 16 |
| `chunk-y-offset` | Kutunun başladığı Y koordinatı | 0 |
| `shape-mode` | Lines / Sides / Both | Lines |
| `side-color` | Yüzey rengi (RGBA) | Sarı, %12 şeffaf |
| `line-color` | Kenar rengi (RGBA) | Sarı, opak |

### Nasıl Çalışır

- Oyuncunun etrafındaki yüklü chunk'ları tarar (server render mesafesine kadar).
- Her chunk'taki `BeehiveBlockEntity`'leri kontrol eder.
- 3 arısı olan (tamamen dolu) arı kovanı veya arı yuvası içeren chunk'ların etrafına kutu çizer.
- Chunk boyutu sabittir (16×16 blok) — sadece kutunun **yüksekliğini** ve **Y konumunu** ayarlayabilirsin.
- Render mesafesi server'ın izin verdiği kadar olur; mod zorla uzatmaz.

## Derleme

### Gereksinimler
- Java 21 veya üzeri
- İnternet bağlantısı (Gradle ilk seferde bağımlılıkları indirir)

### Adımlar

```bash
# Projeyi klonla / indir
cd larp-debug

# Windows:
gradlew.bat build

# Linux / macOS:
./gradlew build
```

Derlenen JAR dosyası:
```
build/libs/larp-debug-1.0.0.jar
```

## Kurulum

1. `larp-debug-1.0.0.jar` dosyasını Minecraft `mods/` klasörüne kopyala.
2. Meteor Client JAR'ının da `mods/` klasöründe olduğundan emin ol.
3. Fabric Loader 1.21.4 ile Minecraft'ı başlat.
4. Meteor Client'ta sol menüden **Larp Debug** sekmesini aç.
5. **LarpDebugV7** modülünü aç ve ayarları düzenle.

## Notlar

- Modül sadece **yüklü** chunk'ları tarar; render mesafesi dışındakiler görünmez.
- Server 32 chunk render'a izin veriyorsa, mod 32 chunk içinde arar.
- `chunk-height` ayarı sadece kutu görselini etkiler, gerçek chunk boyutunu değiştirmez.
