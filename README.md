# MyAqidah Mobile

MyAqidah Mobile adalah aplikasi berbasis Android yang dirancang untuk mempermudah pengguna dalam mendapatkan informasi rumah sakit Aqidah. Aplikasi ini dapat diakses secara offline menggunakan database lokal, dan data diperbarui secara online melalui REST API dengan teknik web scraping.

## Fitur Utama

- **Splash Screen & Onboarding**: Pengguna baru diperkenalkan dengan aplikasi melalui layar onboarding.
- **Login & Session Management**: Login pengguna dengan manajemen sesi. Jika pengguna sudah login, aplikasi langsung membuka halaman utama.
- **Informasi Dokter**: Menampilkan jadwal dokter dengan data yang diperbarui secara real-time.
- **Janji Temu Dokter**: Fitur untuk membuat janji temu dengan dokter.
- **Pengingat Janji Temu**: Mengirimkan notifikasi pengingat kepada pasien dan dokter melalui Firebase Cloud Messaging (FCM).
- **Visualisasi Data**: Menampilkan indikator layanan rumah sakit menggunakan MPAndroidChart.
- **Navigasi Utama**: Grid menu dengan fitur-fitur utama aplikasi.
- **Informasi Darurat**: Layanan darurat dilengkapi dengan animasi Lottie dan kontak penting.

## Teknologi yang Digunakan

- **Bahasa Pemrograman**: Kotlin
- **Framework dan Library**:
  - Jetpack Components (ViewModel, LiveData, Room)
  - Firebase (Authentication, Firestore, Cloud Messaging)
  - MPAndroidChart untuk visualisasi data
  - Lottie untuk animasi
- **Teknik Data**:
  - Web scraping untuk mengambil data dari REST API
  - Local database dengan Room
- **Dependency Injection**: Koin
- **Desain UI**:
  - XML Layout
  - RelativeLayout, GridLayout, dan CardView
- **Arsitektur**: MVVM (Model-View-ViewModel)

## Instalasi

1. Clone repositori ini:
   ```bash
   git clone https://github.com/username/MyAqidah-Mobile.git
2. Buka proyek di Android Studio.
3. ambahkan file google-services.json untuk konfigurasi Firebase.
4. Sync Gradle.
5. Jalankan aplikasi pada emulator atau perangkat fisik.

## Struktur Proyek

MyAqidah-Mobile/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/myaqidah/
│   │   │   │   ├── di/                # Konfigurasi Dependency Injection
│   │   │   │   ├── data/              # Repository, Room Entities, dan DAO
│   │   │   │   ├── ui/                # Aktivitas dan Fragment
│   │   │   │   ├── utils/             # Utility classes
│   │   │   ├── res/
│   │   │   │   ├── layout/            # File XML layout
│   │   │   │   ├── drawable/          # Aset gambar
│   │   │   │   ├── anim/              # Animasi
│   │   │   │   ├── values/            # Strings, colors, themes
│   │   ├── androidTest/               # Pengujian instrumentasi
│   │   ├── test/                      # Pengujian unit
├── build.gradle
├── settings.gradle
└── README.md

### Cara Penggunaan
1. Login menggunakan kredensial yang valid.
2. Navigasikan menu utama untuk melihat fitur-fitur:
 - Jadwal Dokter: Lihat jadwal dokter berdasarkan spesialisasi.
 - Janji Temu: Buat janji temu dengan dokter pilihan.
- Visualisasi Data: Lihat indikator layanan rumah sakit.
3. Pengguna akan menerima notifikasi pengingat sebelum jadwal janji temu.

## Lisensi
## Kontak
Untuk pertanyaan lebih lanjut, hubungi:

Nama: Usmar Manalu
- Email: example@example.com
- GitHub: usmarmanalu
- Email: usmarmnl99@gmail.com
- Github: usmarmanalu

